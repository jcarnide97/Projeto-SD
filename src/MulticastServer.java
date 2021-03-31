import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.InputStreamReader;
import classes.*;

import static java.lang.System.exit;

public class MulticastServer extends Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static String groupAddr;
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;
    private Departamento departamento;
    private ArrayList<Eleicao> listaEleicoes;
    private Boolean estadoMesaVoto;
    private String address_group;
    public Boolean [] terminais;
    static MulticastLibrary rmi;
    private int unlocks = 0;


    public MulticastServer(Departamento departamento) {
        this.departamento = departamento;
        this.listaEleicoes = new ArrayList<>();
        this.estadoMesaVoto = false;
        this.terminais = new Boolean[2];
    }

    public MulticastServer(MulticastLibrary rmi) throws RemoteException {
        super();
        this.rmi = rmi;
    }

    public static boolean autenticacao(String nome, String numero){
        boolean verifica;
        while (true) {
            try {
                verifica = rmi.userAuth(nome,numero);
                System.out.println(verifica);
                return verifica;
            } catch (RemoteException re) {
                reconectarRMI();
            }
        }
    }

    public static void reconectarRMI() {
        int sleep = 1000;
        while (true) {
            try {
                rmi = (MulticastLibrary) Naming.lookup("rmi://localhost:7000/RMI_Server");
                rmi.sayHello();
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(sleep);
                    sleep *= 2;
                    if (sleep > 16000) {
                        System.out.println("Avaria no RMI Server");
                        exit(0);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        try {
            MulticastLibrary rmi = (MulticastLibrary) Naming.lookup("rmi://localhost:7000/RMI_Server");
            rmi.sayHello();
            ArrayList<Eleicao> eleicoes = new ArrayList<>();
            boolean flagEleicoes = false;
            boolean flagDeps = false;
            boolean flagEscolhas = false;
            MulticastServer mesaOficial = null;
            while (true) {
                try {
                    eleicoes = rmi.getListaEleicoes();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }

            if (eleicoes.isEmpty()) {
                System.out.println("Não exitem eleições!");
            }
            else{
                flagEleicoes=true;
            }
            int i=0;
            for (Eleicao ele : eleicoes) {
                System.out.println("["+i+"]"+ele.getTitulo());
                i++;
            }


            if(flagEleicoes){
                System.out.println("Eleicao para associar maquina:");
                Scanner sc = new Scanner(System.in);
                int opcaoEleicao;
                do {
                    System.out.print(">>> ");
                    opcaoEleicao = sc.nextInt();
                } while (opcaoEleicao < 0 || opcaoEleicao > eleicoes.size()-1);
                eleicoes.get(opcaoEleicao).printEleicao();

                ArrayList<MulticastServer> deps;
                while (true) {
                    try {
                        deps = rmi.getMesasVoto();
                        break;
                    } catch (RemoteException re) {
                        reconectarRMI();
                    }
                }
                if (deps.isEmpty()) {
                    System.out.println("Não existem mesas de voto associadas!");
                }
                else{
                    flagDeps=true;
                }
                i=0;
                Map<Integer, MulticastServer> indicesMesas=new HashMap<Integer, MulticastServer>();
                System.out.println("Associar mesa de voto!");

                for (MulticastServer dep : deps) {
                    boolean aux = false;
                    for(Eleicao eles : dep.getListaEleicoes()){
                        if (eles.getTitulo().equals(eleicoes.get(opcaoEleicao).getTitulo())){
                            aux = true;
                            break;
                        }
                    }
                    if(aux){
                        indicesMesas.put(i,dep);
                        System.out.println("["+i+"] "+dep.getDepartamento().getNome());
                    }
                    i++;
                }
                int opcaoDep;
                do {
                    System.out.print(">>> ");
                    opcaoDep = sc.nextInt();
                } while (!indicesMesas.containsKey(opcaoDep));
                System.out.println(indicesMesas.get(opcaoDep).getDepartamento().getNome()+" escolhido!");
                MulticastServer mesaEscolhida  = indicesMesas.get(opcaoDep);
                if (!mesaEscolhida.estadoMesaVoto){
                    rmi.atualizaMesaVoto(mesaEscolhida,true);
                    mesaEscolhida.setEstadoMesaVoto(true);
                    mesaOficial = mesaEscolhida;
                    rmi.setEndereco(mesaOficial,groupAddr);
                    MulticastServer multiserver = new MulticastServer(rmi);
                    InputStreamReader input = new InputStreamReader(System.in);
                    BufferedReader reader = new BufferedReader(input);
                    String nome;
                    String numero;
                    boolean ver;
                    System.out.println("AUTENTICAR UTILIZADOR ");
                    System.out.print("Nome: ");
                    nome = reader.readLine();
                    System.out.print("Numero: ");
                    numero = reader.readLine();
                    ver = autenticacao(nome,numero);
                    if(ver){
                        MulticastSocket socket = null;
                        try {
                            socket = new MulticastSocket();  // create socket and bind it
                            InetAddress group = InetAddress.getByName(groupAddr);
                            socket.joinGroup(group);
                            try {
                                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                                String message = "unlock";
                                byte[] buffer3 = message.getBytes();
                                InetAddress group2 = InetAddress.getByName(groupAddr);
                                DatagramPacket packet = new DatagramPacket(buffer3, buffer3.length, group, 4322);
                                socket.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            socket.close();
                        }
                        //desbloquear terminal durante 120 segundos
                        System.out.println(groupAddr);
                        MulticastReceiver receiver = new MulticastReceiver(groupAddr);
                        receiver.start();
                }
                else{
                    flagEscolhas = true;
                    System.out.println("Mesa a ser utilizada!");
                }
            }
            if(flagEleicoes && flagDeps && !flagEscolhas){
                MulticastServer finalMesaOficial = mesaOficial;
                Runtime.getRuntime().addShutdownHook(new Thread(){
                    @Override
                    public void run() {
                        if (finalMesaOficial !=null ){
                            try {
                                rmi.atualizaMesaVoto(finalMesaOficial,false);
                                System.out.println("\nLibertada a mesa do departamento: "+finalMesaOficial.getDepartamento().getNome());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in main MulticastServer: " + e.getMessage());
            e.printStackTrace();
        }


    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }
    public void run() {
        MulticastSocket socket = null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            this.groupAddr=String.valueOf((int)(Math.random()*(239-225 + 1)+225))+"."+String.valueOf((int)(Math.random()*(255-0 + 1)+0))+"."+String.valueOf((int)(Math.random()*(255-0 + 1)+0))+"."+String.valueOf((int)(Math.random()*(255-1 + 1)+1));
            while (true) {
                byte[] buffer = this.groupAddr.getBytes();
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public ArrayList<Eleicao> getListaEleicoes() {
        return listaEleicoes;
    }

    public void addEleicao(Eleicao e) {
        this.listaEleicoes.add(e);
    }

    public Boolean getEstadoMesaVoto() {
        return estadoMesaVoto;
    }

    public void setEstadoMesaVoto(Boolean estadoMesaVoto) {
        this.estadoMesaVoto = estadoMesaVoto;
    }
}

class MulticastReceiver extends Thread{
    private String multicastGroup;
    private int PORT = 4322;
    static MulticastLibrary rmi;

    public MulticastReceiver(MulticastLibrary rmi) throws RemoteException {
        super();
        this.rmi = rmi;
    }

    public MulticastReceiver(String multicastGroup) {
        this.multicastGroup = multicastGroup;
        System.out.println("Group Address: "+this.multicastGroup);
    }


    public static void reconectarRMI() {
        int sleep = 1000;
        while (true) {
            try {
                rmi = (MulticastLibrary) Naming.lookup("RMI_Multicast");
                rmi.sayHello();
            } catch (Exception e) {
                try {
                    Thread.sleep(sleep);
                    sleep *= 2;
                    if (sleep > 16000) {
                        System.out.println("Avaria no RMI Server");
                        exit(0);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public static boolean autenticacao(String nome, String pass){
        boolean verifica;
        while (true) {
            try {
                verifica = rmi.userLogin(nome,pass);
                System.out.println(verifica);
                return verifica;
            } catch (RemoteException re) {
                reconectarRMI();
            }
        }
    }

    public void run(){
        MulticastSocket socket = null;
        MulticastServer server = new MulticastServer();
        server.start();
        try {
            MulticastLibrary rmi = (MulticastLibrary) Naming.lookup("rmi://localhost:7000/RMI_Server");
            rmi.sayHello();
            MulticastReceiver multiserver = new MulticastReceiver(rmi);
            try {
                socket = new MulticastSocket(PORT);
                InetAddress group = InetAddress.getByName(multicastGroup);
                socket.joinGroup(group);
                socket.setSoTimeout(120 * 1000);
                try{
                    while (true) {
                        byte[] buffer = new byte[256];
                        DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet2);
                        System.out.println("Received message from " + packet2.getAddress().getHostAddress() + ":" + packet2.getPort() + " with message:");
                        String message = new String(packet2.getData(), 0, packet2.getLength());
                        System.out.println(message);
                        String [] login=message.split("\n");
                        boolean condicao = autenticacao(login[0],login[1]);
                        String resposta;
                        if(condicao){
                            System.out.println("Utilizador "+login[0]+" vai votar!");
                            resposta = "Logado!\nMostrar Eleições:";
                        }
                        else{
                            System.out.println("Erro no login!");
                            resposta = "Erro no login!";
                        }
                        packet2.setData(resposta.getBytes());
                        DatagramPacket reply = new DatagramPacket(packet2.getData(),packet2.getLength(), packet2.getAddress(), packet2.getPort());
                        socket.send(reply);
                        break;
                    }
                }catch (SocketTimeoutException e){
                    String message = "lock";
                    byte[] buffer3 = message.getBytes();
                    InetAddress group2 = InetAddress.getByName(multicastGroup);
                    DatagramPacket packet = new DatagramPacket(buffer3, buffer3.length, group, PORT);
                    socket.send(packet);
                    System.out.println("Nao foi obtida resposta.\nA bloquear terminal");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(socket!=null){
                    socket.close();
                }

            }

        } catch (Exception e) {
            System.out.println("Exception in main MulticastServer: " + e.getMessage());
            e.printStackTrace();
        }

    }
}



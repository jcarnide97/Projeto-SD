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
    public Boolean [] auxterminais;
    public Boolean[] terminais;
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
                    if (sleep > 30000) {
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

        try {
            rmi = (MulticastLibrary) Naming.lookup("rmi://localhost:7000/RMI_Server");
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
                    server.start();
                    /// LIBERTAR MESA DE VOTO
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
                    MulticastServer multiserver = new MulticastServer(rmi);

                    while(true){
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader reader = new BufferedReader(input);
                        String nome;
                        String numero;
                        boolean ver;
                        System.out.println("AUTENTICAR UTILIZADOR ");
                        System.out.print("\nNome: ");
                        nome = reader.readLine();
                        System.out.print("Numero: ");
                        numero = reader.readLine();
                        ver = autenticacao(nome,numero);
                        ArrayList<User> votantes;
                        boolean checaUser = false;
                        for (MulticastServer dep : deps) {
                            for(Eleicao eles : dep.getListaEleicoes()){
                                if (eles.getTitulo().equals(eleicoes.get(opcaoEleicao).getTitulo())){
                                    ArrayList<Voto> votos = eles.getListaVotos();
                                    for(Voto voto: votos){
                                        if(voto.getEleitor().getNome().equals(nome) && voto.getEleitor().getNumero().equals(numero)){
                                            checaUser = true;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        ArrayList<String> loggedUsers = rmi.getLoggedUsers();

                        for(String name:loggedUsers){
                            if (name.equals(nome)){
                                checaUser=true;
                                System.out.println("Utilizador já logado, ou utilizador já votou");
                                break;
                            }
                        }

                        ver = autenticacao(nome,numero);
                        if(ver && !checaUser){
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
                                    int portinho = 4321;
                                    ArrayList<MulticastServer> mesas2;
                                    while (true) {
                                        try {
                                            mesas2 = rmi.getMesasVoto();
                                            break;
                                        } catch (RemoteException re) {
                                            reconectarRMI();
                                        }
                                    }
                                    for(MulticastServer mesa: mesas2){
                                        if(mesa.groupAddr.equals(groupAddr)){
                                            if(!mesa.terminais[0]){
                                                portinho = 4321;
                                            }
                                            else if(!mesa.terminais[1]){
                                                portinho = 4322;
                                            }
                                            break;
                                        }
                                    }
                                    DatagramPacket packet = new DatagramPacket(buffer3, buffer3.length, group, portinho);
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

                            ArrayList<MulticastServer> mesas;
                            while (true) {
                                try {
                                    mesas = rmi.getMesasVoto();
                                    break;
                                } catch (RemoteException re) {
                                    reconectarRMI();
                                }
                            }
                            Boolean[] terminaisUsados;
                            for(MulticastServer mesa: mesas){
                                if(mesa.groupAddr.equals(groupAddr)){
                                    terminaisUsados = mesa.terminais;
                                    if(!terminaisUsados[0]){
                                        terminaisUsados[0]=true;
                                        rmi.atualizaTerminais(mesa,terminaisUsados);
                                        MulticastReceiver receiver = new MulticastReceiver(groupAddr,4321,mesa,eleicoes.get(opcaoEleicao).getTitulo(),numero);
                                        receiver.start();
                                    }
                                    else if(!terminaisUsados[1]){
                                        terminaisUsados[1]=true;
                                        rmi.atualizaTerminais(mesa,terminaisUsados);
                                        MulticastReceiver receiver = new MulticastReceiver(groupAddr,4322,mesa,eleicoes.get(opcaoEleicao).getTitulo(),numero);
                                        receiver.start();
                                    }
                                    else{
                                        System.out.println("Todos os terminais estao a ser usados! Repita o processo");
                                    }
                                    break;
                                }
                            }
                        }
                        else{
                            flagEscolhas = true;
                            System.out.println("Mesa a ser utilizada!");
                        }
                    }

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

        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            this.groupAddr=String.valueOf((int)(Math.random()*(239-225 + 1)+225))+"."+String.valueOf((int)(Math.random()*(255-0 + 1)+0))+"."+String.valueOf((int)(Math.random()*(255-0 + 1)+0))+"."+String.valueOf((int)(Math.random()*(255-1 + 1)+1));

            while (true) {
                ArrayList<MulticastServer> mesasTotal;
                while (true) {
                    try {
                        mesasTotal = rmi.getMesasVoto();
                        break;
                    } catch (RemoteException re) {
                        reconectarRMI();
                    }
                }
                Boolean [] termLigados = new Boolean[0];

                for(MulticastServer mesa: mesasTotal){
                    if(mesa.groupAddr.equals(this.groupAddr)){
                        termLigados=mesa.auxterminais;
                        break;
                    }
                }
                if(!termLigados[0]&&!termLigados[1]){

                    int porto = 4321;
                    byte[] buffer = (this.groupAddr+","+porto).getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                    MulticastSocket socket2 = null;
                    socket2 = new MulticastSocket(PORT);
                    socket2.joinGroup(group);

                    DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
                    socket2.setSoTimeout(1000);
                    try{
                        socket2.receive(packet2);
                        socket2.leaveGroup(group);
                        socket2.close();
                        String message = new String(packet2.getData(), 0, packet2.getLength());
                        if (message.equals("okay")){

                            ArrayList<MulticastServer> mesasTotal2 = rmi.getMesasVoto();
                            for(MulticastServer mesa: mesasTotal2){
                                if(mesa.groupAddr.equals(this.groupAddr)){
                                    rmi.setLigados(mesa,0,true);
                                    break;
                                }
                            }
                        }
                    }catch (SocketTimeoutException e){}

                }
                else if(termLigados[0]&&!termLigados[1]){
                    int porto = 4322;

                    byte[] buffer = (this.groupAddr+","+porto).getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                    MulticastSocket socket2 = null;
                    socket2 = new MulticastSocket(PORT);
                    socket2.joinGroup(group);

                    DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
                    socket2.setSoTimeout(1000);
                    try{
                        socket2.receive(packet2);
                        socket2.leaveGroup(group);
                        socket2.close();
                        String message = new String(packet2.getData(), 0, packet2.getLength());
                        if (message.equals("okay")){
                            ArrayList<MulticastServer> mesasTotal2 = rmi.getMesasVoto();
                            for(MulticastServer mesa: mesasTotal2){
                                if(mesa.groupAddr.equals(this.groupAddr)){
                                    rmi.setLigados(mesa,1,true);
                                    break;
                                }
                            }
                        }
                    }catch (SocketTimeoutException e){}

                }
                else if(!termLigados[0]&&termLigados[1]){

                    int porto = 4321;
                    byte[] buffer = (this.groupAddr+","+porto).getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                    MulticastSocket socket2 = null;
                    socket2 = new MulticastSocket(PORT);
                    socket2.joinGroup(group);

                    DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
                    socket2.setSoTimeout(1000);
                    try{
                        socket2.receive(packet2);
                        socket2.leaveGroup(group);
                        socket2.close();
                        String message = new String(packet2.getData(), 0, packet2.getLength());
                        if (message.equals("okay")){
                            ArrayList<MulticastServer> mesasTotal2 = rmi.getMesasVoto();
                            for(MulticastServer mesa: mesasTotal2){
                                if(mesa.groupAddr.equals(this.groupAddr)){
                                    rmi.setLigados(mesa,0,true);
                                    break;
                                }
                            }
                        }
                    }catch (SocketTimeoutException e){}
                }
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
    private int port;
    private MulticastServer mesa;
    static MulticastLibrary rmi;
    private String eleicaoName;
    private String numero;

    public MulticastReceiver(MulticastLibrary rmi) throws RemoteException {
        super();
        this.rmi = rmi;

    }

    public MulticastReceiver(String multicastGroup,int port, MulticastServer mesa, String eleicaoName,String numero){
        this.multicastGroup = multicastGroup;
        this.port = port;
        this.mesa = mesa;
        this.eleicaoName = eleicaoName;
        this.numero = numero;
        //System.out.println(": "+this.multicastGroup);
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
                    if (sleep > 30000) {
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
            MulticastReceiver multiserver = new MulticastReceiver(rmi);
            try {
                socket = new MulticastSocket(port);
                InetAddress group = InetAddress.getByName(multicastGroup);
                socket.joinGroup(group);
                socket.setSoTimeout(120 * 1000);
                try{
                    while (true) {
                        byte[] buffer = new byte[256];
                        DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet2);
                        String message = new String(packet2.getData(), 0, packet2.getLength());
                        String [] login=message.split("\n");
                        boolean condicao = autenticacao(login[0],login[1]);
                        String resposta;
                        if(condicao){
                            String listas="";
                            for(Eleicao elei: rmi.getListaEleicoes()){
                                if(elei.getTitulo().equals(eleicaoName)){
                                    for(ListaCandidata lista: elei.getListaCandidatas()){
                                        listas+=lista.getNome()+"\n";
                                    }
                                    break;
                                }
                            }
                            resposta = listas;
                            packet2.setData(resposta.getBytes());
                            DatagramPacket reply = new DatagramPacket(packet2.getData(),packet2.getLength(), packet2.getAddress(), packet2.getPort());
                            socket.send(reply);

                            byte[] buffer4 = new byte[256];
                            DatagramPacket packet4 = new DatagramPacket(buffer4, buffer4.length);
                            socket.receive(packet4);
                            String message4 = new String(packet4.getData(), 0, packet4.getLength());
                            int voto = Integer.valueOf(message4);
                            for(Eleicao elei: rmi.getListaEleicoes()){
                                if(elei.getTitulo().equals(eleicaoName)){
                                    for(int i=0;i<elei.getListaCandidatas().size();i++){
                                        if(i==voto){
                                            for(User user:rmi.getListaUsers()){
                                                if(user.getNumero().equals(this.numero)){
                                                    for(Departamento dep:rmi.getListaDepartamentos()){
                                                        if(dep.getNome().equals(mesa.getDepartamento().getNome())){
                                                            Voto v = new Voto(user,elei.getListaCandidatas().get(i),dep);
                                                            elei.addVoto(v);
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            while (true) {
                                try {
                                    if(port==4321) rmi.atualizaTerminais1(mesa,false,0);
                                    else rmi.atualizaTerminais1(mesa,false,1);
                                    break;
                                } catch (RemoteException re) {
                                    reconectarRMI();
                                }
                            }
                        }
                        else{
                            System.out.println("Erro no login!");
                            resposta = "Erro no login!";
                        }
                        break;
                    }
                }catch (SocketTimeoutException e){
                    String message = "lock";
                    byte[] buffer3 = message.getBytes();
                    InetAddress group2 = InetAddress.getByName(multicastGroup);
                    DatagramPacket packet = new DatagramPacket(buffer3, buffer3.length, group, port);
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



import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStreamReader;
import classes.*;

import static java.lang.System.exit;

public class MulticastServer extends Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;
    private Departamento departamento;
    private ArrayList<Eleicao> listaEleicoes;
    private Boolean estadoMesaVoto;
    static MulticastLibrary rmi;
    private int unlocks = 0;

    public MulticastServer(Departamento departamento) {
        this.departamento = departamento;
        this.listaEleicoes = new ArrayList<>();
        this.estadoMesaVoto = false;
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
               //desbloquear terminal durante 120 segundos
                MulticastReceiver receiver = new MulticastReceiver();
                receiver.start();
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
            while (true) {
                String message = "Connected to Multicast server";
                byte[] buffer = message.getBytes();
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
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4322;
    static MulticastLibrary rmi;

    public MulticastReceiver(MulticastLibrary rmi) throws RemoteException {
        super();
        this.rmi = rmi;
    }

    public MulticastReceiver() {
        super("Server " + (long) (Math.random() * 1000));
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
                        System.out.println("Avaria no RMI Multicast");
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
                System.out.println("olaaaa");
                verifica = rmi.userLogin(nome,pass);
                System.out.println(verifica);

                return verifica;
            } catch (RemoteException re) {
                reconectarRMI();
            }
        }
    }

    public void run() {
        MulticastSocket socket = null;
        MulticastServer server = new MulticastServer();
        server.start();
        try {
            MulticastLibrary rmi = (MulticastLibrary) Naming.lookup("RMI_Multicast");
            rmi.sayHello();
            MulticastReceiver multiserver = new MulticastReceiver(rmi);
            try {
                socket = new MulticastSocket(PORT);  // create socket and bind it
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                socket.joinGroup(group);
                while (true) {
                    byte[] buffer = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    System.out.println("Received message from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                    String message = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(message);
                    String [] login=message.split("\n");
                    boolean condicao = autenticacao(login[0],login[1]);
                    if(condicao){
                        System.out.println("Utilizador "+login[0]+" vai votar!");
                    }
                    else{
                        System.out.println("Erro no login!");
                    }
                    String resposta = "CONFIRMADO!";
                    packet.setData(resposta.getBytes());
                    DatagramPacket reply = new DatagramPacket(packet.getData(),packet.getLength(), packet.getAddress(), packet.getPort());
                    socket.send(reply);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }

        } catch (Exception e) {
            System.out.println("Exception in main MulticastServer: " + e.getMessage());
            e.printStackTrace();
        }

    }
}



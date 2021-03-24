import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import classes.*;

public class MulticastServer extends Thread implements Serializable {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;
    private Departamento departamento;
    private ArrayList<Eleicao> listaEleicoes;
    private Boolean estadoMesaVoto;

    public MulticastServer(Departamento departamento) {
        this.departamento = departamento;
        this.listaEleicoes = new ArrayList<>();
        this.estadoMesaVoto = false;
    }

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastReceiver receiver = new MulticastReceiver();
        receiver.start();
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
}

class MulticastReceiver extends Thread{
    private String MULTICAST_ADDRESS = "224.0.225.0";
    private int PORT = 4322;

    public MulticastReceiver() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
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
    }
}



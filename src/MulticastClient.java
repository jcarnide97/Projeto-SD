import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.Scanner;
import classes.*;

/**
 * The MulticastClient class joins a multicast group and loops receiving
 * messages from that group. The client also runs a MulticastUser thread that
 * loops reading a string from the keyboard and multicasting it to the group.
 * <p>
 * The example IPv4 address chosen may require you to use a VM option to
 * prefer IPv4 (if your operating system uses IPv6 sockets by default).
 * <p>
 * Usage: java -Djava.net.preferIPv4Stack=true MulticastClient
 *
 * @author Raul Barbosa
 * @version 1.0
 */
public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();

    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());

            byte[] buffer2 = "okay".getBytes();

            DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, group, PORT);
            socket.send(packet2);

            MulticastUser user = new MulticastUser(message);
            user.start();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private String groupAddr;
    private int port;
    private String state = "locked";

    public MulticastUser(String mensagem) {
        this.groupAddr = mensagem.split(",")[0];
        this.port=Integer.parseInt(mensagem.split(",")[1]);
        System.out.println("Vou conectar-me ao endereço: "+this.groupAddr+"\nporto: "+this.port);
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println(this.getName() + " ready...");
        try {
            socket = new MulticastSocket(port);  // create socket without binding it (only for sending)
            //Scanner keyboardScanner = new Scanner(System.in);
            String newState;
            while(true){
                while (true) {
                    newState = "lock";
                    socket = new MulticastSocket(port);  // create socket and bind it
                    InetAddress group = InetAddress.getByName(this.groupAddr);
                    socket.joinGroup(group);
                    byte[] buffer3 = new byte[256];
                    DatagramPacket packet2 = new DatagramPacket(buffer3, buffer3.length);
                    socket.receive(packet2);
                    newState = new String(packet2.getData(), 0, packet2.getLength());
                    System.out.println(newState);

                    if(newState.equals("unlock")){
                        socket = new MulticastSocket();
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader reader = new BufferedReader(input);
                        String nome;
                        String pass;
                        System.out.println("LOGIN ");
                        System.out.print("Nome: ");
                        long startTime = System.currentTimeMillis();
                        while((System.currentTimeMillis() - startTime)<120000 && !reader.ready()){}
                        if((System.currentTimeMillis() - startTime)<120000 && reader.ready()){
                            nome = reader.readLine();
                        }
                        else{
                            break;
                        }
                        System.out.print("password: ");
                        while((System.currentTimeMillis() - startTime)<120000 && !reader.ready()){}
                        if((System.currentTimeMillis() - startTime)<120000 && reader.ready()){
                            pass = reader.readLine();
                        }
                        else{
                            break;
                        }
                        String total = nome + "\n" +pass;
                        byte[] buffer = total.getBytes();
                        group = InetAddress.getByName(this.groupAddr);
                        socket.joinGroup(group);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
                        socket.send(packet);
                        byte[] buffer2 = new byte[1000];
                        DatagramPacket reply = new DatagramPacket(buffer2, buffer2.length);
                        try {
                            socket.receive(reply);
                            String listas = new String(reply.getData(), 0, reply.getLength());
                            if(!listas.equals("")){
                                String [] listasFinais = listas.split("\n");
                                int i;

                                for(i=0;i< listasFinais.length;i++){
                                    System.out.println("["+i+"]->"+listasFinais[i]);
                                }
                            }

                            break;
                        } catch (SocketTimeoutException e) {
                            System.out.println("À espera que algo aconteça...");
                            continue;
                        }
                    }
                    else{
                        System.out.println("Terminal bloqueado!\n============\n============\n============\n============");
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

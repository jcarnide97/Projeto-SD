import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import classes.*;

import javax.xml.crypto.Data;

public class RMIServer extends UnicastRemoteObject implements ServerLibrary, ClientLibrary,MulticastLibrary {
    private static final long serialVersionUID = 1L;
    private ArrayList<Eleicao> listaEleicoes;
    private ArrayList<Departamento> listaDepartamentos;
    private ArrayList<User> listaUsers;
    private ArrayList<MulticastServer> mesasVoto;
    private Map<String, String> usersAuth;
    private Map<String, String> usersAuth2;
    private ArrayList<String> loggedUsers;

    public RMIServer() throws RemoteException {
        super();
        this.listaEleicoes = new ArrayList<>();
        this.listaDepartamentos = new ArrayList<>();
        this.listaUsers = new ArrayList<>();
        this.mesasVoto = new ArrayList<>();
        this.usersAuth = new HashMap<String, String>();
        this.usersAuth2 = new HashMap<String, String>();
        this.loggedUsers = new ArrayList<>();
        startDatabase();
        System.out.println("Leitura dos ficheiros de utilizadores...");
        for (User user : listaUsers) {
            System.out.println(user.getNome());
            System.out.println(user.getNumero());
            System.out.println(user.getPassword());
            // Autenticação de users através do nome e password
            this.usersAuth.put(user.getNome(), user.getPassword());
            this.usersAuth2.put(user.getNome(), user.getNumero());
        }
        System.out.println("Leitura do ficheiro de departamentos...");
        for (Departamento dep : listaDepartamentos) {
            System.out.println(dep.getNome());
        }
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            currentDate.setTime(formatedDate.parse("25/03/2021 13:18"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Leitura do ficheiro de Eleições...");
        for (Eleicao eleicao : listaEleicoes) {
            eleicao.printEleicao();
            System.out.println("------Listas:");
            for(ListaCandidata lista: eleicao.getListaCandidatas()){
                System.out.println("------------"+lista.getNome());
            }
        }
    }

    synchronized public void sayHello() throws RemoteException {
        System.out.println("print do lado do servidor...");
    }

    synchronized public void addDepartamento(Departamento dep) {
        this.listaDepartamentos.add(dep);
        guardaDatabase();
    }

    synchronized public Boolean registarUser(User user) throws RemoteException {
        for (User u : this.listaUsers) {
            if (user.getNumero().equals(u.getNumero())) {
                return false;
            }
        }
        for (Departamento d : listaDepartamentos) {
            if ((user.getDepartamento().getNome().toUpperCase().equals(d.getNome().toUpperCase()))) {
                user.addUser(listaUsers, d);
            }
        }
        return false;
    }

    synchronized public ArrayList<User> getListaUsers() {
        return listaUsers;
    }

    synchronized public ArrayList<String> getLoggedUsers() {
        return loggedUsers;
    }

    synchronized public ArrayList<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    synchronized public void addEleicao(Eleicao eleicao) throws RemoteException {
        this.listaEleicoes.add(eleicao);
        guardaDatabase();
    }

    synchronized public ArrayList<Eleicao> getListaEleicoes() {
        return listaEleicoes;
    }

    synchronized public void removeEleicao(int i) {
        this.listaEleicoes.remove(i);
        guardaDatabase();
    }

    synchronized public ArrayList<MulticastServer> getMesasVoto() {
        return mesasVoto;
    }

    synchronized public void addMesaVoto(MulticastServer mesaVoto) {
        mesasVoto.add(mesaVoto);
        guardaDatabase();
    }

    synchronized public void atualizaMesaVoto(MulticastServer mesaVoto,boolean newState) {
        for(MulticastServer mesa:mesasVoto){
            if(mesa.getDepartamento().getNome().equals(mesaVoto.getDepartamento().getNome())){
                mesa.setEstadoMesaVoto(newState);
                break;
            }
        }
        guardaDatabase();
    }

    synchronized public void atualizaTerminais(MulticastServer mesaVoto,Boolean[] newState) throws RemoteException {
        for(MulticastServer mesa:mesasVoto){
            if(mesa.getDepartamento().getNome().equals(mesaVoto.getDepartamento().getNome())){
                mesa.terminais=newState;
                break;
            }
        }
        guardaDatabase();
    }

    synchronized public void atualizaTerminais1(MulticastServer mesaVoto,Boolean newState, int indice) throws RemoteException {
        for(MulticastServer mesa:mesasVoto){
            if(mesa.getDepartamento().getNome().equals(mesaVoto.getDepartamento().getNome())){
                mesa.terminais[indice]=newState;
                break;
            }
        }
        guardaDatabase();
    }

    synchronized public void setEndereco(MulticastServer mesaVoto,String address) throws RemoteException {
        for(MulticastServer mesa:mesasVoto){
            if(mesa.getDepartamento().getNome().equals(mesaVoto.getDepartamento().getNome())){
                mesa.groupAddr = address;
                mesa.terminais= new Boolean[]{false, false};
                mesa.auxterminais = new Boolean[]{false, false};
                break;
            }
        }
        guardaDatabase();
    }

    synchronized public void setLigados(MulticastServer mesaVoto,int indice, boolean newstate) throws RemoteException {
        for(MulticastServer mesa:mesasVoto){
            if(mesa.getDepartamento().getNome().equals(mesaVoto.getDepartamento().getNome())){
                mesa.auxterminais[indice]=newstate;
                break;
            }
        }
        guardaDatabase();
    }

    synchronized public void removeMesaVoto(int i) {
        this.mesasVoto.remove(i);
        guardaDatabase();
    }

    public boolean userAuth(String nome, String numero) throws RemoteException {
        System.out.println("Autenticar " + nome + " na database...");
        boolean valido;
        valido = usersAuth2.containsKey(nome) && usersAuth2.get(nome).equals(numero);
        return valido;
    }

    public boolean userLogin(String nome, String password) throws RemoteException {
        System.out.println("Verificar " + nome + " na database...");
        boolean valido;
        valido = usersAuth.containsKey(nome) && usersAuth.get(nome).equals(password);
        if (valido) {
            loggedUsers.add(nome);
        }
        return valido;
    }

    public void startDatabase() {
        ObjectInputStream oisUsers = null;
        ObjectInputStream oisDepartamentos = null;
        ObjectInputStream oisEleicoes = null;
        ObjectInputStream oisMesasVoto = null;
        try {
            FileInputStream fis = new FileInputStream("database/users.obj");
            oisUsers = new ObjectInputStream(fis);
            this.listaUsers = (ArrayList<User>) oisUsers.readObject();
            fis = new FileInputStream("database/departamentos.obj");
            oisDepartamentos = new ObjectInputStream(fis);
            this.listaDepartamentos = (ArrayList<Departamento>) oisDepartamentos.readObject();
            fis = new FileInputStream("database/eleicoes.obj");
            oisEleicoes = new ObjectInputStream(fis);
            this.listaEleicoes = (ArrayList<Eleicao>) oisEleicoes.readObject();
            fis = new FileInputStream("database/mesasVoto.obj");
            oisMesasVoto = new ObjectInputStream(fis);
            this.mesasVoto = (ArrayList<MulticastServer>) oisMesasVoto.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oisUsers != null && oisDepartamentos != null && oisEleicoes != null && oisMesasVoto != null) {
                try {
                    oisUsers.close();
                    oisDepartamentos.close();
                    oisEleicoes.close();
                    oisMesasVoto.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void guardaDatabase() {
        FileOutputStream fosUsers;
        ObjectOutputStream oosUsers = null;
        FileOutputStream fosDepartamentos;
        ObjectOutputStream oosDepartamentos = null;
        FileOutputStream fosEleicoes;
        ObjectOutputStream oosEleicoes = null;
        FileOutputStream fosMesasVoto;
        ObjectOutputStream oosMesasVoto = null;
        try {
            fosUsers = new FileOutputStream("database/users.obj");
            oosUsers = new ObjectOutputStream(fosUsers);
            oosUsers.writeObject(listaUsers);
            fosDepartamentos = new FileOutputStream("database/departamentos.obj");
            oosDepartamentos = new ObjectOutputStream(fosDepartamentos);
            oosDepartamentos.writeObject(listaDepartamentos);
            fosEleicoes = new FileOutputStream("database/eleicoes.obj");
            oosEleicoes = new ObjectOutputStream(fosEleicoes);
            oosEleicoes.writeObject(listaEleicoes);
            fosMesasVoto = new FileOutputStream("database/mesasVoto.obj");
            oosMesasVoto = new ObjectOutputStream(fosMesasVoto);
            oosMesasVoto.writeObject(mesasVoto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oosUsers != null && oosDepartamentos != null && oosEleicoes != null && oosMesasVoto != null) {
                try {
                    oosUsers.close();
                    oosDepartamentos.close();
                    oosEleicoes.close();
                    oosMesasVoto.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void udpServerConnection() {
        new Thread(new UDPServer()).start();
    }

    public static void main(String[] args) throws RemoteException {
        RMIServer rmiServer = null;
        try {
            Registry rmiRegistry = LocateRegistry.createRegistry(7000);
            rmiServer = new RMIServer();
            for (MulticastServer mesa : rmiServer.mesasVoto) {
                mesa.setEstadoMesaVoto(false);
            }
            rmiRegistry.rebind("RMI_Server", rmiServer);
            rmiServer.udpServerConnection();
            System.out.println("Primary RMI Server is ready...");
        } catch (RemoteException re) {
            System.out.println("Exception in Primary RMI Server: " + re.getMessage());
            try {
                DatagramSocket cSocket = null;
                try {
                    cSocket = new DatagramSocket();
                    while (true) {
                        InetAddress IPAddress = InetAddress.getByName("localhost");
                        byte[] sendData;
                        byte[] receiveData = new byte[1024];
                        sendData = "ping".getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6000);
                        cSocket.send(sendPacket);
                        cSocket.setSoTimeout(5000);  // timeout de resposta do servidor primário de 5 segundos
                        try {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            cSocket.receive(receivePacket);
                            Thread.sleep(5000);
                            String s = new String(receivePacket.getData(), 0, receivePacket.getLength());
                            System.out.println("Primary RMI Server: " + s);
                        } catch (SocketTimeoutException e) {
                            cSocket.close();
                            System.out.println("Timeout no Primary RMI Server: " + e.getMessage());
                            try {
                                rmiServer = new RMIServer();
                                Registry rmiRegistry = LocateRegistry.createRegistry(7000);
                                rmiRegistry.rebind("RMI_Server", rmiServer);
                                rmiServer.udpServerConnection();
                                System.out.println("Secondary RMI Server ready...\nNow I'm the Primary RMI Server");
                            } catch (RemoteException re2) {
                                System.out.println(re2.getMessage());
                            }
                        }
                    }
                } catch (SocketException e) {
                    System.out.println("Socket: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IO: " + e.getMessage());
                } finally {
                    if (cSocket != null) {
                        cSocket.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class UDPServer implements Runnable {

    public UDPServer() {
    }

    @Override
    public void run() {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(6000);
            byte[] receiveData = new byte[1024];
            byte[] sendData;
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                aSocket.receive(receivePacket);
                String s = new String(receivePacket.getData());
                sendData = "pong".getBytes();  // servidor primário envia msg a secundário a dizer que está tudo bem
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                aSocket.send(sendPacket);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}

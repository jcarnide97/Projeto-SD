import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import classes.*;

public class RMIServer extends UnicastRemoteObject implements ServerLibrary, ClientLibrary {
    private ArrayList<Eleicao> listaEleicoes;
    private ArrayList<Departamento> listaDepartamentos;
    private ArrayList<User> listaUsers;
    private ArrayList<MulticastServer> mesasVoto;
    private Map<String, String> users;
    private ArrayList<String> loggedUsers;

    public RMIServer() throws RemoteException {
        super();
    }

    synchronized public void sayHello() throws RemoteException {
        System.out.println("print do lado do servidor...");
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

    synchronized public void removeMesaVoto(int i) {
        this.mesasVoto.remove(i);
        guardaDatabase();
    }
/*
    public void subscribe(String name, ClientLibrary client) throws RemoteException {
        System.out.println("Subscribing " + name);
        System.out.print("> ");
        adminConsole = client;

 */

    public void startDatabase() {

    }

    public void guardaDatabase() {

    }

    public static void main(String[] args) throws RemoteException {
        /*
        String teste;

        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try {
            RMIServer rmiServer = new RMIServer();
            Naming.rebind("RMI_Server", rmiServer);
            System.out.println("RMI Server is ready...");
            while (true) {
                teste = reader.readLine();
                adminConsole.print_on_client(teste);
            }
        } catch (Exception re) {
            System.out.println("Exception in main: " + re);
        }
         */
    }

}

import java.io.*;
import java.net.MalformedURLException;
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

public class RMIServer extends UnicastRemoteObject implements ServerLibrary, ClientLibrary {
    private static final long serialVersionUID = 1L;
    private ArrayList<Eleicao> listaEleicoes;
    private ArrayList<Departamento> listaDepartamentos;
    private ArrayList<User> listaUsers;
    private ArrayList<MulticastServer> mesasVoto;
    private Map<String, String> users;
    private ArrayList<String> loggedUsers;

    public RMIServer() throws RemoteException {
        super();
        this.listaEleicoes = new ArrayList<>();
        this.listaDepartamentos = new ArrayList<>();
        this.listaUsers = new ArrayList<>();
        this.mesasVoto = new ArrayList<>();
        this.users = new HashMap<String, String>();
        this.loggedUsers = new ArrayList<>();
        startDatabase();
        System.out.println("Leitura dos ficheiros de utilizadores...");
        for (User user : listaUsers) {
            System.out.println(user.getNome());
            System.out.println(user.getNumero());
            System.out.println(user.getPassword());
            // Autenticação de users através do numero e password
            this.users.put(user.getNumero(), user.getPassword());
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

    public static void main(String[] args) throws RemoteException {
        try {
            RMIServer rmiServer = new RMIServer();
            Naming.rebind("RMI_Server", rmiServer);
            System.out.println("RMI Server is ready...");
        } catch (RemoteException re) {
            System.out.println("Exception in RMIServer.main: " + re);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException in RMIServer.main: " + e);
        }
    }

}

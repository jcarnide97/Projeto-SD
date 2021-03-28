import classes.Departamento;
import classes.Eleicao;
import classes.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MulticastLibrary extends Remote {
    public void addDepartamento(Departamento dep) throws RemoteException;
    public Boolean registarUser(User user) throws RemoteException;
    public ArrayList<User> getListaUsers() throws RemoteException;
    void sayHello() throws RemoteException;
    public ArrayList<Departamento> getListaDepartamentos() throws RemoteException;
    public void addEleicao(Eleicao eleicao) throws RemoteException;
    public ArrayList<Eleicao> getListaEleicoes() throws RemoteException;
    public void removeEleicao(int i) throws RemoteException;
    public ArrayList<MulticastServer> getMesasVoto() throws RemoteException;
    public void addMesaVoto(MulticastServer mesaVoto) throws RemoteException;
    public void removeMesaVoto(int i) throws RemoteException;
    public boolean userLogin(String nome, String password) throws RemoteException;
    public boolean userAuth(String nome, String numero) throws RemoteException;
}

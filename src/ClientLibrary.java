import java.rmi.*;
import java.text.ParseException;
import java.util.ArrayList;

import classes.*;

public interface ClientLibrary extends Remote {
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
    public ArrayList<String> getLoggedUsers() throws RemoteException;
    public void numAtualVotos(Eleicao eleicao) throws RemoteException;
}

package meta1;

import meta1.classes.Departamento;
import meta1.classes.Eleicao;
import meta1.classes.User;
import meta1.classes.Voto;

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
    public void atualizaMesaVoto(MulticastServer mesaVoto,boolean newState) throws RemoteException;
    public void setEndereco(MulticastServer mesaVoto,String address) throws RemoteException;
    public boolean userAuth(String nome, String numero, String tipo, Eleicao eleicao) throws RemoteException;
    public void setLigados(MulticastServer mesaVoto,int indice, boolean newstate) throws RemoteException;
    public void atualizaTerminais(MulticastServer mesaVoto,Boolean[] newState) throws RemoteException;
    public ArrayList<String> getLoggedUsers() throws RemoteException;
    public void atualizaTerminais1(MulticastServer mesaVoto,Boolean newState, int indice) throws RemoteException;
    public Boolean checkEleicaoTime(Eleicao eleicao) throws RemoteException;
    public void logout(String nome, String password) throws RemoteException;
    public void addVotos(Eleicao elei, Voto voto)throws  RemoteException;

    public Departamento getDepartamento(String departamento) throws RemoteException;
}

package model;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import meta1.ClientLibrary;
import meta1.MulticastLibrary;
import meta1.classes.Departamento;

public class eVotingBean extends UnicastRemoteObject {
    private static final long serialVersionUID = 1L;
    private MulticastLibrary rmi;
    private String nome;
    private String password;

    public eVotingBean() throws RemoteException {
        this.ligarRMI();
    }

    private void ligarRMI() {
        boolean check = false;
        while (!check) {
            try {
                this.rmi = (MulticastLibrary) Naming.lookup("rmi://localhost:7000/RMI_Server");
                check = true;
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                System.out.println("Exception in Primary RMI Server: " + e.getMessage());
            }
        }
    }

    public boolean userLogin() throws RemoteException {
        return rmi.userLogin(this.nome, this.password);
    }

    public Departamento getDepartamento(String departamento) throws RemoteException {
        return rmi.getDepartamento(departamento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void logout(String nome) {
        try {
            this.rmi.logout(nome);
        } catch (RemoteException re) {
            re.printStackTrace();
        }
    }
}

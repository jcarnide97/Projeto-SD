package model;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import meta1.ClientLibrary;
import meta1.MulticastLibrary;

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

}

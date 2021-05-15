package model;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import meta1.ClientLibrary;
import meta1.MulticastLibrary;
import meta1.MulticastServer;
import meta1.classes.Departamento;
import meta1.classes.Eleicao;
import meta1.classes.ListaCandidata;
import meta1.classes.User;

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

    public Departamento getDepartamento(String departamento) throws RemoteException {
        return rmi.getDepartamento(departamento);
    }

    public boolean criarUser(User user) throws RemoteException {
        boolean res = rmi.registarUser(user);
        if (res) {
            rmi.addUser(user.getNome(), user.getPassword());
        }
        return res;
    }

    public boolean criarDepartamento(String nomeDep) {
        ArrayList<Departamento> departamentos = null;
        try {
            departamentos = this.rmi.getListaDepartamentos();
            for (Departamento dep : departamentos) {
                if ((nomeDep.toUpperCase().equals(dep.getNome().toUpperCase()))) {
                    System.out.println("Departamento já existe");
                    return false;
                }
            }
            Departamento departamento = new Departamento(nomeDep, new ArrayList<>());
            this.rmi.addDepartamento(departamento);
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        return true;
    }

    public boolean criaEleicao(String titulo, String descricao, Date dataComeco, Date dataFim, String tipo, ArrayList<Departamento> listaDepartamento, ArrayList<ListaCandidata> listaCandidatas) throws RemoteException{
        Eleicao eleicao = new Eleicao(dataComeco, dataFim, titulo, descricao, tipo, listaDepartamento, listaCandidatas);
        this.rmi.addEleicao(eleicao);
        return true;
    }

    public boolean addListaCandidata(String nomeLista, String tituloEleicao) {
        try {
            return rmi.addListaCandidata(nomeLista, tituloEleicao);
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        return false;
    }

    public boolean criaMesaVoto(String departamento) {
        try {
            ArrayList<MulticastServer> mesasVoto = this.rmi.getMesasVoto();
            for (MulticastServer mesaVoto : mesasVoto) {
                if (mesaVoto.getDepartamento().getNome().toUpperCase().equals(departamento.toUpperCase())) {
                    System.out.println("Departamento já com uma mesa associada");
                    return false;
                }
            }
            Departamento dep = rmi.getDepartamento(departamento);
            if (dep != null) {
                MulticastServer novaMesaVoto = new MulticastServer(dep);
                rmi.addMesaVoto(novaMesaVoto);
                return true;
            }
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        return false;
    }

    public boolean removeMesaVoto(String departamento) {
        try {
            ArrayList<MulticastServer> mesasVoto = this.rmi.getMesasVoto();
            int i = 0;
            for (MulticastServer mesaVoto : mesasVoto) {
                if (mesaVoto.getDepartamento().getNome().toUpperCase().equals(departamento.toUpperCase())) {
                    rmi.removeMesaVoto(i);
                    return true;
                }
                i++;
            }
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        return false;
    }

    public ArrayList<Eleicao> getListaEleicoes() throws RemoteException {
        return this.rmi.getListaEleicoes();
    }

    public ArrayList<MulticastServer> getMesasVoto() throws RemoteException {
        return this.rmi.getMesasVoto();
    }
}

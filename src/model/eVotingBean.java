package model;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import meta1.MulticastLibrary;
import meta1.MulticastServer;
import meta1.classes.*;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuthService;

import uc.sd.apis.FacebookApi2;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;

public class eVotingBean extends UnicastRemoteObject {
    private static final long serialVersionUID = 1L;
    private MulticastLibrary rmi;
    private String nome;
    private String password;
    private OAuthService service;

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

    public String devolveOauth(){
        Token EMPTY_TOKEN = null;
        // Replace these with your own api key and secret
        String apiKey = "482895983046573";
        String apiSecret = "secretKey";

        service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("https://eden.dei.uc.pt/~fmduarte/echo.php") // Do not change this.
                .scope("public_profile")
                .build();

        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        return authorizationUrl;
    }

    public void ligaFace(String code,User user) throws RemoteException {
        String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
        Token EMPTY_TOKEN = null;
        Verifier verifier = new Verifier(code);
        System.out.println();
        // Trade the Request Token and Verfier for the Access Token
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        // Now let's go and ask for a protected resource!
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        String coisas= response.getBody();
        String[] parts = coisas.split("\"");
        this.rmi.setFacebook(parts[7],user);
    }

    public User loginFace(String code)throws RemoteException{
        String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
        Token EMPTY_TOKEN = null;
        Verifier verifier = new Verifier(code);
        System.out.println();
        // Trade the Request Token and Verfier for the Access Token
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

        // Now let's go and ask for a protected resource!
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        String coisas= response.getBody();
        String[] parts = coisas.split("\"");

        ArrayList<User> todos = this.rmi.getListaUsers();
        User utiliza = null;
        for(User user:todos){
            if(user.getFaceId().equals(parts[7])){
                utiliza = user;
                break;
            }
        }
        if(utiliza!=null){
            return utiliza;
        }
        return null;
    }

    public User getUser(String nome, String password) throws RemoteException{
       ArrayList<User> all = rmi.getListaUsers();
       for(User utilizador : all){
           if(utilizador.getNome().equals(nome) && utilizador.getPassword().equals(password)){
               return utilizador;
           }
       }
       return null;
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
                System.out.println(novaMesaVoto.getDepartamento().getNome() + "adicionado às mesas de voto");
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
                    System.out.println("Mesa removida com sucesso");
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

    public void addVoto(Eleicao elei,Voto voto) throws RemoteException {
        this.rmi.addVotos(elei,voto);
    }

    public Eleicao getEleicao(String nomeEleicao) throws RemoteException{
        ArrayList<Eleicao> eleicoes= this.rmi.getListaEleicoes();
        for(Eleicao eleicao: eleicoes){
            if(eleicao.getTitulo().equals(nomeEleicao)){
                return eleicao;
            }
        }
        return null;
    }
    public ListaCandidata getLista(Eleicao eleicao,String lista) throws RemoteException{
       Eleicao elei = this.getEleicao(eleicao.getTitulo());
       for(ListaCandidata listas:elei.getListaCandidatas()){
           if(listas.getNome().equals(lista)){
               return listas;
           }

       }
       return null;
    }
    public ArrayList<MulticastServer> getMesasVoto() throws RemoteException {
        return this.rmi.getMesasVoto();
    }

    public Eleicao removeEleicao(String nomeEleicao) throws RemoteException {
        Eleicao eleicao;
        ArrayList<Eleicao> listaEleicoes;
        int iEleicao;
        while (true) {
            try {
                listaEleicoes = this.rmi.getListaEleicoes();
                break;
            } catch (RemoteException re) {
                this.ligarRMI();
            }
        }
        for (int i = 0; i < listaEleicoes.size(); i++) {
            if (nomeEleicao.toLowerCase().equals(listaEleicoes.get(i).getTitulo().toLowerCase())) {
                iEleicao = i;
                eleicao = listaEleicoes.get(i);
                this.rmi.removeEleicao(iEleicao);
                return eleicao;
            }
        }
        return null;
    }

    public boolean addEleicao(Eleicao eleicao) {
        while (true) {
            try {
                this.rmi.addEleicao(eleicao);
                return true;
            } catch (RemoteException re) {
                this.ligarRMI();
            }
        }
    }

    public String getOnlineUsers() throws RemoteException{
        ArrayList<String> onlineUsers = this.rmi.getLoggedUsers();
        String prevLogged = "";
        for (String user : onlineUsers) {
            prevLogged += user + "já loggado";
        }
        return prevLogged;
    }
}

package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaCandidata implements Serializable {
    private String nome;
    private ArrayList<User> membrosLista;
    public int votos;

    public ListaCandidata() {
    }

    public ListaCandidata(String nome) {
        this.nome = nome;
        this.votos = 0;
    }

    public ListaCandidata(String nome, ArrayList<User> membrosLista) {
        this.nome = nome;
        this.membrosLista = membrosLista;
    }

    public void increaseVotes(){
        this.votos++;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<User> getMembrosLista() {
        return membrosLista;
    }
}

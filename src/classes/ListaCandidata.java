package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaCandidata implements Serializable {
    private String nome;
    private ArrayList<User> membrosLista;

    public ListaCandidata() {
    }

    public ListaCandidata(String nome) {
        this.nome = nome;
    }

    public ListaCandidata(String nome, ArrayList<User> membrosLista) {
        this.nome = nome;
        this.membrosLista = membrosLista;
    }


    public String getNome() {
        return nome;
    }

    public ArrayList<User> getMembrosLista() {
        return membrosLista;
    }
}

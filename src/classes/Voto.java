package classes;

import java.io.Serializable;

public class Voto implements Serializable {
    private User eleitor;
    private ListaCandidata escolhaVoto;
    private Departamento localVoto;

    public Voto(User eleitor, ListaCandidata escolhaVoto, Departamento localVoto) {
        this.eleitor = eleitor;
        this.escolhaVoto = escolhaVoto;
        this.localVoto = localVoto;
    }

    public User getEleitor() {
        return eleitor;
    }

    public ListaCandidata getEscolhaVoto() {
        return escolhaVoto;
    }

    public Departamento getLocalVoto() {
        return localVoto;
    }
}

package classes;

import java.io.Serializable;
import java.util.Date;

public class Voto implements Serializable {
    private User eleitor;
    private ListaCandidata escolhaVoto;
    private Departamento localVoto;
    private Date horaVoto;

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

    public Date getHoraVoto() {
        return horaVoto;
    }
}

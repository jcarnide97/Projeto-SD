package meta1.classes;

import java.io.Serializable;
import java.util.Date;

public class Voto implements Serializable {
    private User eleitor;
    private ListaCandidata escolhaVoto;
    private Departamento localVoto;
    private String votoOnline;
    private Date horaVoto;

    public Voto(User eleitor, ListaCandidata escolhaVoto, Departamento localVoto, Date horaVoto) {
        this.eleitor = eleitor;
        this.escolhaVoto = escolhaVoto;
        this.localVoto = localVoto;
        this.horaVoto = horaVoto;
    }
    public Voto(User eleitor, ListaCandidata escolhaVoto, String votoOnline, Date horaVoto) {
        this.eleitor = eleitor;
        this.escolhaVoto = escolhaVoto;
        this.localVoto = null;
        this.votoOnline = votoOnline;
        this.horaVoto = horaVoto;
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

    public String getVotoOnline(){
        return this.votoOnline;
    }

    public Date getHoraVoto() {
        return horaVoto;
    }
}

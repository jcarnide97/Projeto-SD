package action;

import meta1.classes.Eleicao;
import meta1.classes.ListaCandidata;
import meta1.classes.User;
import meta1.classes.Voto;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

public class VotarTotalAction extends Action implements SessionAware {
        private String lista = "";
    @Override
    public String execute() throws RemoteException {
        if (this.lista != "") {
            Date date = new Date(System.currentTimeMillis());
            ListaCandidata listaTotal = this.getEVotingBean().getLista((Eleicao)this.session.get("eleicao"),this.lista);
            Voto v = new Voto((User)this.session.get("utilizador"),listaTotal,"online",date);
            this.getEVotingBean().addVoto((Eleicao)this.session.get("eleicao"),v);
            System.out.println("votou em "+listaTotal.getNome()+" eleicao: "+((Eleicao)this.session.get("eleicao")).getTitulo());
            return SUCCESS;
        }
        return ERROR;
    }

    public void setLista(String lista){
        this.lista = lista;
    }

    public String getLista(){
        return this.lista;
    }
}

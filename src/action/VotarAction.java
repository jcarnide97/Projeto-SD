package action;

import meta1.classes.Eleicao;
import meta1.classes.ListaCandidata;
import meta1.classes.Voto;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VotarAction extends Action implements SessionAware {
        private String eleicao = "";
    @Override
    public String execute() throws RemoteException {
        if (this.eleicao != "") {
            System.out.println(this.eleicao);
            Eleicao elei = this.getEVotingBean().getEleicao(this.eleicao);
            if(elei!=null){
                ArrayList<ListaCandidata> listas = elei.getListaCandidatas();
                this.session.put("listas",listas);
                return SUCCESS;
            }
            return ERROR;
        }
        return ERROR;
    }

    public void setEleicao(String eleicao){
        this.eleicao = eleicao;
    }

    public String getEleicao(){
        return this.eleicao;
    }
}

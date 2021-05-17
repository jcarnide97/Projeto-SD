package action;

import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;

public class CriarListaCandidataAction extends Action implements SessionAware {
    private String nomeLista = null;
    private String tituloEleicao = null;

    @Override
    public String execute() {
        if ((this.nomeLista != null && !nomeLista.equals("")) || (this.tituloEleicao != null && !tituloEleicao.equals(""))) {
            try {
                if (this.getEVotingBean().addListaCandidata(nomeLista, tituloEleicao)) {
                    return SUCCESS;
                }
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }
        return ERROR;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getTituloEleicao() {
        return tituloEleicao;
    }

    public void setTituloEleicao(String tituloEleicao) {
        this.tituloEleicao = tituloEleicao;
    }
}

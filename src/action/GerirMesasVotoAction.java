package action;

import org.apache.struts2.interceptor.SessionAware;

/**
 * Action para criar ou remover uma mesa de voto (com base nos departamentos existentes)
 */
public class GerirMesasVotoAction extends Action implements SessionAware {
    private String criaMesa = null;
    private String removeMesa = null;

    @Override
    public String execute() throws Exception {
        if (criaMesa != null && !criaMesa.equals("")) {
            if (this.getEVotingBean().criaMesaVoto(criaMesa)) {
                return SUCCESS;
            }
        }
        else if (removeMesa != null && !removeMesa.equals("")) {
            if (this.getEVotingBean().removeMesaVoto(removeMesa)) {
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public String getCriaMesa() {
        return criaMesa;
    }

    public void setCriaMesa(String criaMesa) {
        this.criaMesa = criaMesa;
    }

    public String getRemoveMesa() {
        return removeMesa;
    }

    public void setRemoveMesa(String removeMesa) {
        this.removeMesa = removeMesa;
    }
}

package action;

import org.apache.struts2.interceptor.SessionAware;

/**
 * Action para criar um novo departamento
 */
public class CriarDepartamentoAction extends Action implements SessionAware {
    private String nomeDep = null;

    @Override
    public String execute() throws Exception {
        if (nomeDep != null && !nomeDep.equals("")) {
            if (this.getEVotingBean().criarDepartamento(nomeDep)) {
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public String getNomeDep() {
        return nomeDep;
    }

    public void setNomeDep(String nomeDep) {
        this.nomeDep = nomeDep;
    }
}

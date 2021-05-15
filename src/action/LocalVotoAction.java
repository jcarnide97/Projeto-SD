package action;

import meta1.classes.Eleicao;
import meta1.classes.Voto;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;

public class LocalVotoAction extends Action implements SessionAware {
    private String numero = null;
    private String listaLocais = "";

    @Override
    public String execute() {
        if (this.numero != null && !numero.equals("")) {
            try {
                for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                    listaLocais += "Eleição: " + eleicao.getTitulo() + "\n";
                    for (Voto voto : eleicao.getListaVotos()) {
                        if (voto.getEleitor().getNumero().equals(numero)) {
                            listaLocais += "Nome: " + voto.getEleitor().getNome() + "\n";
                            listaLocais += "Local Voto: " + voto.getLocalVoto().getNome() + "\n";
                            listaLocais += "Hora Voto: " + voto.getHoraVoto() + "\n";
                        }
                    }
                    listaLocais += "--------------------------------------------\n";
                }
                return SUCCESS;
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }
        return ERROR;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getListaLocais() {
        return listaLocais;
    }

    public void setListaLocais(String listaLocais) {
        this.listaLocais = listaLocais;
    }
}

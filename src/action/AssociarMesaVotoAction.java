package action;

import meta1.MulticastServer;
import meta1.classes.Eleicao;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;

public class AssociarMesaVotoAction extends Action implements SessionAware {
    private String tituloEleicao = null;
    private String nomeDep = null;

    @Override
    public String execute() throws Exception {
        ArrayList<Eleicao> eleicoes = this.getEVotingBean().getListaEleicoes();
        for (Eleicao eleicao : eleicoes) {
            if (tituloEleicao.toLowerCase().equals(eleicao.getTitulo().toLowerCase())) {
                for (MulticastServer mesaVoto : this.getEVotingBean().getMesasVoto()) {
                    if (nomeDep.toUpperCase().equals(mesaVoto.getDepartamento().getNome().toUpperCase())) {
                        if (!mesaVoto.getListaEleicoes().contains(mesaVoto)) {
                            mesaVoto.addEleicao(eleicao);
                            System.out.println("Mesa " + mesaVoto.getDepartamento().getNome() + " adicionada à eleição " + eleicao.getTitulo());
                            return SUCCESS;
                        }
                    }
                }
            }
        }
        return ERROR;
    }

    public String getTituloEleicao() {
        return tituloEleicao;
    }

    public void setTituloEleicao(String tituloEleicao) {
        this.tituloEleicao = tituloEleicao;
    }

    public String getNomeDep() {
        return nomeDep;
    }

    public void setNomeDep(String nomeDep) {
        this.nomeDep = nomeDep;
    }
}

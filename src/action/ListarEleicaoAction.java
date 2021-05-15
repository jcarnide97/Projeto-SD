package action;

import meta1.classes.Departamento;
import meta1.classes.Eleicao;
import meta1.classes.ListaCandidata;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;

public class ListarEleicaoAction extends Action implements SessionAware {
    private String titulo = null;
    private String detalhesEleicao = "";

    @Override
    public String execute() {
        if (this.titulo != null && !titulo.equals("")) {
            try {
                detalhesEleicao += "----------Detalhes----------\n";
                for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                    if (eleicao.getTitulo().toLowerCase().equals(titulo.toLowerCase())) {
                        detalhesEleicao += "Titulo: " + eleicao.getTitulo() + "\n";
                        detalhesEleicao += "Descrição: " + eleicao.getDescricao() + "\n";
                        detalhesEleicao += "Data Inicio: " + eleicao.getDataComeco() + "\n";
                        detalhesEleicao += "Data Fim: " + eleicao.getDataFim() + "\n";
                        detalhesEleicao += "Eleição para " + eleicao.getTipo() + "s\n";
                        detalhesEleicao += "Listas Candidatas:\n";
                        for (ListaCandidata lista : eleicao.getListaCandidatas()) {
                            if (lista.getNome().equals("Voto em Branco") || lista.getNome().equals("Voto Nulo")) {
                                System.out.println("Ignorar");
                            } else {
                                detalhesEleicao += "-> " + lista.getNome() + "\n";
                            }
                        }
                    }
                }
                return SUCCESS;
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }
        return ERROR;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalhesEleicao() {
        return detalhesEleicao;
    }

    public void setDetalhesEleicao(String detalhesEleicao) {
        this.detalhesEleicao = detalhesEleicao;
    }
}

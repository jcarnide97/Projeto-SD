package action;

import meta1.classes.Eleicao;
import meta1.classes.ListaCandidata;
import meta1.classes.Voto;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class EleicoesPassadasAction extends Action implements SessionAware {
    private String titulo = null;
    public String resultados = "";

    @Override
    public String execute() throws Exception {
        if (this.titulo != null && !titulo.equals("")) {
            try {
                for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                    if (titulo.toLowerCase().equals(eleicao.getTitulo())) {
                        String res = "Resultados das Eleições:\n";
                        float totalVotos = eleicao.getListaVotos().size();
                        res += "Total de votos: " + (int)totalVotos + "\n";
                        float percVotos;
                        for (ListaCandidata listaCandidata : eleicao.getListaCandidatas()) {
                            int conta = 0;
                            for (Voto voto : eleicao.getListaVotos()) {
                                if (voto.getEscolhaVoto().getNome().equals(listaCandidata.getNome())) {
                                    conta++;
                                }
                            }
                            if (listaCandidata.getNome().equals("Voto em Branco")) {
                                res += "\tNúmero de votos em branco = " + conta + "\n";
                                percVotos = (conta/totalVotos)*100;
                                res += "\tPercentagem de votos em branco = " + percVotos + "\n";
                            } else if (listaCandidata.getNome().equals("Voto Nulo")) {
                                res += "\tNúmero de votos nulos = " + conta + "\n";
                                percVotos = (conta/totalVotos)*100;
                                res += "\tPercentagem de votos nulos = " + percVotos + "\n";
                            } else {
                                res += "\tNúmero de votos da lista " + listaCandidata.getNome() + " = " + conta +"\n";
                                percVotos = (conta/totalVotos)*100;
                                res += "\tPercentagem de votos da lista " + listaCandidata.getNome() + " = " + percVotos + "\n";
                            }
                        }
                        resultados += "Titulo: " + eleicao.getTitulo() + "\n";
                        resultados += "Descrição: " + eleicao.getDescricao() + "\n";
                        resultados += "Data Início: " + eleicao.getDataComeco() + "\n";
                        resultados += "Data Fim: " + eleicao.getDataFim() + "\n";
                        resultados += "Eleições para " + eleicao.getTipo() + "s\n";
                        resultados += res;
                        return SUCCESS;
                    }
                }
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

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public int contaVotos(String listaCadidata, ArrayList<Voto> votos) {
        int nVotos = 0;
        for (Voto voto : votos) {
            if (voto.getEscolhaVoto().equals(listaCadidata)) {
                nVotos++;
            }
        }
        return nVotos;
    }
}

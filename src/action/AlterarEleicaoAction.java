package action;

import meta1.classes.Eleicao;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlterarEleicaoAction extends Action implements SessionAware {
    private String titulo = null;
    private String novoTitulo = null;
    private String novaDescricao = null;
    private String novoInicio = null;
    private String novoFim = null;

    @Override
    public String execute() throws Exception {
        Eleicao editEleicao;
        if (titulo != null && !titulo.equals("")) {
            for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                if (titulo.toLowerCase().equals(eleicao.getTitulo().toLowerCase())) {
                    if (!eleicao.votacaoAberta() && eleicao.votacaoAcabou()) {
                        editEleicao = this.getEVotingBean().removeEleicao(titulo);
                        Date dataComeco = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(novoInicio);
                        Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(novoFim);
                        eleicao.setTitulo(novoTitulo);
                        eleicao.setDescricao(novaDescricao);
                        eleicao.setDataComeco(dataComeco);
                        eleicao.setDataFim(dataFim);
                        this.getEVotingBean().addEleicao(eleicao);
                        return SUCCESS;
                    } else {
                        System.out.println("Eleição a decorrer ou já terminada");
                    }
                }
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

    public String getNovoTitulo() {
        return novoTitulo;
    }

    public void setNovoTitulo(String novoTitulo) {
        this.novoTitulo = novoTitulo;
    }

    public String getNovaDescricao() {
        return novaDescricao;
    }

    public void setNovaDescricao(String novaDescricao) {
        this.novaDescricao = novaDescricao;
    }

    public String getNovoInicio() {
        return novoInicio;
    }

    public void setNovoInicio(String novoInicio) {
        this.novoInicio = novoInicio;
    }

    public String getNovoFim() {
        return novoFim;
    }

    public void setNovoFim(String novoFim) {
        this.novoFim = novoFim;
    }
}

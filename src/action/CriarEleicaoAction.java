package action;

import meta1.classes.Departamento;
import meta1.classes.ListaCandidata;
import org.apache.struts2.interceptor.SessionAware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CriarEleicaoAction extends Action implements SessionAware {
    private String titulo = null;
    private String descricao = null;
    private String inicio = null;
    private Date dataComeco = null;
    private String fim = null;
    private Date dataFim = null;
    private String tipo = null;

    @Override
    public String execute() throws Exception {
        if (titulo != null && descricao != null && inicio != null && fim != null && tipo != null) {
            dataComeco = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(inicio);
            dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fim);
            ArrayList<Departamento> departamentos = new ArrayList<>();
            ArrayList<ListaCandidata> listaCandidata = new ArrayList<>();
            if (tipo.toUpperCase().equals("ESTUDANTE")) {
                if (this.getEVotingBean().criaEleicao(titulo, descricao, dataComeco, dataFim, tipo, departamentos, listaCandidata)) {
                    return SUCCESS;
                }
            } else if (tipo.toUpperCase().equals("DOCENTE")) {
                if (this.getEVotingBean().criaEleicao(titulo, descricao, dataComeco, dataFim, tipo, departamentos, listaCandidata)) {
                    return SUCCESS;
                }
            } else if (tipo.toUpperCase().equals("FUNCIONÁRIO")){
                if (this.getEVotingBean().criaEleicao(titulo, descricao, dataComeco, dataFim, tipo, departamentos, listaCandidata)) {
                    return SUCCESS;
                }
            } else {
                return ERROR;
            }
        } else {
            return ERROR;
        }
        return ERROR;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataComeco() {
        return dataComeco;
    }

    public void setDataComeco(Date dataComeco) {
        this.dataComeco = dataComeco;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}

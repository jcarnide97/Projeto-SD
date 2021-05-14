package action;

import meta1.classes.*;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Locale;

public class CriarUserAction extends Action implements SessionAware {
    private static final long serialVersionUID = 4L;
    private String nome = null;
    private String numero = null;
    private String departamento = null;
    private String telefone = null;
    private String morada = null;
    private String validadeCC = null;
    private String tipo = null;
    private String password = null;

    @Override
    public String execute() throws Exception {
        if (nome != null && numero != null && departamento != null && telefone != null && morada != null && validadeCC != null && tipo != null && password != null) {
            Departamento dep = this.getEVotingBean().getDepartamento(departamento);
            if (dep != null) {
                User novoUser;
                switch (tipo.toUpperCase()) {
                    case "ESTUDANTE":
                        novoUser = new Estudante(nome, numero, telefone, morada, password, dep, validadeCC);
                        break;
                    case "DOCENTE":
                        novoUser = new Docente(nome, numero, telefone, morada, password, dep, validadeCC);
                        break;
                    case "FUNCIONARIO":
                        novoUser = new Funcionario(nome, numero, telefone, morada, password, dep, validadeCC);
                        break;
                    default:
                        return ERROR;
                }
                if (this.getEVotingBean().criarUser(novoUser)) {
                    return SUCCESS;
                }
            } else {
                System.out.println("Departamento não existe");
                return ERROR;
            }
        } else {
            return ERROR;
        }
        return ERROR;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setValidadeCC(String validadeCC) {
        this.validadeCC = validadeCC;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

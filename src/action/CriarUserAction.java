package action;

import meta1.classes.*;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Locale;

/**
 * Action que vai criar um novo utilizador
 */
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
                    case "FUNCIONÁRIO":
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getValidadeCC() {
        return validadeCC;
    }

    public void setValidadeCC(String validadeCC) {
        this.validadeCC = validadeCC;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

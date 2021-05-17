package action;

import meta1.classes.Eleicao;
import meta1.classes.User;
import org.apache.struts2.interceptor.SessionAware;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LoginAction extends Action implements SessionAware {
    private static final long serialVersionUID = 4L;
    private String nome = null;
    private String password = null;
    private User utilizador = null;
    private ArrayList<Eleicao> eleicoes = null;

    @Override
    public String execute() throws RemoteException {
        if ((this.nome != null && !nome.equals("")) || (this.password != null && !password.equals(""))) {
            this.getEVotingBean().setNome(this.nome);
            this.getEVotingBean().setPassword(this.password);
            if (this.nome.equals("admin") && this.password.equals("admin")) {
                this.session.put("nome", this.nome);
                this.session.put("password", this.password);
                this.session.put("loggedin", true);
                return "admin";
            } else if (this.getEVotingBean().userLogin()) {
                this.session.put("nome", this.nome);
                this.session.put("password", this.password);
                this.session.put("loggedin", true);
                this.session.put("utilizador",this.getEVotingBean().getUser(this.nome,this.password));
                if (this.session.containsKey("utilizador")) {
                    setUtilizador((User)this.session.get("utilizador"));
                    if (this.utilizador != null) {
                        ArrayList<Eleicao> elei=new ArrayList<Eleicao>();
                        try {
                            for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                                int flag = 0;
                                if(eleicao.getListaVotantes()!=null) {
                                    ArrayList<User> votantes = eleicao.getListaVotantes();
                                    for (User utiliza : votantes) {
                                        if (utiliza.getNumero().equals(this.utilizador.getNumero())) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                }
                                if(flag==0){
                                    if(eleicao.getTipo().equals("estudante") && this.utilizador.isEstudante()){
                                        elei.add(eleicao);
                                    }
                                    else if(eleicao.getTipo().equals("funcionario") && this.utilizador.isFuncionario()){
                                        elei.add(eleicao);
                                    }
                                    else if(eleicao.getTipo().equals("docente") && this.utilizador.isDocente()){
                                        elei.add(eleicao);
                                    }
                                }
                            }
                            if(elei!=null){
                                setEleicoes(elei);
                                this.session.put("eleicoes",this.eleicoes);
                            }
                            return SUCCESS;
                        } catch (RemoteException re) {
                            re.printStackTrace();
                        }
                    }
                }
                else{
                    System.out.println("Utilizador não está na base de dados.");
                    return ERROR;
                }
                return SUCCESS;
            } else {
                return LOGIN;
            }
        }
        return LOGIN;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public User getUtilizador(){
        return this.utilizador;
    }
    public ArrayList<Eleicao> getEleicoes(){
        return this.eleicoes;
    }
    public void setEleicoes(ArrayList<Eleicao> eleicoes) {
        this.eleicoes = eleicoes;

    }
    public void setUtilizador(User utilizador){
        this.utilizador = utilizador;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String logout() throws Exception {
        this.getEVotingBean().logout((String)this.session.get("nome"));
        this.session.clear();
        return SUCCESS;
    }
}

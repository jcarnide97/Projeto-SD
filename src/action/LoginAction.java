package action;

import org.apache.struts2.interceptor.SessionAware;
import java.rmi.RemoteException;

public class LoginAction extends Action implements SessionAware {
    private static final long serialVersionUID = 4L;
    private String nome = null;
    private String password = null;

    @Override
    public String execute() throws RemoteException {
        if ((this.nome != null && !nome.equals("")) || (this.password != null && !password.equals(""))) {
            this.getEVotingBean().setNome(this.nome);
            this.getEVotingBean().setPassword(this.password);
            if (this.getEVotingBean().userLogin()) {
                this.session.put("nome", this.nome);
                this.session.put("password", this.password);
                this.session.put("loggedin", true);
                return SUCCESS;
            } else if (this.nome.equals("admin") && this.password.equals("admin")) {
                this.session.put("nome", this.nome);
                this.session.put("password", this.password);
                this.session.put("loggedin", true);
                return "ADMIN";
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

package action;

import meta1.classes.Eleicao;
import meta1.classes.User;
import meta1.classes.Voto;
import org.apache.struts2.interceptor.SessionAware;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class FacebookAction3 extends Action implements SessionAware {
    public String code;
    private User utilizador = null;
    private ArrayList<Eleicao> eleicoes = null;
    @Override
    public String execute() throws MalformedURLException, RemoteException {
        System.out.println(this.code);
        User x = this.getEVotingBean().loginFace(this.code);
        if(x!=null) {
            this.session.put("utilizador",x);
            if (this.session.containsKey("utilizador")) {
                setUtilizador((User)this.session.get("utilizador"));
                if (this.utilizador != null) {
                    ArrayList<Eleicao> elei=new ArrayList<Eleicao>();
                    try {
                        for (Eleicao eleicao : this.getEVotingBean().getListaEleicoes()) {
                            int flag = 0;
                            if(eleicao.getListaVotos()!=null) {
                                ArrayList<Voto> votantes = eleicao.getListaVotos();
                                for (Voto voto : votantes) {
                                    if (voto.getEleitor().getNumero().equals(this.utilizador.getNumero())) {
                                        flag = 1;
                                        break;
                                    }
                                }
                            }
                            if(flag==0 && eleicao.votacaoAberta()){
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
            return SUCCESS;
        }
        return ERROR;
    }
    public void setEleicoes(ArrayList<Eleicao> eleicoes) {
        this.eleicoes = eleicoes;

    }
    public void setUtilizador(User utilizador){
        this.utilizador = utilizador;
    }
    public void setCode(String code){
        this.code = code;
    }
}
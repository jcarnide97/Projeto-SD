package action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuthService;
import meta1.classes.User;
import org.apache.struts2.interceptor.SessionAware;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import uc.sd.apis.FacebookApi2;


import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class FacebookAction2 extends Action implements SessionAware {
    public String code;
    @Override
    public String execute() throws MalformedURLException, RemoteException {
        System.out.println(this.code);
        this.getEVotingBean().ligaFace(this.code,(User)this.session.get("utilizador"));
        return SUCCESS;
    }

    public void setCode(String code){
        this.code = code;
    }
}
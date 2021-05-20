package action;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;

import org.apache.struts2.interceptor.SessionAware;
import uc.sd.apis.FacebookApi2;


public class FacebookAction extends Action implements SessionAware {
    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;
    public String authorizationUrl = "";

    @Override
    public String execute() throws MalformedURLException, RemoteException {
        System.out.println("weiiiiiiiii ");
        this.setAuthorizationUrl(this.getEVotingBean().devolveOauth());
        System.out.println(this.authorizationUrl);
        return SUCCESS;
    }
    public void outroM() throws MalformedURLException, RemoteException {
        this.setAuthorizationUrl(this.getEVotingBean().devolveOauth());
        System.out.println(this.authorizationUrl);
    }
    public void setAuthorizationUrl(String authorizationUrl){
        this.authorizationUrl=authorizationUrl;

    }
    public String getAuthorizationUrl(){
        return this.authorizationUrl;
    }
}
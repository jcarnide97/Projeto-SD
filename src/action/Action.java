package action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import model.eVotingBean;
import uc.sd.apis.FacebookApi2;

import java.rmi.RemoteException;
import java.util.Map;

public class Action extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    Map<String, Object> session;


    public eVotingBean getEVotingBean() throws RemoteException {
        if (!session.containsKey("eVotingBean")) {
            this.setEVotingBean(new eVotingBean());
        }
        return (eVotingBean) session.get("eVotingBean");
    }

    public void setEVotingBean(eVotingBean bean) {
        this.session.put("eVotingBean", bean);

    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

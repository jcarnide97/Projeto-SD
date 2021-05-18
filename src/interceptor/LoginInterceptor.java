package interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Map;

public class LoginInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<String,Object> session = actionInvocation.getInvocationContext().getSession();

        String nome = (String) session.get("nome");

        if (nome == null) {
            return Action.LOGIN;
        } else {
            return actionInvocation.invoke();
        }
    }
}

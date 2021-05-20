package interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Map;

public class LoginInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    /**
     * Método que fará a interseção caso os utilizadores tentem mudar o URL para aceder a outras páginas
     * Caso um user normal tente aceder a um URL exclusivo para o admin, será redirecionado para a página de login
     * @param actionInvocation
     * @return ação dos utilizadores normais ou redireciona para o login caso um user tente aceder a páginas exclusivas
     * a administradores
     * @throws Exception
     */
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

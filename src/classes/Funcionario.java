package classes;
import java.io.Serializable;
import java.util.ArrayList;

public class Funcionario extends User implements Serializable {

    public Funcionario(String nome, String numero, String telefone, String morada, String password, Departamento departamento, String validadeCC) {
        super(nome, numero, telefone, morada, password, departamento, validadeCC);
    }

    @Override
    public void addUser(ArrayList<User> listaUsers, Departamento dep) {
        listaUsers.add(this);
    }

    @Override
    public boolean isEstudante() {
        return false;
    }

    @Override
    public boolean isDocente() {
        return false;
    }

    @Override
    public boolean isFuncionario() {
        return true;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", numero=" + numero +
                ", telefone=" + telefone +
                ", morada='" + morada + '\'' +
                ", password='" + password + '\'' +
                ", departamento=" + departamento +
                ", validadeCC='" + validadeCC + '\'' +
                '}';
    }

    
}

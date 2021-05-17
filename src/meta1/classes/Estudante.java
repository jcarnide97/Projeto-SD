package meta1.classes;
import java.io.Serializable;
import java.util.ArrayList;

public class Estudante extends User implements Serializable {

    public Estudante(String nome, String numero, String telefone, String morada, String password, Departamento departamento, String validadeCC) {
        super(nome, numero, telefone, morada, password, departamento, validadeCC);
    }

    @Override
    public void addUser(ArrayList<User> listaUsers, Departamento dep) {
        dep.addEstudante(this);
        listaUsers.add(this);
    }

    @Override
    public boolean isEstudante() {
        return true;
    }

    @Override
    public boolean isDocente() {
        return false;
    }

    @Override
    public boolean isFuncionario() {
        return false;
    }

    @Override
    public String toString() {
        return "Estudante{" +
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

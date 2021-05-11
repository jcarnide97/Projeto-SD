package meta1.classes;
import java.io.Serializable;
import java.util.ArrayList;

public class Docente extends User implements Serializable {

    public Docente(String nome, String numero, String telefone, String morada, String password, Departamento departamento, String validadeCC) {
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
        return true;
    }

    @Override
    public boolean isFuncionario() {
        return false;
    }

    @Override
    public String toString() {
        return "Docente{" +
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

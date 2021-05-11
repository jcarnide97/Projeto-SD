package meta1.classes;
import java.io.Serializable;
import java.util.ArrayList;

public class Departamento implements Serializable {
    public String nome;
    public ArrayList<Estudante> listaEstudantes;

    public Departamento(String nome, ArrayList<Estudante> listaEstudantes) {
        this.nome = nome;
        this.listaEstudantes = listaEstudantes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Estudante> getListaEstudantes() {
        return listaEstudantes;
    }

    public void setListaEstudantes(ArrayList<Estudante> listaEstudantes) {
        this.listaEstudantes = listaEstudantes;
    }

    public void addEstudante(Estudante estudante) {
        listaEstudantes.add(estudante);
    }
}

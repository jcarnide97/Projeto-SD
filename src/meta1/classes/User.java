package meta1.classes;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class User implements Serializable {
    public String nome;
    public String numero;
    public String telefone;
    public String morada;
    public String password;
    public Departamento departamento;
    public String validadeCC;

    public User(String nome, String numero, String telefone, String morada, String password, Departamento departamento, String validadeCC) {
        this.nome = nome;
        this.numero = numero;
        this.telefone = telefone;
        this.morada = morada;
        this.password = password;
        this.departamento = departamento;
        this.validadeCC = validadeCC;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getValidadeCC() {
        return validadeCC;
    }

    public void setValidadeCC(String validadeCC) {
        this.validadeCC = validadeCC;
    }

    // tipos de users: estudantes, docentes ou funcionarios
    public abstract boolean isEstudante();
    public abstract boolean isDocente();
    public abstract boolean isFuncionario();

    public abstract void addUser(ArrayList<User> listaUsers, Departamento dep);

    public void printUsers() {
        System.out.println(getNome());
        System.out.println(getNumero());
        System.out.println(getPassword());
        System.out.println(getValidadeCC());
        System.out.println(getDepartamento().getNome());
        if (isEstudante()) {
            System.out.println("Tipo: Estudante");
        } else if (isDocente()) {
            System.out.println("Tipo: Docente");
        } else if (isFuncionario()) {
            System.out.println("Tipo: Funcionário");
        }
    }
}

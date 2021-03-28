import java.io.*;
import java.util.ArrayList;
import classes.*;

public class DatabaseFiles {
    String usersFile;
    String eleicoesFile;
    // criar um ficheiro para cada eleicao depois maybe

    public DatabaseFiles() {
        this.usersFile = "database/users.obj";
        this.eleicoesFile = "database/eleicoes.obj";
    }

    public synchronized void saveUsers(ArrayList<User> usersList) {
        try {
            FileOutputStream fos = new FileOutputStream(this.usersFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(usersList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Erro a escrever para o ficheiro " + this.usersFile);
            e.printStackTrace();
        }
    }

    public synchronized void saveEleicoes(ArrayList<Eleicao> eleicoesList) {
        try {
            FileOutputStream fos = new FileOutputStream(this.eleicoesFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(eleicoesList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Erro a escrever para o ficheiro " + this.eleicoesFile);
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<User> loadUsers() {
        File usersFile = new File(this.usersFile);
        FileInputStream fis;
        ObjectInputStream ois;
        ArrayList<User> usersList = new ArrayList<>();
        try {
            fis = new FileInputStream(usersFile);
            ois = new ObjectInputStream(fis);
            usersList = (ArrayList<User>) ois.readObject();
            return usersList;
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + usersFile);
            return new ArrayList<User>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new ArrayList<User>();
        }
        return usersList;
    }

    public synchronized ArrayList<Eleicao> loadEleicoes() {
        File eleicoesFile = new File(this.eleicoesFile);
        FileInputStream fis;
        ObjectInputStream ois;
        ArrayList<Eleicao> eleicoesList = new ArrayList<>();
        try {
            fis = new FileInputStream(eleicoesFile);
            ois = new ObjectInputStream(fis);
            eleicoesList = (ArrayList<Eleicao>) ois.readObject();
            return eleicoesList;
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + eleicoesFile);
            return new ArrayList<Eleicao>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new ArrayList<Eleicao>();
        }
        return eleicoesList;
    }

}

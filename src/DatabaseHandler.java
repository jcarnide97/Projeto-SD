import java.util.ArrayList;
import classes.*;

public class DatabaseHandler extends Thread {
    private ArrayList<User> usersList = null;
    private ArrayList<Eleicao> eleicoesList = null;
    private DatabaseFiles filesManager;

    // construtor para guardar users
    public DatabaseHandler(ArrayList<User> usersList, DatabaseFiles filesManager) {
        super();
        this.setName("DatabaseHandler-" + this.getId());
        this.usersList = usersList;
        this.filesManager = filesManager;
    }

    // construtor para guardar eleicoes
    public DatabaseHandler(DatabaseFiles filesManager, ArrayList<Eleicao> eleicoesList) {
        super();
        this.setName("DatabaseHandler-" + this.getId());
        this.eleicoesList = eleicoesList;
        this.filesManager = filesManager;
    }

    @Override
    public void run() {
        if (this.usersList == null) {
            this.filesManager.saveEleicoes(this.eleicoesList);
        } else {
            this.filesManager.saveUsers(this.usersList);
        }
    }
}

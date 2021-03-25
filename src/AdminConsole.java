import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.Buffer;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import classes.*;

import javax.crypto.spec.PSource;

import static java.lang.System.*;

public class AdminConsole extends UnicastRemoteObject implements Serializable {
    ClientLibrary rmi;

    public AdminConsole(ClientLibrary rmi) throws RemoteException {
        super();
        this.rmi = rmi;
        adminConsoleMenu();
    }

    public void criaDepartamento() {
        try {
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            System.out.println("Nome do departamento: ");
            String nomeDep = reader.readLine();
            ArrayList<Departamento> listaDepartamentos;
            while (true) {
                try {
                    listaDepartamentos = rmi.getListaDepartamentos();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            for (Departamento departamento : listaDepartamentos) {
                if (nomeDep.toUpperCase().equals(departamento.getNome().toUpperCase())) {
                    System.out.println("Departamento já existente");
                    return;
                }
            }
            Departamento departamento = new Departamento(nomeDep, new ArrayList<>());
            while (true) {
                try {
                    rmi.addDepartamento(departamento);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Opção 1 - Registar Utilizadores
    public void registarUser() {
        String nome;
        String numero;
        Departamento departamento;
        String telefone;
        String morada;
        String validadeCC;
        String password;

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        try {
            User novoUser;
            System.out.print("Nome: ");
            nome = reader.readLine();
            System.out.print("Número: ");
            numero = reader.readLine();
            System.out.print("Departamento: ");
            departamento = escolheDepartamento();
            System.out.print("Telefone: ");
            telefone = reader.readLine();
            System.out.print("Morada: ");
            morada = reader.readLine();
            System.out.print("Validade Cartão Cidadão (mm/yyyy): ");
            validadeCC = reader.readLine();
            password = generatePassword();
            System.out.print("Código de acesso gerado: " + password + "\n");
            System.out.println("Tipo:\n[1] estudante\n[2] docente\n[3] funcionário");
            String tipo = reader.readLine();
            if (tipo.equals("1")) {
                novoUser = new Estudante(nome, numero, telefone, morada, password, departamento, validadeCC);
            } else if (tipo.equals("2")) {
                novoUser = new Docente(nome, numero, telefone, morada, password, departamento, validadeCC);
            } else if (tipo.equals("3")) {
                novoUser = new Funcionario(nome, numero, telefone, morada, password, departamento, validadeCC);
            } else {
                System.out.println("Comando inválido");
                return;
            }

            while (true) {
                try {
                    rmi.registarUser(novoUser);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public String generatePassword() {
        int leftLimit = 33; // caracter !
        int rightLimit = 122;  // caracter z
        int passwordLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String passwordGenerated = buffer.toString();
        return passwordGenerated;
    }

    public Departamento escolheDepartamento() {
        try {
            System.out.println("Departamentos existentes");
            ArrayList<Departamento> listaDepartamentos;
            while (true) {
                try {
                    listaDepartamentos = rmi.getListaDepartamentos();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            int i = 0;
            for (Departamento d : listaDepartamentos) {
                i++;
                System.out.println("[" + i + "] " + d.getNome());
            }
            Scanner scDep = new Scanner(System.in);
            int opcao;
            do {
                System.out.print("Selecione o departamento: ");
                opcao = scDep.nextInt();
            } while (opcao <= 0 || opcao > listaDepartamentos.size());
            return listaDepartamentos.get(opcao - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Opção 2 - Criar Eleição
    public void criaEleicao() {
        try {
            Scanner scEleicao = new Scanner(System.in);
            System.out.println("Data de inicio da eleição (dd/MM/yyyy hh:mm): ");
            String inicio = scEleicao.nextLine();
            Date dataComeco = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(inicio);
            System.out.println("Data de fim da eleição (dd/MM/yyyy hh:mm): ");
            String fim = scEleicao.nextLine();
            Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fim);
            System.out.print("Título: ");
            String titulo = scEleicao.nextLine();
            System.out.print("Descrição: ");
            String descricao = scEleicao.nextLine();
            ArrayList<Departamento> departamentos = new ArrayList<>();
            ArrayList<ListaCandidata> listaCandidata = new ArrayList<>();
            Eleicao novaEleicao = new Eleicao(dataComeco, dataFim, titulo, descricao, departamentos, listaCandidata);
            while (true) {
                try {
                    rmi.addEleicao(novaEleicao);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Opção 3 - Gerir Listas
    public void gerirListas() {
        try {
            Eleicao novaEleicao = escolheEleicao();
            ArrayList<Eleicao> eleicoes;
            while (true) {
                try {
                    eleicoes = rmi.getListaEleicoes();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            for (int i = 0; i < eleicoes.size(); i++) {
                if (novaEleicao.getTitulo().toUpperCase().equals(rmi.getListaEleicoes().get(i).getTitulo().toUpperCase()) && (novaEleicao.getDescricao().toUpperCase().equals(rmi.getListaEleicoes().get(i).getDescricao().toUpperCase()))) {
                    rmi.removeEleicao(i);
                }
            }
            if (novaEleicao != null) {
                novaEleicao.gerirCandidatos();
                while (true) {
                    try {
                        rmi.addEleicao(novaEleicao);
                        break;
                    } catch (RemoteException re) {
                        reconectarRMI();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Eleicao escolheEleicao() {
        try {
            System.out.println("Lista de eleições existentes:");
            ArrayList<Eleicao> listaEleicoes;
            while (true) {
                try {
                    listaEleicoes = rmi.getListaEleicoes();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            int i = 0;
            if (listaEleicoes.isEmpty()) {
                System.out.println("Ainda não foram criadas eleições!");
                return null;
            }
            for (Eleicao e : listaEleicoes) {
                if (!e.votacaoAberta() && e.votacaoAcabou()) {
                    i++;
                    System.out.println("[" + i + "] " + e.getTitulo());
                }
            }
            if (i == 0) {
                System.out.println("Não há eleições atualmente para editar!");
                return null;
            }
            Scanner sc = new Scanner(System.in);
            int opcao;
            do {
                System.out.print("Selecione uma eleicao: ");
                opcao = sc.nextInt();
            } while (opcao <= 0 || opcao > listaEleicoes.size());
            return listaEleicoes.get(opcao - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Opção 4 - Gerir mesas de voto
    public void gerirMesasVoto() {
        System.out.println("Gestão de Mesas de voto");
        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("[1] Criar mesa de voto\n[2] Remover mesa de voto\n[3] Associar mesa de voto a eleição");
            opcao = sc.nextInt();
        } while (opcao != 1 && opcao != 2 && opcao != 3);
        switch (opcao) {
            case 1:
                criaMesaVoto();
                break;
            case 2:
                removeMesaVoto();
            case 3:
                associaMesaVoto();
                break;
            default:
                System.out.println("Comando inválido");
        }
    }

    public void criaMesaVoto() {
        try {
            Departamento departamento = escolheDepartamento();
            ArrayList<MulticastServer> mesasVoto;
            while (true) {
                try {
                    mesasVoto = rmi.getMesasVoto();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            for (MulticastServer mesa : mesasVoto) {
                if ((mesa.getDepartamento().getNome().toUpperCase().equals(departamento.getNome().toUpperCase()))) {
                    System.out.println("Este departamento já tem uma mesa de voto associada");
                    return;
                }
            }
            MulticastServer novaMesaVoto = new MulticastServer(departamento);
            while (true) {
                try {
                    rmi.addMesaVoto(novaMesaVoto);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeMesaVoto() {
        try {
            ArrayList<MulticastServer> mesasVoto;
            while (true) {
                try {
                    mesasVoto = rmi.getMesasVoto();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            if (mesasVoto.isEmpty()) {
                System.out.println("Não exitem mesas de voto, crie as mesas primeiro");
                return;
            }
            int i = 0;
            System.out.println("Mesas de voto existentes:");
            for (MulticastServer mesa : mesasVoto) {
                i++;
                System.out.println("[" + i + "] " + mesa.getDepartamento().getNome());
            }
            int opcao;
            Scanner sc = new Scanner(System.in);
            do {
                System.out.print("Mesa a apagar: ");
                opcao = sc.nextInt();
            } while (opcao < 1 || opcao > mesasVoto.size());
            MulticastServer mesaRemover = mesasVoto.get(opcao - 1);
            for (Eleicao eleicao : mesaRemover.getListaEleicoes()) {
                if (eleicao.votacaoAcabou()) {
                    System.out.println("Impossível apagar mesa de voto!");
                    return;
                }
            }
            rmi.removeMesaVoto(opcao - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void associaMesaVoto() {
        try {
            Eleicao eleicao = escolheEleicao();
            if (eleicao == null) {
                return;
            }
            int i = 0;
            ArrayList<MulticastServer> mesasVoto;
            while (true) {
                try {
                    mesasVoto = rmi.getMesasVoto();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            for (MulticastServer mesa : mesasVoto) {
                i++;
                System.out.println("[" + i + "] " + mesa.getDepartamento().getNome());
            }
            if (i == 0) {
                System.out.println("Não exitem mesas de voto, crie as mesas primeiro");
                return;
            }
            int opcao;
            Scanner sc = new Scanner(System.in);
            do {
                System.out.println("Escolha mesa para associar: ");
                opcao = sc.nextInt();
            } while (opcao < 1 || opcao > mesasVoto.size());
            MulticastServer mesaAssociar = mesasVoto.get(opcao - 1);
            for (Eleicao ele : mesaAssociar.getListaEleicoes()) {
                if (ele.getTitulo().equals(eleicao.getTitulo()) && ele.getDescricao().equals(eleicao.getDescricao())) {
                    System.out.println("Eleição já tem uma mesa de voto associada a este departamento");
                    return;
                }
            }
            while (true) {
                try {
                    rmi.removeMesaVoto(opcao - 1);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            mesaAssociar.addEleicao(eleicao);
            while (true) {
                try {
                    rmi.addMesaVoto(mesaAssociar);
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // Opção 5 (9) - Alterar propriedades de uma eleição
    private void editarEleicao() {
        try {
            Scanner sc = new Scanner(System.in);
            Eleicao eleicao = escolheEleicao();
            if (eleicao == null) {
                return;
            }
            for (int i = 0; i < rmi.getListaEleicoes().size(); i++) {
                if (eleicao.getTitulo().equals(rmi.getListaEleicoes().get(i).getTitulo()) && eleicao.getDescricao().equals(rmi.getListaEleicoes().get(i).getDescricao())) {
                    rmi.removeEleicao(i);
                }
            }
            System.out.println("Data de inicio da eleição (dd/MM/yyyy hh:mm): ");
            String inicio = sc.nextLine();
            Date dataComeco = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(inicio);
            System.out.println("Data de fim da eleição (dd/MM/yyyy hh:mm): ");
            String fim = sc.nextLine();
            Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fim);
            System.out.print("Título: ");
            String titulo = sc.nextLine();
            System.out.print("Descrição: ");
            String descricao = sc.nextLine();
            if (eleicao != null) {
                eleicao.setDataComeco(dataComeco);
                eleicao.setDataFim(dataFim);
                eleicao.setTitulo(titulo);
                eleicao.setDescricao(descricao);
                while (true) {
                    try {
                        rmi.addEleicao(eleicao);
                        break;
                    } catch (RemoteException re) {
                        reconectarRMI();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Opção 6 (10) - Saber em que local votou cada eleitor
    public void localVotoUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Locais de voto dos eleitores");
        System.out.print("Numero de utilizador do user: ");
        String numero = reader.readLine();
        ArrayList<Eleicao> eleicoes = new ArrayList<>();
        while (true) {
            try {
                eleicoes = rmi.getListaEleicoes();
                break;
            } catch (RemoteException re) {
                reconectarRMI();
            }
        }
        for (Eleicao ele : eleicoes) {
            ele.localVoto(numero);
        }
    }

    // Opção 7 (11) - Ver estado das mesas de voto
    public void estadoMesasVoto() {
        try {
            ArrayList<MulticastServer> mesasVoto;
            while (true) {
                try {
                    mesasVoto = rmi.getMesasVoto();
                    break;
                } catch (RemoteException re) {
                    reconectarRMI();
                }
            }
            for (MulticastServer mesa : mesasVoto) {
                System.out.println(mesa.getDepartamento().getNome() + " - " + mesa.getEstadoMesaVoto());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void reconectarRMI() {
        int sleep = 1000;
        while (true) {
            try {
                this.rmi = (ClientLibrary) Naming.lookup("RMI_Server");
                this.rmi.sayHello();
            } catch (Exception e) {
                try {
                    Thread.sleep(sleep);
                    sleep *= 2;
                    if (sleep > 16000) {
                        System.out.println("Avaria no RMI Server");
                        exit(0);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public void eVotingStats(ClientLibrary rmi) {
        try {
            System.out.println("Users");
            for (User user : rmi.getListaUsers()) {
                user.printUsers();
            }
            System.out.println("-------------------------");
            System.out.println("Eleições");
            for (Eleicao eleicao : rmi.getListaEleicoes()) {
                eleicao.printEleicao();
                System.out.println("Número dos users da eleição");
                for (User user : rmi.getListaUsers()) {
                    System.out.println(user.getNumero());
                }
            }
            System.out.println("-------------------------");
            System.out.println("Departamento");
            for (Departamento departamento : rmi.getListaDepartamentos()) {
                System.out.println(departamento.getNome());
            }
            System.out.println("-------------------------");
            System.out.println("Mesas de Voto");
            for (MulticastServer mesa : rmi.getMesasVoto()) {
                System.out.println(mesa.getDepartamento().getNome());
                System.out.println("Eleições associadas à mesa " + mesa.getDepartamento().getNome());
                for (Eleicao eleicao : mesa.getListaEleicoes()) {
                    System.out.println(eleicao.getTitulo());
                }
            }
            System.out.println("-------------------------");
            System.out.println("\n\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void adminConsoleMenu() {
        // eVotingStats(this.rmi);
        System.out.println("\teVoting Console Admin Main Menu");
        System.out.println("Selecione uma opção:\n" +
                "[0] Criar Departamento\n" +
                "[1] Registar Utilizador\n" +
                "[2] Criar Eleição\n" +
                "[3] Gerir Listas de Candidatos a uma Eleição\n" +
                "[4] Gerir Mesas de Voto\n" +
                "[5] Alterar Propriedades de uma eleição\n" +
                "[6] Saber Local de Voto dos Eleitores\n" +
                "[7] Ver Estado das Mesas de Voto");
        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.print(">>> ");
            opcao = sc.nextInt();
        } while (opcao < 0 || opcao > 7);
        try {
            switch (opcao) {
                case 0:
                    criaDepartamento();
                    break;
                case 1:
                    registarUser();
                    break;
                case 2:
                    criaEleicao();
                    break;
                case 3:
                    gerirListas();
                    break;
                case 4:
                    gerirMesasVoto();
                    break;
                case 5:
                    editarEleicao();
                    break;
                case 6:
                    localVotoUsers();
                    break;
                case 7:
                    estadoMesasVoto();
                    break;
                default:
                    System.out.println("Comando Inválido");
            }
            adminConsoleMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        try {
            ClientLibrary rmi = (ClientLibrary) Naming.lookup("RMI_Server");
            rmi.sayHello();
            AdminConsole adminConsole = new AdminConsole(rmi);
        } catch (Exception e) {
            System.out.println("Exception in main AdminConsole: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package classes;
import java.io.Serializable;
import java.util.*;

public class Eleicao implements Serializable {
    Date dataComeco;
    Date dataFim;
    public String titulo;
    public String descricao;

    private ArrayList<Departamento> listaDepartamento;
    private ArrayList<User> listaVotantes;
    private ArrayList<Voto> listaVotos;
    private ArrayList<ListaCandidata> listaCandidatas;


    public Eleicao() {
    }

    public Eleicao(Date dataComeco, Date dataFim, String titulo, String descricao) {
        this.dataComeco = dataComeco;
        this.dataFim = dataFim;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public Eleicao(Date dataComeco, Date dataFim, String titulo, String descricao, ArrayList<Departamento> listaDepartamento, ArrayList<ListaCandidata> listaCandidatas) {
        this.dataComeco = dataComeco;
        this.dataFim = dataFim;
        this.titulo = titulo;
        this.descricao = descricao;
        this.listaDepartamento = listaDepartamento;
        this.listaCandidatas = listaCandidatas;
    }

    public Date getDataComeco() {
        return dataComeco;
    }

    public void setDataComeco(Date dataComeco) {
        this.dataComeco = dataComeco;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<User> getListaVotantes() {
        return listaVotantes;
    }

    public void setListaVotantes(ArrayList<User> listaVotantes) {
        this.listaVotantes = listaVotantes;
    }

    public void addVoto(Voto voto) {
        this.listaVotos.add(voto);
        for (User u : listaVotantes) {
            if (voto.getEleitor().getNumero().equals(u.getNumero())) {
                listaVotantes.remove(u);
            }
        }
    }

    public ArrayList<ListaCandidata> getListaCandidatos(User user) {
        return listaCandidatas;
    }

    public boolean votacaoAberta() {
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(currentDate.after(this.dataComeco));
        System.out.println(currentDate.before(this.dataFim));
        return currentDate.after(this.dataComeco) && currentDate.before(this.dataFim);
    }

    public boolean votacaoAcabou() {
        Date currentDate = new Date(System.currentTimeMillis());
        return !currentDate.after(this.dataFim);
    }

    public void gerirCandidatos() {
        System.out.println("Pretende adicionar ou remover lista candidata?\n[1] Adicionar\n[2] Remover");
        Scanner sc = new Scanner(System.in);
        String opcao = sc.nextLine();
        if (opcao.equals("1")) {
            System.out.println("Nome da lista");
            String nomeLista = sc.nextLine();
            ListaCandidata novaLista = new ListaCandidata(nomeLista);
            this.listaCandidatas.add(novaLista);
        } else if (opcao.equals("2")) {
            if (this.listaCandidatas.isEmpty()) {
                System.out.println("Ainda não existem listas associadas a esta eleição");
                return;
            }
            System.out.println("Escolha a lista a remover");
            int i = 0;
            for (ListaCandidata lista : this.listaCandidatas) {
                i++;
                System.out.println("[" + i + "] " + lista.getNome());
            }
            int opcaoRem;
            do {
                System.out.print("Selecione uma lista: ");
                opcaoRem = sc.nextInt();
            } while (opcaoRem <= 0 || opcaoRem > listaCandidatas.size());
            this.listaCandidatas.remove(opcaoRem - 1);
        } else {
            System.out.println("Comando inválido");
        }
    }

    public StringBuilder localVoto(String numero) {
        System.out.println(this.getTitulo());
        StringBuilder infoEleitor = new StringBuilder();
        for (Voto voto : listaVotos) {
            if (infoEleitor.equals(voto.getEleitor().getNumero())) {
                infoEleitor.append("Nome: ").append(voto.getEleitor().getNome()).append("\n");
                infoEleitor.append("Local voto: ").append(voto.getLocalVoto().getNome()).append("\n");
                infoEleitor.append("Hora voto: ").append(voto.getHoraVoto()).append("\n\n");
            }
        }
        return infoEleitor;
    }

}

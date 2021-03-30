package classes;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    private static DecimalFormat df = new DecimalFormat("0.00");

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
        this.listaCandidatas.add(new ListaCandidata("Voto em Branco"));
        this.listaCandidatas.add(new ListaCandidata("Voto Nulo"));
    }

    public ArrayList<Departamento> getListaDepartamento(){
        return this.listaDepartamento;
    }

    public void addDepartamento (Departamento dep){
        this.listaDepartamento.add(dep);
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

    public void printEleicao() {
        System.out.println(titulo);
        System.out.println(descricao);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        System.out.println("Inicio: " + dateFormat.format(dataComeco));
        System.out.println("Fim: " + dateFormat.format(dataFim));
        /*float totalVotos = this.listaVotos.size();
        System.out.println("Total de votos: " + (int)totalVotos);
        float percVotos;
        for (ListaCandidata lista : listaCandidatas) {
            int conta = 0;
            for (Voto voto : listaVotos) {
                if (voto.getEscolhaVoto().getNome().equals(lista.getNome())) {
                    conta++;
                }
            }
            if (lista.getNome().equals("Voto em Branco")) {
                System.out.println("\tNúmero de votos em branco = " + conta);
                percVotos = (conta/totalVotos)*100;
                System.out.println("\tPercentagem de votos em branco = " + df.format(percVotos));
            }
            else if (lista.getNome().equals("Voto Nulo")){
                percVotos = (conta/totalVotos)*100;
            }
            else {
                System.out.println("\tNúmero de votos da lista " + lista.getNome() + "= " + conta);
                percVotos = (conta/totalVotos)*100;
                System.out.println("\tPercentagem de votos da lista " + lista.getNome() + "= " + percVotos);
            }
        }*/
    }

    public void numVotosAtual() {
        System.out.println("Voto até ao momento = " + this.listaVotos.size());
    }

}

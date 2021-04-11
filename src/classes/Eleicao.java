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
    public String tipo;

    private ArrayList<Departamento> listaDepartamento;
    private ArrayList<User> listaVotantes;
    private ArrayList<Voto> listaVotos=new ArrayList<Voto>();
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

    public Eleicao(Date dataComeco, Date dataFim, String titulo, String descricao, String tipo, ArrayList<Departamento> listaDepartamento, ArrayList<ListaCandidata> listaCandidatas) {
        this.dataComeco = dataComeco;
        this.dataFim = dataFim;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.listaDepartamento = listaDepartamento;
        this.listaCandidatas = listaCandidatas;
        this.listaCandidatas.add(new ListaCandidata("Voto em Branco"));
        this.listaCandidatas.add(new ListaCandidata("Voto Nulo"));
        this.listaVotos = new ArrayList<>();
    }

    public ArrayList<ListaCandidata> getListaCandidatas(){
        return this.listaCandidatas;
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

    public ArrayList<Voto> getListaVotos(){
        return this.listaVotos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void addVoto(Voto voto) {

        if(this.listaVotos==null){
            this.listaVotos = new ArrayList<Voto>();
        }
        this.listaVotos.add(voto);
    }

    public ArrayList<ListaCandidata> getListaCandidatos() {
        return listaCandidatas;
    }

    /**
     * Método para verificar se a votação já está aberta ou não
     * @return
     */
    public boolean votacaoAberta() {
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(currentDate.after(this.dataComeco));
        System.out.println(currentDate.before(this.dataFim));
        return currentDate.after(this.dataComeco) && currentDate.before(this.dataFim);
    }

    /**
     * Método para verificar se a votação já acabou
     * @return
     */
    public boolean votacaoAcabou() {
        Date currentDate = new Date(System.currentTimeMillis());
        return !currentDate.after(this.dataFim);
    }

    /**
     * Método para gerir (adicionar e remover) listas de candidatos de uma eleição
     */
    public void gerirCandidatos() {
        System.out.println("Pretende adicionar ou remover lista candidata?\n[1] Adicionar\n[2] Remover");
        Scanner sc = new Scanner(System.in);
        String opcao = sc.nextLine();
        if (opcao.equals("1")) {
            System.out.println("Nome da lista");
            String nomeLista = sc.nextLine();
            ListaCandidata novaLista = new ListaCandidata(nomeLista);
            this.listaCandidatas.add(novaLista);
            System.out.println(this.listaCandidatas.size());
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

    public void localVoto(String numero) {
        System.out.println("Eleição: "+this.getTitulo());
        for (Voto voto : listaVotos) {
            if(voto.getEleitor().getNumero().equals(numero)){
                System.out.println("Nome: "+voto.getEleitor().getNome());
                System.out.println("Local Voto: "+voto.getLocalVoto().getNome());
                System.out.println("Hora Voto: "+voto.getHoraVoto());
            }
        }
    }

    public void printEleicao() {
        System.out.println("Eleição: " + titulo);
        System.out.println("Descrição: " + descricao);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        System.out.println("Data início: " + dateFormat.format(dataComeco));
        System.out.println("Data fim: " + dateFormat.format(dataFim));
        float totalVotos = this.listaVotos.size();
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
            else if (lista.getNome().equals("Voto Nulo")) {
                System.out.println("\tNúmero de votos nulos = " + conta);
                percVotos = (conta/totalVotos)*100;
                System.out.println("\tPercentagem de votos nulos = " + df.format(percVotos));
            }
            else {
                System.out.println("\tNúmero de votos da lista " + lista.getNome() + " = " + conta);
                percVotos = (conta/totalVotos)*100;
                System.out.println("\tPercentagem de votos da lista " + lista.getNome() + " = " + df.format(percVotos));
            }
        }
    }

    public void numVotosAtual() {
        System.out.println("Votos até ao momento = " + this.listaVotos.size());
    }

}

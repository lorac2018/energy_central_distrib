package edu.inf.lp2_aed2_projeto1;

import edu.inf.algoritmos.RedBlackBST_AED2;
import java.io.Serializable;


public class Equipamento implements Serializable {

    private String nome;

    private String category;

    private boolean consome_energia;

    private boolean estado;

    private Integer kwh;

    private Integer ep_id;

    private Habitacao habit;

    private RedBlackBST_AED2<Date, Historico> historicos = new RedBlackBST_AED2<>();

    public Equipamento(String nome, Integer id, String category, boolean consume_energia, boolean estado, Integer kwh) {
        this.nome = nome;
        this.ep_id = id;
        this.category = category;
        this.consome_energia = consume_energia;
        this.estado = estado;
        this.kwh = kwh;
    }

    public boolean ePossivelLigarEquipamento() {
        int aux1 = this.kwh + this.getHabit().getCont_energia();

        if (this.getHabit().getPotencia_suportada() >= aux1) {
            return true;
        }
        return false;
    }

    /**
     * Cria uma instancia de um historico com data de inicio e adiciona à
     * redblack
     */
    public void ligar() throws ImpossivelAdicionarEquipamentosException {
 
        if(!this.consome_energia){    
            this.setEstado(true);
            Date inicio = new Date();
            this.getHabit().getZona_habit().getDist_en().atualizar_zonas();
            Historico hist = new Historico(inicio, this);
            historicos.put(inicio, hist);
            return;
        }
        else if (this.ePossivelLigarEquipamento()) {
            
            this.setEstado(true);
            Date inicio = new Date();
            this.getHabit().getZona_habit().getDist_en().atualizar_zonas();
            Historico hist = new Historico(inicio, this);
            historicos.put(inicio, hist);
            return;
        } else {
            throw new ImpossivelAdicionarEquipamentosException("Nao e possivel ligar o equipamento");
        }
    }

    /**
     * Cria uma instancia da data em que foi desligado , procura a ultima
     * insercao na redblack , calcula a energia consumida ou produzida nesse
     * intervalo , e atribui valors
     */
    public void desligar() {
        double aux = 0.0;
        Date fim = new Date();
        Date inicio = historicos.max(); // para saber a data de inicio . Vai ter a maior data , ou seja a ultima data inserida
        aux = calcularEnergiaIntervaloTempo(inicio, fim);
        Historico hist = historicos.get(inicio);
        hist.setConsumo(aux);
        hist.setData_fim(fim);
        this.setEstado(false);
        this.getHabit().getZona_habit().getDist_en().atualizar_zonas();
    }

    /**
     * Faz o calculo da energia num determinado intervalo de tempo.
     *
     * @param inicio data de inicio , ou seja , quando o equipamento foi ligado
     * @param fim data de fim , ou seja , quando o equipamento foi desligado
     * @return energia calculada nesse intervalo de tempo
     */
    public double calcularEnergiaIntervaloTempo(Date inicio, Date fim) {

        int intervaloSegundos = inicio.intervaloEmSegundos(fim); // calcula o intervalo de tempo em segundos
        double energia;
        energia = (intervaloSegundos * this.kwh) / 3600;
        return energia;
    }

    /**
     * Saber historico num determinado intervalo de tempo
     *
     * @param inicio - data incial
     * @param fim - data final
     * @return
     */
    public RedBlackBST_AED2 saberHistoricoEntrevalaTempo(Date inicio, Date fim) {
        
        return null;
    }

    /**
     * Faz a associação com uma habitacao, ou seja , um equipamento pertence a
     * uma habitacao
     *
     * @param habt habitacao a associar
     */
    public void associarHabitacao(Habitacao habt) {

        this.habit = habt;
    }

    
    
    
    /**
     * Faz a remoção de um historico de uma redblack
     *
     * @param h1
     */
    public void removeHistorico(Historico h1) {

        for (Date k : this.historicos.keys()) {

            if (this.historicos.get(k).getData_inicio().getDia() == h1.getData_inicio().getDia()
                    && this.historicos.get(k).getData_fim().getDia() == h1.getData_fim().getDia()
                    && this.historicos.get(k).getData_inicio().getMes() == h1.getData_inicio().getMes()
                    && this.historicos.get(k).getData_fim().getMes() == h1.getData_fim().getMes()
                    && this.historicos.get(k).getData_inicio().getAno() == h1.getData_inicio().getAno()
                    && this.historicos.get(k).getData_fim().getAno() == h1.getData_fim().getAno()
                    && this.historicos.get(k).getData_inicio().getHora() == h1.getData_inicio().getHora()
                    && this.historicos.get(k).getData_fim().getHora() == h1.getData_fim().getHora()
                    && this.historicos.get(k).getData_inicio().getMinuto() == h1.getData_inicio().getMinuto()
                    && this.historicos.get(k).getData_fim().getMinuto() == h1.getData_fim().getMinuto()
                    && this.historicos.get(k).getData_inicio().getSegundo() == h1.getData_inicio().getSegundo()
                    && this.historicos.get(k).getData_fim().getSegundo() == h1.getData_fim().getSegundo()) {

                this.historicos.delete(k);

                return;
            }

        }

    }

    /**
     * Editar historico , envia como parametro um obejto historico para
     * modificar o original
     *
     * @param h1 objeto enviado para modificar o original
     */
    public void editHistorico(Historico h1) {

        for (Date k : this.historicos.keys()) {

            this.historicos.get(k).getData_inicio().setDia(h1.getData_inicio().getDia());
            this.historicos.get(k).getData_inicio().setAno(h1.getData_inicio().getAno());
            this.historicos.get(k).getData_inicio().setMes(h1.getData_inicio().getMes());
            this.historicos.get(k).getData_inicio().setHora(h1.getData_inicio().getHora());
            this.historicos.get(k).getData_inicio().setMinuto(h1.getData_inicio().getMinuto());
            this.historicos.get(k).getData_inicio().setSegundo(h1.getData_inicio().getSegundo());
            this.historicos.get(k).getData_fim().setDia(h1.getData_fim().getDia());
            this.historicos.get(k).getData_fim().setAno(h1.getData_fim().getAno());
            this.historicos.get(k).getData_fim().setMes(h1.getData_fim().getMes());
            this.historicos.get(k).getData_fim().setHora(h1.getData_fim().getHora());
            this.historicos.get(k).getData_fim().setMinuto(h1.getData_fim().getMinuto());
            this.historicos.get(k).getData_fim().setSegundo(h1.getData_fim().getSegundo());
            this.historicos.get(k).setConsumo(h1.getConsumo());
            this.historicos.get(k).setEquipamento(h1.getEquipamento());

        }

    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the consume_energia
     */
    public boolean isConsume_energia() {
        return consome_energia;
    }

    /**
     * @param consume_energia the consume_energia to set
     */
    public void setConsume_energia(boolean consume_energia) {
        this.consome_energia = consume_energia;
    }

    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return the kwh
     */
    public Integer getKwh() {
        return kwh;
    }

    /**
     * @param kwh the kwh to set
     */
    public void setKwh(Integer kwh) {
        this.kwh = kwh;
    }

    /**
     * @return the habit
     */
    public Habitacao getHabit() {
        return habit;
    }

    /**
     * @param habit the habit to set
     */
    public void setHabit(Habitacao habit) {
        this.habit = habit;
    }

    /**
     * @return the historicos
     */
    public RedBlackBST_AED2<Date, Historico> getHistoricos() {
        return historicos;
    }

    /**
     * @param historicos the historicos to set
     */
    public void setHistoricos(RedBlackBST_AED2<Date, Historico> historicos) {
        this.historicos = historicos;
    }

    /**
     * @return the ep_id
     */
    public Integer getEp_id() {
        return ep_id;
    }

    /**
     * @param ep_id the ep_id to set
     */
    public void setEp_id(Integer ep_id) {
        this.ep_id = ep_id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Funcao de teste
     */
    public void printt() {
        System.out.println("Nome: " + this.nome);
        System.out.println("Kwh: " + this.kwh);
    }

    @Override
    public String toString() {
        return "" + this.habit.getHab_id() + "," + this.nome + "," + this.ep_id + "," + this.category + "," + this.kwh + "," + this.estado + "," + this.consome_energia;
    }

}

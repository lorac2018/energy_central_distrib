package edu.inf.lp2_aed2_projeto1;


import edu.inf.algoritmos.SeparateChainingHashST_AED2;
import java.io.Serializable;

public class Habitacao extends GraphNode implements Serializable {

    private Integer cont_energia;

    private Integer potencia_suportada;

    private boolean sustentavilidade ;

    private Integer energ_prod;

    private Integer hab_id;
    
    private ZonaHabitacao zona_habit;

    private SeparateChainingHashST_AED2<Integer, Equipamento> equipamentos = new SeparateChainingHashST_AED2<>();

    public Habitacao(Integer cont_energia, Integer potencia_suportada, boolean sustentavilidade, Integer energ_prod, Integer hab_id) {
        super();
        this.cont_energia = cont_energia;
        this.potencia_suportada = potencia_suportada;
        this.sustentavilidade = sustentavilidade;
        this.energ_prod = energ_prod;
        this.hab_id = hab_id;
        
    }

    /**
     * Associa a sua zona de habitação
     *
     * @param zona é a nova zona de habitação que a habitação pertencerá.
     */
    public void associarZona(ZonaHabitacao zona) {
        this.zona_habit = zona;

    }

    /**
     * Procura, na habitação, um equipamento, por um id definido
     *
     * @param id do equipamento que pretendemos procurar.
     */
    public Equipamento procurarEquipamento(Integer id) {
        for (Integer k : this.equipamentos.keys()) {
            if (equipamentos.get(k).getEp_id().equals(id)) {
                return equipamentos.get(k);
            }
        }
        System.out.println("NAO ENCONTROU O EQUIPAMENTO");
        return null;
    }
    
    
    
    

    /**
     * Função que verifica se a potência fornecida não ultrapassa a potência
     * consumida pelos equipamentos.
     *
     * @param varConsome energia consumida pelos vários equipamentos, se for
     * negativo, significa que não temos uma casa auto-sustentável.
     * @param varProduz energia produzida pelos vários equipamentos
     * @param varCasa energia fornecida para uma habitação
     * @return Retorna true se suportar , retorna false se nao suportar
     */
    public boolean validarSePotenciaSuportaEquipamentos() {
        int varConsome = this.energiaConsumida(); 
        int varProduz = this.energiaProduzida(); 
        int varCasa = this.potencia_suportada; 

        varConsome = varConsome - varProduz; 

        if (varConsome > varCasa) { 

            return false;
        }
        return true;

    }

    /**
     * Função que calcula o valor produzido ou consumido pelos vários
     * equipamentos
     *
     * @param varConsome energia consumida pelos equipamentos
     * @param varProduz energia produzida pelos equipamentos
     * @return res, o valor produzido ou consumido pelos vários equipamentos.
     */
    public float calculaSustentabilidadeCasa() {
        int varConsome = this.energiaConsumida(); // valor energia que Ã© consumida pelos varios equipamentos
        int varProduz = this.energiaProduzida(); // valor nergia que Ã© produzida pelos varios equipamentos
        //System.out.println(this.getHab_id()+"-------");
        //System.out.println(varConsome);
        //System.out.println(varProduz);
        if(varProduz == 0){
            return 0;
        }
        float res;
        res = (float) varProduz * 100;
        //System.out.println("RES1 = " + res);
        //System.out.println("Consome = " + varConsome);
        res = (float) res / (float) varConsome;
        
        //System.out.println("RES = " + res);
        return res;
    }

    /**
     * Função que retorna a energia consumida pelos equipamentos
     *
     * @param varConsome energia consumida pelos equipamentos
     * @param varProduz energia produzida pelos equipamentos
     * @return varConsume, o valor produzido ou consumido pelos vários
     * equipamentos.
     */
    public int energiaConsumida() {
        int varConsome = 0;

        for (Integer d : getEquipamentos().keys()) {
            if (getEquipamentos().get(d).isConsume_energia()) { // verifica se o equipamento Ã© produtor ou consumidor
                if (getEquipamentos().get(d).isEstado()) {
                    //System.out.println("energiaProduzia: varConsome: " + varConsome);
                    varConsome += getEquipamentos().get(d).getKwh();
                }
            }
        }

        return varConsome;
    }

    /**
     * Função que retorna o valor da energia produzida
     *
     * @param varConsome energia consumida pelos equipamentos
     * @param varProduz energia produzida pelos equipamentos
     * @return varProduz
     */
    public int energiaProduzida() {

        int varProduz = 0;

        for (Integer d : getEquipamentos().keys()) {
            if (!equipamentos.get(d).isConsume_energia()) { // verifica se o equipamento Ã© produtor ou consumidor
                if (equipamentos.get(d).isEstado()) {
                    varProduz += getEquipamentos().get(d).getKwh();
                }
            }
        }
        return varProduz;
    }

    /**
     * Função que verifica se uma casa é sustentável
     *
     * @param varConsome energia consumida pelos equipamentos
     * @param varProduz energia produzida pelos equipamentos
     * @return res, o valor produzido ou consumido pelos vários equipamentos.
     */
    public boolean verificarSeESustentavel() {
        int varProduz = this.energiaProduzida();
        int varConsome = this.energiaConsumida();

        if (varConsome >= varProduz) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Função que adiciona equipamentos
     *
     * @param varConsumo energia consumida pelos equipamentos
     * @param varProduz energia produzida pelos equipamentos
     * @Se o equipamento não existir, é adicionado um novo valor de consumo para
     * gastar para certo equipamento. Se o equipamento existir, e for
     * consumidor, verificamos se o valor do consumo não é superior ao valor
     * fornecido por habitação e atualiza os valores de potência. Se for
     * superior, lançamos uma excepção. Se o equipamento for produtor, apenas
     * adicionamos e escrevemos por cima o novo valor.
     * @return res, o valor produzido ou consumido pelos vários equipamentos.
     */
    public void adicionarEquipamento(Equipamento novo) //throws ImpossivelAdicionarEquipamentosException 
    {
        novo.associarHabitacao(this);
        if (!novo.isEstado()) {
            //System.out.println(novo.getNome());
            this.getEquipamentos().put(novo.getEp_id(), novo);
            this.zona_habit.getDist_en().atualizar_zonas();
            return;
        }

        //novo.associarHabitacao(this);
        int varConsumo = this.energiaConsumida();
        //System.out.println("Energia consumida = " + varConsumo);
        int varProduz = this.energiaProduzida();
        //System.out.println("Energia Produzida= " + varProduz);
        if (novo.isConsume_energia()) { // se for um equipamento consumidor
            varConsumo += novo.getKwh();
            if (varConsumo <= this.potencia_suportada) {
                //System.out.println(novo.getNome());
                this.getEquipamentos().put(novo.getEp_id(), novo);
                //this.atualizarValoresDepoisDeAdicionarEquipamentoConsumidor(novo);

                this.zona_habit.getDist_en().atualizar_zonas();
            } else {
                //throw new ImpossivelAdicionarEquipamentosException("Nao e possivel adicionar o equipamento, a potencia e demaiado alta");
            }
        } else {
            //varProduz += novo.getPotencia_energ();
            this.getEquipamentos().put(novo.getEp_id(), novo);
            //this.atualizarValoresDepoisDeAdicionarEquipamentoProdutor(novo);
            
            this.zona_habit.getDist_en().atualizar_zonas();

        }

    }

//    public void atualizarValoresDepoisDeAdicionarEquipamentoConsumidor(Equipamento novo) {
//        this.cont_energia = this.energiaConsumida();
//    }
//
//    public void atualizarValoresDepoisDeAdicionarEquipamentoProdutor(Equipamento novo) {
//        this.energ_prod += novo.getKwh();
//        this.sustentavilidade = this.verificarSeESustentavel();
//
//    }
    /**
     * Função que permite veriificar se é possível "emprestar" uma maior quantia
     * de potência suportada. Se a potência consumida for superior a potência
     * fornecida, pela distribuição elétrica, não é possível o emprestimo e será
     * lançada uma excepção a informar do mesmo.
     * @param valor é a potência "pedida" para o empréstimo.
     */
    public void alterarPotenciaContratada(Integer valor) throws ImpossivelEmprestimoDePotenciaException {

        if (this.zona_habit.ePossivelAumentarPotenciaDeUmaCasa(valor) == false) {
            throw new ImpossivelEmprestimoDePotenciaException("Nao pode adicionar mais");
        } else if (this.zona_habit.ePossivelAumentarPotenciaDeUmaCasa(valor) == true) {
            this.zona_habit.setPotencia_casas(this.zona_habit.getPotencia_casas() + valor);
            this.potencia_suportada += valor;
            this.zona_habit.getDist_en().atualizar_zonas();
        }
    }
    
      /**
     * Remove todos os dados de um equipamento, pelo seu id.
     *
     * @param id, id do equipamento a remover
     */

    public void removerEquipamento(Integer id) {

        for (Integer k : this.equipamentos.keys()) {

            if (this.equipamentos.get(k).getEp_id().equals(id)) {

                this.equipamentos.delete(k);
                this.zona_habit.getDist_en().atualizar_zonas();
                return;
            }
        }
    }
      /**
     * Edita todos os dados referentes ao equipamento
     *
     * @param e é o equipamento que se pretende editar
     */

    public void editEquipamento(Equipamento e) throws ImpossivelEmprestimoDePotenciaException {
        int aux1 = 0;
        int aux2 = 0;
        for (Integer k : this.equipamentos.keys()) {
            if (this.equipamentos.get(k).getEp_id().equals(e.getEp_id())) {
                if (e.isEstado()) {
                    aux1 = e.getKwh();
                    aux2 = this.equipamentos.get(k).getKwh();
                    if (aux1 > aux2) {
                        if ((aux1 + (this.cont_energia - aux2)) > this.potencia_suportada) {
                            throw new ImpossivelEmprestimoDePotenciaException("Nao e possvel alterar a potencia energetica");
                        }
                    } else {
                        this.equipamentos.get(k).setKwh(e.getKwh());
                        this.equipamentos.get(k).setEp_id(e.getEp_id());
                        this.equipamentos.get(k).setCategory(e.getCategory());
                        this.equipamentos.get(k).setConsume_energia(e.isConsume_energia());
                        this.equipamentos.get(k).setEstado(e.isEstado());
                        this.equipamentos.get(k).associarHabitacao(this);

                        this.zona_habit.getDist_en().atualizar_zonas();
                        return;
                    }
                }
                this.equipamentos.get(k).setKwh(e.getKwh());
                this.equipamentos.get(k).setEp_id(e.getEp_id());
                this.equipamentos.get(k).setCategory(e.getCategory());
                this.equipamentos.get(k).setConsume_energia(e.isConsume_energia());
                this.equipamentos.get(k).setEstado(e.isEstado());
                this.equipamentos.get(k).associarHabitacao(this);

                this.zona_habit.getDist_en().atualizar_zonas();
                return;
            }
        }

    }

    /**
     * @return the cont_energia
     */
    public Integer getCont_energia() {
        return cont_energia;
    }

    /**
     * @param cont_energia the cont_energia to set
     */
    public void setCont_energia(Integer cont_energia) {
        this.cont_energia = cont_energia;
    }

    /**
     * @return the potencia_suportada
     */
    public Integer getPotencia_suportada() {
        return potencia_suportada;
    }

    /**
     * @param potencia_suportada the potencia_suportada to set
     */
    public void setPotencia_suportada(Integer potencia_suportada) {
        this.potencia_suportada = potencia_suportada;
    }

    /**
     * @return the sustentavilidade
     */
    public boolean isSustentavilidade() {
        return sustentavilidade;
    }

    /**
     * @param sustentavilidade the sustentavilidade to set
     */
    public void setSustentavilidade(boolean sustentavilidade) {
        this.sustentavilidade = sustentavilidade;
    }

    /**
     * @return the energ_prod
     */
    public Integer getEnerg_prod() {
        return energ_prod;
    }

    /**
     * @param energ_prod the energ_prod to set
     */
    public void setEnerg_prod(Integer energ_prod) {
        this.energ_prod = energ_prod;
    }

    /**
     * @return the hab_id
     */
    public Integer getHab_id() {
        return hab_id;
    }

    /**
     * @param hab_id the hab_id to set
     */
    public void setHab_id(Integer hab_id) {
        this.hab_id = hab_id;
    }

    /**
     * @return the zona_habit
     */
    public ZonaHabitacao getZona_habit() {
        return zona_habit;
    }

    /**
     * @param zona_habit the zona_habit to set
     */
    public void setZona_habit(ZonaHabitacao zona_habit) {
        this.zona_habit = zona_habit;
    }

    /**
     * @return the equipamentos
     */
    public SeparateChainingHashST_AED2<Integer, Equipamento> getEquipamentos() {
        return equipamentos;
    }

    /**
     * @param equipamentos the equipamentos to set
     */
    public void setEquipamentos(SeparateChainingHashST_AED2<Integer, Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }

    @Override
    public String toString() {
        return "Habitacao{" + "cont_energia=" + cont_energia + ", potencia_suportada=" + potencia_suportada + ", sustentavilidade=" + sustentavilidade + ", energ_prod=" + energ_prod + ", hab_id=" + hab_id + ", zona_habit=" + zona_habit.getZona_id() + '}';
    }

}
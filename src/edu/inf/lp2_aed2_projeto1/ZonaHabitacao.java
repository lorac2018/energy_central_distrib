package edu.inf.lp2_aed2_projeto1;

import edu.inf.algoritmos.RedBlackBST_AED2;
import java.io.Serializable;

public class ZonaHabitacao implements Serializable{

    private Integer num_habs;
    private Integer potencia_casas;
    private Integer zona_id;
    private Integer energ_suportada;
    private DistribuicaoEletrica dist_en;

    private RedBlackBST_AED2<Integer, Habitacao> habitacoes = new RedBlackBST_AED2<>();

    public ZonaHabitacao(Integer id, Integer potencia_suportada) {
        this.zona_id = id;
        this.energ_suportada = potencia_suportada;
    }

    /**
     * Associa a rede de distribuição elétrica
     *
     * @param dist é a rede de distribuição eletrica que se pretende associar à
     * zona.
     */
    public void associarDist(DistribuicaoEletrica dist) {
        this.setDist_en(dist);

    }

    /**
     * Adiciona uma habitação Lança uma excepção, se a potência consumida pelos
     * seus equipamentos (de cada habitação) for superior à potência fornecida
     * pela rede de distribuição elétrica, dizendo não ser possível o emprestimo
     * de mais energia, caso a necessitem.
     *
     * @param h as habitações ou a habitação que se pretende adicionar
     */
    public void adicionarHabitacao(Habitacao h) throws ImpossivelEmprestimoDePotenciaException {
        //System.out.println(this.potencia_casas);
        //System.out.println(h.getPotencia_suportada());
        //System.out.println(this.energ_suportada);
        int aux1 = this.potencia_casas + h.getPotencia_suportada();
        if (this.energ_suportada >= aux1) {
            //System.out.println("entrou");
            h.associarZona(this);
            this.getHabitacoes().put(h.getHab_id(), h);
            this.getDist_en().atualizar_zonas();

        } else {
            throw new ImpossivelEmprestimoDePotenciaException("Impossivel Adicionar , potencia nao suporta");
        }
    }
    
    /**
     * Metodo para verificar se existe uma habitacao 
     * @param id id da habitacao que queremos verificar
     * @return true se existir , false se nao existir
     */
    public boolean verificarSeExisteHabitacao(Integer id){
        for(Integer k : this.habitacoes.keys()){
            if(this.habitacoes.get(k).getHab_id().equals(id)){
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Calcula a sustentabilidade de uma habitacao com base no que produz e no que consome
     * @return retonr um floar com a % de sustentabilidade
     */
    public float calcularSustentabilidade(){
        float aux1 = 0;
        float aux2 = 0;
        
        for(Integer k : this.getHabitacoes().keys()){
            aux1 += this.getHabitacoes().get(k).calculaSustentabilidadeCasa();
        }
        
        aux2 = aux1 / num_habs;
        //System.out.println(aux1);
        return aux2;
    }
    
    

    /**
     * Imprime todos os dados da habitação
     */
    public void printHabitacoes() {
        for (Integer k : getHabitacoes().keys()) {
            System.out.println("Zona: " + getHabitacoes().get(k).getZona_habit().zona_id);
            System.out.println("Habitacao ID: " + getHabitacoes().get(k).getHab_id());
            System.out.println("Contador: " + getHabitacoes().get(k).getCont_energia());
            System.out.println("Potencia Contratada: " + getHabitacoes().get(k).getPotencia_suportada());
            System.out.println("Sustentavididade: " + getHabitacoes().get(k).isSustentavilidade());
            if (getHabitacoes().get(k).isSustentavilidade()) {
                System.out.println("Energia produzida: " + getHabitacoes().get(k).getEnerg_prod());
            }
//            this.guardarHabitacoesTxt(habitacoes);
        }

    }

    /**
     * Edita todos os dados referentes as habitacões ou habitacao
     *
     * @param h é a habitação que se pretende modificar
     */
    public void editHabitacoes(Habitacao h) throws ImpossivelEmprestimoDePotenciaException {

        for (Integer k : this.getHabitacoes().keys()) {

            //System.out.println(this.habitacoes.get(k).getHab_id());
            //System.out.println(h.getHab_id());
            if (this.getHabitacoes().get(k).getHab_id().equals(h.getHab_id())) {
                if (this.getHabitacoes().get(k).getZona_habit().ePossivelAumentarPotenciaDeUmaCasa(h.getPotencia_suportada())) {
                    this.getHabitacoes().get(k).setPotencia_suportada(h.getPotencia_suportada());
                    System.out.println("aqui");
                } else {
                    throw new ImpossivelEmprestimoDePotenciaException("Nao e possivel modificar a potencia contratada");
                }
                this.getHabitacoes().get(k).setHab_id(h.getHab_id());
                this.getHabitacoes().get(k).setEnerg_prod(h.getEnerg_prod());
                this.getHabitacoes().get(k).setSustentavilidade(h.isSustentavilidade());
                this.dist_en.atualizar_zonas();
                this.getDist_en().guardarHabitacoesTxt("data/output/habitacao.txt");
                return;
            }
            //this.guardarHabitacoesTxt(habitacoes);
            //this.dist_en.atualizar_zonas();
        }
    }

    /**
     * Remove todos os dados da habitação, pelo seu id
     *
     * @param id, id da habitação a ser removida
     */
    public void removeHabitacao(Integer id) {
        for (Integer k : this.getHabitacoes().keys()) {
            if (this.getHabitacoes().get(k).getHab_id().equals(id)) {
                this.getHabitacoes().delete(k);
                this.dist_en.atualizar_zonas();
                this.getDist_en().guardarHabitacoesTxt("data/output/habitacao.txt");
                return;
            }
        }
    }

    /**
     * @return the potencia_casas
     */
    public Integer getPotencia_casas() {
        return potencia_casas;
    }

    /**
     * @param potencia_casas the potencia_casas to set
     */
    public void setPotencia_casas(Integer potencia_casas) {
        this.potencia_casas = potencia_casas;
    }

    /**
     * @return the zona_id
     */
    public Integer getZona_id() {
        return zona_id;
    }

    /**
     * @param zona_id the zona_id to set
     */
    public void setZona_id(Integer zona_id) {
        this.zona_id = zona_id;
    }

    /**
     * @return the dist_en
     */
    public DistribuicaoEletrica getDist_en() {
        return dist_en;
    }

    /**
     * @param dist_en the dist_en to set
     */
    public void setDist_en(DistribuicaoEletrica dist_en) {
        this.dist_en = dist_en;
    }

    /**
     * Verifica se é possível aumentar a quantia da potência numa casa.
     *
     * @param valor é o valor de padrão. Se for menor que o valor, é possível aumentar. 
     */
    public boolean ePossivelAumentarPotenciaDeUmaCasa(Integer valor) {
        int val = valor;
        int aux1 = potencia_casas;
        aux1 += val;
        int aux2 = energ_suportada;
        if (aux1 <= aux2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the energ_suportada
     */
    public Integer getEnerg_suportada() {
        return energ_suportada;
    }

    /**
     * @param energ_suportada the energ_suportada to set
     */
    public void setEnerg_suportada(Integer energ_suportada) {
        this.energ_suportada = energ_suportada;
    }

    @Override
    public String toString() {
        return "Zona Habitacao{" + "Id= " + zona_id + ", energia suportada=" + this.energ_suportada + ", Potencia total casas=" + this.potencia_casas + ", total casas=" + this.num_habs + '}';
    }

    /*    public void guardarHabitacoesTxt(RedBlackBST_AED2<Integer, Habitacao> habitacoes) {

     Out ficheiro = new Out("Data/output/Habitacoes.txt");
     for (Integer k : this.habitacoes.keys()) {

     ficheiro.println(this.habitacoes.get(k).getZona_habit() + ";" + this.habitacoes.get(k).getHab_id() + ";"
     + this.habitacoes.get(k).getCont_energia() + ";" + this.habitacoes.get(k).getPotencia_suportada() + ";"
     + this.habitacoes.get(k).getEnerg_prod());

     }
     ficheiro.close();*/
    /**
     * @return the num_habs
     */
    public Integer getNum_habs() {
        return num_habs;
    }

    /**
     * @param num_habs the num_habs to set
     */
    public void setNum_habs(Integer num_habs) {
        this.num_habs = num_habs;
    }

    /**
     * @return the habitacoes
     */
    public RedBlackBST_AED2<Integer, Habitacao> getHabitacoes() {
        return habitacoes;
    }

    /**
     * @param habitacoes the habitacoes to set
     */
    public void setHabitacoes(RedBlackBST_AED2<Integer, Habitacao> habitacoes) {
        this.habitacoes = habitacoes;
    }

}
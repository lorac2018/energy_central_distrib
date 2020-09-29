package edu.inf.lp2_aed2_projeto1;


import java.io.Serializable;





public class FonteEnergia extends GraphNode implements Serializable{

    private String tipo;

    private Integer potencia_gerada;

    private Integer tensao_energ;

    private Integer fonte_id;

    
    //private DistribuicaoEletrica dist_ele;
    public FonteEnergia(String tipo, Integer potencia_gerada, Integer tensao_energ, Integer fonte_id) {
        super();
        this.tipo = tipo;
        this.potencia_gerada = potencia_gerada;
        this.tensao_energ = tensao_energ;
        this.fonte_id = fonte_id;
    }
 public FonteEnergia(Integer fonte_id,String tipo, Integer potencia_gerada, Integer tensao_energ) {
        super();
        this.tipo = tipo;
        this.potencia_gerada = potencia_gerada;
        this.tensao_energ = tensao_energ;
        this.fonte_id = fonte_id;
    }
   

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the potencia_gerada
     */
    public Integer getPotencia_gerada() {
        return potencia_gerada;
    }

    /**
     * @param potencia_gerada the potencia_gerada to set
     */
    public void setPotencia_gerada(Integer potencia_gerada) {
        this.potencia_gerada = potencia_gerada;
    }

    /**
     * @return the tensao_energ
     */
    public Integer getTensao_energ() {
        return tensao_energ;
    }

    /**
     * @param tensao_energ the tensao_energ to set
     */
    public void setTensao_energ(Integer tensao_energ) {
        this.tensao_energ = tensao_energ;
    }

    /**
     * @return the fonte_id
     */
    public Integer getFonte_id() {
        return fonte_id;
    }

    /**
     * @param fonte_id the fonte_id to set
     */
    public void setFonte_id(Integer fonte_id) {
        this.fonte_id = fonte_id;
    }
    
    @Override
    public String toString() {
        return "FontesEnergia{" + "Id=" + fonte_id +"Tipo=" + tipo + ", potencia gerada=" + potencia_gerada + ", Tensao energia=" + tensao_energ +'}';
    }
 

}
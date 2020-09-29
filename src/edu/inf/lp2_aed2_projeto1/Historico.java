package edu.inf.lp2_aed2_projeto1;

import java.io.Serializable;

public class Historico implements Serializable{

    private Date data_inicio;

    private Date data_fim;

    private Double consumo;

    private Equipamento equipamento;

    public Historico() {

    }

    public Historico(Date data_inicio, Equipamento equipamento) {
        this.data_inicio = data_inicio;
        this.equipamento = equipamento;
    }

    public Historico(Date data_inicio, Date data_fim, Double consumo, Equipamento equipamento) {
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.consumo = consumo;
        this.equipamento = equipamento;
    }

    public Historico(Date data_inicio, Date data_fim, Double consumo) {

        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.consumo = consumo;

    }

    /**
     * Associa um equipamento
     *
     * @param e é o equipamento que se pretende adicionar ao histórico
     */
    public void associarEquipamento(Equipamento e) {
        this.equipamento = e;
    }

    /**
     * @return the data_inicio
     */
    public Date getData_inicio() {
        return data_inicio;
    }

    /**
     * @param data_inicio the data_inicio to set
     */
    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    /**
     * @return the data_fim
     */
    public Date getData_fim() {
        return data_fim;
    }

    /**
     * @param data_fim the data_fim to set
     */
    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    /**
     * @return the consumo
     */
    public double getConsumo() {
        return consumo;
    }

    /**
     * @param consumo the consumo to set
     */
    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    /**
     * @return the equipamento
     */
    public Equipamento getEquipamento() {
        return equipamento;
    }

    /**
     * @param equipamento the equipamento to set
     */
    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    @Override
    public String toString() {
        return "Historico{" + "data_inicio=" + data_inicio.toString() + ", data_fim=" + data_fim.toString() + ", consumo=" + consumo + '}';
    }

}
package edu.inf.lp2_aed2_projeto1;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date implements Comparable<Date>{

  private int dia;

  private int mes;

  private int ano;

  private int hora;

  private int minuto;

  private int segundo;


    public Date(int dia, int mes, int ano, int hora, int minuto, int segundo) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }
    
    
    public Date() {
        Calendar gregCalendar = new GregorianCalendar();
        this.dia = gregCalendar.get(Calendar.DAY_OF_MONTH);
        this.mes = gregCalendar.get(Calendar.MONTH) + 1;
        this.ano = gregCalendar.get(Calendar.YEAR);
        this.hora = gregCalendar.get(Calendar.HOUR_OF_DAY);
        this.minuto = gregCalendar.get(Calendar.MINUTE);
        this.segundo = gregCalendar.get(Calendar.SECOND);
    }
    
    
    
    
    
    /**
     * Calcula o numero de segundos de um intervalo de duas datas
     * @param d data
     * @return  numero de segundos
     */
    public int intervaloEmSegundos(Date d) {
        int min = 0;
        int dias = this.numeroDiasMeses(d);
        //System.out.println(dias);
        min += (this.segundo - d.segundo);
        min += (this.minuto - d.minuto) * (60);
        min += (this.hora - d.hora) * (60 * 60);
        min += (this.dia - d.dia) * (24 * 60 * 60);
        if(this.ano == d.ano && this.mes == d.mes){
        } else {
            //System.out.println("entra");
            min +=  dias * 24 * 60 * 60;
        }
        min += (this.ano - d.ano) * 365 * 24 * 60 * 60;

        return Math.abs(min);
    }

    
    
    
    
    
    
    /**
     * Faz a contagem em dias de um intervalo de tempo entre duas datas
     * @param d data
     * @return numero de dias
     */
  public int numeroDiasMeses(Date d) {
        int count = 0;
        int ano = this.ano;
        //System.out.println("this.mes =" + this.mes);
        //System.out.println("d.mes=" + d.mes);
        if (this.ano == d.ano) { // se for no mesmo ano 
            for (int i = this.mes; i != d.mes; i++) {
                if (i == 12) {
                    i = 1;
                    ano++;
                }
                switch (i) {
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        count += 30;
                        break;
                    case 2:
                        if (isLeapYear(ano)) {
                            count += 29;
                        } else {
                            count += 28;
                        }
                        break;
                    default:
                        count += 31;
                        break;

                }
            }
        } else { // se for em anos diferentes
            int aux = 0;
            aux = Math.abs(((12 - this.mes) + d.mes)*(this.ano - d.ano));
            //System.out.println("aux="+aux);
            int count_mes = this.mes;
            for (int i = 0; i!=aux; i++) {
                if (count_mes > 12) {
                    count_mes = 1;
                    ano++;
                }
                switch (count_mes) {
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        count += 30;
                        break;
                    case 2:
                        if (isLeapYear(ano)) {
                            count += 29;
                        } else {
                            count += 28;
                        }
                        break;
                    default:
                        count += 31;
                        break;

                }
                count_mes ++;
            }
        }
        return count;
    }

  
  
  
  
    /**
     * Verifica se um ano e bixesto ou nao 
     * @param year
     * @return true se for e false se nao
     */
    public static boolean isLeapYear(Integer year) {
        return ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)));
    }
    
    
    
    
    
    
    /**
     * Metodo para comprar duas datas 
     * @param t data
     * @return retorna 0 se for iguais , 1 se for menor , e -1 de for maior
     */
    @Override
    public int compareTo(Date t) {
        if(this.getDia() == t.getDia() && this.getMes() == t.getMes() && this.getAno() == t.getAno() && this.getHora() == t.getHora() && this.getMinuto() == t.getMinuto() && this.getSegundo() == t.getSegundo()){
            return 0;
        }
        return beforeDate(t) ? -1 : 1;
    }
    
    
    
    
    
    /**
     * Converte uma string numa data . a string data tem que estar da forma dia/mes/ano/hora/minuto/segundo . 
     * O metodo é estatico para ser utilizado quando temos uma string com uma data e a queremos transformar num objeto Date
     * @param data string 
     * @return 
     */
    public static Date stringToDate(String data){
        String[] texto = data.split("/");
        int d = Integer.parseInt(texto[0]);
        int m = Integer.parseInt(texto[1]);
        int a = Integer.parseInt(texto[2]);
        int h = Integer.parseInt(texto[3]);
        int mi = Integer.parseInt(texto[4]);
        int seg = Integer.parseInt(texto[5]);
        Date nova = new Date(d,m,a,h,mi,seg);
        return nova;
    }
    
    
    public static String dateToString (Date d){
        String str = "";
        String t = "/";
        str = String.valueOf(d.dia);
        str = str.concat("/");
        str = str.concat(String.valueOf(d.mes));
        str = str.concat("/");
        str = str.concat(String.valueOf(d.ano));
        str = str.concat("/");
        str = str.concat(String.valueOf(d.hora));
        str = str.concat("/");
        str = str.concat(String.valueOf(d.minuto));
        str = str.concat("/");
        str = str.concat(String.valueOf(d.segundo));
        
        return str;
    }
    
    
    
    
    
    
    /**
     * Verifica se a data é depois da data passada por parametro
     * @param d data
     * @return true se for depois , false se nao
     */
    public boolean afterDate(Date d) {
        if (this.getAno() > d.getAno()) {
            return true;
        } else if (this.getAno() == d.getAno()) {
            if (this.getMes() > d.getMes()) {
                return true;
            } else if (this.getMes() == d.getMes()) {
                if(this.getDia() > d.getDia()){
                    return true;
                } else if(this.getDia() == d.getDia()){
                    if(this.getHora() > d.getHora()){
                        return true;
                    } else if (this.getHora() == d.getHora()){
                        if(this.getMinuto() > d.getMinuto()){
                            return true;
                        } else if (this.getMinuto() ==  d.getMinuto()){
                            return this.getSegundo() > d.getSegundo() ;
                        }
                    }
                }
            }
        }
        return false;
    }
  
    
    
    public boolean beforeDate(Date d) {

        if (this.ano < d.ano) {
            return true;
        } else if (this.ano == d.ano) {
            if (this.mes < d.mes) {
                return true;
            } else if (this.mes == d.mes) {
                return this.dia < d.dia;
            }
        }
        return false;
    }
    
    
    
//    /**
//     * Verifica se a data é antes da data passada como parametro
//     * @param d data
//     * @return true se for antes e false se nao
//     */
//     public boolean beforeDate(Date d) {
//        if (this.getAno() < d.getAno()) {
//            return true;
//        } else if (this.getAno() == d.getAno()) {
//            if (this.getMes() < d.getMes()) {
//                return true;
//            } else if (this.getMes() == d.getMes()) {
//                if(this.getDia() < d.getDia()){
//                    return true;
//                } else if(this.getDia() == d.getDia()){
//                    if(this.getHora() < d.getHora()){
//                        return true;
//                    } else if (this.getHora() == d.getHora()){
//                        if(this.getMinuto() < d.getMinuto()){
//                            return true;
//                        } else if (this.getMinuto() ==  d.getMinuto()){
//                            return this.getSegundo() < d.getSegundo() ;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
//     
    
     

    /**
     * @return the dia
     */
    public int getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(int dia) {
        this.dia = dia;
    }

    /**
     * @return the mes
     */
    public int getMes() {
        return mes;
    }

    /**
     * @param mes the mes to set
     */
    public void setMes(int mes) {
        this.mes = mes;
    }

    /**
     * @return the ano
     */
    public int getAno() {
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * @return the hora
     */
    public int getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(int hora) {
        this.hora = hora;
    }

    /**
     * @return the minuto
     */
    public int getMinuto() {
        return minuto;
    }

    /**
     * @param minuto the minuto to set
     */
    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    /**
     * @return the segundo
     */
    public int getSegundo() {
        return segundo;
    }

    /**
     * @param segundo the segundo to set
     */
    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }
    
    
    
  @Override
     public String toString(){
         return "Data@ {" + this.dia +"/" + this.mes +"/" + this.ano + "  " +this.hora + ":" + this.minuto + ":" + this.segundo + '}';
     }
     
     
     

}
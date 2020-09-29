package edu.inf.lp2_aed2_projeto1;


import edu.inf.algoritmos.RedBlackBST_AED2;
import static edu.inf.lp2_aed2_projeto1.Date.dateToString;
import static edu.inf.lp2_aed2_projeto1.Date.stringToDate;
import static edu.inf.lp2_aed2_projeto1.Rede.loggerfiles_habitacoes;
import static edu.inf.lp2_aed2_projeto1.Rede.loggerfiles_zonas_habitacao;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import java.io.Serializable;



public class DistribuicaoEletrica extends GraphNode implements Serializable {

    private int dist_id = 0;

    private int num_de_hab = 0;

    private int num_de_fontes = 0;

    private int energia_consumida_zonas = 0;

    private int energia_suportada = 0;

    //private RedBlackBST_AED2<Integer, FonteEnergia> fontes_energ = new RedBlackBST_AED2<>();

    private RedBlackBST_AED2<Integer, ZonaHabitacao> zona_habit = new RedBlackBST_AED2<>();

    public DistribuicaoEletrica() {

    }



    DistribuicaoEletrica(Integer id, Integer suportado) {
        super();
        this.dist_id = id;
        this.energia_suportada = suportado;
    }


    
    
    /**
     * Função responsavel pela atualização do sistema 
     */
    public void atualizar_zonas() {

        int aux1 = 0;
        int aux2 = 0;
        int aux3 = 0;
        int aux4 = 0;
        int num_habs = 0;
        int num_habs2 = 0;

        for (Integer k : zona_habit.keys()) {
            for (Integer g : zona_habit.get(k).getHabitacoes().keys()) {
                for (Integer t : zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().keys()) {
                    Equipamento e = zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().get(t);
                    if (e.isEstado()) {
                        if (e.isConsume_energia()) {
                            aux1 += e.getKwh(); // energia consumida 
                        } else {
                            aux2 += e.getKwh(); // energia produzida
                        }
                    }
                }
                Habitacao h = zona_habit.get(k).getHabitacoes().get(g);
                if (aux1 <= aux2) {
                    h.setSustentavilidade(true);
                } else {
                    h.setSustentavilidade(false);
                }
                //System.out.println("aux1 = " + aux1);
                h.setCont_energia(aux1);
                h.setEnerg_prod(aux2);
                num_habs++;
                aux1 = 0;
                aux2 = 0;
                aux3 += h.getPotencia_suportada();

            }
            ZonaHabitacao zh = zona_habit.get(k);
            zh.setNum_habs(num_habs);
            //System.out.println(" aux3 =" + aux3);
            zh.setPotencia_casas(aux3);
            aux4 += zh.getEnerg_suportada();
            num_habs2 += num_habs;
            num_habs = 0;
            aux3 = 0;
            loggerfiles_zonas_habitacao(zh);
        }
        this.setNum_de_hab(num_habs2);
        this.setEnergia_consumida_zonas(aux4);
    }
    
    
    
    /**
     * Metodo que calcula a energia produziada nas habitacoes da distribuicao
     * @return energia produziada
     */
    public int energiaProduzidaHabitacoesDistribuicao(){
        int energia_produzida = 0;
        for(Integer k : this.getZona_habit().keys()){
            for(Integer p : this.getZona_habit().get(k).getHabitacoes().keys()){
                Habitacao h = this.getZona_habit().get(k).getHabitacoes().get(p);
                energia_produzida += h.getEnerg_prod();
            }
        }
        return energia_produzida;
    }
    

    /*
     * Função que imprime todos os equipamentos de uma habitação, numa zona de habitação definida.
     */
    public void listarHabitacoesEEquipamentosTodasZonas() {
        for (Integer k : zona_habit.keys()) {
            for (Integer g : zona_habit.get(k).getHabitacoes().keys()) {
                System.out.println("----------------------------------------------");
                System.out.println(zona_habit.get(k).getHabitacoes().get(g).toString());
                System.out.println("------------------------------------------------");
                for (Integer p : zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().keys()) {
                    System.out.println(zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().get(p).toString());
                }
                System.out.println("--------------------------------------\n\n\n");
            }
        }
    }
    /*
     * Função que imprime todos os equipamentos de uma habitação, numa zona de habitação definida, através do seu id. 
     @param id é o id da habitação
     */

    public void listarHabitacoesEEquipamentosDeterminadaZona(Integer id) {
        for (Integer g : zona_habit.get(id).getHabitacoes().keys()) {
            System.out.println("----------------------------------------------");
            System.out.println(zona_habit.get(id).getHabitacoes().get(g).toString());
            System.out.println("------------------------------------------------");
            for (Integer p : zona_habit.get(id).getHabitacoes().get(g).getEquipamentos().keys()) {
                System.out.println(zona_habit.get(id).getHabitacoes().get(g).getEquipamentos().get(p).toString());
            }
            System.out.println("--------------------------------------\n\n\n");
        }
    }
//    /*
//     * Função que lista todas as casas que possuem equipamentos que produzem energia, ou seja, 
//     que possuam equipamentos "sustentáveis".
//     */
//
//    public void listarCasasAparelhosProdutores() {
//        for (Integer k : zona_habit.keys()) {
//            for (Integer g : zona_habit.get(k).getHabitacoes().keys()) {
//                if (zona_habit.get(k).getHabitacoes().get(g).getEnerg_prod() > 0) {
//                    System.out.println(zona_habit.get(k).getHabitacoes().get(g).toString());
//                    System.out.println("-------------------------\n\n");
//                }
//            }
//        }
//    }
//
//    /*
//     * Função responsável pela "descoberta" de casas sustentáveis, por zonas de habitação.
//     @param
//     return A red black das casas mais sustentáveis. 
//     */
//    public RedBlackBST_AED2 habitacoeSustentaveis() {
//        RedBlackBST_AED2<Float, Habitacao> st = new RedBlackBST_AED2<>();
//        for (Integer k : zona_habit.keys()) {
//            for (Integer g : zona_habit.get(k).getHabitacoes().keys()) {
//                Habitacao h = zona_habit.get(k).getHabitacoes().get(g);
//                if (h.getEnerg_prod() > 0) {
//                    Float novo = h.calculaSustentabilidadeCasa();
//                    //System.out.println(novo);
//                    st.put(novo, h);
//                }
//            }
//        }
//        return st;
//    }
//
//    /*
//     * Função que imprime as habitações com mais sustentabilidade. 
//     * @param x é o id da habitação
//     */
//    public void printXCasasMaisSustentaveis(Integer x) {
//        System.out.println("Mais sustentaveis:");
//        RedBlackBST_AED2<Float, Habitacao> st = habitacoeSustentaveis();
//        ArrayList<Float> ar = new ArrayList<>();
//        for (Float k : st.keys()) {
//            //System.out.println(st.get(k).toString());
//            ar.add(k);
//        }
//        //System.out.println("ar.size() = " + ar.size());
//        int aux1 = x;
//        aux1 = ar.size() - x;
//        //System.out.println("aux=" + aux1);
//        for (int i = ar.size() - 1; i >= aux1; i--) {
//            Float f = ar.get(i);
//            System.out.println("Percentagem " + ar.get(i) + "% = " + st.get(f).toString());
//        }
//    }
//
//    /*
//     * Função que imprime as habitações com menos sustentabilidade. 
//     @param x é o id da habitação?
//     */
//    public void printXCasasMenosSustentaveis(Integer x) {
//        System.out.println("Menos Sustentaveis:");
//        RedBlackBST_AED2<Float, Habitacao> st = habitacoeSustentaveis();
//        int i = 0;
//        for (Float k : st.keys()) {
//            if (i >= x) {
//                break;
//            }
//            System.out.print("Percentagem " + k + "% = ");
//            System.out.println(st.get(k).toString());
//            i++;
//        }
//    }

    /*
     * Função que procura pelo id um equipamento
     @param id do equipamento
     */
    public Equipamento procurarEquipamento(Integer id) {
        for (Integer k : this.zona_habit.keys()) {
            for (Integer p : this.zona_habit.get(k).getHabitacoes().keys()) {
                for (Integer j : this.zona_habit.get(k).getHabitacoes().get(p).getEquipamentos().keys()) {
                    Equipamento e = this.zona_habit.get(k).getHabitacoes().get(p).getEquipamentos().get(j);
                    if (e.getEp_id().equals(id)) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    /*
     * Função responsável pela consulta do historico de um equipamento, através do seu id.
     @param id é o id do equipameno
     */
    public void consultarHistoricoEquipamento(Integer id) {
        Equipamento e = procurarEquipamento(id);
        for (Date k : e.getHistoricos().keys()) {
            System.out.println(e.getHistoricos().get(k).toString());
        }
    }

    /*
     * Função responsável pela consulta do historico de um equipamento, através do seu id, e por datas de inicio e fim definidas
     @param id é o id do equipameno
     @param  inicio é a data de inicio que se pretende começar a pesquisar
     @param fim é a data de fim que se pretende que a pesquisa vá.
     */
    public void consultarHistoricoEquipamentoIntervalo(Date inicio, Date fim, Integer id) {
        //RedBlackBST_AED2<Date, Historico> st = new RedBlackBST_AED2<>();
        Equipamento e = procurarEquipamento(id);
        for (Date k : e.getHistoricos().keys(inicio, fim)) {
            System.out.println(e.getHistoricos().get(k).toString());
        }

    }

    /*
     * Função que permite ler do ficheiro do texto dados sobre o histórico.
     @param path é o caminho do ficheiro de texto
     */
    public void carregarHistoricoTxt(String path) {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer id_habitacao = Integer.parseInt(texto[0]);
            Integer id = Integer.parseInt(texto[1]);
            String data_inicio = texto[2];
            String data_fim = texto[3];
            Double count = Double.parseDouble(texto[4]);

            Date data_ini = stringToDate(data_inicio);
            Date data_fi = stringToDate(data_fim);
            //System.out.println(data_ini.toString());
            //System.out.println(data_fi.toString());

            Historico novo = new Historico(data_ini, data_fi, count);

            Habitacao hab = procurarNasZonas(id_habitacao);

            Equipamento n = hab.procurarEquipamento(id);
            //System.out.println(n.getNome());
            novo.associarEquipamento(n);
            n.getHistoricos().put(data_ini, novo);
            //System.out.println(n.getHistoricos().get(data_ini).toString());

        }
    }

    /**
     * Guarda todos os dados do historico para um ficheiro de texto.
     *
     * @param path caminho para o ficheiro txt
     */
    public void guardarHistoricoTxt(String path) {
        String d1;
        String d2;
        Historico h;
        Out ficheiro = new Out(path);
        for (Integer k : this.zona_habit.keys()) {
            //ficheiro.println(this.zona_habit.get(k).getZona_id() + ";");
            for (Integer g : this.zona_habit.get(k).getHabitacoes().keys()) {

                for (Integer q : this.zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().keys()) {

                    for (Date w : this.zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().get(q).getHistoricos().keys()) {

                        ficheiro.print(this.zona_habit.get(k).getHabitacoes().get(g).getHab_id() + ";");
                        ficheiro.print(this.zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().get(q).getEp_id() + ";");
                        h = this.zona_habit.get(k).getHabitacoes().get(g).getEquipamentos().get(q).getHistoricos().get(w);
                        d1 = dateToString(h.getData_inicio());
                        d2 = dateToString(h.getData_fim());
                        //ficheiro.println(this.zona_habit.get(k).habitacoes.get(g).getEquipamentos().get(q).getHistoricos().get(w)+";");
                        ficheiro.println(d1 + ";" + d2 + ";" + h.getConsumo() + ";");
                    }
                }

            }
        }
        ficheiro.close();
    }

    /**
     * Guarda todos os dados relativos a uma habitação num ficheiro de texto.
     *
     * @param path caminho para o ficheiro txt
     */
    public void carregarHabitacaoTxt(String path) throws ImpossivelEmprestimoDePotenciaException {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer id_zona = Integer.parseInt(texto[0]);
            Integer id = Integer.parseInt(texto[1]);
            Integer count = Integer.parseInt(texto[2]);
            Integer potencia_suportada = Integer.parseInt(texto[3]);
            String bool = texto[4];
            boolean sustentavilidade;
            if (bool.equals("T")) {
                sustentavilidade = true;
            } else {
                sustentavilidade = false;
            }
            Integer energia_produzida = Integer.parseInt(texto[5]);
            Habitacao nova = new Habitacao(count, potencia_suportada, sustentavilidade, energia_produzida, id);
            ZonaHabitacao h = procurarZona(id_zona);
            if (h == null) {
                System.out.println("Null");
            } else {
                nova.associarZona(h);
                h.adicionarHabitacao(nova);
                //h.getHabitacoes().put(id, nova);
                loggerfiles_habitacoes(h);
            }
        }
        this.atualizar_zonas();
    }

    /**
     * Lê a informação relativa às zonas de habitacao dum ficheiro de texto
     *
     * @param path caminho para o ficheiro txt
     */
    public void carregarZonasHabitacaoTxt(String path) throws ImpossivelEmprestimoDePotenciaException {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer id = Integer.parseInt(texto[0]);
            Integer potencia_suportada = Integer.parseInt(texto[1]);

            ZonaHabitacao zh = new ZonaHabitacao(id, potencia_suportada);
            zh.associarDist(this);
            adicionarZona(zh);
            //zona_habit.put(id, zh);
            loggerfiles_zonas_habitacao(zh);

        }
    }

    /**
     * Carrega a informacao relativa aos equipamentos de um ficheiro txt
     *
     * @param path caminho para o ficheiro txt
     */
    public void carregarEquipamentosTxt(String path) throws ImpossivelAdicionarEquipamentosException {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer casa_id = Integer.parseInt(texto[0]);
            Integer id = Integer.parseInt(texto[1]);
            String nome = texto[2];
            String categoria = texto[3];
            String bool = texto[4];
            boolean consumo;
            if (bool.equals("T")) {
                consumo = true;
            } else {
                consumo = false;
            }
            String bool2 = texto[5];
            boolean estado;
            if (bool2.equals("T")) {
                estado = true;
            } else {
                estado = false;
            }
            Integer kwh = Integer.parseInt(texto[6]);

            Equipamento f = new Equipamento(nome, id, categoria, consumo, estado, kwh);
            Habitacao h = procurarNasZonas(casa_id);
            if (h != null) {
                //System.out.println("aaaa");
                f.associarHabitacao(h);
                h.adicionarEquipamento(f);
                //System.out.println("aaaa");
            } else {
                System.out.println("NULL");
            }
        }
    }

    /**
     * Procura na redblack de zonas de habitacao, a habitacao através de um id
     * definido.
     *
     * @param id identificador de uma zona
     * @return
     */
    public Habitacao procurarNasZonas(Integer id) {
        for (Integer k : zona_habit.keys()) {
            for (Integer g : zona_habit.get(k).getHabitacoes().keys()) {
                if (zona_habit.get(k).getHabitacoes().get(g).getHab_id().equals(id)) {
                    return zona_habit.get(k).getHabitacoes().get(g);
                }
            }
        }
        return null;
    }



    /**
     * Funcao de teste para imprimir informacao relativa à redblack de zonas de
     * habitacao
     */
    public void printZonasHabitacao() {
        for (Integer k : getZona_habit().keys()) {
            System.out.println(getZona_habit().get(k).getZona_id());
            System.out.println(getZona_habit().get(k).getEnerg_suportada());
            //System.out.println(getZona_habit().get(k).getContador_total_energia_casas());
            //System.out.println(getZona_habit().get(k).getPotencia_casas());
            //System.out.println("--------------------------------------");
        }
    }



    //"data/output/zonahabitacao.txt"
    /**
     * Guarda as zonas de Habitação num ficheiro de texto
     *@param path é o caminho do ficheiro de texto
     */
    public void guardarZonasHabitacaoTxt(String path) {

        Out ficheiro = new Out(path);

        for (Integer k : this.zona_habit.keys()) {

            ficheiro.println(this.zona_habit.get(k).getZona_id() + ";" + this.zona_habit.get(k).getEnerg_suportada() + ";");
            loggerfiles_zonas_habitacao(k);
        }
        ficheiro.close();

    }
    
    
    public void editDistribuicaoEletrica(DistribuicaoEletrica nova) throws ImpossivelEmprestimoDePotenciaException{
        int aux1 = this.energia_suportada;
        int aux2 = this.energia_consumida_zonas;
        
        
        if(nova.getEnergia_suportada() < aux2){
            throw new ImpossivelEmprestimoDePotenciaException("Nao é possivel baixar a energia suportada");
        } else{
            this.setEnergia_suportada(nova.getEnergia_suportada());
        }
    }



    /**
     * Remove uma zona de habitacao da redblack
     *
     * @param h
     */
    public void removeZonaHabitacao(Integer id) {

        for (Integer k : this.zona_habit.keys()) {

            if (this.zona_habit.get(k).getZona_id().equals(id)) {

                this.zona_habit.delete(k);
                atualizar_zonas();
                this.guardarZonasHabitacaoTxt("data/output/zonahabitacao.txt");
                loggerfiles_zonas_habitacao(id);
                return;
            }

        }
    }



    /**
     * Edita todos os dados referentes a uma zona de habitacao Lança uma
     * excepção, se a potência consumida for maior que a potência suportada.
     *
     * @param zh consiste na habitação que queremos editar
     */
    public void editZonasHabitacao(ZonaHabitacao zh) throws ImpossivelEmprestimoDePotenciaException {
        int aux1 = 0;
        for (Integer k : this.zona_habit.keys()) {
            {
                if (this.zona_habit.get(k).getZona_id().equals(zh.getZona_id())) {
                    aux1 = zona_habit.get(k).getEnerg_suportada();
                    if (((this.getEnergia_consumida_zonas() - aux1) + zh.getEnerg_suportada()) <= this.getEnergia_suportada()) {
                        this.zona_habit.get(k).setZona_id(zh.getZona_id());
                    } else {
                        throw new ImpossivelEmprestimoDePotenciaException("Nao e possivel alterar a energia suportada");
                    }
                    this.zona_habit.get(k).setEnerg_suportada(zh.getEnerg_suportada());
                    this.guardarZonasHabitacaoTxt("data/output/zonahabitacao.txt");
                    atualizar_zonas();
                    loggerfiles_zonas_habitacao(k);
                    return;
                }
            }

        }
    }



      /**
     *Adiiciona uma nova zona de habitação a red black de zonas de habitação
     *Lança uma excepção, se verificar que a potência consumida pelos equipamentos (através das habitações associadas)
     * correspondentes a essa zona de habitação for maior que a potência suportada, não podendo associar assim a rede
     * para distribuir energia, nem adicionar zonas de habitação ou atualizá-las. 
     * Se for possível adicionar, é enviado para o "logger file" a nova adição.
     * @param novo consiste na zona de habitação que queremos adicionar
     */

    
    public void adicionarZona(ZonaHabitacao novo) throws ImpossivelEmprestimoDePotenciaException {
        int aux1 = novo.getEnerg_suportada() + this.energia_consumida_zonas;
        if (aux1 <= this.getEnergia_suportada()) {
            novo.associarDist(this);
            this.zona_habit.put(novo.getZona_id(), novo);
            atualizar_zonas();
            loggerfiles_zonas_habitacao(novo);
        } else {
            throw new ImpossivelEmprestimoDePotenciaException("Nao e possivel adicionar esta zona , nao ha energica suficiente");
        }
    }

    //"data/output/Habitacao.txt"
    
       /**
     * Guarda todos os dados da habitacao para um ficheiro de texto.
     *
     * @param path caminho para o ficheiro txt
     */
    
    public void guardarHabitacoesTxt(String path) {
        Out ficheiro = new Out(path);
        for (Integer k : this.getZona_habit().keys()) {
            //ficheiro.print(this.getZona_habit().get(k).getZona_id());
            for (Integer g : this.getZona_habit().get(k).getHabitacoes().keys()) {
                String bool;
                if (this.getZona_habit().get(k).getHabitacoes().get(g).isSustentavilidade()) {
                    bool = "T";
                } else {
                    bool = "F";
                }
                ficheiro.print(this.getZona_habit().get(k).getZona_id() + ";");
                ficheiro.println(this.getZona_habit().get(k).getHabitacoes().get(g).getHab_id() + ";"
                        + this.getZona_habit().get(k).getHabitacoes().get(g).getCont_energia() + ";"
                        + this.getZona_habit().get(k).getHabitacoes().get(g).getPotencia_suportada() + ";"
                        + bool + ";"
                        + this.getZona_habit().get(k).getHabitacoes().get(g).getEnerg_prod() + ";");
                loggerfiles_habitacoes(k);
            }

        }

    }

    //"data/output/Equipamentos.txt"
       /**
     * Guarda todos os dados dos equipamentos para um ficheiro de texto.
     *
     * @param path caminho para o ficheiro txt
     */
    
    public void guardarEquipamentosTxt(String path) {

        Out ficheiro = new Out(path);
        for (Integer k : this.getZona_habit().keys()) {
            for (Integer g : this.getZona_habit().get(k).getHabitacoes().keys()) {
                for (Integer q : this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().keys()) {
                    String bool1;
                    String bool2;
                    if (this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).isConsume_energia()) {
                        bool1 = "T";
                    } else {
                        bool1 = "F";
                    }

                    if (this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).isEstado()) {
                        bool2 = "T";

                    } else {
                        bool2 = "F";
                    }
                    ficheiro.print(this.zona_habit.get(k).getHabitacoes().get(g).getHab_id() + ";");
                    ficheiro.println(this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).getEp_id() + ";"
                            + this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).getNome() + ";"
                            + this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).getCategory() + ";"
                            + bool1 + ";"
                            + bool2 + ";"
                            + this.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(q).getKwh() + ";");
                }
            }
        }
        ficheiro.close();

    }

    /**
     * Procura uma ZonaHabitação com um determinado um id, e devolve os dados
     * relativos a mesma
     *
     * @param id é o identificador da zona habitacao que pretendemos obter
     * @return os dados da zonahabitacao pretendida.
     */
    public ZonaHabitacao procurarZona(Integer id) {
        for (Integer k : zona_habit.keys()) {
            if (zona_habit.get(k).getZona_id().equals(id)) {
                return zona_habit.get(k);
            }
        }
        return null;
    }
    
       /**
     * 
     *Armazena todos os ficheiros de texto numa só pasta
     * 
     */

    public void dump() {
        String idd = Integer.toString(dist_id);
        String path = "data/dump/" + idd + "/" ;
        String str ;
        str = path + "equipamentos.txt";
        guardarEquipamentosTxt(str);
        str = path + "habitacoes.txt";
        guardarHabitacoesTxt(str);
        str = path + "historicos.txt";
        guardarHistoricoTxt(str);
//        str = path + "fontesenergia.txt";
//        guardarfontenergiaTxt(str);
        str = path + "zonahabtacao.txt";
        guardarZonasHabitacaoTxt(str);
    }

    


    /**
     * @return the dist_id
     */
    public Integer getDist_id() {
        return dist_id;
    }

    /**
     * @param dist_id the dist_id to set
     */
    public void setDist_id(Integer dist_id) {
        this.dist_id = dist_id;
    }

    /**
     * @return the num_de_hab
     */
    public Integer getNum_de_hab() {
        return num_de_hab;
    }

    /**
     * @param num_de_hab the num_de_hab to set
     */
    public void setNum_de_hab(Integer num_de_hab) {
        this.num_de_hab = num_de_hab;
    }

    

    /**
     * @return the energia_consumida_zonas
     */
    public Integer getEnergia_consumida_zonas() {
        return energia_consumida_zonas;
    }

    /**
     * @param energia_consumida_zonas the energia_consumida_zonas to set
     */
    public void setEnergia_consumida_zonas(Integer energia_consumida_zonas) {
        this.energia_consumida_zonas = energia_consumida_zonas;
    }

//    /**
//     * @return the fontes_energ
//     */
//    public RedBlackBST_AED2<Integer, FonteEnergia> getFontes_energ() {
//        return fontes_energ;
//    }
//
//    /**
//     * @param fontes_energ the fontes_energ to set
//     */
//    public void setFontes_energ(RedBlackBST_AED2<Integer, FonteEnergia> fontes_energ) {
//        this.fontes_energ = fontes_energ;
//    }

    /**
     * @return the zona_habit
     */
    public RedBlackBST_AED2<Integer, ZonaHabitacao> getZona_habit() {
        return zona_habit;
    }

    /**
     * @param zona_habit the zona_habit to set
     */
    public void setZona_habit(RedBlackBST_AED2<Integer, ZonaHabitacao> zona_habit) {
        this.zona_habit = zona_habit;
    }

    public String toString() {
        return "Distribuicao Eletrica{" + "Id= " + this.dist_id + ", energia suportada=" + this.getEnergia_suportada() + ", Numero de fontes=" + this.num_de_fontes + ", Numero de habitacoes=" + this.num_de_hab + ", Energia consumida =" + this.energia_consumida_zonas + '}';
    }

    /**
     * @return the energia_suportada
     */
    public int getEnergia_suportada() {
        return energia_suportada;
    }

    /**
     * @param energia_suportada the energia_suportada to set
     */
    public void setEnergia_suportada(int energia_suportada) {
        this.energia_suportada = energia_suportada;
    }

}

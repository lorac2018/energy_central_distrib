/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.inf.lp2_aed2_projeto1;


import edu.inf.algoritmos.RedBlackBST_AED2;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KosarajuSharirSCC;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;




/**
 *
 * @author Tiago
 */
public class Rede {
    
    private Integer total_energia_rede = 0;
    
    private Integer total_energia_consumida = 0;
    
    private RedBlackBST_AED2<Integer, DistribuicaoEletrica> distribuicoes = new RedBlackBST_AED2<>();
    
    private RedBlackBST_AED2<Integer, FonteEnergia> fontes_energ = new RedBlackBST_AED2<>();
   
    private final ArrayList<GraphNode> nodes = new ArrayList<>();
    
    private FlowNetwork grafo ;
    
    private static final byte CURTOCAMINHO = 0;
    private static final byte ESTADOATUAL = CURTOCAMINHO;
    
    
    
    /**
     * Construtor
     */
    public Rede (){
        
    }
    
    
    
    
    /**
     * Metodo para criar o grafo e fazer todas as ligações entre os vertices do grafo.
     * Atribui distancias entre nós . Considereamos as distancias como metade de uma capacidade da ligação
     */
    public void Grafo(){
        int size = nodes.size();
        
        //System.out.println(nodes.size());
        //System.out.println(size);
        grafo = new FlowNetwork(nodes.size()+2);
        
        
        // Ligar fontes de energia a distribuicoes
        for(Integer k : this.fontes_energ.keys()){
           for(Integer p : this.distribuicoes.keys()){
               FonteEnergia f = this.fontes_energ.get(k);
               DistribuicaoEletrica d = this.distribuicoes.get(p);
                getGrafo().addEdge(new FlowEdge(f.getId() , d.getId() , d.getEnergia_suportada()));
           } 
        }
        // Ligar Distribuicoes a Habitacoes
        for(Integer k: this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer v : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                    DistribuicaoEletrica d = this.distribuicoes.get(k);
                    Habitacao h = this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(v);
                    getGrafo().addEdge(new FlowEdge(d.getId() , h.getId() , h.getPotencia_suportada()));
                }      
            }
        }
        
        
        // ligar Casas à distribuicao eletrica
        
         for(Integer k: this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer v : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                    DistribuicaoEletrica d = this.distribuicoes.get(k);
                    Habitacao h = this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(v);
                    if(h.getEnerg_prod() > 0){
                        getGrafo().addEdge(new FlowEdge(h.getId() , d.getId() , h.getEnerg_prod()));
                    }
                }      
            }
         }
            
         
         // Liga distribuicoes a distribuicoes , a capacidad da ligação vai ser a soma da energia produzida das habitações dessa distribuição
         for(Integer k : this.distribuicoes.keys()){
             for(Integer p : this.distribuicoes.keys()){
                 DistribuicaoEletrica aux1 = this.distribuicoes.get(k);
                 DistribuicaoEletrica aux2 = this.distribuicoes.get(p);
                 getGrafo().addEdge(new FlowEdge(aux1.getId() , aux2.getId() , aux1.energiaProduzidaHabitacoesDistribuicao() ));
             }
         }
            
        //
        
       int source_dummy = nodes.size(); // nó que vai fornecer energia às fontes de energia
        //System.out.println("source_dummy:" + source_dummy);
        
        int sink_dummy = nodes.size()+1; // nó que vai ser o destino de todos os nos
        //System.out.println("sink_dummy:" + sink_dummy);
        for(Integer k: this.fontes_energ.keys()){
            FonteEnergia f = this.fontes_energ.get(k);
            
            getGrafo().addEdge(new FlowEdge(source_dummy , f.getId() , f.getPotencia_gerada()));
            //System.out.println("Fonte : " + f.getFonte_id());
            //System.out.println("Potencia : " + f.getPotencia_gerada());
        }
        
        for(Integer k: this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer v : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                    Habitacao h = this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(v);
                    getGrafo().addEdge(new FlowEdge(h.getId() , sink_dummy , h.getPotencia_suportada()));
                }      
            }
        }
        
        FordFulkerson maxflow = new FordFulkerson(getGrafo(), source_dummy, sink_dummy);
        atribuirDistancias();
        //System.out.println("Vertices : " +grafo.V());
    }
    
    
    
    
    /**
     * Atribui distancias entre nós do grafo . Cada distancia é metade da capacidade de ligação
     */
    public void atribuirDistancias(){
        for (int v = 0; v < grafo.V(); v++) {
            for (FlowEdge e : grafo.adj(v)) {
                e.setDistance(e.capacity()/2);
            }
        }
    }
    
    
    
    /**
     * Metodo para escrever grafo para ficheiro .txt
     * @param path caminho para o ficheiro
     */
    public void escreverGrafoParatxt(String path){
        Out ficheiro = new Out(path);
        
        ficheiro.println(grafo.V());
        ficheiro.println(grafo.E());
        
        
        for (int v = 0; v < grafo.V(); v++) {
            for (FlowEdge e : grafo.adj(v)) {
                ficheiro.println(e.from() + " " +  e.to() + " " + e.capacity());
                }
         }
        
    }
    
    
    /**
     * Metodo para ler grafo do ficheiro .txt . Utiliza metedo da classe FlowNetwork
     * @param path caminho do ficheiro .txt
     * @return Grafo
     */
    public FlowNetwork lerGrafoParatxt(String path){
        In in = new In(path);
        FlowNetwork G = new FlowNetwork(in);
        return G;
    }

    
    
    /**
     * Adiciona um equipamento produtor a uma habitaçao e cria uma ligação com a sua respetiva distribuicao
     * @param eq Novo equipamento
     */
    public void adicionarEquipamentoProdutor(Equipamento eq){
        
        Habitacao h = this.procurarHabitacao(eq.getHabit().getHab_id());
        h.adicionarEquipamento(eq);
        
        getGrafo().addEdge(new FlowEdge(h.getId() , h.getZona_habit().getDist_en().getId(), h.getEnerg_prod()));
        
    }
    
    
    /**
     * Imprime todas as casas AutoSustentaiveis
     */
    public void casasAutoSustentaveis(){
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer l : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                    Habitacao h = this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l);
                    if(h.verificarSeESustentavel()){
                        System.out.println(h.toString());
                    }
                }
            }
        }
    }
    
    
    
    /**
     * Imprime todas as zonas sustentaveis.
     * Supondo que uma zona é sustentavel quando a soma das producoes das habitacoes é mais que 25% do que consome
     */
    public void ZonasSustentaveis(){
        
        for(Integer k : this.getDistribuicoes().keys()){
            for(Integer p : this.getDistribuicoes().get(k).getZona_habit().keys()){
                ZonaHabitacao zh = this.getDistribuicoes().get(k).getZona_habit().get(p);
                float aux1 = zh.calcularSustentabilidade();
                if(aux1 >= 25){
                    System.out.println(zh.toString());
                    System.out.println("Sustentavilidade -> " + aux1 + "%");
                    if((100-aux1) < 0){
                        System.out.println("Nao ha necessidade de pedir energia a rede");
                    } else {
                       
                        System.out.println("Ha necessidade de pedir a rede " + (100-aux1) + "% de energia");
                    }
                }
            }
        }
    }
    
    
    
    /**
     * Imprime as ligações com Flow acima de uma percentagem
     * @param f Percentagem que queremos pedir
     */
    public void verificarFlowLigacoesAcimaDe(Integer f){
        for (int v = 0; v < grafo.V(); v++) {
            for (FlowEdge e : grafo.adj(v)) {
                float aux1 = 0;
                
                aux1 = (float)(e.flow()*100) / (float)e.capacity();
                
                
                if ((v == e.from()) && aux1 > f ) {
                    StdOut.println("   " + e);
                }
            }
        }
    }
    
    
    
    /**
     * Imprime as ligações com Flow abaixo de uma percentagem
     * @param f Percentagem que queremos pedir
     */
    public void verificarFlowLigacoesAbaixoDe(Integer f){
        for (int v = 0; v < grafo.V(); v++) {
            for (FlowEdge e : grafo.adj(v)) {
                float aux1 = 0;
                
                aux1 = (float)(e.flow()*100) / (float)e.capacity();
                
                
                if ((v == e.from()) && aux1 < f ) {
                    StdOut.println("   " + e);
                }
            }
        }
    }
    
   
    
    
    /**
     * Dado um id de um nó verifica que classe é
     * @param id id que queremos procurar
     */
    public void toStringDeUmNode(Integer id){
        
        if(this.nodes.get(id) instanceof Habitacao){
            Habitacao h = (Habitacao)this.nodes.get(id);
            System.out.println(h.toString());
        } else if (this.nodes.get(id) instanceof FonteEnergia){
            FonteEnergia f = (FonteEnergia) this.nodes.get(id);
            System.out.println(f.toString());
        } else if (this.nodes.get(id) instanceof DistribuicaoEletrica){
            DistribuicaoEletrica d = (DistribuicaoEletrica) this.nodes.get(id);
            System.out.println(d.toString());
        }
        
    }
    
    public Object retornaClasseRespetivaNode(Integer id){
         if(this.nodes.get(id) instanceof Habitacao){
            Habitacao h = (Habitacao)this.nodes.get(id);
            return h;
        } else if (this.nodes.get(id) instanceof FonteEnergia){
            FonteEnergia f = (FonteEnergia) this.nodes.get(id);
            return f;
        } else if (this.nodes.get(id) instanceof DistribuicaoEletrica){
            DistribuicaoEletrica d = (DistribuicaoEletrica) this.nodes.get(id);
            return d;
        }
        return null;
    }
    
    public Habitacao retornaHabi(Habitacao h){
        return h;
    }
    
    public DistribuicaoEletrica retornaDist(DistribuicaoEletrica d){
        return d;
    }
    
    public FonteEnergia retornaFonte(FonteEnergia f){
        return f;
    }
    
    
    /**
     * Metodo que adiciona uma Habitação a uma redblack e também um nó ao grafo e respetiva ligação. Se houver aparelhos produtores também adiciona uma ligação com sentido à distribuição
     * @param h Habitacao que queremos adicionar
     * @throws ImpossivelAdicionarEquipamentosException
     * @throws ImpossivelEmprestimoDePotenciaException 
     */
    public void adicionarHabitacoes(Habitacao h) throws ImpossivelAdicionarEquipamentosException, ImpossivelEmprestimoDePotenciaException{
        ZonaHabitacao zh = this.procurarZona(h.getZona_habit().getZona_id());
        if(zh.verificarSeExisteHabitacao(h.getHab_id())){
            throw new ImpossivelAdicionarEquipamentosException("Ja existe uma habitacao com este id");
        } else {
            zh.adicionarHabitacao(h);
            this.addNode(h);
//            getGrafo().addEdge(new FlowEdge(zh.getDist_en().getId() , h.getId() , h.getPotencia_suportada()));
//             if(h.getEnerg_prod() > 0){
//                 getGrafo().addEdge(new FlowEdge(h.getId() , zh.getDist_en().getId() , h.getEnerg_prod()));
//              }
             Grafo();
        }
        
    }
    
    
    
    /**
     * Metodo para adicionar uma distribuição a uma redblack e tambem adiciona um nó ao grafo
     * @param d Distribuicao que queremos adicionar
     * @throws ImpossivelAdicionarEquipamentosException 
     */
    public void adidiconarDistribuicao(DistribuicaoEletrica d) throws ImpossivelAdicionarEquipamentosException{
        
        if(verificarSeExiteDistribuicao(d.getDist_id())){
            throw new ImpossivelAdicionarEquipamentosException("Ja existe uma distribuicao com este id");
        } else{
            this.addNode(d);
            this.distribuicoes.put(d.getDist_id(), d);
            Grafo();
        }
    }
    
    
    /**
     * Metodo para adicionar Fonte de energia a uma redblack e tambem adiciona um no ao grafo
     * @param f Fonte de energia que queremos adicionar
     * @throws ImpossivelAdicionarEquipamentosException 
     */
    public void adicionarFonteEnergia(FonteEnergia f) throws ImpossivelAdicionarEquipamentosException{
        
        if(this.verificarSeExisteFonte(f.getFonte_id())){
            throw new ImpossivelAdicionarEquipamentosException("Ja existe uma fonte com este id");
        }else{
            
            this.addNode(f);
            this.adicionarFonte(f);
//            for(Integer k : this.distribuicoes.keys()){
//               getGrafo().addEdge(new FlowEdge(f.getId() , this.distribuicoes.get(k).getId(), f.getPotencia_gerada()));
//            }
            Grafo();
        }
        
    }
    
    
    /**
     * Carrega distribuição/s eletrica/s de um ficheiro de texto .txt
     * @param path Caminho para o ficheiro .txt
     * @throws ImpossivelAdicionarEquipamentosException
     * @throws ImpossivelEmprestimoDePotenciaException 
     */
   public void carregarDistribuicaoTxt(String path) throws ImpossivelAdicionarEquipamentosException, ImpossivelEmprestimoDePotenciaException {
        
       In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer id = Integer.parseInt(texto[0]);
            Integer suportado = Integer.parseInt(texto[1]);
            DistribuicaoEletrica dist = new DistribuicaoEletrica(id , suportado);
            String path2 = "Data/input/"+ id + "/";
            
//            String str = path2 + "fontesenergia.txt";
//            dist.carregarFontesEnergiaTxt(str);
            String str = path2;

            str = path2 + "zonahabitacao.txt";
            //System.out.println(str);
            dist.carregarZonasHabitacaoTxt(str);
            
            
            str = path2 + "habitacoes.txt";
            //System.out.println(str);
            dist.carregarHabitacaoTxt(str);
            
            
            str = path2 + "equipamentos.txt";
            //System.out.println(str);
            dist.carregarEquipamentosTxt(str);
            str = path2 + "historicos.txt"; 
            //System.out.println(str);
            dist.carregarHistoricoTxt(str);

            

            this.distribuicoes.put(id, dist);
            dist.atualizar_zonas();
        }
    
        this.setTotal_energia_consumida(energiaGastaPelasDistribuicoes());
        
    }

   
   /**
    * Adiciona um nó a um arraylist de nós
    * @param node Recebe um objeto que extende GraphNode
    */
   public void addNode(GraphNode node){
       
       this.nodes.add(node);
 
   }
   
   
   /**
    * Verifica se exite uma distribuição com o mesmo id na redblack
    * @param id id da distribuicao
    * @return false se nao houver e true se houver
    */
   public boolean verificarSeExiteDistribuicao(Integer id){
       for(Integer k : this.distribuicoes.keys()){
           if(this.distribuicoes.get(k).getDist_id().equals(id)){
               return true;
           }
       }
       return false;
   }
   
   
   
   /**
    * Verifica se existe uma fonte de energia com o mesmo id na redblack
    * @param id id da fonte de energia
    * @return false se nao existir e true se existir
    */
   public boolean verificarSeExisteFonte(Integer id){
       
       for(Integer k : this.fontes_energ.keys()){
           if(this.fontes_energ.get(k).getFonte_id().equals(id)){
               return true;
           }
       }
       return false;
   }
   
   
   
   
    /**
     * Carrega a informação relativa às fontes de energia de um ficheiro txt
     *
     * @param path caminho para o ficheiro txt
     */
    public void carregarFontesEnergiaTxt(String path) {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] texto = in.readLine().split(";");
            Integer id = Integer.parseInt(texto[0]);
            String nome = texto[1];
            Integer tensao = Integer.parseInt(texto[2]);
            Integer potencia = Integer.parseInt(texto[3]);
            FonteEnergia f = new FonteEnergia(nome, potencia, tensao, id);
            getFontes_energ().put(id, f);
            
        }
        
        this.setTotal_energia_rede(energiaProduzidaFontes());
        
    }
    
    
    
    
        /**
     * Adiciona uma fonte à redblack de fontes
     *
     * @param nome nome da fonte
     * @param potencia potencia produziada pela fonte
     * @param tensao tensao da fonte
     * @param id identificador
     */
    public FonteEnergia adicionarFonte(String nome, Integer potencia, Integer tensao, Integer id) {
        FonteEnergia nova = new FonteEnergia(nome, potencia, tensao, id);
        getFontes_energ().put(id, nova);
        atualizar_fontes();
        this.guardarfontenergiaTxt("data/output/fontesenergia.txt");
        this.loggerfiles(nome);
        return nova;
    }

    /**
     * Adiciona uma fonte à redblack de fontes
     *
     * @param nova objeto para adicionar
     */
    public void adicionarFonte(FonteEnergia nova) {
        getFontes_energ().put(nova.getFonte_id(), nova);
        atualizar_fontes();
        this.guardarfontenergiaTxt("data/output/fontesenergia.txt");
        this.loggerfiles(nova);
    }

    //"data/output/fontesenergia.txt"
    /**
     * Guarda informacao de uma fonte de energia num ficheiro texto
     *
     * @param fontes_energ redblack para guardar infromacao
     */
    public void guardarfontenergiaTxt(String path) {

        Out ficheiro = new Out(path);

        for (Integer k : this.getFontes_energ().keys()) {

            ficheiro.println(this.getFontes_energ().get(k).getFonte_id() + ";" + this.getFontes_energ().get(k).getTipo() + ";" + this.getFontes_energ().get(k).getTensao_energ() + ";" + this.getFontes_energ().get(k).getPotencia_gerada() + ";");
        }
        this.loggerfiles(path);
        ficheiro.close();
    }
    
    
        /**
         * Função para atualizar o estado da rede semrpe que é adicionado , removido ou editado uma fonte de energia
         */
        public void atualizar_fontes() {
        int aux1 = 0;
        
        for (Integer k : getFontes_energ().keys()) {
            aux1 += getFontes_energ().get(k).getPotencia_gerada();
            
        }
        this.setTotal_energia_rede(aux1);
        
        this.loggerfiles(aux1);
       
    }
    

    
    /**
     * Gaurda todos os dados relatios às redblacks
     */
    public void dump(){
        for(Integer k : this.distribuicoes.keys()){
            this.distribuicoes.get(k).dump();
        }
        guardarDistribuicoes("data/dump/distribuicoes.txt");
    }
    
    
    
    /**
     * Metodo que imprime para o ficheiro todas as distribuicoes presentes nas redblacks
     * @param path caminho para o ficheiro
     */
    public void guardarDistribuicoes(String path){
        Out ficheiro = new Out(path);

        for (Integer k : this.distribuicoes.keys()) {

            ficheiro.println(this.distribuicoes.get(k).getDist_id() + ";" + this.distribuicoes.get(k).getEnergia_suportada() + ";");
            
        }
        ficheiro.close();
    }
    
    
    
    /**
     * Energia que está a ser consumida pelas distribuicoes no momento
     * @return retorna o numero de energia a ser consumido
     */
    public int energiaGastaPelasDistribuicoes(){
        int energia = 0;
        for(Integer k : this.distribuicoes.keys()){
            energia += this.distribuicoes.get(k).getEnergia_consumida_zonas();
        }
        return energia;
    }
    
    
    
    /**
     * Metodo que retorna a energia produzida pelas fontes de energia
     * @return O numero de energia produziada
     */
    public int energiaProduzidaFontes(){
        int energia = 0;
        for(Integer k : this.distribuicoes.keys()){
            energia += this.fontes_energ.get(k).getPotencia_gerada();
        }
        return energia;
    }
    
    
    
    /**
     * Metodo para adicionar os nós ao arraylist de nós de todas as classes que usamos para o grafo
     */
    public void preencherNodes(){
      
        for(Integer a : this.fontes_energ.keys()){
            this.addNode(this.fontes_energ.get(a));
        }
        for(Integer b : this.distribuicoes.keys()){
            this.addNode(this.distribuicoes.get(b));
        }
        
        for(Integer c : this.distribuicoes.keys()){
            for(Integer i : this.distribuicoes.get(c).getZona_habit().keys()){
                for(Integer p : this.distribuicoes.get(c).getZona_habit().get(i).getHabitacoes().keys()){
                    this.addNode(this.distribuicoes.get(c).getZona_habit().get(i).getHabitacoes().get(p));
                }
            }
        } 
    }
    
    
        /**
     * Funcao para apagar uma fonte de energia
     *
     * @param id id da fonte que queremos remover
     */
    public void deleteFonte(Integer id) {
        getFontes_energ().delete(id);
        atualizar_fontes();

    }


    /**
     * Pesquisa uma fonte por id
     *
     * @param id identificador
     * @return fonte
     */
    public FonteEnergia procuraFonte(Integer id) {
        return getFontes_energ().get(id);
    }

    
    
    
    /**
     * Edita todos os dados referentes a uma fonte de energia
     *
     * @param f consiste na fonte de energia que queremos editar
     */
    public void editFonteEnergia(FonteEnergia f) {

        for (Integer k : getFontes_energ().keys()) {
            if (getFontes_energ().get(k).getFonte_id().equals(f.getFonte_id())) {
                getFontes_energ().get(k).setFonte_id(f.getFonte_id());
                getFontes_energ().get(k).setPotencia_gerada(f.getPotencia_gerada());
                getFontes_energ().get(k).setTensao_energ(f.getTensao_energ());
                getFontes_energ().get(k).setTipo(f.getTipo());
                this.guardarfontenergiaTxt("data/output/fontesenergia.txt");
                this.loggerfiles(f);
                atualizar_fontes();
                return;
            }
        }
    }
    
    
    
    
    /**
     * Metodo que retorna uma redblack com os ids das habitacoes e um float que representa a % de sustentavilidade
     * @return Redblack com a informação de todas as habitacoes que são sustentaveis 
     */
    public RedBlackBST_AED2 habitacoeSustentaveis() {
        RedBlackBST_AED2<Integer, Float> st = new RedBlackBST_AED2<>();
        for(Integer p : this.distribuicoes.keys()){
            for (Integer k : this.distribuicoes.get(p).getZona_habit().keys()) {
                for (Integer g : this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().keys()) {
                        Habitacao h = this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().get(g);
                        if (h.getEnerg_prod() > 0) {
                            Float novo = h.calculaSustentabilidadeCasa();
                            //System.out.println(novo);
                            st.put(h.getHab_id(), novo);
                    }
                }
            }
        }
        return st;
    }

    
    
    /**
     * Metodo para procurar uma habitacao pelo seu id nas redblacks
     * @param id id da habitação a procurar
     * @return Retorna a habitação
     */
    public Habitacao procurarHabitacao(Integer id){
        for(Integer p : this.distribuicoes.keys()){
            for (Integer k : this.distribuicoes.get(p).getZona_habit().keys()) {
                for (Integer g : this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().keys()) {
                        Habitacao h = this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().get(g);
                        if(h.getHab_id().equals(id)){
                            return h;
                        }
                }
            }
        }
        return null;
    }
    
    
    /**
     * Imprime todas as casa sustentaveis
     */
    public void printTodasAsSustentaveis() {
        System.out.println("Casas sustentaveis:");
        RedBlackBST_AED2<Integer, Float> st = habitacoeSustentaveis();
        
//        for(Integer k : st.keys()){
//            System.out.println("Redblack : " + st.get(k));
//        }
        
        ArrayList<Float> ar = new ArrayList<>();
        
        for(Integer k : st.keys()){
            ar.add(st.get(k));
        }
        
        Collections.sort(ar);
        int id =0 ;
        
       
        Habitacao h = null;
        //System.out.println("aux=" + aux1);
        for (int i = ar.size() - 1; i >=0; i--) {
            Float f = ar.get(i);
            for(Integer k : st.keys()){
                if(st.get(k).equals(f)){
                     h = procurarHabitacao(k);
                     st.delete(k);
                     break;
                }
            }
            System.out.println("Percentagem " + ar.get(i) + "% = " + h.toString());
        }
    }
    
    
    /*
     * Função que imprime as habitações com mais sustentabilidade. 
     * @param x é o id da habitação
     */
    public void printXCasasMaisSustentaveis(Integer x) {
        System.out.println("Mais sustentaveis:");
        RedBlackBST_AED2<Integer, Float> st = habitacoeSustentaveis();
        ArrayList<Float> ar = new ArrayList<>();
        
        for(Integer k : st.keys()){
            ar.add(st.get(k));
        }
        
        Collections.sort(ar);
        int id =0 ;
        int aux1;
        aux1 = ar.size() - x;
        Habitacao h = null;
        //System.out.println("aux=" + aux1);
        for (int i = ar.size() - 1; i >= aux1; i--) {
            Float f = ar.get(i);
            for(Integer k : st.keys()){
                if(st.get(k).equals(f)){
                     h = procurarHabitacao(k);
                     st.delete(k);
                     break;
                }
            }
            System.out.println("Percentagem " + ar.get(i) + "% = " + h.toString());
        }
    }
    
    /**
     * Metodo que procura uma distribuicao nas redblacks pelo seu id
     * @param id id da distribuicao a procurar
     * @return retorna a distribuicao 
     */
    public DistribuicaoEletrica procurarDistribuicao(Integer id){
        for(Integer k : this.distribuicoes.keys()){
            if(this.distribuicoes.get(k).getDist_id().equals(id)){
                return this.distribuicoes.get(k);
            }
        }
        return null;
    }
    
    
    
    
    
    // FICHEIROS BINARIOS 
    
  
    /**
     * Metodo que escreve o grafo para um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverGrafoParaFicheiroBin() throws FileNotFoundException, IOException{
         String filename = "Grafo.bin";
        
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        
        oos.writeObject(grafo);
    }
    
    
    /**
     * Metodo que escreve informação das fontes de energia par aum ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverFontesEnergiaParaFicheiroBin() throws FileNotFoundException, IOException {
        String filename = "FontesEnergia.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.fontes_energ.keys()){
            oos.writeObject(this.fontes_energ.get(k));
        }
    }
    
    
    /**
     * Metodo que escreve informação das distribuições para um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverDistribuicaoEnergiaParaFicheiroBin() throws FileNotFoundException, IOException{
        String filename = "DistribuicaoEnergia.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.distribuicoes.keys()){
            oos.writeObject(this.distribuicoes.get(k));
        }
    }
    
    
    /**
     * Metodo que escreve as zonas de habitação para um ficheiro binario 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverZonasParaFicherosBin() throws FileNotFoundException, IOException{
         String filename = "Zonas.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                oos.writeObject(this.distribuicoes.get(k).getZona_habit().get(p));
            }
        }
    }
    
    /**
     * Metodo que escreve as habitacoes para um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverHabitacoesParaFicheiroBin() throws FileNotFoundException , IOException{
        String filename = "Habitacoes.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer l : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                    oos.writeObject(this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l));
                }
            }
        }
    }
    
    
    /**
     * Metodo que escreve equipamentos para um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverEquipamentosParaFicheiroBin() throws FileNotFoundException , IOException{
        String filename = "Equipamentos.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer l : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                   for(Integer t : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l).getEquipamentos().keys()){
                       oos.writeObject(this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l).getEquipamentos().get(t));
                   }
                }
            }
        }
    }
    
    
    /**
     * Metodo que escreve historicos de equipamentos para ficheiros binarios 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void escreverHistoricosParaFicheiroBin() throws FileNotFoundException , IOException{
        String filename = "Historicos.bin";
        
        File f = new File(filename);
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream (fos);
        
        
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                for(Integer l : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().keys()){
                   for(Integer t : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l).getEquipamentos().keys()){
                       for(Date h : this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l).getEquipamentos().get(t).getHistoricos().keys() ){
                           oos.writeObject(this.distribuicoes.get(k).getZona_habit().get(p).getHabitacoes().get(l).getEquipamentos().get(t).getHistoricos().get(h));
                       }
                   }
                }
            }
        }
    }
    
    
    /**
     * Metodo utilizado para ler um grafo de um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerGrafoBin() throws FileNotFoundException, IOException, ClassNotFoundException{
        String filename = "Grafo.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        FlowNetwork graph = (FlowNetwork)ois.readObject();
        grafo = graph;
        
//        System.out.println("\n\nREAD!!!!!!\n\n");
//        
//        
//        System.out.println("Graph: " + grafo);
//        System.out.println("Graph -> V: " + grafo.V());
//        System.out.println("Graph -> edges: ");
//        for (FlowEdge de : grafo.edges()) {
//            System.out.println(de.from() + " " + de.to() + " " + de.capacity());
//        }
    }
    
    
    
    /**
     * Metodo utilizado para ler fontes de energia de um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerFontesEnergiaBin() throws FileNotFoundException, IOException, ClassNotFoundException{
        String filename = "FontesEnergia.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        RedBlackBST_AED2<Integer, FonteEnergia> font= new RedBlackBST_AED2<>();
        
        while(ois.available()> 0){
        FonteEnergia fonte = (FonteEnergia)ois.readObject();
        font.put(fonte.getFonte_id(), fonte);
        //this.adicionarFonte(fonte);
        }
        
        
        for(Integer k : font.keys()){
            System.out.println(font.get(k).toString());
        }
        
    }
    
    
    /**
     * Metodo utilizado para ler distribuicoes de um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerDistribuicoesBin() throws FileNotFoundException, IOException, ClassNotFoundException{
        String filename = "Distribuicoes.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        while(ois.available()> 0){
        DistribuicaoEletrica dist = (DistribuicaoEletrica)ois.readObject();
        
        this.distribuicoes.put(dist.getDist_id(), dist);
        }
        
    }
    
    /**
     * Metodo utilizado para ler zonas de habitacao de ficheiros binarios
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ImpossivelEmprestimoDePotenciaException 
     */
    public void lerZonasHabitacaoBin() throws FileNotFoundException, IOException, ClassNotFoundException, ImpossivelEmprestimoDePotenciaException{
        
        String filename = "ZonasHabitacao.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        while(ois.available()> 0){
        ZonaHabitacao zh = (ZonaHabitacao)ois.readObject();
        
        this.distribuicoes.get(zh.getZona_id()).adicionarZona(zh);
        }
    }
    
    
    /**
     * Metodo utilizado para ler habitacoes de um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ImpossivelEmprestimoDePotenciaException 
     */
    public void lerHabiacoesBin() throws FileNotFoundException, IOException, ClassNotFoundException, ImpossivelEmprestimoDePotenciaException{
        String filename = "Habitacoes.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        while(ois.available()> 0){
        Habitacao h = (Habitacao)ois.readObject();
        ZonaHabitacao zh = procurarZona(h.getZona_habit().getZona_id());
        
        zh.adicionarHabitacao(h);
        }
    }
    
    
    /**
     * Metodo utilizado para ler equipamentos de um ficheiro binario 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerEquipamentosBin() throws FileNotFoundException, IOException, ClassNotFoundException{
        String filename = "Habitacoes.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        
        while(ois.available()> 0){
        Equipamento e = (Equipamento)ois.readObject();
        
        Habitacao h = procurarHabitacao(e.getHabit().getHab_id());
        
        h.adicionarEquipamento(e);
        }
      
    }
    
    /**
     * Metodo utilizado para ler historicos de um ficheiro binario
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerHistoricos() throws FileNotFoundException, IOException, ClassNotFoundException{
        String filename = "Habitacoes.bin";
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        while(ois.available()> 0){
        Historico hi = (Historico)ois.readObject();
        
        Equipamento e = procurarEquipamento(hi.getEquipamento().getEp_id());
        }
        
    }
    
    
    
    
    
    
    
    /**
     * Procaura um equipamento com um determinado id 
     * @param id id do equipamento que queremos procurar
     * @return equipamento 
     */
    public Equipamento procurarEquipamento(Integer id){
        
        for(Integer p : this.distribuicoes.keys()){
            for (Integer k : this.distribuicoes.get(p).getZona_habit().keys()) {
                for (Integer g : this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().keys()) {
                      for(Integer f : this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().keys()){
                          Equipamento eq = this.distribuicoes.get(p).getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(f);
                          if(eq.getEp_id().equals(id)){
                              return eq;
                          }
                      }
                }
            }
        }
        return null;
    }
    
    
    /**
     * Metodo que procura uma zona de habitacao com um determinado id
     * @param id id da zona que queremos procurar
     * @return Zona de Habitacao
     */
    public ZonaHabitacao procurarZona(Integer id){
        for(Integer k : this.distribuicoes.keys()){
            for(Integer p : this.distribuicoes.get(k).getZona_habit().keys()){
                if(this.distribuicoes.get(k).getZona_habit().get(p).getZona_id().equals(id)){
                    ZonaHabitacao zh = this.distribuicoes.get(k).getZona_habit().get(p);
                    return zh;
                }
            }
        }
        return null;
    }
    
    
   
    /**
     * Funcao que imprime as x habitacoes com menos sustentabilidade
     * @param x numero de habitacoes menos sustentaveis 
     */
    public void printXCasasMenosSustentaveis(Integer x) {
        System.out.println("Menos sustentaveis:");
        RedBlackBST_AED2<Integer, Float> st = habitacoeSustentaveis();
        ArrayList<Float> ar = new ArrayList<>();
        
        for(Integer k : st.keys()){
            ar.add(st.get(k));
        }
        
        Collections.sort(ar);
        int id =0 ;
        int aux1 = x;
        aux1 = ar.size() - x;
        Habitacao h = null;
        //System.out.println("aux=" + aux1);
        for (int i = 0; i <= aux1; i++) {
            Float f = ar.get(i);
            for(Integer k : st.keys()){
                if(st.get(k).equals(f)){
                     h = procurarHabitacao(k);
                     st.delete(k);
                     break;
                }
            }
            System.out.println("Percentagem " + ar.get(i) + "% = " + h.toString());
        }
    }
    
    
    
    
     /**
     * Permite criar ficheiros log, que guardam todos os eventos associados às fontes de energia, 
     * como por exemplo, adicionar, remover, editar fontes.
     * 
     *
     * @param c objecto que iriamos usar para originarmos os seus eventos. Neste contexto, será as fontes de energia.
     */
    public static void loggerfiles(Object c) {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("Data/LogFile.log");

            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            //logger.info("Log1");
            // (SecurityException e) {
            // e.printStackTrace();
        } catch (IOException e) {
        }

        //logger.log(Level.INFO, "Fontes adicionadas {0}", c);
        //logger.log(Level.INFO, "Fontes removidas {0}", c.toString());
        //logger.log(Level.INFO, "Fontes editadas {0}", c.toString());
    }

     /* Permite criar ficheiros log, que guardam todos os eventos associados, 
     * como por exemplo, adicionar, remover, editar zonas de habitacao
     * 
     *
     * @param c objecto que iriamos usar para originarmos os seus eventos, ações, nesta função será as zonas de habitacao
     */
    public static void loggerfiles_zonas_habitacao(Object c) {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("Data/LogFile.log");

            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            //logger.info("Log1");
            // (SecurityException e) {
            // e.printStackTrace();
        } catch (IOException e) {
        }

        //logger.log(Level.INFO, "Zonas Habitacao adicionadas {0}", c);
        //logger.log(Level.INFO, "Zonas Habitacao removidas {0}", c.toString());
        //logger.log(Level.INFO, "Zonas Habitacao editadas {0} ", c.toString());
    }
    
     /* Permite criar ficheiros log, que guardam todos os eventos associados
     * como por exemplo, adicionar, remover, editar habitações
     * 
     *
     * @param c objecto que iriamos usar para originarmos os seus eventos, ações, nesta função será as habitações
     */

    public static void loggerfiles_habitacoes(Object c) {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("Data/LogFile.log");

            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            //logger.info("Log1");
            // (SecurityException e) {
            // e.printStackTrace();
        } catch (IOException e) {
        }

        //logger.log(Level.INFO, "Habitacoes adicionadas {0}", c);
    }
    
    /**
     * Imprime todos os nos de um arraylist de nos
     */
    public void printNodes(){
        //System.out.println("node size" + nodes.size());
        
        for(int i = 0 ; i < nodes.size() ; i++){
            System.out.println("Id: " + nodes.get(i).getId());
            
        }
    }
    
    
    
     /**
     * Funcao de teste para imprimir a informacao relativa à redblack de fontes
     * de energia
     */
    public void printFontesEnergia() {
        for (Integer k : getFontes_energ().keys()) {
            System.out.println("Nome:" + getFontes_energ().get(k).getTipo());
            System.out.println("Fonte Id :" + getFontes_energ().get(k).getFonte_id());
            System.out.println("Pontencia gerada:" + getFontes_energ().get(k).getPotencia_gerada());
            System.out.println("Tensao:" + getFontes_energ().get(k).getTensao_energ());
            System.out.println("-------------------------------");
        }
    }

   
    /**
     * @return the distribuicoes
     */
    public RedBlackBST_AED2<Integer, DistribuicaoEletrica> getDistribuicoes() {
        return distribuicoes;
    }

    /**
     * @param distribuicoes the distribuicoes to set
     */
    public void setDistribuicoes(RedBlackBST_AED2<Integer, DistribuicaoEletrica> distribuicoes) {
        this.distribuicoes = distribuicoes;
    }

    /**
     * @return the nodes
     */
    public ArrayList<GraphNode> getNodes() {
        return nodes;
    }

    
    
    
 
    /**
     * Função que converte um grafo do tipo FlowNetwork num grafo EdgeWeightedDigraph
     * @param fn Grafo FlowNetwork
     * @return EdgeWeightedDigraph
     */
    public EdgeWeightedDigraph exportFlowToEdgeWeightedDiGraph(FlowNetwork fn) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(fn.V());

        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    DirectedEdge k = new DirectedEdge(e.from(), e.to(), e.flow());
                    g.addEdge(k);
                }
            }
        }
//        System.out.println("-----------------------");
//        System.out.println(g);
        return g;
    }

//requisito 12f
    //O dijsktra é o algoritmo que nos permite descobrir os melhores caminhos, isto é, 
    //os caminhos mais curtos de um vertice ao outro.
//Através do relaxamento das arestas, é possivel descobrir a ligação com o menor peso.
    //Usamos o flownetwork fn, visto que iremos ter o fluxo da rede como vertice de origem e 
    //Convertemos o flownetwork para edgeweightedigraph, visto que todos os algoritmos
    //referentes ao shortest path (acyclic, e bellman-ford) usam todos o mesmo tipo de grafos
    //denominado por EdgeWeightedDigraph, grafos dirigidos com pesos nas arestas
    //(dai existir também o directed edge)
    
    /**
     * Metodo para calcular o caminho mais curto entre um nó de origem e outro de destino
     * @param fn Grafo do tipo FlowNetwork 
     * @param from vertice de origem
     * @param to vertice de destino
     * @return Conjunto de ligacoes que formam o caminho mais curto
     */
    public Iterable<DirectedEdge> CalcularCaminhoMaisCurto(FlowNetwork fn, int from, int to) {
     
       EdgeWeightedDigraph g = new EdgeWeightedDigraph(fn.V());

        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    DirectedEdge k = new DirectedEdge(e.from(), e.to(), e.getDistance());
                    g.addEdge(k);
                }
            }
        }

        DijkstraSP dijkstra = new DijkstraSP(g, from);
        return dijkstra.pathTo(to);

    }
    
    
    
    
    
    //Falta o mapeamento, pelos custos, só não entendo, que custos serão.
    //Se é do fluxo para as casas pelo zona id ou pela potência, ou ambos?
    //requisito 11d
    //Deverá poder-se verificar se o grafo de ligações da rede energética é conexo;
    //Um grafo é fortemente conexo (= strongly connected) se cada um de seus vértices é fortemente ligado a cada um dos demais. 
    //Kosaraju, corre dfs, obtem a pos ordem inversa (topological order), dá nos se:
    //1. os componentes sao fortemente conexados
    //2. quais os componentes sao fortemente conexados.
    //Neste cenario, apenas precisamos de verificar se são
    //Se o output for true, significa que estão fortemente conexados, pois estao tao strongly connected.

    /**
     * Metodo para verificar se o grafo é conexo
     * @param fn Grafo do tipo FLowNetwork
     * @return True se for conexo e false se não for conexo
     */
    public boolean VerificarGrafoConexo(FlowNetwork fn) {

        EdgeWeightedDigraph g = this.exportFlowToEdgeWeightedDiGraph(fn);

        Digraph gd = new Digraph(g.V());

        for (int i = 0; i < g.V(); i++) {
            for (DirectedEdge de : g.adj(i)) {

                gd.addEdge(de.from(), de.to());
            }
        }
        KosarajuSharirSCC conexo = new KosarajuSharirSCC(gd);
        return conexo.count() == 1;

    }
    
    
     //Conversão do grafo FlowNetwork para EdgeWeighteddigraph
    
    /**
     * Metodo que faz a conversao de grafo FlowNetwork para edgeweightedDigraph 
     * @param fn Grafo Flownetwork
     * @return grafo EdgeWeightedGraph 
     */
    public EdgeWeightedGraph exportFlowToEdgeWeightedGraph(FlowNetwork fn) {
        EdgeWeightedGraph g = new EdgeWeightedGraph(fn.V());

        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    Edge k = new Edge(e.from(), e.to(), e.flow());
                    g.addEdge(k);
                }
            }
        }
        return g;
    }
    
    
    
    
    //R17:Calcular a MST (Árvore de Custos Mínimos) para realizar a manutenção a toda a
    //rede
    //Para verificar a rede, teremos de ter um vértice de origem por onde a nossa manutenção
    //começará, teremos de ter um vertice de destino, onde iremos verificar se é possivel a ligação entre 
    //ambos vertices
    //The either() and other() methods are useful for accessing the edge's vertices; 
    //the compareTo() method compares edges by weight.
    
    
    /**
     * Calcula a arvore de custos minimos 
     * @param fn Grafo flowNetwork
     */
    public void verificarRede(FlowNetwork fn) {

        EdgeWeightedGraph g = exportFlowToEdgeWeightedGraph(fn);
        KruskalMST kruskal = new KruskalMST(g);
        
        for (Edge g1 : kruskal.edges()) {

            System.out.println(g1.toString());
            //g.addEdge(g1);
            //System.out.println(" "+g1.other(vertix));

        }
        
        //return g;
    }

    
    
    
    /**
     * @return the fontes_energia
     */
    public RedBlackBST_AED2<Integer, FonteEnergia> getFontes_energ() {
        return fontes_energ;
    }

    /**
     * @param fontes_energia the fontes_energia to set
     */
    public void setFontes_energ(RedBlackBST_AED2<Integer, FonteEnergia> fontes_energia) {
        this.fontes_energ = fontes_energia;
    }

    /**
     * @return the total_energia_rede
     */
    public Integer getTotal_energia_rede() {
        return total_energia_rede;
    }

    /**
     * @param total_energia_rede the total_energia_rede to set
     */
    public void setTotal_energia_rede(Integer total_energia_rede) {
        this.total_energia_rede = total_energia_rede;
    }

    /**
     * @return the total_energia_consumida
     */
    public Integer getTotal_energia_consumida() {
        return total_energia_consumida;
    }

    /**
     * @param total_energia_consumida the total_energia_consumida to set
     */
    public void setTotal_energia_consumida(Integer total_energia_consumida) {
        this.total_energia_consumida = total_energia_consumida;
    }

    /**
     * @return the grafo
     */
    public FlowNetwork getGrafo() {
        return grafo;
    }
   
}

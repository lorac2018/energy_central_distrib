/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.inf.lp2_aed2_projeto1;

import Menu.Gestor;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ImpossivelEmprestimoDePotenciaException, ImpossivelEmprestimoDePotenciaException, ImpossivelAdicionarEquipamentosException, InterruptedException, IOException, FileNotFoundException, ClassNotFoundException {
       // Gestor g = new Gestor();
       // g.setVisible(true);

        Rede r = new Rede();

        r.carregarFontesEnergiaTxt(".//Data//input//fontesenergia.txt");
        r.carregarDistribuicaoTxt(".//Data//input//Distribuicoes.txt");
        

        r.preencherNodes();
        r.Grafo();

        // System.out.println(r.getGrafo());
     
//        
//        System.out.println("---------------------------------");
//        ZonaHabitacao zh = r.procurarZona(1);
//        Habitacao h = new Habitacao(1000 , 2222 , false , 0, 20);
//        h.associarZona(zh);
//        
//        
//        r.adicionarHabitacoes(h);
//        
//        
//        System.out.println(r.getGrafo());

//        for(Integer a : r.getDistribuicoes().keys()){
//            for(Integer b : r.getDistribuicoes().get(a).getZona_habit().keys()){
//                for(Integer c : r.getDistribuicoes().get(a).getZona_habit().get(b).getHabitacoes().keys()){
//                    System.out.println(r.getDistribuicoes().get(a).getZona_habit().get(b).getHabitacoes().get(c).toString());
//                    //for(Integer d : r.getDistribuicoes().get(a).getZona_habit().get(b).getHabitacoes().get(c).getEquipamentos().keys()){
//                    //    System.out.println(r.getDistribuicoes().get(a).getZona_habit().get(b).getHabitacoes().get(c).getEquipamentos().get(d).toString());
//                   // }
//                }
//            }
//        }
       // System.out.println(r.calcularCaminhoMaisCurto(r.getGrafo(), r.getFontes_energ().get(1).getId(), r.getDistribuicoes().get(1).getZona_habit().get(1).getHabitacoes().get(1).getId()));
        //System.out.println(" " + r.verificarGrafoConexo(r.getGrafo()));

//     System.out.println( r.CalcularCaminhoMaisCurto(r.getGrafo(), r.getFontes_energ().get(1).getId() , r.getDistribuicoes().get(1).getZona_habit().get(1).getHabitacoes().get(1).getId()));
        // System.out.println(" " +r.verificarRede(r.getGrafo(), r.getDistribuicoes().get(1).getZona_habit().get(1).getZona_id()));
        //System.out.println(" " + r.verificarRede(r.getGrafo(), r.getDistribuicoes().get(1).getId()));

        //r.escreverFontesEnergiaParaFicheiroBin();
        //r.lerFontesEnergiaBin();
        //System.out.println(r.getGrafo());
        //r.ZonasSustentaveis();
        //r.casasAutoSustentaveis();
        //r.escreverGrafoParaFicheiroBin();
        //r.lerGrafoBin();
       // r.printXCasasMaisSustentaveis(2);
        //r.printTodasAsSustentaveis();
        //r.printXCasasMenosSustentaveis(3);
       // r.verificarFlowLigacoesAcimaDe(50);
        //r.verificarFlowLigacoesAbaixoDe(50);
        
        //r.toStringDeUmNode(6);
        
      r.verificarRede(r.getGrafo());
        
//        for(Integer k : r.getDistribuicoes().keys()){
//
//            for(Integer o : r.getDistribuicoes().get(k).getZona_habit().keys()){
//                System.out.println("->" + r.getDistribuicoes().get(k).getZona_habit().get(o).getPotencia_casas());
//                for(Integer p :r.getDistribuicoes().get(k).getZona_habit().get(o).getHabitacoes().keys()){
//                    System.out.println(" -* " + r.getDistribuicoes().get(k).getZona_habit().get(o).getHabitacoes().get(p).getCont_energia());
//                }
//                
//            }
//        }
//        for(Integer k :r.getDistribuicoes().keys()){
//            for(Integer o : r.getDistribuicoes().get(k).getZona_habit().keys()){
//                System.out.println(r.getDistribuicoes().get(k).getZona_habit().get(o).toString());
//            }
//        }
//        
//        String path = ".//Data//input//fontesenergia.txt";
//        DistribuicaoEletrica dist = new DistribuicaoEletrica();
//
//        dist.carregarFontesEnergiaTxt(path);
//        dist.carregarZonasHabitacaoTxt(".//Data//input//zonahabitacao.txt");
//        dist.carregarHabitacaoTxt(".//Data//input//habitacoes.txt");
//        dist.carregarEquipamentosTxt(".//Data//input//equipamentos.txt");
//        dist.carregarHistoricoTxt("data/input/historicos.txt");
//
//        dist.guardarEquipamentosTxt("data/output/equipamentos.txt");
//        dist.guardarHabitacoesTxt("data/output/habitacoes.txt");
//        dist.guardarZonasHabitacaoTxt("data/output/zonahabitacao.txt");
//        dist.guardarfontesequipamentoTxt("data/output/fontesenergia.txt");
//        dist.guardarHistoricoTxt("data/output/historicos.txt");
        //EDITAR / REMOVER / ADICIONAR HABITACAO
//        
//        
//        
//        
//        System.out.println(dist.getZona_habit().get(1).toString());
//        
//        for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//            System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).toString());
//        }
//        //}
//        
//        Habitacao t = new Habitacao(200, 2000, true, 1000, 100);
//        
//        dist.getZona_habit().get(1).adicionarHabitacao(t);
//        //dist.getZona_habit().get(1).toString();
//        System.out.println("-------------------------------");
//        //for(Integer k : dist.getZona_habit().keys()){
//        for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//            System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).toString());
//        }
//        
//        Habitacao z = new Habitacao(100, 1001, true, 10, 100);
//        //dist.atualizar_zonas();
//        
//        dist.getZona_habit().get(1).editHabitacoes(z);
//        System.out.println("---------------------------------------");
//        for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//            System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).toString());
//        }
//        
//        dist.getZona_habit().get(1).removeHabitacao(100);
//        
//        System.out.println("---------------------------------------");
//        for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//            System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).toString());
//        }
//        
//         dist.getZona_habit().get(1).removeHabitacao(1);
//         
//        System.out.println("---------------------------------------");
//        for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//            System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).toString());
//        }
//        System.out.println(dist.getZona_habit().get(1).toString());
//        
//        
//        System.out.println(dist.toString());
        // EDITAR / REMOVER / ADICIONAR FONTES
//        for (Integer k : r.getFontes_energ().keys()) {
 //           System.out.println(r.getFontes_energ().get(k).toString());
//        }
//       
//       
//       FonteEnergia nova = new FonteEnergia ("Ondas" , 1000 , 100 ,5);
//       
//       dist.adicionarFonte(nova);
//
//       for(Integer k : dist.getFontes_energ().keys()){
//           System.out.println(dist.getFontes_energ().get(k).toString());
//       }
//       
//       dist.removeFontesEnergia(1);
//       
//       
//        System.out.println("------------------------------------");
//       for(Integer k : dist.getFontes_energ().keys()){
//           System.out.println(dist.getFontes_energ().get(k).toString());
//       }
//       
//       FonteEnergia nova2 = new FonteEnergia("Barragens" , 2000, 200 ,2);
//       
//       dist.editFonteEnergia(nova2);
//       
//        System.out.println("--------------------------------------");
//       
//       for(Integer k : dist.getFontes_energ().keys()){
//           System.out.println(dist.getFontes_energ().get(k).toString());
//       }
//       
        // EDITAR / REMOVER / ADICIONAR Zonas
//        System.out.println(dist.toString());
//        for (Integer k : dist.getZona_habit().keys()) {
//            System.out.println(dist.getZona_habit().get(k).toString());
//        }
//
//        ZonaHabitacao zh = new ZonaHabitacao(5, 10000);
//
//        dist.adicionarZona(zh);
//
//        System.out.println("---------------------------");
//
//        for (Integer k : dist.getZona_habit().keys()) {
//            System.out.println(dist.getZona_habit().get(k).toString());
//        }
//
//        System.out.println(dist.toString());
//
//        System.out.println("---------------------------");
//
//        ZonaHabitacao zh2 = new ZonaHabitacao(2, 10000);
//
//        dist.removeZonaHabitacao(1);
//
//        for (Integer k : dist.getZona_habit().keys()) {
//            System.out.println(dist.getZona_habit().get(k).toString());
//        }
//
//        dist.editZonasHabitacao(zh2);
//        System.out.println("---------------------------");
//
//        for (Integer k : dist.getZona_habit().keys()) {
//            System.out.println(dist.getZona_habit().get(k).toString());
//        }
        //dist.listarHabitacoesEEquipamentosTodasZonas();
        //dist.listarHabitacoesEEquipamentosDeterminadaZona(2);
        //dist.listarCasasAparelhosProdutores();
        //dist.printXCasasMenosSustentaveis(3);
        //dist.printXCasasMaisSustentaveis(4);
        //Date d = new Date (2,4,2001,12,2,13);
        //dateToString(d);
        // EDITAR / REMOVER / ADICIONAR equipamentos
//             for(Integer k : dist.getZona_habit().keys()){
//                for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//                    for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().get(p).toString());
//                    }
//                }
//             }
//             
//             dist.getZona_habit().get(1).getHabitacoes().get(1).removerEquipamento(2);
//             
//             System.out.println("---------------------------------------");
//             
//             for(Integer k : dist.getZona_habit().keys()){
//                for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//                    for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().get(p).toString());
//                    }
//                }
//             }
//             
//             Equipamento novo = new Equipamento ("Tv" , 30 , "C", true , true , 2000);
//             dist.getZona_habit().get(1).getHabitacoes().get(1).adicionarEquipamento(novo);
//             
//             
//             System.out.println("--------------------------------------\n\n\n");
//             
//              
//                for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//                    for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().get(p).toString());
//                    }
//                }
//                
//                
//                
//                
//                System.out.println("--------------------------------------\n\n\n");
//             
//              
//                Equipamento novo2 = new Equipamento ("Tv" , 30 , "A", false , true , 1500);
//                
//                dist.getZona_habit().get(1).getHabitacoes().get(1).editEquipamento(novo2);
//                
//                for (Integer g : dist.getZona_habit().get(1).getHabitacoes().keys()) {
//                    for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(g).getEquipamentos().get(p).toString());
//                    }
//                }
        // Testar Historicos
//        for(Integer k : dist.getZona_habit().keys()){
//            for(Integer g : dist.getZona_habit().get(k).getHabitacoes().keys()){
//                System.out.println("ID = "+dist.getZona_habit().get(k).getHabitacoes().get(g).getHab_id() + " :");
//                for(Integer p : dist.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().keys()){
//                    System.out.println(dist.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(p).getNome() + " :");
//                    for(Date r : dist.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(p).getHistoricos().keys()){
//                        System.out.println(dist.getZona_habit().get(k).getHabitacoes().get(g).getEquipamentos().get(p).getHistoricos().get(r).toString());
//                    }
//                }
//                System.out.println("-----------------------------------");
//            }
//        } 
        //dist.loggerfiles(dist);
        //dist.loggerfiles_zonas_habitacao(dist);
        //dist.loggerfiles_habitacoes(dist);
        //dist.guardarHistoricoTxt("Data/output/historicos.txt");
        // Ligar / Desligar Equipamentos
//           
//                    for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().get(p).toString());
//                    }
//              
//       dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().get(1).ligar();
//       
//       
//        System.out.println("--------------------------------------");
//        for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().get(p).toString());
//                    }
//       for(long i = 0 ; i<=1000000; i++){
//           for(long j = 0 ; j<=1000000; j++){
//           }
//       }
//        TimeUnit.SECONDS.sleep(15);
//       
//        System.out.println("----------------------------------------");
//       
//       dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().get(1).desligar();
//       
//        for(Integer p : dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().keys()){
//                        System.out.println(dist.getZona_habit().get(1).getHabitacoes().get(1).getEquipamentos().get(p).toString());
//        }
//        
//        
//        dist.dump();
        // Consultar Historico
        //dist.consultarHistoricoEquipamento(4);
        Date d1 = new Date(15, 4, 2010, 20, 40, 10);
        Date d2 = new Date(12, 11, 2018, 21, 10, 40);

        // r.getDistribuicoes().get(1).consultarHistoricoEquipamentoIntervalo(d1, d2, 4);
        //
        //r.printNodes();
    }
}

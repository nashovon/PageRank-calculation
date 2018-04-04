/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author SHOVON
 */
public class PageRank {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Map<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
        Map<String, Double> rank = new HashMap<String, Double>();
        ArrayList<String> source = new ArrayList<String>();
        ArrayList<String> destination = new ArrayList<String>();
        Set<String> vertex = new HashSet<String>();
        Set<String> dangling_node = new HashSet<String>();
        Map<String, Double> temp_rank = new HashMap<String, Double>();
        Map<String, Integer> indegree = new HashMap<String, Integer>();
        Map<String, Integer> outdegree = new HashMap<String, Integer>();
        Map<String, Integer> depth = new HashMap<String, Integer>();
        Map<String, String> anchor = new HashMap<String, String>();

        ///READING DATA FROM FILE
        double dampingfactor = 0.85;

        for (int file_num = 18260; file_num <= 18260; file_num++) {
            
//<editor-fold defaultstate="collapsed" desc="Initializations">
            graph.clear();
            rank.clear();
            source.clear();
            destination.clear();
            vertex.clear();
            dangling_node.clear();
            temp_rank.clear();
            indegree.clear();
            outdegree.clear();
//</editor-fold>

            System.out.println("Reading file " + file_num);

 //<editor-fold defaultstate="collapsed" desc="File Read">           
            try {

                FileInputStream fileInputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader readdata = null;

                String part1 = "E:/data/test2/url_";
                String part2 = Integer.toString(file_num);
                String part3 = "/URL_DB/1.url";

                String filename = part1.concat(part2.concat(part3));
                fileInputStream = new FileInputStream(filename);
                inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                readdata = new BufferedReader(inputStreamReader);

                String temp = null;

                while ((temp = readdata.readLine()) != null) {

                    String split[] = temp.split("\\t");

                    if (split.length == 4) {

                        String child = split[0];
                        String parent = split[1];

                        // System.out.println(parent +"\n"+child);
                        vertex.add(child);
                        vertex.add(parent);

                        source.add(parent);
                        destination.add(child);
                        depth.put(parent, Integer.parseInt(split[3]));

                        //set anchor
                        if (anchor.get(child) == null) {
                            anchor.put(child, split[2]);
                        }

                        //incoming count
                        if (indegree.get(child) == null) {
                            indegree.put(child, 1);
                        } else {
                            indegree.put(child, indegree.get(child) + 1);
                        }

                        if (outdegree.get(parent) == null) {
                            outdegree.put(parent, 1);
                        } else {
                            outdegree.put(parent, outdegree.get(parent) + 1);
                        }

                        //graph create
                        ArrayList<String> atemp = new ArrayList<String>();

                        if (graph.get(parent) != null) {
                            atemp.addAll(graph.get(parent));
                        }
                        atemp.add(child);

                        graph.put(parent, atemp);
                    }
                }
            } catch (FileNotFoundException ex) {

                System.out.println("Unable to open file.");
            }
//</editor-fold>


//<editor-fold defaultstate="collapsed" desc="Use of Hypothetical nodes">
//            Iterator<String> hyp_node_iterator_1 = vertex.iterator();
//
//            while (hyp_node_iterator_1.hasNext()) {
//
//                String temp_1 = hyp_node_iterator_1.next();
//
//                if (graph.get(temp_1) == null) {
//
//                    Iterator<String> hyp_node_iterator_2 = vertex.iterator();
//
//                    while (hyp_node_iterator_2.hasNext()) {
//
//                        String temp_2 = hyp_node_iterator_2.next();
//
//                        source.add( temp_1);
//                        destination.add( temp_2);
//                        
//                        //incoming + outgoing recount
//                        if (indegree.get(temp_2) == null) {
//                            indegree.put(temp_2, 1);
//                        } else {
//                            indegree.put(temp_2, indegree.get(temp_2) + 1);
//                        }
//
//                        if (outdegree.get(temp_1) == null) {
//                            outdegree.put(temp_1, 1);
//                        } else {
//                            outdegree.put(temp_1, outdegree.get(temp_1) + 1);
//                        }
//                        
//                        //graph recreate
//                        ArrayList<String> atemp = new ArrayList<String>();
//
//                        if (graph.get(temp_1) != null) {
//                            atemp.addAll(graph.get(temp_1));
//                        }
//                        atemp.add(temp_2);
//
//                        graph.put(temp_1, atemp);
//
//                    }
//
//                }
//
//            }
//</editor-fold>


//<editor-fold defaultstate="collapsed" desc="INITIALIZE RANK">

            
            Iterator<String> node_iterator = vertex.iterator();

            int totalpage = vertex.size();

            while (node_iterator.hasNext()) {

                String it = node_iterator.next();

                rank.put(it, 1.0);

            }

            for (String A : vertex) {
                temp_rank.put(A, 0.0);

            }
            
 //</editor-fold>           

   
 
 //<editor-fold defaultstate="collapsed" desc="Calculations">
 
 for (int key = 0; key < source.size(); key++) {

                if (graph.get(destination.get(key)) == null) {
                    continue;
                }
                int x = graph.get(source.get(key)).size();

                double n = rank.get(source.get(key)) / (double) x;

                temp_rank.put(destination.get(key), temp_rank.get(destination.get(key)) + n);

            }

            for (String key2 : temp_rank.keySet()) {

                rank.put(key2, temp_rank.get(key2));

            }

            for (String key3 : rank.keySet()) {

                rank.put(key3, ((1 - dampingfactor) + (dampingfactor * rank.get(key3))));

            }
            
//</editor-fold>

          
//<editor-fold defaultstate="collapsed" desc="Sorting">
            
            String part1 = "E:/data/test2/url_";
            String part2 = Integer.toString(file_num);
            String part3 = "/URL_DB/output.txt";

            String filename = part1.concat(part2.concat(part3));

            PrintWriter outputfile = new PrintWriter(filename);

            Map<String, Double> sortedmap = new HashMap<String, Double>();

            sortedmap = rank.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

 //</editor-fold>
            
            
            
 //<editor-fold defaultstate="collapsed" desc="Save Data">          
            for (String print : sortedmap.keySet()) {

                System.out.println(print + "[" + anchor.get(print) + "]" + "---[" + depth.get(print) + "]" + " ----- [IN] " + indegree.get(print) + "  ----  [OUT] ");

                outputfile.print(print + "[" + anchor.get(print) + "]" + "---[" + depth.get(print) + "]" + " ----- [IN] " + indegree.get(print) + "  ----  [OUT] ");

                if (graph.get(print) != null) {
                    outputfile.print(graph.get(print).size());
                } else {
                    outputfile.print("0");
                }

                outputfile.println(" ---- [RANK]" + sortedmap.get(print));

            }
            outputfile.close();
            
 //</editor-fold>           

        }

    }
}

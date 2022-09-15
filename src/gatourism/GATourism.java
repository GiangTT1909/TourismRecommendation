/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author Tran Thi Nguyet Ha
 */
public class GATourism {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Data data = Data.getDatafromFile();
//
    ArrayList<Solution> results = new ArrayList<>();
//for(int i=1;i<=15;i++){

//            Solution s = new Solution(data);
//            ArrayList<ArrayList<Integer>> gene = new ArrayList<>();
//            int[] day1 = new int[]{7, 160, 8, 28, 30, 71};
//            ArrayList<Integer> Day1 = new ArrayList<>();
//            for (int j = 0; j<day1.length; j++){
//                Day1.add(day1[j]+1);
//            }
//            gene.add(Day1);
//            int[] day2 = new int[]{132, 113, 82, 97, 148, 120};
//            ArrayList<Integer> Day2 = new ArrayList<>();
//            for (int j = 0; j<day2.length; j++){
//                Day2.add(day2[j]+1);
//            }
//            gene.add(Day2);
//            int[] day3 = new int[]{16, 149, 35, 40, 41, 19, 117, 79};
//            ArrayList<Integer> Day3 = new ArrayList<>();
//            for (int j = 0; j<day3.length; j++){
//                Day3.add(day3[j]+1);
//            }
//            gene.add(Day3);
//            
//            s.gene = gene;
//            s.printSolution();
//   
    ACO aco = new ACO(data);
//   ArrayList<Solution>  result = aco.generateAntColony(data);
//    GA ga = new GA(data);
//   results =ga.implementGA(data);
//   result.get(result.size()-1).printSolution();
//    GA.writeSolution(results);

//     results.add(result.get(result.size()-1));
          // results.add(result.get(result.size()-1));
//     ArrayList<Solution> result = ga.implementGA(data);
 // result.get(result.size()-1).printSolution();
//       ACO.writeSolution(result,i+"th");
//}
       GA ga = new GA(data);

 //       ArrayList<Solution> result = ga.implementGA(data);
//        for(Solution s: result){
//            System.out.println(s.cal_fitness());
        //}
//ACO.writeSolution(results,"15_times");
  //      ACO aco = new ACO(data);
//           ArrayList<Solution> result = aco.generateAntColony(data);
     
//
        for (int i = 0; i < 1; i++) {
            System.out.println(i);
//            Random rand = new Random();
//            data.w1=(double) rand.nextInt(10) + 1;
//            data.w1 =(double) rand.nextInt(10) + 1;
//            data.w2 =(double) rand.nextInt(10) + 1;
//            data.w3 =(double) rand.nextInt(10) + 1;
//            data.w4 =(double) rand.nextInt(10) + 1;

            ArrayList<Solution> result = ga.implementGA(data);
            results.add(result.get(result.size()-1));
            GA.writeSolution(result);
            result.get(result.size()-1).printSolution();
        }
     //GA.writeSolution(results);
        

//        for (Solution s: result){
//            System.out.println(s.cal_fitness());
//            
//            System.out.println(s.gene);
//            System.out.println(s.cal_hapiness_obj());
//            System.out.println(s.cal_distance_obj());
//
//            System.out.println(s.cal_number_of_destination_obj());
//            System.out.println(s.cal_waiting_time_obj());
    }

}

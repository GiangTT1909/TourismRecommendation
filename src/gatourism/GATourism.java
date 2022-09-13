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
   
  
//    GA ga = new GA(data);
//   results =ga.implementGA(data);
    //GA.writeSolution(results);
    ACO aco = new ACO(data);
    // ArrayList<Solution> result = aco.generateAntColony(data);
//     results.add(result.get(result.size()-1));
          // results.add(result.get(result.size()-1));
//     ArrayList<Solution> result = ga.implementGA(data);

       // ACO.writeSolution(result,i+"th");
//}
      //  GA ga = new GA(data);

 //       ArrayList<Solution> result = ga.implementGA(data);
//        for(Solution s: result){
//            System.out.println(s.cal_fitness());
        //}
//ACO.writeSolution(results,"15_times");
  //      ACO aco = new ACO(data);
//           ArrayList<Solution> result = aco.generateAntColony(data);
     

        for (int i = 0; i < 1; i++) {
            System.out.println(i);
            Random rand = new Random();
//            data.w1=10;
//            data.w1 =(double) rand.nextInt(10) + 1;
//            data.w2 =(double) rand.nextInt(10) + 1;
//            data.w3 =(double) rand.nextInt(10) + 1;
           data.w4 =0;

            ArrayList<Solution> result = aco.generateAntColony(data);
            results.add(result.get(result.size()-1));
            GA.writeSolution(result);
            result.get(result.size()-1).printSolution();
        }
    // GA.writeSolution(results);
        

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

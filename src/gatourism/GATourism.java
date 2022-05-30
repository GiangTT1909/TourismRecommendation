/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        GA ga = new GA();

        ArrayList<Solution> result = ga.implementGA(data);
        for(Solution s: result){
            System.out.println(s.cal_fitness());
        }

        
        
//        ACO aco = new ACO(data);
//        ArrayList<Solution> result = aco.generateAntColony(data);
//        for (Solution s: result){
//            System.out.println(s.cal_fitness());
//        
//            System.out.println(s.gene);
//            System.out.println(s.cal_hapiness_obj());
//            System.out.println(s.cal_distance_obj());
//
//            System.out.println(s.cal_number_of_destination_obj());
//            System.out.println(s.cal_waiting_time_obj());
//        }
//        }
    }

}

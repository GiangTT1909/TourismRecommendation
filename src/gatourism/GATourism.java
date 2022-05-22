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

        ArrayList<Solution> results = new ArrayList<>();
        results = ga.implementGA(data);
        for (Solution result : results) {
            System.out.println(result.cal_fitness());
        }
        System.out.println(results.get(999).gene);
                System.out.println(results.get(999).cal_distance_obj());
        System.out.println(results.get(999).cal_hapiness_obj());
        System.out.println(results.get(999).cal_number_of_destination_obj());
        System.out.println(results.get(999).cal_waiting_time_obj());

    }

}

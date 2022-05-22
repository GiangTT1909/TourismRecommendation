/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
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

        Solution s = ga.generatePopulation(data);
        Solution s1 = ga.generatePopulation(data);
        Solution s2 = ga.crossover(s1, s1, data);
        System.out.println(s2.gene);
        System.out.println(Data.MAX_NUMBER_OF_DESTINATION);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    ArrayList<Solution> results = new ArrayList<>();

   ACO aco = new ACO(data);

    GA ga = new GA(data);

        for (int i = 0; i <150; i++) {
            System.out.println(i);
            Random rand = new Random();
            data.w1=(double) rand.nextInt(10) + 1;
            data.w1 =(double) rand.nextInt(10) + 1;
            data.w2 =(double) rand.nextInt(10) + 1;
            data.w3 =(double) rand.nextInt(10) + 1;
            data.w4 =(double) rand.nextInt(10) + 1;

          ArrayList<Solution> result = ga.implementGA(data);
            results.add(result.get(result.size()-1));
           
            result.get(result.size()-1).printSolution();
        }
        

    GA.writeSolution(results);
    
        

 

    }

}

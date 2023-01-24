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
//
    ArrayList<Solution> results = new ArrayList<>();
//for(int i=1;i<=6;i++){
//                data.K=i;
//            Solution s = new Solution(data);
//            ArrayList<ArrayList<Integer>> gene = new ArrayList<>();
//            int[] day1 = new int[]{133, 11, 5, 40, 152, 69};
//            ArrayList<Integer> Day1 = new ArrayList<>();
//            for (int j = 0; j<day1.length; j++){
//                Day1.add(day1[j]);
//            }
//            gene.add(Day1);
//            int[] day2 = new int[]{62, 145, 55, 113, 17, 79, 34};
//            ArrayList<Integer> Day2 = new ArrayList<>();
//            for (int j = 0; j<day2.length; j++){
//                Day2.add(day2[j]);
//            }
//            gene.add(Day2);
//            int[] day3 = new int[]{110, 175, 29, 36, 139, 35};
//            ArrayList<Integer> Day3 = new ArrayList<>();
//            for (int j = 0; j<day3.length; j++){
//                Day3.add(day3[j]);
//            }
//            gene.add(Day3);
//                        int[] day4 = new int[]{16, 18, 27, 1, 93, 150, 72};
//            ArrayList<Integer> Day4 = new ArrayList<>();
//            for (int j = 0; j<day4.length; j++){
//                Day4.add(day4[j]);
//            }
//            gene.add(Day4);
//                        int[] day5 = new int[]{4, 167, 7, 15, 12, 147};
//            ArrayList<Integer> Day5 = new ArrayList<>();
//            for (int j = 0; j<day5.length; j++){
//                Day5.add(day5[j]);
//            }
//            gene.add(Day5);
//                        int[] day6 = new int[]{149, 68, 44, 161, 101, 132};
//            ArrayList<Integer> Day6 = new ArrayList<>();
//            for (int j = 0; j<day6.length; j++){
//                Day6.add(day6[j]);
//            }
//            gene.add(Day6);
//                        int[] day7 = new int[]{135, 38, 148, 25, 19, 169, 117};
//            ArrayList<Integer> Day7 = new ArrayList<>();
//            for (int j = 0; j<day7.length; j++){
//                Day7.add(day7[j]);
//            }
//            gene.add(Day7);
//            
//            s.gene = gene;
//            s.printSolution();
//   
   ACO aco = new ACO(data);
//   ArrayList<Solution>  result = aco.generateAntColony(data);
    GA ga = new GA(data);
//   results =ga.implementGA(data);
//   result.get(result.size()-1).printSolution();
//    GA.writeSolution(results);

//    
//           results.add(result.get(result.size()-1));
//     ArrayList<Solution> result = ga.implementGA(data);
//      results.add(result.get(result.size()-1));
//  result.get(result.size()-1).printSolution();
//       ACO.writeSolution(result,7+"th");
//}
  //     GA ga = new GA(data);

 
//        for(Solution s: result){
//            System.out.println(s.cal_fitness());
//        }
//ACO.writeSolution(results,"15_times");
  //      ACO aco = new ACO(data);
//           ArrayList<Solution> result = ga.implementGA(data);
//           System.out.println(result.get(0).cal_distance_obj());
     
//
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
//    result.get(0).printSolution();
//for(int i=1;i<result.size();i++){
//    if(result.get(i).cal_fitness()!=result.get(i-1).cal_fitness()){
//        result.get(i).printSolution();
//    }
//}
    GA.writeSolution(results);
    
        

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
//public static void main(String[] args) throws FileNotFoundException, IOException{
//        String workingDirectory = System.getProperty("user.dir");
//        String excelFilePath = workingDirectory + "//src//gatourism//hotel_distance.xlsx";
//        InputStream inputStream = new FileInputStream(new File(excelFilePath));
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = (Sheet) workbook.getSheetAt(0);
//        XSSFWorkbook outWorkbook = new XSSFWorkbook();
//        XSSFSheet sheet1 = outWorkbook.createSheet("Sheet1");
//        int rowCount = 0;
//        Row row;
//        Cell cell;
//        for (int i = 1; i <= 79; i++) {
//            for (int j = 80; j <= 120; j++) {
//                row = sheet1.createRow(rowCount++);
//                cell = row.createCell(0);
//                cell.setCellValue(i);
//                
//                cell = row.createCell(1);
//                cell.setCellValue(j);
//                
//                cell = row.createCell(2);
//                cell.setCellValue(sheet.getRow(i).getCell(j-79).getNumericCellValue());
//            }
//        }
//        
//        try (FileOutputStream outputStream = new FileOutputStream("Result.xlsx")) {
//            outWorkbook.write(outputStream);
//        }  
//    }
}

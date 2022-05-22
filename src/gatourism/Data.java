/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Tran Thi Nguyet Ha
 */
public class Data {
    
    public static double MAX_HAPPINESS = 884.0000004;
    public static double MIN_HAPPINESS = 171.3;

    public static double MAX_NUMBER_OF_DESTINATION = 28;
    public static double MIN_NUMBER_OF_DESTINATION = 0;
    
    public static double MAX_WATING_TIME = 43191.16;
    public static double MIN_WATING_TIME = 0;
    
    public static double MAX_DISTANCE = 43191.16;
    public static double MIN_DISTANCE = 0;
    
    
    int P; // Number destinations;
    Destination[] POI = new Destination[550]; // Destination
    int F; // Number of Factors
    double[][] factor;
    int C;// Number of tourisms
    double[][] tourist = new double[550][550];//Tourist Preference
    double[] A = new double[550]; // estimated start time visiting tourism destination
    double[][] D = new double[550][550]; // distance between each destination
    double[][] T = new double[550][550]; // time travel between each destination
    double S; // Cost per km
    int K; // Number of Trip
    double[] T_max = new double[550];// Max time for each trip
    double[] C_max = new double[550];// Max budget for each trip
    ArrayList<Integer> M = new ArrayList<>();// Mandatory destination
    double[] z_s = new double[550]; // start of trip
    double[] z_e = new double[550]; // end of trip
    double[] t_s = new double[550]; // start of service each trip
    double[] t_e = new double[550]; // end of service each trip
    double w1;
    double w2;
    double w3;
    double w4;
    
    public Data() {

    }
    
    public static Data getDatafromFile() throws FileNotFoundException, IOException {
        Data data = new Data();
        data.P = 190;
        data.K = 5;
        data.F = 10;
        data.C = 10;
        data.S = 15000;
        data.C_max[0] = 5000000;
        data.C_max[1] = 5000000;
        data.C_max[2] = 5000000;
        data.C_max[3] = 5000000;
        data.C_max[4] = 5000000;
        data.T_max[0] = 73800;
        data.T_max[1] = 73800;
        data.T_max[2] = 73800;
        data.T_max[3] = 73800;
        data.T_max[4] = 73800;
        data.t_s[0] = 27000;
        data.t_s[1] = 27000;
        data.t_s[2] = 27000;
        data.t_s[3] = 27000;
        data.t_s[4] = 27000;
        data.t_e[0] = 75600;
        data.t_e[1] = 75600;
        data.t_e[2] = 75600;
        data.t_e[3] = 75600;
        data.t_e[4] = 75600;
        data.w1 = 1;
        data.w2 = 1;
        data.w3 = 1;
        data.w4=1;
        

        // read destination data
        String excelFilePath = "C:\\Users\\ACER\\Desktop\\Tourism\\Tourism\\src\\gatourism\\data_P.xlsx";
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 1; i <= data.P; i++) {
            Destination d = new Destination();
            d.setId(i);
            d.setStart((float) sheet.getRow(i).getCell(2).getNumericCellValue());
            d.setEnd((float) sheet.getRow(i).getCell(3).getNumericCellValue());
            d.setCost((float) sheet.getRow(i).getCell(4).getNumericCellValue());
            d.setDuration((float) sheet.getRow(i).getCell(5).getNumericCellValue());
            data.POI[i - 1] = d;
        }
        
        // read rating
        data.factor = new double[data.P][data.F];
        excelFilePath = "C:\\Users\\ACER\\Desktop\\Tourism\\Tourism\\src\\gatourism\\data_C.xlsx";
        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j <= data.C; j++) {
                data.tourist[i][j] = sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }
        
        // read distance
        excelFilePath = "C:\\Users\\ACER\\Desktop\\Tourism\\Tourism\\src\\gatourism\\data_M-New.xlsx";
        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);

        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j < data.P; j++) {
                data.D[i][j] = sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }
        
         // calc max values
        MAX_NUMBER_OF_DESTINATION = data.calcMaxNumberOfDestination();
        MAX_DISTANCE = data.calcMaxDistance();
        MAX_HAPPINESS = data.calcMaxHappiness();
        MAX_WATING_TIME = data.calcMaxWaitingTime();
        
        MIN_NUMBER_OF_DESTINATION = 0;
        MIN_DISTANCE = 0;
        MIN_HAPPINESS = 0;
        MIN_WATING_TIME = 0;
        
        return data;
    }
    
    public int calcMaxNumberOfDestination(){
        double[] costArray = new double[P];
        for (int i = 0; i < this.P; i++){
            costArray[i] = this.POI[i].getCost() ;
        }
        Arrays.sort(costArray);
        double totalBudget = Arrays.stream(this.C_max).sum();
        int countCost = 0;
        double currentBudget = 0;
        for (int i = 0; i < costArray.length; i++){
            currentBudget += costArray[i];
            if (currentBudget >= totalBudget){
                
                break;
            }
            countCost = i;
        }
        int countTime = 0;
        double totalTimeBudget = Arrays.stream(this.T_max).sum();
        double currentTimeBudget = 0;
        double[] durationArray = new double[P];
        for (int i = 0; i < this.P; i++){
            durationArray[i] = this.POI[i].getDuration();
        }
        Arrays.sort(durationArray);
        for (int i = 0; i < durationArray.length; i++){
            currentTimeBudget += durationArray[i];
            if (currentTimeBudget >= totalTimeBudget){
                break;
            }
            countTime = i;
        }
        return Math.min(countCost, countTime);
    }
    
    public double calcMaxDistance(){
        double maxElement = Double.MIN_VALUE;
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < P; j++) {
                if (D[i][j] > maxElement) {
                    maxElement = D[i][j];
                }
            }
        }
        return maxElement * (MAX_NUMBER_OF_DESTINATION - 1);
    }
    
    public double calcMaxHappiness(){
        double maxElement = Double.MIN_VALUE;
        for (int i = 0; i < P; i++) {
            if (tourist[C][i] > maxElement) {
                maxElement = tourist[C][i];
            }
        }
        return maxElement * MAX_NUMBER_OF_DESTINATION;
    }
    
    public double calcMaxWaitingTime(){
        double[] startTimeArray = new double[P];
        for (int i = 0; i < this.P; i++){
            startTimeArray[i] = this.POI[i].getStart();
        }
        Arrays.sort(startTimeArray);
        double waitingTime = 0;
        for (int i = P - 1; i >= P - K - 1; i--){
            waitingTime += startTimeArray[i] - t_s[0];
        }
        return waitingTime;
    }
}

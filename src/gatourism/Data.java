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

    public static double MAX_NUMBER_OF_DESTINATION = 30;
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
    int[] user_preference;
    int number_of_tags;
    int[][] POI_tags;
    double w1;
    double w2;
    double w3;
    double w4;
    double v;
    double d_max = 53.85;
    double[] D_imagine = new double[550]; //distance between imagine start and every destination

    public Data() {

    }

    public static Data getDatafromFile() throws FileNotFoundException, IOException {
        Data data = new Data();
        data.number_of_tags =8;
        data.user_preference = new int[]{0, 0, 0, 1, 1,0,0,1};
        data.P = 175;
        data.K =7;
        data.F = 10;
        data.C = 10;
        data.S = 15000;
        data.v=360;
        data.C_max[0] = 1000000;
        data.C_max[1] = 1000000;
        data.C_max[2] = 1000000;
        data.C_max[3] = 1000000;
        data.C_max[4] = 1000000;
        data.C_max[5] = 1000000;
        data.C_max[6] = 1000000;
        data.C_max[7] = 1000000;
        data.T_max[0] = 45000;
        data.T_max[1] = 45000;
        data.T_max[2] = 45000;
        data.T_max[3] = 45000;
        data.T_max[4] = 45000;
        data.T_max[5] = 45000;
        data.T_max[6] = 45000;
//        data.T_max[7] = 45000;
        data.t_s[0] = 30600;
        data.t_s[1] = 30600;
        data.t_s[2] = 30600;
        data.t_s[3] = 30600;
        data.t_s[4] = 30600;
        data.t_s[5] = 30600;
        data.t_s[6] = 30600;
        data.t_s[7] = 30600;
        data.t_e[0] = 75600;
        data.t_e[1] = 75600;
        data.t_e[2] = 75600;
        data.t_e[3] = 75600;
        data.t_e[4] = 75600;
        data.t_e[5] = 75600;
        data.t_e[6] = 75600;
        data.t_e[7] = 75600;
        data.w1 = 1;
        data.w2 = 1;
        data.w3 = 1;
        data.w4 = 1;
        data.POI_tags = new int[data.P][data.number_of_tags];
        // read destination data
        String workingDirectory = System.getProperty("user.dir");
        String excelFilePath = workingDirectory + "//src//gatourism//data_P_fix.xlsx";

        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 1; i <= data.P; i++) {
            Destination d = new Destination();
            d.setId(i);
            d.setTitle(sheet.getRow(i).getCell(1).toString());
            d.setStart((float) sheet.getRow(i).getCell(2).getNumericCellValue());
            d.setEnd((float) sheet.getRow(i).getCell(3).getNumericCellValue());
            d.setCost((float) sheet.getRow(i).getCell(4).getNumericCellValue());
            d.setDuration((float) sheet.getRow(i).getCell(5).getNumericCellValue());
            data.POI[i - 1] = d;
        }

        // read rating
        data.factor = new double[data.P][data.F];

        excelFilePath = workingDirectory + "//src//gatourism//data_C_fix.xlsx";

        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j <= data.C; j++) {
                data.tourist[i][j] = sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }

        excelFilePath = workingDirectory + "//src//gatourism//data_T_fix.xlsx";

        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j < data.number_of_tags; j++) {
                data.POI_tags[i][j] = (int) sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }
        // read distance

        excelFilePath = workingDirectory + "//src//gatourism//data_M_fix.xlsx";

        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);

        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j < data.P; j++) {
                data.D[i][j] = sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }

//        for (int i = 0; i < data.P; i++) {
//            double sum = 0;
//            for (int j = 0; j < data.P; j++) {
//                sum += data.D[i][j];
//            }
//            data.D_imagine[i] = sum / data.P;
//        }
       data.recalculateRate();
        // calc max values
        MAX_NUMBER_OF_DESTINATION = data.calcMaxNumberOfDestination();
        MAX_DISTANCE = data.calcMaxDistance();
        MAX_HAPPINESS = 10;
        MAX_WATING_TIME = data.calcMaxWaitingTime();

        MIN_NUMBER_OF_DESTINATION = 0;
        MIN_DISTANCE = 0;
        MIN_HAPPINESS = 0;
        MIN_WATING_TIME = 0;

        return data;
    }

    public void recalculateRate() {
        for (int i = 0; i < P; i++) {
            int goodTag =0;
            for (int j = 0; j < number_of_tags; j++) {
                    goodTag += POI_tags[i][j]*user_preference[j];
            }
            tourist[i][C] *=(1.0+ (double)goodTag/(double)number_of_tags);
        }

    }

    public int calcMaxNumberOfDestination() {
        double[] costArray = new double[P];
        for (int i = 0; i < this.P; i++) {
            costArray[i] = this.POI[i].getCost();
        }
        Arrays.sort(costArray);
        double totalBudget = Arrays.stream(this.C_max).sum();
        int countCost = 0;
        double currentBudget = 0;
        for (int i = 0; i < costArray.length; i++) {
            currentBudget += costArray[i];
            if (currentBudget >= totalBudget) {

                break;
            }
            countCost = i;
        }
        int countTime = 0;
        double totalTimeBudget = Arrays.stream(this.T_max).sum();
        double currentTimeBudget = 0;
        double[] durationArray = new double[P];
        for (int i = 0; i < this.P; i++) {
            durationArray[i] = this.POI[i].getDuration();
        }
        Arrays.sort(durationArray);
        for (int i = 0; i < durationArray.length; i++) {
            currentTimeBudget += durationArray[i];
            if (currentTimeBudget >= totalTimeBudget) {
                break;
            }
            countTime = i;
        }
        return Math.min(countCost, countTime) - 1;
    }

    public double calcMaxDistance() {
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

    public double calcMaxHappiness() {
        double maxElement = Double.MIN_VALUE;
        for (int i = 0; i < P; i++) {
            if (tourist[i][10] > maxElement) {
                maxElement = tourist[i][10];
            }
        }
        return maxElement * MAX_NUMBER_OF_DESTINATION;
    }

    public double calcMaxWaitingTime() {
        double[] startTimeArray = new double[P];
        for (int i = 0; i < this.P; i++) {
            startTimeArray[i] = this.POI[i].getStart();
        }
        Arrays.sort(startTimeArray);
        double waitingTime = 0;
        for (int i = P - 1; i >= P - K - 1; i--) {
            waitingTime += startTimeArray[i] - t_s[0];
        }
        return waitingTime;
    }
}

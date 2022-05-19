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
        
        // read destination data
        String excelFilePath = "D:\\FPTU Materials\\Capstone\\GATourism\\src\\gatourism\\data_P.xlsx";
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
        excelFilePath = "D:\\FPTU Materials\\Capstone\\GATourism\\src\\gatourism\\data_C.xlsx";
        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);
        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j <= data.C; j++) {
                data.tourist[i][j] = sheet.getRow(i + 1).getCell(j+1).getNumericCellValue();
            }
        }
        
        // read distance
        excelFilePath = "D:\\FPTU Materials\\Capstone\\GATourism\\src\\gatourism\\data_M-New.xlsx";
        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        sheet = (Sheet) workbook.getSheetAt(0);

        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j < data.P; j++) {
                data.D[i][j] = sheet.getRow(i + 1).getCell(j + 1).getNumericCellValue();
            }
        }
        return data;
    }
}
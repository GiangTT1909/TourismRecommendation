/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Tran Thi Nguyet Ha
 */
public class Solution {
    
    Data data;
    ArrayList<ArrayList<Integer>> gene;
    
    public Solution(Data data) throws IOException {
        this.data = data;
        gene = new ArrayList<>();
    }
    
    public boolean isValid(ArrayList<ArrayList<Integer>> gene){
        boolean isStartEndTimeValid = true;
        double[][] A = new double[500][data.K];
        for (int i = 0; i< data.K; i++){
            ArrayList<Integer> trip = gene.get(i);
            for (int j = 0; j < trip.size(); j++){
                int poi = trip.get(j);
                if (j == 0){
                    A[j][i] = data.t_s[i]+ data.POI[poi].getDuration();
                } else {
                    A[j][i] = A[j-1][i] + data.POI[poi].getDuration();
                }
            }
        }
        
        return isStartEndTimeValid;
    }
}

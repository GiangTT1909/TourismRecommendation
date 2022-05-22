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
                if (A[j][i] >= data.POI[poi].getEnd() || A[j][i] - data.POI[poi].getDuration() <= data.POI[poi].getStart() )
                    isStartEndTimeValid = false;
            }
        }
        
        return isStartEndTimeValid;
    }
    public double cal_hapiness_obj(){
        double happiness = 0;
        for (ArrayList<Integer> arrayList : gene) {
            for (Integer index : arrayList) {
                 happiness += data.tourist[index][data.C];
            }
        }
        return happiness;
    }
    
    public double cal_distance_obj(){
        double distance =0;
        for(int i =0;i<data.K;i++){
            for(int j=0;j<this.gene.get(i).size()-1;j++){
                distance+= data.D[this.gene.get(i).get(j)][this.gene.get(i).get(j)];
            }
        }
        return distance;
    }
    
    public double cal_number_of_destination_obj(){
        double number =0;
        for (ArrayList<Integer> arrayList : gene) {
            number+=arrayList.size();
        }
        return number;
    }
    
    public double cal_waiting_time_obj(){
        double waiting_time =0;
        for (int i = 0; i < data.K; i++) {
            double current_time = data.t_s[i]+data.POI[this.gene.get(i).get(0)].getDuration();
            for (int j = 1; j < this.gene.get(i).size(); j++) {
                if(current_time+data.D[this.gene.get(i).get(j-1)][this.gene.get(i).get(j)]*90<data.POI[this.gene.get(i).get(j)].getStart()){
                    waiting_time+= data.POI[this.gene.get(i).get(j)].getStart()- current_time+data.D[this.gene.get(i).get(j-1)][this.gene.get(i).get(j)]*90;
                    
                }
                 current_time= Double.max(current_time+data.D[this.gene.get(i).get(j-1)][this.gene.get(i).get(j)]*90, data.POI[this.gene.get(i).get(j)].getStart())+data.POI[this.gene.get(i).get(j)].getDuration();
            }
        }
        return waiting_time;
    }
    
}

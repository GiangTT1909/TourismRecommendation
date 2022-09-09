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

    public int rank;
    public int domination_count;
    public ArrayList<Solution> dominated_solution;
    public int[] features;
    public double fitness = 0;
    public double crowding_distance = 0;
    public double[] objectives;
    Data data;
    ArrayList<ArrayList<Integer>> gene;

    public Solution(Data data) throws IOException {
        this.data = data;
        gene = new ArrayList<>();
         this.fitness = 0;
        this.crowding_distance = 0;
        this.domination_count = 0;
        this.objectives = new double[4];
    }



    public boolean isValid(ArrayList<ArrayList<Integer>> gene) {
        boolean isStartEndTimeValid = true;
        double[][] A = new double[500][data.K];
        for (int i = 0; i < data.K; i++) {
            ArrayList<Integer> trip = gene.get(i);
            for (int j = 0; j < trip.size(); j++) {
                int poi = trip.get(j);
                if (j == 0) {
                    A[j][i] = data.t_s[i] + data.POI[poi].getDuration();
                } else {
                    A[j][i] = A[j - 1][i] + data.POI[poi].getDuration();
                }
                if (A[j][i] >= data.POI[poi].getEnd() || A[j][i] - data.POI[poi].getDuration() <= data.POI[poi].getStart()) {
                    isStartEndTimeValid = false;
                }
            }
        }

        return isStartEndTimeValid;
    }

    public double cal_hapiness_obj() {
        double happiness = 0;
        for (ArrayList<Integer> arrayList : gene) {
            for (Integer index : arrayList) {
                happiness += data.tourist[index][data.C];
            }
        }
        return happiness;
    }

    public double cal_distance_obj() {
        double distance = 0;
        int lastLocation = 0;
        for (int i = 0; i < data.K; i++) {
            for (int j = 0; j < this.gene.get(i).size() - 1; j++) {
                distance += data.D[this.gene.get(i).get(j)][this.gene.get(i).get(j + 1)];
            }
            if (i > 0){
                distance += data.D[lastLocation][this.gene.get(i).get(0)];
            }
            lastLocation = this.gene.get(i).get(this.gene.get(i).size() - 1);
        }
        return distance/cal_number_of_destination_obj();
    }

    public double cal_number_of_destination_obj() {
        double number = 0;
        for (ArrayList<Integer> arrayList : gene) {
            number += arrayList.size();
        }
        return number;
    }

    public double cal_waiting_time_obj() {
        double waiting_time = 0;
        for (int i = 0; i < data.K; i++) {
            double current_time = data.t_s[i] + data.POI[this.gene.get(i).get(0)].getDuration();
            for (int j = 1; j < this.gene.get(i).size(); j++) {
                if (current_time + data.D[this.gene.get(i).get(j - 1)][this.gene.get(i).get(j)] * 90 < data.POI[this.gene.get(i).get(j)].getStart()) {
                    waiting_time += data.POI[this.gene.get(i).get(j)].getStart() - current_time + data.D[this.gene.get(i).get(j - 1)][this.gene.get(i).get(j)] * 90;

                }
                current_time = Double.max(current_time + data.D[this.gene.get(i).get(j - 1)][this.gene.get(i).get(j)] * 90, data.POI[this.gene.get(i).get(j)].getStart()) + data.POI[this.gene.get(i).get(j)].getDuration();
            }
        }
        return waiting_time;
    }

    public void cal_obj(){
        objectives[0] = cal_distance_obj();
        objectives[1] = cal_waiting_time_obj();
        objectives[2] = Math.abs(cal_hapiness_obj() - data.MAX_HAPPINESS);
        objectives[3] =Math.abs(cal_number_of_destination_obj() - data.MAX_NUMBER_OF_DESTINATION);
    }
    public double cal_fitness() {

        

        double fitness = 0;
        fitness += Math.pow((cal_distance_obj() - data.MIN_DISTANCE) / (data.MAX_DISTANCE - data.MIN_DISTANCE), 2) * data.w1;
        fitness += Math.pow((data.MIN_WATING_TIME - cal_waiting_time_obj()) / (data.MAX_WATING_TIME - data.MIN_WATING_TIME), 2) * data.w2;
        fitness += Math.pow((cal_hapiness_obj() - data.MAX_HAPPINESS) / (data.MAX_HAPPINESS - data.MIN_HAPPINESS), 2) * data.w3;
        fitness += Math.pow((cal_number_of_destination_obj() - data.MAX_NUMBER_OF_DESTINATION) / (data.MAX_NUMBER_OF_DESTINATION - data.MIN_NUMBER_OF_DESTINATION), 2) * data.w4;
        fitness = Math.sqrt(fitness);
        return fitness;
    }

    public void printSolution() {
        for (int i = 0; i < gene.size(); i++) {
            ArrayList<Integer> trip = gene.get(i);
            System.out.println("Day " + (i + 1));
            double currentTime = Double.max(data.t_s[i], data.POI[trip.get(0)].getStart());
            for (int j = 0; j < trip.size() - 1; j++) {
                System.out.println(data.POI[trip.get(j)].getTitle() + ": " + currentTime / 3600 + " -> " + (currentTime + data.POI[trip.get(j)].getDuration()) / 3600);
                //currentTime += data.POI[trip.get(j)].getDuration() + data.D[trip.get(j)][trip.get(j+1)]*90;
                currentTime = Double.max(currentTime + data.D[this.gene.get(i).get(j)][this.gene.get(i).get(j + 1)] * 90, data.POI[this.gene.get(i).get(j)].getStart()) + data.POI[this.gene.get(i).get(j)].getDuration();
            }
            System.out.println(data.POI[trip.get(trip.size() - 1)].getTitle() + ": " + currentTime / 3600 + " -> " + (currentTime + data.POI[trip.get(trip.size() - 1)].getDuration()) / 3600);
        }
    }
     public boolean dominates(Object o){

        this.cal_obj();
        if (!(o instanceof Solution)){
            return false;
        }
        
        Solution other = (Solution) o;
        other.cal_obj();
        boolean and_condition = true;
        boolean or_condition = false;
        for (int i=0;i<this.objectives.length;i++){
            and_condition = and_condition && (this.objectives[i]<=other.objectives[i]);
            or_condition = or_condition || (this.objectives[i]<other.objectives[i]);
        }
        return (and_condition && or_condition);
    }
}

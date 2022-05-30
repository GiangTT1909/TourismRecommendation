/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Tran Thi Nguyet Ha
 */
public class ACO {
    public double[][] costMatrix;
    public double[][] pheromoneMatrix;
    public int hotel;

    public ACO(Data data) {
        costMatrix = new double[data.P][data.P];
        pheromoneMatrix = new double[data.P][data.P];
        for (int i = 0; i < data.P; i++) {
            for (int j = 0; j < data.P; j++) {
                if (i != j) {
                    costMatrix[i][j] = data.D[i][j];
                }
                else{
                    costMatrix[i][j]=0;
                }
                if (costMatrix[i][j] == 0) {
                    pheromoneMatrix[i][j] = 0;
                } else {
                    pheromoneMatrix[i][j] = 1 / costMatrix[i][j];
                }
            }
        }
    }

    public ArrayList<Solution> generateAntColony(Data data) throws IOException {
        ArrayList<Solution> arr = new ArrayList<>();
        Solution s = new Solution(data);
        ACO Algorithm = new ACO(data);
        for (int l = 0; l < 10; l++) {
            ArrayList<Solution> ants = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Solution ant = generatePopulation(data);

                ants.add(ant);
            }
            Collections.sort(ants, (o1, o2) -> {
                return Double.compare(o1.cal_fitness(), o2.cal_fitness());
            });
            int index=0;
            for (Solution ant : ants) { 
                if(index>20) break;
                index++;
                double pheromone = 0;
//                for (int j = 0; j < data.K; j++) {
//                    for (int k = 0; k <= ant.gene.get(j).size(); k++) {
//                        if (k == 0) {
//                            pheromone += Algorithm.pheromoneMatrix[0][ant.gene.get(j).get(k) + 1];
//                        } else if (k == ant.gene.get(j).size()) {
//                            pheromone += Algorithm.pheromoneMatrix[ant.gene.get(j).get(k - 1) + 1][0];
//                        } else {
//                            pheromone += Algorithm.pheromoneMatrix[ant.gene.get(j).get(k - 1) + 1][ant.gene.get(j).get(k) + 1];
//                        }
//                    }
//                }
                for (int j = 0; j < data.K; j++) {
                    for (int k = 0; k <= ant.gene.get(j).size(); k++) {
                        if (k == 0) {
                            Algorithm.pheromoneMatrix[0][ant.gene.get(j).get(k)] += 1/Algorithm.costMatrix[0][ant.gene.get(j).get(k)];
                        } else if (k == ant.gene.get(j).size()) {
                            Algorithm.pheromoneMatrix[ant.gene.get(j).get(k - 1)][0] += 1/Algorithm.costMatrix[ant.gene.get(j).get(k - 1)][0];
                        } else {
                            Algorithm.pheromoneMatrix[ant.gene.get(j).get(k - 1)][ant.gene.get(j).get(k)] +=1/Algorithm.costMatrix[ant.gene.get(j).get(k - 1)][ant.gene.get(j).get(k)] ;
                        }
                    }
                }
            }
            Collections.sort(ants, (o1, o2) -> {
                return Double.compare(o1.cal_fitness(), o2.cal_fitness());
            });
            arr.add(ants.get(0));
        }

        return arr;
    }
//    public ArrayList<ArrayList<Integer>> generateTour(Data data) throws IOException {
//        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
//        Solution s = new Solution(data);
//        ArrayList<Integer> chosen = new ArrayList<>();
//        Solution ant = new Solution();
//        for (int j = 0; j < data.K; j++) {
//                    double budget = data.hotel[ant.hotel][1];
//                    double currentTime = 27000;
//                    int currentLocation = 0;
//                    ArrayList<Integer> oneTrip = new ArrayList<Integer>();
//                    while (budget < data.C_max[j] || currentTime < data.T_max[j]) {
//                        ArrayList<Integer> canVisit = new ArrayList<Integer>();
//                        for (int k = 0; k < data.P - data.H; k++) {
//                            double timePrediction=0;
//                            if(currentLocation==0){
//                                timePrediction = currentTime + data.D[ant.hotel][k + data.H] + data.POI[k].getDuration() + data.D[k + data.H][ant.hotel];
//                            }
//                            
//                            else
//                            {
//                                timePrediction = currentTime + data.D[currentLocation+data.H][k + data.H]*90 + data.POI[k].getDuration() + data.D[k + data.H][ant.hotel]*90;
//                            }
//                            if (timePrediction <= data.T_max[j] && chosen.indexOf(k) < 0) {
//                                if (currentLocation == 0) {
//                                    if (budget + data.S * data.D[ant.hotel][k + data.H] + data.POI[k].getCost() < data.C_max[j]) {
//                                        canVisit.add(k);
//                                    }
//                                } else {
//                                    if (budget + data.S * data.D[currentLocation + data.H][k + data.H] + data.POI[k].getCost() < data.C_max[j]) {
//                                        canVisit.add(k);
//                                    }
//                                }
//
//                            }
//                        }
//                        if (canVisit.size() == 0) {
//                            break;
//                        }
//                        Random random = new Random();
//                        int nextDest = random.nextInt(42);
//
//                        while (chosen.contains(nextDest)){
//                            nextDest = random.nextInt(42);
//                        }
//                        oneTrip.add(nextDest);
//                        if (currentLocation == 0) {
//                            budget += data.S * data.D[ant.hotel][nextDest + data.H] + data.POI[nextDest].getCost();
//                        } else {
//                            budget += data.S * data.D[currentLocation + data.H][nextDest + data.H] + data.POI[nextDest].getCost();
//                        }
//                        currentTime += data.D[currentLocation][nextDest + data.H]*90 + data.POI[nextDest].getDuration();
//                        currentLocation = nextDest;
//                        chosen.add(nextDest);
//
//                    }
//                    arr.add(oneTrip);
//
//                }
//
//        return arr;
//    }
    
    static int getNum(ArrayList<Integer> v) {
        // Size of the vector
        int n = v.size();

        // Make sure the number is within
        // the index range
        int index = (int) (Math.random() * n);

        // Get random number from the vector
        int num = v.get(index);

        // Remove the number from the vector
        v.set(index, v.get(n - 1));
        v.remove(n - 1);

        // Return the removed number
        return num;
    }

    // Function to generate n
    // non-repeating random numbers
    static ArrayList<Integer> generateRandom(int n) {
        ArrayList<Integer> v = new ArrayList<>(n);
        ArrayList<Integer> vc = new ArrayList<>(n);
        // Fill the vector with the values
        // 1, 2, 3, ..., n
        for (int i = 0; i < n; i++) {
            v.add(i);
        }
        for (int i = 0; i < n; i++) {
            vc.add(getNum(v));
        }
        // While vector has elements
        // get a random number from the vector and print it
        return vc;
    }

    // Driver code
    public Solution generatePopulation(Data data) throws IOException {
        ArrayList<Solution> generation = new ArrayList<>();
        ArrayList<Integer> fullTrip = generateRandom(data.P);
        Solution s = new Solution(data);
        for (int i = 0; i < data.K; i++) {
            ArrayList<Integer> dayTrip = new ArrayList<>();
            double time = Double.max(data.t_s[i], data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
            double cost = data.POI[fullTrip.get(0)].getCost();
            dayTrip.add(fullTrip.get(0));
            int current = fullTrip.get(0);
            fullTrip.remove(0);

            while (true) {
                double predict = Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
                if (Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration() < data.t_e[i]
                        && cost + data.POI[fullTrip.get(0)].getCost() < data.C_max[i]) {
                    time = Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
                    cost += data.POI[fullTrip.get(0)].getCost();
                    current = fullTrip.get(0);
                    dayTrip.add(fullTrip.get(0));
                    fullTrip.remove(0);
                    continue;
                } else {
                    break;
                }
            }
            s.gene.add(dayTrip);
        }
        return s;

    }
}

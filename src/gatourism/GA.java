/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ACER
 */
public class GA {

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
        Solution s = new Solution();
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

    public Solution crossover(Solution parent1, Solution parent2,Data data) throws IOException {
        Solution child = new Solution();
        Set<Integer> set = new HashSet<Integer>();
        for (ArrayList<Integer> list : parent1.gene) {
            for (Integer i : list) {
                set.add(i);
            }

        }
        for (ArrayList<Integer> list : parent2.gene) {
            for (Integer i : list) {
                set.add(i);
            }

        }
        set = newShuffledSet(set);
        
       
        ArrayList<Integer> fullTrip = new ArrayList<>(set);
      
        for (int i = 0; i < data.K; i++) {
            ArrayList<Integer> dayTrip = new ArrayList<>();
            double time = Double.max(data.t_s[i], data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
            double cost = data.POI[fullTrip.get(0)].getCost();
            dayTrip.add(fullTrip.get(0));
            int current = fullTrip.get(0);
            fullTrip.remove(0);

            while (fullTrip.size()>0) {
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
            child.gene.add(dayTrip);
        }
        return  child;
}
    public Solution mutation(Solution s,Data data) throws IOException{
        Solution newS = new Solution();
        
        return generatePopulation(data);
    }

    public static <T> Set<T> newShuffledSet(Collection<T> collection) {
        List<T> shuffleMe = new ArrayList<T>(collection);
        Collections.shuffle(shuffleMe);
        return new HashSet<T>(shuffleMe);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import static gatourism.GA.generateRandom;
import static gatourism.GA.getNum;
import static gatourism.GA.newShuffledSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Acer
 */
public class NSGA2 {
    
    public Data data;
    public NSGA2( Data data){
      
        this.data = data;
    }
    
    public Population initial_population() throws IOException{
        Population population = new Population(data);
        for (int i=0;i<500;i++){
            Solution solution = generatePopulation(data);
           
            population.add(solution);
        }
        return population;
    }
    
    public void fast_nondominated_sort(Population population){
        population.fronts = new ArrayList<ArrayList<Solution>>();
        ArrayList<Solution> lst = new ArrayList<Solution>();
        for (Solution solution:population.population){
            solution.domination_count = 0;
            solution.dominated_solution = new ArrayList<Solution>();
            
            for (Solution other_solution:population.population){
                if (solution.dominates(other_solution)){
                    solution.dominated_solution.add(other_solution);
                }else{
                    if (other_solution.dominates(solution)){
                        solution.domination_count++;
                    }
                }
            }
            
            
            
            if (solution.domination_count==0){
                solution.rank = 0;
                lst.add(solution);   
                
            }
            
            
        }
        population.fronts.add(0,lst);
//        System.out.println(population.fronts.get(0).size()+" aa");
//        for (Solution i:population.population){
//            System.out.println(i.domination_count+" "+i.dominated_solution.size());
//        }
        int i = 0;
        while (population.fronts.get(i).size()>0){
            lst = new ArrayList<Solution>();
            for (Solution solution:population.fronts.get(i)){
                for (Solution other_solution:solution.dominated_solution){
                    
                    other_solution.domination_count--;
                    
                    if (other_solution.domination_count==0){
                        other_solution.rank = i+1;
                        lst.add(other_solution);
                    }
                }
            i++;
            population.fronts.add(i,lst);
            }
        }  
    }
    
    public void calculate_crowding_distance(ArrayList<Solution> front){
        if (front.size()>0){
            int solution_num = front.size();
            
            for (Solution solution:front){
                solution.cal_obj();
                solution.crowding_distance = 0;
            }
            
            for (int i=0;i<front.get(0).objectives.length;i++){
                final int idx = i;
                front.sort((o1,o2)->{
                    int flag = 0;
                    if (o1.objectives[idx]<o2.objectives[idx]) flag = -1;
                    if (o1.objectives[idx]>o2.objectives[idx]) flag = 1;
                    return flag;
                });
                
                front.get(0).crowding_distance = Math.pow(10, 9);
                front.get(solution_num-1).crowding_distance = Math.pow(10, 9);
                    
                double max = -1;
                double min = -1;
                for (int j=0;j<front.size();j++){
                    double curr = front.get(j).objectives[i];
                    if ((max==-1) || (curr>max)){
                        max = curr;
                    }
                    
                    if ((min==-1) || (curr<min)){
                        min = curr;
                    } 
                }
                
                double scale = max-min;
                if (scale==0) scale =1;
                for (int j = 1; j<solution_num-1;j++){
                    front.get(j).crowding_distance+=(front.get(j+1).objectives[idx]-front.get(j-1).objectives[idx])/scale;
                }
                   
            }
        }
    }
    
    public int crowding_operator(Solution solution, Solution other_solution){
        if ((solution.rank<other_solution.rank) || ((solution.rank== other_solution.rank) && (solution.crowding_distance>other_solution.crowding_distance))){
            return 1;
        }else{
            return 0;
        }
    }
    
    public ArrayList<Solution> create_children(Population population) throws IOException{
        
        population.sortChromosomesByFitness();
        ArrayList<Solution> nextPopulation = new ArrayList<>();
            for (int i = 0; i < 300; i++) {
                nextPopulation.add(population.population.get(i));
            }
            //Crossover
            for (int i = 0; i < 2700; i++) {
                Random rand = new Random();
                int mom = rand.nextInt(3000);
               
                //nextPopulation.add(crossover(population.get(dad), population.get(mom), data));
                nextPopulation.add(mutation(population.population.get(mom),data));
            }
            Collections.sort(nextPopulation, new Comparator<Solution>() {
                @Override
                public int compare(Solution o1, Solution o2) {

                    return Double.compare(o1.cal_fitness(), o2.cal_fitness());
                }
            });

            //mutation
            for (int i = 0; i < 600; i++) {
                Random rand = new Random();
                int choosen = rand.nextInt(2900);
                choosen += 100;
                nextPopulation.set(choosen, _mutate(nextPopulation.get(choosen)));
            }
            Collections.sort(nextPopulation, new Comparator<Solution>() {
                @Override
                public int compare(Solution o1, Solution o2) {

                    return Double.compare(o1.cal_fitness(), o2.cal_fitness());
                }
            });
        return nextPopulation;
    }
    
    public Solution mutation(Solution s, Data data) throws IOException {
        ArrayList<Integer> poiList = new ArrayList<>();
        for (int i = 1; i < data.P; i++) {
            poiList.add(i);
        }

        // random trip number and cutoff point
        Random rand = new Random();
        int tripNumber = rand.nextInt(data.K);
        int cutoffPoint = rand.nextInt(s.gene.get(tripNumber).size());
        //  System.out.println(tripNumber + "|" + cutoffPoint);
        Solution newS = new Solution(data);
        for (int i = 0; i < tripNumber; i++) {
            if (tripNumber != 0) {
                for (Integer poi : s.gene.get(i)) {
                    poiList.remove(poiList.indexOf(poi));
                }
                newS.gene.add(s.gene.get(i));
            }
        }
        ArrayList<Integer> newTrip = new ArrayList<>();
        double time = Double.max(data.t_s[tripNumber], data.POI[s.gene.get(tripNumber).get(0)].getStart()) + data.POI[s.gene.get(tripNumber).get(0)].getDuration();;
        double cost = data.POI[s.gene.get(tripNumber).get(0)].getCost();
        for (int i = 0; i < cutoffPoint; i++) {
            int currentPOI = s.gene.get(tripNumber).get(i);
            newTrip.add(currentPOI);
            poiList.remove(poiList.indexOf(currentPOI));
            time = Double.max(time + data.D[currentPOI][s.gene.get(tripNumber).get(i + 1)] * 90, data.POI[s.gene.get(tripNumber).get(i + 1)].getStart()) + data.POI[s.gene.get(tripNumber).get(i + 1)].getDuration();
            cost += data.POI[s.gene.get(tripNumber).get(i)].getCost();
        }

        int current = s.gene.get(tripNumber).get(cutoffPoint);
        newTrip.add(current);
        poiList.remove(poiList.indexOf(current));

        ArrayList<Integer> fullTrip = new ArrayList<>();
        int size = poiList.size();
        for (int i = 0; i < size; i++) {
            fullTrip.add(getNum(poiList));
        }
        while (fullTrip.size() > 0) {
            //double predict = Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
            if (Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration() < data.t_e[tripNumber]
                    && cost + data.POI[fullTrip.get(0)].getCost() < data.C_max[tripNumber]) {
                time = Double.max(time + data.D[current][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
                cost += data.POI[fullTrip.get(0)].getCost();
                current = fullTrip.get(0);
                newTrip.add(fullTrip.get(0));
                fullTrip.remove(0);
                continue;
            } else {
                break;
            }
        }

        newS.gene.add(newTrip);

        for (int i = tripNumber + 1; i < data.K; i++) {
            ArrayList<Integer> dayTrip = new ArrayList<>();
            double newTime = Double.max(data.t_s[i], data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
            double newCost = data.POI[fullTrip.get(0)].getCost();
            dayTrip.add(fullTrip.get(0));
            int currentPOI = fullTrip.get(0);
            fullTrip.remove(0);
            while (fullTrip.size() > 0) {
                //double predict = Double.max(newTime + data.D[currentPOI][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
                if (Double.max(newTime + data.D[currentPOI][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration() < data.t_e[i]
                        && newCost + data.POI[fullTrip.get(0)].getCost() < data.C_max[i]) {
                    newTime = Double.max(newTime + data.D[currentPOI][fullTrip.get(0)] * 90, data.POI[fullTrip.get(0)].getStart()) + data.POI[fullTrip.get(0)].getDuration();
                    newCost += data.POI[fullTrip.get(0)].getCost();
                    currentPOI = fullTrip.get(0);
                    dayTrip.add(fullTrip.get(0));
                    fullTrip.remove(0);
                    continue;
                } else {
                    break;
                }
            }
            newS.gene.add(dayTrip);
        }
        return newS;
    }
    
    public Solution _crossover(Solution parent1, Solution parent2) throws IOException{
        Solution child = new Solution(data);
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

            while (fullTrip.size() > 0) {
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
        return child;
    }
    
    public Solution _mutate(Solution solution) throws IOException{
        
            return generatePopulation(data);
        
    }
    
    public Solution _selection(Population population){
        Solution best = null;
        for(int x=0;x<300;++x)
        {
            int c = (int)(Math.random()*population.population.size());
            if ((best==null)){
                best = population.population.get(c);
            }else{
                if ((crowding_operator(population.population.get(c), best)==1) && (Math.random()<0.1)){
                    best = population.population.get(c);
                }
            }
        }
        return best;
    }
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

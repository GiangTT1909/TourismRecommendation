/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ACER
 */
public class GA {

    NSGA2 utils;
    Population population;

    public GA(Data data) {
         
        utils = new NSGA2(data);
        population = new Population(data);
    }

    public GA(NSGA2 utils, Population population) {
        this.utils = utils;
        this.population = population;
    }
    
     public ArrayList<Solution> Search(Data data) throws IOException {
        this.population = new Population(data);
        for (int i = 0; i < 3000; i++) {
            Solution s = generatePopulation(data);
            
            population.add(s);
        }
        utils.fast_nondominated_sort(population);
        for (ArrayList<Solution> front : population.fronts) {
            utils.calculate_crowding_distance(front);
        }
        ArrayList<Solution> children = new ArrayList<>();
        children = utils.create_children(population);
        Population return_population = null;
        for (int i = 0; i < 200; i++) {
            System.out.println(i);
            population.population = new ArrayList<>(children);
            utils.fast_nondominated_sort(population);
            Population new_population = new Population(data);
            int front_num = 0;
            while (new_population.population.size() + population.fronts.get(front_num).size() <3000) {
                utils.calculate_crowding_distance(population.fronts.get(front_num));
                new_population.extend(population.fronts.get(front_num));
                front_num++;
//                System.out.println(population.fronts.get(front_num).size()+" "+ front_num);
            }
            utils.calculate_crowding_distance(population.fronts.get(front_num));
            population.fronts.get(front_num).sort((o1, o2) -> {
                int flag = 0;
                if (o1.crowding_distance < o2.crowding_distance) {
                    flag = 1;
                }
                if (o1.crowding_distance > o2.crowding_distance) {
                    flag = -1;
                }
                return flag;
            });
            int remainSolution = 3000 - new_population.population.size();
            for (int j = 0; j < remainSolution; j++) {
                new_population.add(population.fronts.get(front_num).get(j));
            }
            return_population = population;
            population = new_population;
            utils.fast_nondominated_sort(population);
            for (ArrayList<Solution> front : population.fronts) {
                utils.calculate_crowding_distance(front);
            }

            children = utils.create_children(population);
        }
        return return_population.fronts.get(0);
    }

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
        for (int i = 1; i < n; i++) {
            v.add(i);
        }
        for (int i = 1; i < n; i++) {
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

    public Solution crossover(Solution parent1, Solution parent2, Data data) throws IOException {
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

    public Solution mutation2(Solution s, Data data) throws IOException {
        Solution newS = new Solution(data);

        return generatePopulation(data);
    }

    public ArrayList<Solution> implementGA(Data data) throws IOException {
        ArrayList<Solution> results = new ArrayList<>();
        ArrayList<Solution> population = new ArrayList<>();
        //Generation 
        for (int i = 0; i < 6000; i++) {
            population.add(generatePopulation(data));
        }
        Collections.sort(population, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {

                return Double.compare(o1.cal_fitness(), o2.cal_fitness());
            }
        });
        for (int j = 0; j < 300; j++) {
            //Selection

            ArrayList<Solution> nextPopulation = new ArrayList<>();
            for (int i = 0; i < 600; i++) {
                nextPopulation.add(population.get(i));
            }
            //Crossover
            for (int i = 0; i < 5400; i++) {
                Random rand = new Random();
                int mom = rand.nextInt(6000);
                int dad = rand.nextInt(6000);
                while (mom == dad) {
                    dad = rand.nextInt(6000);
                }
                //nextPopulation.add(crossover(population.get(dad), population.get(mom), data));
                nextPopulation.add(mutation(population.get(mom), data));
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
                int choosen = rand.nextInt(5900);
                choosen += 100;
                nextPopulation.set(choosen, mutation2(nextPopulation.get(choosen), data));
            }
            Collections.sort(nextPopulation, new Comparator<Solution>() {
                @Override
                public int compare(Solution o1, Solution o2) {

                    return Double.compare(o1.cal_fitness(), o2.cal_fitness());
                }
            });
            results.add(nextPopulation.get(0));
            population.clear();
            population = new ArrayList<>(nextPopulation);
        }

        return results;
    }

    public static <T> Set<T> newShuffledSet(Collection<T> collection) {
        List<T> shuffleMe = new ArrayList<T>(collection);
        Collections.shuffle(shuffleMe);
        return new HashSet<T>(shuffleMe);
    }

    public static void writeSolution(ArrayList<Solution> solutions) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Add normal");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Fitness");

        cell = row.createCell(1);
        cell.setCellValue("Trip");

        cell = row.createCell(2);
        cell.setCellValue("Happiness");

        cell = row.createCell(3);
        cell.setCellValue("Distance");

        cell = row.createCell(4);
        cell.setCellValue("No. dest");

        cell = row.createCell(5);
        cell.setCellValue("Waiting time");

        int rowCount = 0;
        for (Solution s : solutions) {
            row = sheet.createRow(++rowCount);
            cell = row.createCell(0);
            cell.setCellValue(s.cal_fitness());

            cell = row.createCell(1);
            cell.setCellValue(s.gene.toString());

            cell = row.createCell(2);
            cell.setCellValue(s.cal_hapiness_obj());

            cell = row.createCell(3);
            cell.setCellValue(s.cal_distance_obj());

            cell = row.createCell(4);
            cell.setCellValue(s.cal_number_of_destination_obj());

            cell = row.createCell(5);
            cell.setCellValue(s.cal_waiting_time_obj());

        }
        try (FileOutputStream outputStream = new FileOutputStream("Result.xlsx")) {
            workbook.write(outputStream);
        }
    }

}

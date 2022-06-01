/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ACER
 */
public class DistributedRandomNumberGenerator {

    private Map<Integer, Double> distribution;
    private double distSum;

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double Rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (Rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }

}
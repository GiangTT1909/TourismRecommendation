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
    
    public Solution() throws IOException {
        this.data = Data.getDatafromFile();
        gene = new ArrayList<>();
        
    }
    
    
}

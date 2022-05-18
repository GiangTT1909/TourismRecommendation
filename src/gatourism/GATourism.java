/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatourism;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tran Thi Nguyet Ha
 */
public class GATourism {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Data data = Data.getDatafromFile();
            return;
        } catch (IOException ex) {
            Logger.getLogger(GATourism.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

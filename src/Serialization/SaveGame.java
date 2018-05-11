/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serialization;

import helper.GameHelper;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matej
 */
public class SaveGame {
    GameHelper helper;
    
    public SaveGame(GameHelper helper){
        this.helper = helper;
         try(ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("Game.dat"))) {
            outStream.writeObject(helper);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

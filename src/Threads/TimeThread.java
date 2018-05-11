/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import java.time.LocalTime;
import javafx.scene.control.Label;

/**
 *
 * @author Matej
 */
public class TimeThread implements Runnable{
    Label lblTime;
    
    public TimeThread(Label lblTime){
        this.lblTime = lblTime;
    }

    @Override
    public void run() {
        try {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.withNano(0).toString());
        } catch (Exception e) {
            System.out.println("Problem with time thread");
            System.out.println(e.getMessage());
        }
    }
}

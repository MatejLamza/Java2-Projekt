/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack2;

import helper.GameHelper;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Card;
import models.Dealer;
import models.Player;

/**
 *
 * @author Matej
 */
public class ClientMain extends Application {

    static Socket socket = null;
    static ObjectInputStream objInStream = null;
    static ObjectOutputStream objOutStream = null;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static GameHelper getGameData(String playerName) {
        GameHelper helper = null;
        System.out.println("---- CLIENT ----");
        try {
            socket = new Socket("localhost", ServerMain.PORT);
            System.out.println("Client is sucessfuly connected");

            objOutStream = new ObjectOutputStream(socket.getOutputStream());
            objInStream = new ObjectInputStream(socket.getInputStream());

            Player player = new Player();
            player.setPlayerName(playerName);
            System.out.println("Sending player ...");
            objOutStream.writeObject(player);
            System.out.println("Player sent!");
            
            System.out.println("Player newgame " + player.isIsNewGame());

            System.out.println("Reciving game data...");
            helper = (GameHelper) objInStream.readObject();
            System.out.println("Game data recived!");

            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return helper;
    }

     public static GameHelper getGameData2(String playerName,Player player) {
        GameHelper helper = null;
        System.out.println("---- CLIENT ----");
        try {
            socket = new Socket("localhost", ServerMain.PORT);
            System.out.println("Client is sucessfuly connected");

            objOutStream = new ObjectOutputStream(socket.getOutputStream());
            objInStream = new ObjectInputStream(socket.getInputStream());
            
            player.setPlayerName(playerName);
            System.out.println("Sending player ...");
            objOutStream.writeObject(player);
            System.out.println("Player sent!");
            
            System.out.println("Player newgame " + player.isIsNewGame());

            System.out.println("Reciving game data...");
            helper = (GameHelper) objInStream.readObject();
            System.out.println("Game data recived!");

            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return helper;
    }
    

}

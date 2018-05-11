/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import helper.GameHelper;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Dealer;
import models.Player;
import java.util.ArrayList;
import models.Card;

/**
 *
 * @author Matej
 */
public class ServerThread implements Runnable {

    private static int tempId = 1;
    private Socket socket;
    private Dealer dealer;
    private ObjectInputStream objInStream;
    private ObjectOutputStream objOutStream;

    public ServerThread(Socket socket, Dealer dealer) {
        this.socket = socket;
        this.dealer = dealer;
    }

    @Override
    public void run() {

        try {
            objInStream = new ObjectInputStream(socket.getInputStream());
            objOutStream = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Receving player ... ");
            Player receviedPlayer = (Player) objInStream.readObject();
            System.out.println("Player recevied!");

            receviedPlayer.setIdPlayer(tempId);
            System.out.println("Playerov novi game" + receviedPlayer.isIsNewGame());
            
         
//            if (receviedPlayer.isIsNewGame()) {
//                dealer = new Dealer();
//                System.out.println("Poslao sam novog dealera");
//                for (Card card : dealer.getDealerHand()) {
//                    System.out.println(card.toString());
//                }
//            }

            GameHelper helper = new GameHelper(dealer, receviedPlayer);
            helper.initialDeal();

            System.out.println("Sending game to client ...");
            objOutStream.writeObject(helper);
            System.out.println("Game has been sent!");

            System.out.println("ID playera je : " + tempId);
            tempId++;
            System.out.println("Trenutni id je : " + tempId);

        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

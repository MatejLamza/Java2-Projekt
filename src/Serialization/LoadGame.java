/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serialization;

import helper.GameHelper;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javafx.scene.control.TextArea;
import models.Card;
import models.Dealer;
import models.Player;

/**
 *
 * @author Matej
 */
public class LoadGame {
    GameHelper game;
    TextArea txtDealerHand;
    TextArea txtPlayerHand;
    
     public LoadGame(GameHelper game,TextArea txtDealerHand, TextArea txtPlayerHand){
        this.game = game;
        this.txtDealerHand = txtDealerHand;
        this.txtPlayerHand = txtPlayerHand;
        
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Game.dat"))) {
            while (true) {
                Object currentObject = inputStream.readObject();
                
                if (currentObject instanceof GameHelper) {
                    game = (GameHelper)currentObject;
                    Player player = game.getPlayer();
                    Dealer dealer = game.getDealer();
                  
                    for (Card card : player.getPlayerHand()) {
                        txtPlayerHand.appendText(card.toString());
                    }
                    
                    dealer.getDealerHand().forEach((card) -> {
                        txtDealerHand.appendText(card.toString());
                    });
                }
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

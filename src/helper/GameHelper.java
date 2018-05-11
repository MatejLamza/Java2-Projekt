/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import models.Card;
import models.Deck;
import models.Player;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import models.Dealer;

/**
 *
 * @author Matej
 */
public class GameHelper implements Serializable {

    private Dealer dealer;
    private Player player;

    public GameHelper(Dealer dealer, Player player) {
        this.dealer = dealer;
        this.player = player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Player getPlayer() {
        return player;
    }

    public void initialDeal() {
        player.getPlayerHand().add(dealer.dealCardToPlayer());
        player.getPlayerHand().add(dealer.dealCardToPlayer());
        System.out.println("Inital deal done!");
    }

    public void updatePlayerHand(TextArea txtHand) {
        for (Card card : player.getPlayerHand()) {
            txtHand.appendText(card.toString());
        }
    }

    public void updateDealerTextArea(TextArea txtDealerHand) {
        for (Card card : dealer.getDealerHand()) {
            txtDealerHand.appendText(card.toString());
        }
    }

    public int getPlayerScore(Player player) {
        int currentScore = 0;
        for (Card card : player.getPlayerHand()) {
            currentScore += card.getValue().getCardValue();
        }
        return currentScore;
    }

    public void updateScore(int score, Label lblScore) {
        lblScore.setText(new Integer(score).toString());
    }

    public boolean didPlayerWin(int playerCount, int dealerCount) {
        boolean isWon = false;
        if (dealerCount > 21) {
            isWon = true;
        } else if (playerCount > dealerCount && playerCount <= 21) {
            isWon = true;
        }
        return isWon;
    }

    public boolean isDraw(int playerCount, int dealerCount) {
        boolean isDraw = false;
        if (playerCount == dealerCount) {
            isDraw = true;
        }
        return isDraw;
    }

    public void clearAll(TextArea txtPlayer, TextArea txtDealer, Label countPlayer, Label countDealer) {
        txtDealer.clear();
        txtPlayer.clear();
        countDealer.setText("");
        countPlayer.setText("");
    }

    public void clearAllCards(Player p, Dealer d) {
        p.getPlayerHand().removeAll(p.getPlayerHand());
        d.getDealerHand().removeAll(d.getDealerHand());
    }

    public void saveGameData(File f, Player tmpPlayer, Dealer tmpDealer,int winCounter,int dealerCounter) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f, true));
            writer.append("Date played:" + Date.from(Instant.now()).toString());
            writer.append("\n--------------------------------------------------\n");
            
            if (isDraw(getPlayerScore(player),dealer.getDealerScore())) {
                writer.append("GAME DRAW!!\n");
            } 
            else if(didPlayerWin(getPlayerScore(player), dealer.getDealerScore())) {
                writer.append("WINNER IS:" + tmpPlayer.getPlayerName() + "\n");
            }
            else{
                writer.append("WINNDER IS DEALER!!\n");
            }
            
            writer.append("\nDEALER CARDS:" + "\n");
            for (Card card : tmpDealer.getDealerHand()) {
                writer.append("\t" + card.toString());
            }
            writer.append("\nPLAYER CARDS: " + "\n");
            for (Card card : tmpPlayer.getPlayerHand()) {
                writer.append("\t" + card.toString());
            }
            
            writer.append("\nDEALER COUNT: " + dealer.getDealerScore() + "\n");
            writer.append("PLAYER COUNT: " + getPlayerScore(player));
            writer.append("TOTAL SCORE VS DEALER: " + winCounter + " : " + dealerCounter);
            writer.append("\n--------------------------------------------------\n");
            
        } catch (IOException ex) {
            Logger.getLogger(GameHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(GameHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void alertForDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME STATUS");
        alert.setHeaderText(null);
        alert.setContentText("GAME DRAW!");
        alert.showAndWait();
    }

    public void alertForPlayerWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME STATUS");
        alert.setHeaderText(null);
        alert.setContentText("PLAYER WON !!");
        alert.showAndWait();
    }

    public void alertForDealerWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME STATUS");
        alert.setHeaderText(null);
        alert.setContentText("DEALER WON!!");
        alert.showAndWait();
    }

}

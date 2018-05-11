/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Matej
 */
public class Dealer implements Serializable {

    private Deck deck = new Deck();
    private ArrayList<Card> dealerHand = new ArrayList<>();

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public Dealer() {
        initialDeal();
    }

    private void initialDeal() {
        dealerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        giveCardToDealer();
    }

    private boolean isCountUnder16() {
        boolean isScoreLessTh16 = false;
        if (getDealerScore() < 16) {
            isScoreLessTh16 = true;
        }
        return isScoreLessTh16;
    }

    public int getDealerScore() {
        int dealerScore = 0;
        for (Card card : dealerHand) {
            dealerScore += card.getValue().getCardValue();
        }
        return dealerScore;
    }

    private void giveCardToDealer() {
        if (isCountUnder16()) {
            dealerHand.add(deck.drawCard());
        }
    }

    public Card dealCardToPlayer() {
        Card dummyCard = deck.drawCard();
        return dummyCard;
    }

    public int brKarata() {
        int i = deck.getNumberOfCards();
        return i;
    }

}

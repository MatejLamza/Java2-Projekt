/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.CardSuit;
import enums.CardValue;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Matej
 */
public class Deck implements Serializable {

    private ArrayList<Card> deck = new ArrayList<>();

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Deck() {
        generateDeck();
        deck = shuffleDeck();
    }

    private void generateDeck() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                deck.add(new Card(value, suit));
            }
        }
    }

    private ArrayList<Card> shuffleDeck() {
        ArrayList<Card> shuffledDeck = new ArrayList<>();
        while (deck.size() > 0) {
            int i = (int) (Math.random() * deck.size());
            shuffledDeck.add(deck.remove(i));
        }
        return shuffledDeck;
    }

    public Card drawCard() {
        Card dummyCard = deck.get(0);
        deck.remove(dummyCard);
        return dummyCard;
    }

    public int getNumberOfCards() {
        int i = 0;
        i = deck.size();
        return i;
    }
}

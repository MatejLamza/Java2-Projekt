/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.CardSuit;
import enums.CardValue;
import java.io.Serializable;

/**
 *
 * @author Matej
 */
public class Card implements Serializable {
    private CardValue value;
    private CardSuit suit;

    public Card() {
        //Default constructor for reflection
    }

    public Card(CardValue value, CardSuit suit) {
        this.value = value;
        this.suit = suit;
    }
    
    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return this.value + " OF " + this.suit.toString() +"\n";
    }
    
    
}

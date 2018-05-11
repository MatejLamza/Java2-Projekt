/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import enums.CardSuit;
import enums.CardValue;
import models.Card;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matej
 */
public class XmlHelper {

    public static Element rootElement(Document doc) {
        Element rootEl = doc.createElement("Blackjack");
        doc.appendChild(rootEl);
        return rootEl;
    }

    public static Element moveElement(Document doc, Element parentElement) {
        Element moveEl = doc.createElement("Move");
        parentElement.appendChild(moveEl);

        return moveEl;
    }

    public static Element dealerElement(Document doc, Element parentElemet) {
        Element dealerEl = doc.createElement("Dealer");
        parentElemet.appendChild(dealerEl);

        return dealerEl;
    }

    public static Element playerHandElement(Document doc, Element parentElement) {
        Element dealerCards = doc.createElement("Cards");
        parentElement.appendChild(dealerCards);

        return dealerCards;
    }

    public static Element cardElement(Document doc, Element parentElement) {
        Element cardEl = doc.createElement("Card");
        parentElement.appendChild(cardEl);

        return cardEl;
    }

    public static void cardSuitElement(Document doc, Element parentElement, CardSuit suit) {
        Element cardSuit = doc.createElement("Suit");
        cardSuit.appendChild(doc.createTextNode(suit.toString()));
        parentElement.appendChild(cardSuit);
    }

    public static void cardValueElement(Document doc, Element parentElement, CardValue value) {
        Element cardVal = doc.createElement("Value");
        cardVal.appendChild(doc.createTextNode(value.toString()));
        parentElement.appendChild(cardVal);
    }

    public static Card cardFromString(String suit, String value) {
        Card dummyCard = new Card();

        switch (suit) {
            case "HEARTS":

                dummyCard.setSuit(CardSuit.HEARTS);
                break;
            case "DIAMONDS":

                dummyCard.setSuit(CardSuit.DIAMONDS);
                break;
            case "CLUBS":
                dummyCard.setSuit(CardSuit.CLUBS);
                break;
            case "SPADES":
                System.out.println("test spades");
                dummyCard.setSuit(CardSuit.SPADES);
                break;
        }

        switch (value) {
            case "TWO":
                dummyCard.setValue(CardValue.TWO);
                break;
            case "THREE":
                dummyCard.setValue(CardValue.THREE);
                break;
            case "FOUR":
                dummyCard.setValue(CardValue.FOUR);
                break;
            case "FIVE":
                dummyCard.setValue(CardValue.FIVE);
                break;
            case "SIX":
                dummyCard.setValue(CardValue.SIX);
                break;
            case "SEVEN":
                dummyCard.setValue(CardValue.SEVEN);
                break;
            case "EIGHT":
                dummyCard.setValue(CardValue.EIGHT);
                break;
            case "NINE":
                dummyCard.setValue(CardValue.NINE);
                break;
            case "TEN":
                dummyCard.setValue(CardValue.TEN);
                break;
            case "JACK":
                dummyCard.setValue(CardValue.JACK);
                break;
            case "QUEEN":
                dummyCard.setValue(CardValue.QUEEN);
                break;
            case "KING":
                dummyCard.setValue(CardValue.KING);
                break;
            case "ACE":
                dummyCard.setValue(CardValue.ACE);
                break;
        }
        return dummyCard;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack2;

import Serialization.LoadGame;
import Serialization.SaveGame;
import Threads.TimeThread;
import enums.CardSuit;
import enums.CardValue;
import helper.ClassInfoHelper;
import helper.GameHelper;
import helper.XmlHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import models.Card;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import models.Dealer;
import models.Player;
import networking.RmiInterface;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Matej
 */
public class ClientFXMLController implements Initializable {

    @FXML
    TextArea txtPlayerHand;

    @FXML
    TextArea txtDealerHand;

    @FXML
    Label lblName;

    @FXML
    Label lblTime;

    @FXML
    Label lblID;

    @FXML
    Label lblPlayerCount;

    @FXML
    Label lblDealerCount;

    @FXML
    Button btnHit;

    @FXML
    Button btnSave;

    @FXML
    Button btnNextCard;

    @FXML
    Button btnPrevious;

    @FXML
    Button btnStand;

    private GameHelper helper;
    private Dealer dealer;
    private Player player = new Player();
    private ArrayList<Card> newPlayerCards = new ArrayList<>();
    private ArrayList<Card> loadedCards = new ArrayList<>();
    private ArrayList<Card> dealerLoadedCards = new ArrayList<>();
    private ArrayList<Card> previousCards = new ArrayList<>();
    private ArrayList<Card> nextCards = new ArrayList<>();
    private String ime;
    private File gameStatus = new File("StatusIgre.dat");
    private int winCounter = 0;
    private int dealerWinCounter = 0;

    @FXML
    private MenuItem btnLoad;

    public ClientFXMLController() {
    }

    public void startGame(String playerName) {
        if (player.isIsNewGame() == false) {
            helper = ClientMain.getGameData(playerName);
            dealer = helper.getDealer();
            player = helper.getPlayer();

            helper.updatePlayerHand(txtPlayerHand);
            helper.updateScore(helper.getPlayerScore(player), lblPlayerCount);

            helper.updateDealerTextArea(txtDealerHand);
            helper.updateScore(dealer.getDealerScore(), lblDealerCount);
        } else {
            helper = ClientMain.getGameData2(playerName, player);
            dealer = helper.getDealer();
            player = helper.getPlayer();

            helper.updatePlayerHand(txtPlayerHand);
            helper.updateScore(helper.getPlayerScore(player), lblPlayerCount);

            helper.updateDealerTextArea(txtDealerHand);
            helper.updateScore(dealer.getDealerScore(), lblDealerCount);
        }
    }

    @FXML
    public void setCard() {
//        try {
        int count = 1;
        Card receviedCard = new Card();

//            if (count == 1) {
//                Registry registry = LocateRegistry.getRegistry(ServerMain.RMI_PORT);
//                RmiInterface stub = (RmiInterface) registry.lookup("RmiInterface");
//                receviedCard = stub.giveCard(dealer);
//                count++;
//            }else{
        receviedCard = dealer.getDeck().drawCard();
        //}

        newPlayerCards.add(receviedCard);
        txtPlayerHand.appendText(receviedCard.toString());
        player.getPlayerHand().add(receviedCard);
        helper.updateScore(helper.getPlayerScore(player), lblPlayerCount);

//        } catch (RemoteException ex) {
//            Logger.getLogger(ClientFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NotBoundException ex) {
//            Logger.getLogger(ClientFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    public void saveXML() {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //Root element +
            Element rootEl = XmlHelper.rootElement(doc);

            //Move +          
            //Element moveEl = XmlHelper.moveElement(doc, rootEl);
            //Dealer element +          
            Element dealerEl = XmlHelper.dealerElement(doc, rootEl);

            //Dealer hand element +            
            Element dealerCards = XmlHelper.playerHandElement(doc, dealerEl);

            for (Card card : dealer.getDealerHand()) {
                //First card + 
                Element cardEl = doc.createElement("Card");
                dealerCards.appendChild(cardEl);

                //card value EL +
                XmlHelper.cardValueElement(doc, cardEl, card.getValue());

                //Card suit element +
                XmlHelper.cardSuitElement(doc, cardEl, card.getSuit());
            }
            Element listMoves = doc.createElement("Moves");
            rootEl.appendChild(listMoves);

            Element move2 = XmlHelper.moveElement(doc, listMoves);

            //Player element
            Element playerEl = doc.createElement("Player");
            playerEl.setAttribute("ID", new Integer(player.getIdPlayer()).toString());
            move2.appendChild(playerEl);

            //Player name element
            Element playerName = doc.createElement("Name");
            playerName.appendChild(doc.createTextNode(player.getPlayerName()));
            playerEl.appendChild(playerName);

            Element playerCardsEl = XmlHelper.playerHandElement(doc, playerEl);
            int counter = 0;
            for (Card card : player.getPlayerHand()) {
                if (counter < 2) {
                    Element cardEl = doc.createElement("Card");
                    playerCardsEl.appendChild(cardEl);

                    XmlHelper.cardValueElement(doc, cardEl, card.getValue());
                    XmlHelper.cardSuitElement(doc, cardEl, card.getSuit());
                    counter++;
                } else {
                    break;
                }
            }

            for (Card newPlayerCard : newPlayerCards) {
                Element newMoveEl = XmlHelper.moveElement(doc, listMoves);

                Element playEl = doc.createElement("Player");
                playEl.setAttribute("ID", new Integer(player.getIdPlayer()).toString());
                newMoveEl.appendChild(playEl);

                Element playNameEl = doc.createElement("Name");
                playNameEl.appendChild(doc.createTextNode(player.getPlayerName()));
                playEl.appendChild(playNameEl);

                Element playCardsEl = XmlHelper.playerHandElement(doc, playEl);

                Element cardEl2 = doc.createElement("Card");
                playCardsEl.appendChild(cardEl2);

                XmlHelper.cardValueElement(doc, cardEl2, newPlayerCard.getValue());
                XmlHelper.cardSuitElement(doc, cardEl2, newPlayerCard.getSuit());

            }

            TransformerFactory trFactory = TransformerFactory.newInstance();
            Transformer transformer = trFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Ime datoteke: ");
            String fileName = scanner.nextLine();

            StreamResult result = new StreamResult(new File(fileName + ".xml"));
            transformer.transform(source, result);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspješno spremanje");
            alert.setHeaderText(null);
            alert.setContentText("XML datoteka je uspješno spremljena!");

            alert.showAndWait();

        } catch (Exception e) {
        }

    }

    public ArrayList<Card> readDealerCardsFromXML(File f) {
        ArrayList<Card> dealerCards = new ArrayList<>();
        try {
            DocumentBuilder docBuilder = getDB();
            Document docXML = docBuilder.parse(f);
            //root je Blackjack
            Node root = docXML.getDocumentElement();
            NodeList lista = root.getFirstChild().getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                Node cardNode = lista.item(i);
                NodeList listaCards = cardNode.getChildNodes();
                for (int j = 0; j < listaCards.getLength(); j++) {
                    Card dummyCard = new Card();
                    Node card = listaCards.item(j);

                    Node value = card.getFirstChild();
                    Node suit = value.getNextSibling();

                    String suit2 = ((Text) suit.getFirstChild()).getWholeText();
                    String value2 = ((Text) value.getFirstChild()).getWholeText();

                    dummyCard = XmlHelper.cardFromString(suit2, value2);
                    dealerCards.add(dummyCard);
                }
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION U DAELER XML: " + e.getMessage());
        }
        return dealerCards;
    }

    public ArrayList<Card> readFromXML(File f) {
        ArrayList<Card> playerCards = new ArrayList<>();
        try {
            DocumentBuilder docBuild = getDB();
            Document docXML = docBuild.parse(f);
            Node root = docXML.getDocumentElement();
            NodeList lista = root.getLastChild().getChildNodes();

            for (int i = 0; i < lista.getLength(); i++) {
                Node dummyNode = lista.item(i);
                NodeList cardList = dummyNode.getFirstChild().getLastChild().getChildNodes();
                for (int j = 0; j < cardList.getLength(); j++) {

                    Card dummyCard = new Card();

                    Node card = cardList.item(j);
                    Node value = card.getFirstChild();
                    Node suit = value.getNextSibling();

                    String suit2 = ((Text) suit.getFirstChild()).getWholeText();
                    String value2 = ((Text) value.getFirstChild()).getWholeText();

                    dummyCard = XmlHelper.cardFromString(suit2, value2);

                    playerCards.add(dummyCard);
                }
            }
            return playerCards;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static DocumentBuilder getDB() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("Ime playera: ");
        Scanner sc = new Scanner(System.in);
        ime = sc.nextLine();

        player.setPlayerName(ime);
        lblName.setText(ime);
        startGame(ime);
        lblID.setText(new Integer(player.getIdPlayer()).toString());
        displayTime();

    }

    public void next() {
        Card tempCard = new Card();
        if (dealerLoadedCards.size() > 0) {
            tempCard = dealerLoadedCards.get(0);
            txtDealerHand.appendText(tempCard.toString());
            dealerLoadedCards.remove(tempCard);
        } else {
            if (previousCards.size() > 0) {
                tempCard = previousCards.get(0);
                previousCards.remove(tempCard);
            } else {
                tempCard = loadedCards.get(0);
                loadedCards.remove(tempCard);
            }
            txtPlayerHand.appendText(tempCard.toString());
            nextCards.add(tempCard);
        }

    }

    public void previous() {

        Card dummyCard = nextCards.get(nextCards.size() - 1);
        if (txtPlayerHand.getText().indexOf(dummyCard.toString().trim()) >= 0) {
            nextCards.remove(dummyCard);
            previousCards.add(dummyCard);
        }
        txtPlayerHand.clear();
        for (Card previousCard : nextCards) {
            txtPlayerHand.appendText(previousCard.toString());
        }

    }

    @FXML
    private void onLoadClick() {

        FileChooser choser = new FileChooser();
        choser.setTitle("ODABERI DATOTEKU");
        File load = choser.showOpenDialog(lblName.getScene().getWindow());
        if (load != null) {
            txtPlayerHand.clear();
            txtDealerHand.clear();

            loadedCards = readFromXML(load);
            dealerLoadedCards = readDealerCardsFromXML(load);

            btnHit.setVisible(false);
            btnStand.setVisible(false);
            lblDealerCount.setVisible(false);
            lblPlayerCount.setVisible(false);

            btnNextCard.setVisible(true);
            btnPrevious.setVisible(true);

        }
    }

    public void onStandClick() {

        if (helper.isDraw(helper.getPlayerScore(player), dealer.getDealerScore())) {
            helper.alertForDraw();
        } else if (helper.didPlayerWin(helper.getPlayerScore(player), dealer.getDealerScore())) {
            helper.alertForPlayerWin();
            winCounter++;
        } else {
            helper.alertForDealerWin();
            dealerWinCounter++;
        }

        helper.saveGameData(gameStatus, player, dealer, winCounter, dealerWinCounter);
        helper.clearAll(txtPlayerHand, txtDealerHand, lblPlayerCount, lblDealerCount);
        helper.clearAllCards(player, dealer);
        player.setIsNewGame(true);
        startGame(ime);
    }

    public void saveGame() {
        SaveGame save = new SaveGame(helper);
    }

    public void loadGame() {
        helper.clearAll(txtPlayerHand, txtDealerHand, lblPlayerCount, lblDealerCount);
        LoadGame load = new LoadGame(helper, txtDealerHand, txtPlayerHand);
    }

    public void printInfo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"))) {
            writer.write(ClassInfoHelper.writeInfo("models.Card"));
            writer.write(ClassInfoHelper.writeInfo("models.Dealer"));
            writer.write(ClassInfoHelper.writeInfo("models.Deck"));
            writer.write(ClassInfoHelper.writeInfo("models.Player"));
            writer.write(ClassInfoHelper.writeInfo("helper.GameHelper"));
            writer.write(ClassInfoHelper.writeInfo("Serialization.LoadGame"));
            writer.write(ClassInfoHelper.writeInfo("Serialization.SaveGame"));
//            writer.write(ClassInfoHelper.writeInfo("Threads.CardCountThread"));
//            writer.write(ClassInfoHelper.writeInfo("Threads.DealerTurn"));
//            writer.write(ClassInfoHelper.writeInfo("Threads.InitialDealerTurnThread"));
//            writer.write(ClassInfoHelper.writeInfo("Threads.InitialPlayerTurnThread"));
//            writer.write(ClassInfoHelper.writeInfo("Threads.TimeThread"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Game information sucessfuly saved!");
            alert.setHeaderText(null);
            alert.setTitle("Game Information");
            alert.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ClientFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayTime() {
        TimeThread timeThread = new TimeThread(lblTime);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(timeThread);

            }
        }, 0, 1000);
    }

}

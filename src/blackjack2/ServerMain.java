/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack2;

import helper.GameHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.text.AbstractDocument;
import models.Card;
import models.Dealer;
import models.Deck;
import models.Player;
import networking.RmiInterface;
import networking.ServerThread;

/**
 *
 * @author Matej
 */
public class ServerMain implements RmiInterface {

    public static final int PORT = 8888;
    public static final int RMI_PORT = 9991;

    public static String CONFIG_PATH = "./";
    public static String CONFIG_NAME = "GameConfig.ini";

    static ServerSocket serverSocket = null;
    static Socket socket = null;

    static ObjectOutputStream objOutStream;
    static ObjectInputStream objInStream;

    static Dealer dealer = new Dealer();
    static int counter = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        savePortNmToFile();
        runRMI();
        runServer();
    }

    private static void runServer() {
        try {
            System.out.println("- - - - SERVER - - - -");
            int port = loadPortNumberFromConfig(CONFIG_NAME);
            try {
                serverSocket = new ServerSocket(port);
                while (true) {
                    if (counter > 2 && counter % 2 == 0) {
                        dealer = new Dealer();
                    }
                    ServerThread serverTh = new ServerThread(serverSocket.accept(), dealer);
                    Thread serverWrapper = new Thread(serverTh, "Server Thread");
                    serverWrapper.start();
                    counter++;
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NamingException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Card giveCard(Dealer dealer) {
        Card dummyCard = dealer.dealCardToPlayer();
        return dummyCard;
    }

    public static void runRMI() {
        try {
            ServerMain obj = new ServerMain();
            RmiInterface stub = (RmiInterface) UnicastRemoteObject.exportObject((Remote) obj, 0);
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.bind("RmiInterface", stub);

        } catch (RemoteException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void savePortNmToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_NAME))) {
            writer.write("8888");
        } catch (Exception e) {
            System.out.println("PROBLEM WITH SAVING PORT NUMBER TO FILE!!\n" + e.getMessage());
        }
    }

    public static int loadPortNumberFromConfig(String fileName) throws NamingException {
        int port = 0;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        env.put(Context.PROVIDER_URL, "file:" + CONFIG_PATH);

        Context context = new InitialContext(env);
        Object readObject = context.lookup(fileName);
        File file = (File) readObject;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            port = Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("Problem with reading port number! \n" + e.getMessage());
        }
        return port;
    }
}

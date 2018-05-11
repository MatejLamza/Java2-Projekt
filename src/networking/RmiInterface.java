/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;
import models.Card;
import models.Dealer;

/**
 *
 * @author Matej
 */
public interface RmiInterface extends Remote{
    Card giveCard(Dealer dealer) throws RemoteException;
}

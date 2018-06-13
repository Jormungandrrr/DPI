/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.BidderFrame;
import forms.BrokerFrame;
import forms.SellerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.Auction;
import models.Bid;

/**
 *
 * @author Jorrit
 */
public class SellerGateway implements MessageListener {

    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private SellerFrame frame;

    public SellerGateway(SellerFrame f) {
        try {
            this.sender = new MessageSenderGateway("AuctionRequest", false);
            this.reciever = new MessageRecieverGateway("AuctionReply", this, false);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(SellerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createAuction(Auction a) {
        try {
            this.sender.sendmessage(a);
        } catch (JMSException ex) {
            Logger.getLogger(SellerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof Bid) {
                    Bid b = (Bid) o;
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(BrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

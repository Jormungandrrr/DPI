/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.BidderFrame;
import forms.BrokerFrame;
import forms.SellerFrame;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.Auction;

/**
 *
 * @author Jorrit
 */
public class BidderGateway implements MessageListener {

    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private BidderFrame frame;

    public BidderGateway(BidderFrame f) {
        try {
            this.sender = new MessageSenderGateway("BiddingRequest", false);
            this.reciever = new MessageRecieverGateway("AuctionBidding", this, true);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(BidderGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void makeBid(Serializable object) {
        try {
            sender.sendmessage(object);
        } catch (JMSException ex) {
            Logger.getLogger(BidderGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof Auction) {
                    Auction a = (Auction) o;
                    frame.updateOrAddAuction(a);
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(BrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

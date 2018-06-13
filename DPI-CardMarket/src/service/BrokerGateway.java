/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.BrokerFrame;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
public class BrokerGateway implements MessageListener {

    MessageSenderGateway clientSender;
    MessageRecieverGateway clientReciever;
    MessageSenderGateway AuctionSender;
    MessageRecieverGateway AuctionReciever;

    private BrokerFrame frame;
    private HashMap<UUID, Auction> auctions = new HashMap<UUID, Auction>();

    public BrokerGateway(BrokerFrame f) {
        try {
            this.clientSender = new MessageSenderGateway("AuctionBidding", true);
            this.clientReciever = new MessageRecieverGateway("BiddingRequest", this, false);
            this.AuctionSender = new MessageSenderGateway("AuctionReply", false);
            this.AuctionReciever = new MessageRecieverGateway("AuctionRequest", this, false);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HandleNewAuction(Auction a) {
        try {
            auctions.put(a.getUuid(), a);
            clientSender.sendmessage(a);
            frame.add(a);
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void EndAuction(Auction a) {
        try {
            auctions.remove(a.getUuid());
            AuctionSender.sendmessage(a);
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//
//    public void SendReply(BankInterestReply bir) {
//        try {
//            LoanReply lr = new LoanReply();
//            lr.setInterest(bir.getInterest());
//            lr.setQuoteID(bir.getQuoteId());
//            RequestReply rrr = new RequestReply(bir.getLoanRequest(), lr);
//            clientSender.sendmessage(rrr);
//            frame.add(bir.getLoanRequest(), bir);
//        } catch (JMSException ex) {
//            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof Auction) {
                    Auction a = (Auction) o;
                     if (!auctions.containsKey(a.getUuid())) 
                     { 
                         HandleNewAuction(a);  
                     }
                }
                
                else if (o instanceof Bid) {
                    Bid b = (Bid) o;
                    if (auctions.containsKey(b.getAuction())) {
                        Auction a =  auctions.get(b.getAuction());
                        if (a.price < b.amount) {
                            auctions.remove(b.getAuction());
                            a.bids.add(b);
                            a.price = b.amount;
                            auctions.put(a.getUuid(), a);
                            frame.updateAuction(a);
                            clientSender.sendmessage(a);
                        }
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(BrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

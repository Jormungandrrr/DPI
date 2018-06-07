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

/**
 *
 * @author Jorrit
 */
public class BrokerGateway implements MessageListener {

    MessageSenderGateway clientSender;
    MessageRecieverGateway clientReciever;
    MessageSenderGateway bankSender;
    MessageRecieverGateway bankReciever;

    private BrokerFrame frame;
    int amountOfBanks = 4;
    private HashMap<UUID, List<BankInterestReply>> messages = new HashMap<UUID, List<BankInterestReply>>();

    public BrokerGateway(BrokerFrame f) {
        try {
            this.clientSender = new MessageSenderGateway("LoanReply", false);
            this.clientReciever = new MessageRecieverGateway("LoanRequest", this, false);
            this.bankSender = new MessageSenderGateway("BankRequest", true);
            this.bankReciever = new MessageRecieverGateway("BankReply", this, false);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendInterest(LoanRequest loanRequest) {
        try {
            BankInterestRequest bir = new BankInterestRequest();
            bir.setAmount(loanRequest.getAmount());
            bir.setTime(loanRequest.getTime());
            bir.setLoanRequest(loanRequest);
            bankSender.sendmessage(bir);
            frame.add(loanRequest, bir);
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendReply(BankInterestReply bir) {
        try {
            LoanReply lr = new LoanReply();
            lr.setInterest(bir.getInterest());
            lr.setQuoteID(bir.getQuoteId());
            RequestReply rrr = new RequestReply(bir.getLoanRequest(), lr);
            clientSender.sendmessage(rrr);
            frame.add(bir.getLoanRequest(), bir);
        } catch (JMSException ex) {
            Logger.getLogger(BrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof LoanRequest) {
                    LoanRequest lr = (LoanRequest) o;
                    SendInterest(lr);
                    frame.add(lr);
                }

                if (o instanceof RequestReply) {
                    RequestReply rr = (RequestReply) o;
                    if (rr.getReply() instanceof BankInterestReply) {
                        BankInterestReply bir = (BankInterestReply) rr.getReply();
                        if (messages.containsKey(bir.loanRequest.getUuid())) {
                            List<BankInterestReply> replies = messages.get(bir.loanRequest.getUuid());
                            replies.add(bir);
                            if (bir.loanRequest.getAmount() != 999999999 ) {
                            if (replies.size() == amountOfBanks) {
                                replies.sort(Comparator.comparing(BankInterestReply::getInterest));
                                SendReply(replies.get(0));
                                messages.remove(bir.loanRequest.getUuid());
                            }
                            }
                        }
                        else{
                                ArrayList<BankInterestReply> tmplst = new ArrayList<BankInterestReply>();
                                tmplst.add(bir);
                                messages.put(bir.loanRequest.getUuid(), tmplst);
                            }
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(BrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

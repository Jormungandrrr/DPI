/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.BankInterestReply;
import models.BankInterestRequest;
import models.LoanReply;
import models.LoanRequest;
import models.RequestReply;

/**
 *
 * @author Jorrit
 */
public class LoanBrokerAppGateway implements MessageListener {

    MessageSenderGateway clientSender;
    MessageRecieverGateway clientReciever;
    MessageSenderGateway bankSender;
    MessageRecieverGateway bankReciever;

    private LoanBrokerFrame frame;

    public LoanBrokerAppGateway(LoanBrokerFrame f) {
        try {
            this.clientSender = new MessageSenderGateway("LoanReply");
            this.clientReciever = new MessageRecieverGateway("LoanRequest", this);
            this.bankSender = new MessageSenderGateway("BankRequest");
            this.bankReciever = new MessageRecieverGateway("BankReply", this);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendInterest(LoanRequest loanRequest){
        try {
            BankInterestRequest bir = new BankInterestRequest();
            bir.setAmount(loanRequest.getAmount());
            bir.setTime(loanRequest.getTime());
            bir.setLoanRequest(loanRequest);
            bankSender.sendmessage(bir);
            frame.add(loanRequest, bir);
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendReply(RequestReply rr){
        try {
            LoanReply lr = new LoanReply();
            BankInterestReply bir = (BankInterestReply) rr.getReply();
            lr.setInterest(bir.getInterest());
            lr.setQuoteID(bir.getQuoteId());
            RequestReply rrr = new RequestReply(bir.getLoanRequest(), lr);
            clientSender.sendmessage(rrr);
            frame.add(bir.getLoanRequest(), bir);
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerAppGateway.class.getName()).log(Level.SEVERE, null, ex);
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
                        SendReply(rr);
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

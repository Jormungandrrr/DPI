/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.JMSBankFrame;
import forms.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.BankInterestRequest;
import models.RequestReply;

/**
 *
 * @author Jorrit
 */
public class BankAppGateway implements MessageListener {

    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private JMSBankFrame frame;

    public BankAppGateway(JMSBankFrame f) {
        try {
            this.sender = new MessageSenderGateway("BankReply");
            this.reciever = new MessageRecieverGateway("BankRequest", this);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(BankAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendReply(RequestReply rr) {
        try {
            sender.sendmessage(rr);
        } catch (JMSException ex) {
            Logger.getLogger(BankAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof BankInterestRequest) {
                    BankInterestRequest bir = (BankInterestRequest) o;
                    frame.listModel.addElement(new RequestReply(bir, null));
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

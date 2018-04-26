/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.BankFrame;
import forms.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.BankInterestReply;
import models.BankInterestRequest;
import models.RequestReply;

/**
 *
 * @author Jorrit
 */
public class BankAppGateway implements MessageListener {

    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private BankFrame frame;
    private int min;
    private int max;
    private int time;

    public BankAppGateway(BankFrame f, int min, int max, int time) {
        try {
            this.sender = new MessageSenderGateway("BankReply", false);
            this.reciever = new MessageRecieverGateway("BankRequest", this, true);
            this.frame = f;
            this.min = min;
            this.max = max;
            this.time = time;
        } catch (JMSException ex) {
            Logger.getLogger(BankAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendReply(RequestReply rr) {
        try {
            this.sender.sendmessage(rr);
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
                    if (bir.getAmount() >= min && bir.getAmount() <= max && bir.getTime() <= time) {
                         frame.listModel.addElement(new RequestReply(bir, null));
                    }
                    else {
                         this.sender.sendmessage(new RequestReply(bir, new BankInterestReply(999999999,frame.name,bir.loanRequest)));
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

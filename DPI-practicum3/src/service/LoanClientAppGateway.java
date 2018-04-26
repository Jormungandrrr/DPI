/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.LoanBrokerFrame;
import forms.LoanClientFrame;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import models.RequestReply;

/**
 *
 * @author Jorrit
 */
public class LoanClientAppGateway implements MessageListener{
    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private LoanClientFrame frame;

    public LoanClientAppGateway(LoanClientFrame f){
        try {
            this.sender = new MessageSenderGateway("LoanRequest", false);
            this.reciever = new MessageRecieverGateway("LoanReply", this, false);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(LoanClientAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendRequest(Serializable object){
        try {
            sender.sendmessage(object);
        } catch (JMSException ex) {
            Logger.getLogger(LoanClientAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg.toString();
            System.out.println(msgText);

            if (msg instanceof ObjectMessage) {

                Object o = ((ObjectMessage) msg).getObject();

                if (o instanceof RequestReply) {
                    RequestReply rr = (RequestReply) o;
                    frame.recieveReply(rr);
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

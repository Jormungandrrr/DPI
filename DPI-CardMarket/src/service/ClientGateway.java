/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import forms.BrokerFrame;
import forms.ClientFrame;
import java.io.Serializable;
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
public class ClientGateway implements MessageListener{
    MessageSenderGateway sender;
    MessageRecieverGateway reciever;
    private ClientFrame frame;

    public ClientGateway(ClientFrame f){
        try {
            this.sender = new MessageSenderGateway("LoanRequest", false);
            this.reciever = new MessageRecieverGateway("LoanReply", this, false);
            this.frame = f;
        } catch (JMSException ex) {
            Logger.getLogger(ClientGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendRequest(Serializable object){
        try {
            sender.sendmessage(object);
        } catch (JMSException ex) {
            Logger.getLogger(ClientGateway.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

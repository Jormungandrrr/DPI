/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Jorrit
 */
public class MessageRecieverGateway {

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public MessageRecieverGateway(String dest, MessageListener ml, boolean topic) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        //connectionFactory.setTrustedPackages(Arrays.asList("models"));
        connectionFactory.setTrustAllPackages(true);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        if (topic) {consumer = session.createConsumer(session.createTopic(dest));}
        else{consumer = session.createConsumer(session.createQueue(dest));} 
        consumer.setMessageListener(ml);
    }
}

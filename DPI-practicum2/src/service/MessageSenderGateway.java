package service;

import java.io.Serializable;
import java.util.Arrays;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jorrit
 */
public class MessageSenderGateway {

    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public MessageSenderGateway(String dest) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connectionFactory.setTrustedPackages(Arrays.asList("models"));
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(session.createQueue(dest));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void sendmessage(Serializable object) throws JMSException {
        Message msg = session.createObjectMessage(object);
        producer.send(msg);
    }
}

package forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import models.LoanRequest;
import models.BankInterestReply;
import models.BankInterestRequest;
import models.LoanReply;
import models.RequestReply;
import org.apache.activemq.ActiveMQConnectionFactory;

public class LoanBrokerFrame extends JFrame implements MessageListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
    private JList<JListLine> list;

    private Connection connection;
    private Session session;
    private MessageConsumer loanConsumer;
    private MessageProducer loanProducer;
    private MessageConsumer bankConsumer;
    private MessageProducer bankProducer;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoanBrokerFrame frame = new LoanBrokerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoanBrokerFrame() throws JMSException {
        setTitle("Loan Broker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
        gbl_contentPane.rowHeights = new int[]{233, 23, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 7;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        list = new JList<JListLine>(listModel);
        scrollPane.setViewportView(list);

        Connect();
    }

    private JListLine getRequestReply(LoanRequest request) {

        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest().equals(request)) {
                return rr;
            }
        }

        return null;
    }

    public void add(LoanRequest loanRequest) {
        listModel.addElement(new JListLine(loanRequest));
    }

    public void add(LoanRequest loanRequest, BankInterestRequest bankRequest) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankRequest != null) {
            rr.setBankRequest(bankRequest);
            list.repaint();
        }
    }

    public void add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.setBankReply(bankReply);;
            list.repaint();
        }
    }

    public void SendInterest(LoanRequest loanRequest) throws JMSException {
        BankInterestRequest bir = new BankInterestRequest();
        bir.setAmount(loanRequest.getAmount());
        bir.setTime(loanRequest.getTime());
        bir.setLoanRequest(loanRequest);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(bir);
        bankProducer.send(objectMessage);

        add(loanRequest, bir);
    }

    public void SendReply(RequestReply rr) throws JMSException {
        LoanReply lr = new LoanReply();
        BankInterestReply bir = (BankInterestReply) rr.getReply();
        lr.setInterest(bir.getInterest());
        lr.setQuoteID(bir.getQuoteId());
        
        RequestReply rrr = new RequestReply(bir.getLoanRequest(),lr);
        
        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(rrr);
        loanProducer.send(objectMessage);
        add(bir.getLoanRequest(),bir);
    }

    public void Connect() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connectionFactory.setTrustedPackages(Arrays.asList("models"));
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        loanConsumer = session.createConsumer(session.createTopic("Loan"));
        loanConsumer.setMessageListener(this);
        loanProducer = session.createProducer(session.createTopic("Loan"));
        loanProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        bankProducer = session.createProducer(session.createTopic("Bank"));
        bankProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        bankConsumer = session.createConsumer(session.createTopic("Bank"));
        bankConsumer.setMessageListener(this);
    }

    public void Dissconect() throws JMSException {
        //consumer.close();
        //session.close();
        //connection.close();
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
                    add(lr);
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

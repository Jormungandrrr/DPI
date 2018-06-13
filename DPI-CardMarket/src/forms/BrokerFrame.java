package forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import models.Auction;
import service.BrokerGateway;

/**
 *
 * @author Jorrit
 */
public class BrokerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<Auction> auctionList = new DefaultListModel<Auction>();
    private JList<Auction> list;
    private BrokerGateway brokerGateway;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BrokerFrame frame = new BrokerFrame();
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
    public BrokerFrame() {
        setTitle("CardBroker");
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
        
        Timer timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                list.repaint();
                checkAuctionTime();
            }
        });
        timer.start();

        list = new JList<Auction>(auctionList);
        scrollPane.setViewportView(list);

        brokerGateway = new BrokerGateway(this);
    }

    public void add(Auction a) {
        auctionList.addElement(a);
        list.repaint();
    }

    public void updateAuction(Auction a) {

        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getUuid().equals(a.getUuid())) {
                auction = a;
            }
        }
        list.repaint();
    }
    
    public void checkAuctionTime(){
        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getEnd() < new Date().getTime()) {
                brokerGateway.EndAuction(auction);
                 auctionList.remove(i);
            }
        }
        list.repaint();
    }
}

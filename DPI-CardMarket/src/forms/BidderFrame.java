package forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import models.Auction;
import models.Bid;
import models.Bidder;
import models.Seller;
import service.BidderGateway;
import service.SellerGateway;

/**
 *
 * @author Jorrit
 */
public class BidderFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfReply;
    public DefaultListModel<Auction> auctionList = new DefaultListModel<Auction>();
    public String name;
    private BidderGateway BidGateway;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BidderFrame bf1 = new BidderFrame(new Bidder("Henk"));
                    BidderFrame bf2 = new BidderFrame(new Bidder("Klaas"));
                    bf1.setVisible(true);
                    bf2.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public BidderFrame(Bidder b) {
        this.name = b.name;
        setTitle(b.name);
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
        gbc_scrollPane.gridwidth = 5;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        JList<Auction> list = new JList<Auction>(auctionList);
        scrollPane.setViewportView(list);

        Timer timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                list.repaint();
                checkAuctionTime();
            }
        });
        timer.start();

        JLabel lblNewLabel = new JLabel("type reply");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        tfReply = new JTextField();
        GridBagConstraints gbc_tfReply = new GridBagConstraints();
        gbc_tfReply.gridwidth = 2;
        gbc_tfReply.insets = new Insets(0, 0, 0, 5);
        gbc_tfReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfReply.gridx = 1;
        gbc_tfReply.gridy = 1;
        contentPane.add(tfReply, gbc_tfReply);
        tfReply.setColumns(10);

        JButton btncreateBid = new JButton("make bid");
        btncreateBid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Auction a = list.getSelectedValue();
                double amount = Double.parseDouble((tfReply.getText()));
                BidGateway.makeBid(new Bid(b, a.uuid, amount));
            }
        });
        GridBagConstraints gbc_btncreateBid = new GridBagConstraints();
        gbc_btncreateBid.anchor = GridBagConstraints.NORTHWEST;
        gbc_btncreateBid.gridx = 4;
        gbc_btncreateBid.gridy = 1;
        contentPane.add(btncreateBid, gbc_btncreateBid);
        BidGateway = new BidderGateway(this);
    }
    
    public void checkAuctionTime(){
        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getEnd() < new Date().getTime()) {
                 auctionList.remove(i);
            }
        }
    }
    
    public void updateOrAddAuction(Auction a) {
        boolean newAuction = true;
        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getUuid().equals(a.getUuid())) {
                auctionList.remove(i);
                auction = a;
                auctionList.addElement(a);
                newAuction = false;
            }
        }
        if (newAuction) {
            auctionList.addElement(a);
        }
    }
}

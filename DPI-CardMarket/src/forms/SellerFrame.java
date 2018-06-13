package forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import models.Auction;
import models.Card;
import models.Sale;
import models.Seller;
import service.BidderGateway;
import service.SellerGateway;

/**
 *
 * @author Jorrit
 */
public class SellerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCard;
    private DefaultListModel<Auction> auctionList = new DefaultListModel<Auction>();
    private JList<Auction> auctionlist;
    private DefaultListModel<Sale> saleList = new DefaultListModel<Sale>();
    private JList<Sale> salelist;

    private JTextField tfCardColor;
    private JLabel lblNewLabel;
    private JTextField tfAmount;
    private JLabel CardlblBody;
    private JLabel lblNewLabel_1;
    private JTextField tfTime;

    private SellerGateway sellerGateway;

    /**
     * Create the frame.
     */
    public SellerFrame(Seller s) {
        setTitle(s.getName() + " - Auctions");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 684, 619);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 30, 30, 30, 30, 0};
        gbl_contentPane.rowHeights = new int[]{30, 30, 30, 30, 30};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblBody = new JLabel("card");
        GridBagConstraints gbc_lblBody = new GridBagConstraints();
        gbc_lblBody.insets = new Insets(0, 0, 5, 5);
        gbc_lblBody.gridx = 0;
        gbc_lblBody.gridy = 0;
        contentPane.add(lblBody, gbc_lblBody);

        tfCard = new JTextField();
        GridBagConstraints gbc_tCard = new GridBagConstraints();
        gbc_tCard.fill = GridBagConstraints.HORIZONTAL;
        gbc_tCard.insets = new Insets(0, 0, 5, 5);
        gbc_tCard.gridx = 1;
        gbc_tCard.gridy = 0;
        contentPane.add(tfCard, gbc_tCard);
        tfCard.setColumns(5);

        JLabel CardlblBody = new JLabel("Color");
        GridBagConstraints gbc_CardlblBody = new GridBagConstraints();
        gbc_CardlblBody.insets = new Insets(0, 0, 5, 5);
        gbc_CardlblBody.gridx = 2;
        gbc_CardlblBody.gridy = 0;
        contentPane.add(CardlblBody, gbc_CardlblBody);

        tfCardColor = new JTextField();
        GridBagConstraints gbc_tfCardColor = new GridBagConstraints();
        gbc_tfCardColor.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfCardColor.insets = new Insets(0, 0, 5, 5);
        gbc_tfCardColor.gridx = 2;
        gbc_tfCardColor.gridy = 1;
        contentPane.add(tfCardColor, gbc_tfCardColor);
        tfCard.setColumns(5);

        lblNewLabel = new JLabel("start amount");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        tfAmount = new JTextField();
        GridBagConstraints gbc_tfAmount = new GridBagConstraints();
        gbc_tfAmount.anchor = GridBagConstraints.NORTH;
        gbc_tfAmount.insets = new Insets(0, 0, 5, 5);
        gbc_tfAmount.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfAmount.gridx = 1;
        gbc_tfAmount.gridy = 1;
        contentPane.add(tfAmount, gbc_tfAmount);
        tfAmount.setColumns(10);

        lblNewLabel_1 = new JLabel("time");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 2;
        contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

        tfTime = new JTextField();
        GridBagConstraints gbc_tfTime = new GridBagConstraints();
        gbc_tfTime.insets = new Insets(0, 0, 5, 5);
        gbc_tfTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfTime.gridx = 1;
        gbc_tfTime.gridy = 2;
        contentPane.add(tfTime, gbc_tfTime);
        tfTime.setColumns(10);

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                auctionlist.repaint();
            }
        });
        timer.start();

        JButton btnQueue = new JButton("Create Auction");
        btnQueue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String cardname = tfCard.getText();
                String color = tfCardColor.getText();
                int amount = Integer.parseInt(tfAmount.getText());
                Instant timestamp = Instant.now();
                timestamp = timestamp.plus(Integer.parseInt(tfTime.getText()), ChronoUnit.SECONDS);
                Card cardToAuction = new Card(cardname, color, "test", "test");
                Auction newAuction = new Auction(cardToAuction, timestamp.toEpochMilli(), amount, s);
                auctionList.addElement(newAuction);
                sellerGateway.createAuction(newAuction);
            }
        });
        GridBagConstraints gbc_btnQueue = new GridBagConstraints();
        gbc_btnQueue.insets = new Insets(0, 0, 5, 5);
        gbc_btnQueue.gridx = 2;
        gbc_btnQueue.gridy = 2;

        contentPane.add(btnQueue, gbc_btnQueue);

        JScrollPane auctionPane = new JScrollPane();
        auctionlist = new JList<Auction>(auctionList);
        auctionPane.setViewportView(auctionlist);

        JScrollPane salePane = new JScrollPane();
        salelist = new JList<Sale>(saleList);
        salePane.setViewportView(salelist);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, auctionPane, salePane);
        splitPane.setDividerLocation(200);
        GridBagConstraints gbc_splitPane = new GridBagConstraints();
        gbc_splitPane.gridheight = 7;
        gbc_splitPane.gridwidth = 6;
        gbc_splitPane.fill = GridBagConstraints.BOTH;
        gbc_splitPane.gridx = 0;
        gbc_splitPane.gridy = 4;
        contentPane.add(splitPane, gbc_splitPane);
        sellerGateway = new SellerGateway(this);
    }

    public void updateAuction(Auction a) {
        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getUuid().equals(a.getUuid())) {
                auctionList.remove(i);
                auction = a;
                auctionList.addElement(a);
            }
        }
    }
    
    public void removeAuction(UUID auctionUUID) {
        for (int i = 0; i < auctionList.getSize(); i++) {
            Auction auction = auctionList.get(i);
            if (auction.getUuid().equals(auctionUUID)) {
                auctionList.remove(i);
            }
        }
    }

    public void addSale(Sale s) {
        removeAuction(s.getAuctionUUID());
        saleList.addElement(s);
        salelist.repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SellerFrame frame = new SellerFrame(new Seller("Hans"));

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

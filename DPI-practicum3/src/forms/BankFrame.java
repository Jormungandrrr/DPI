package forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import models.BankInterestReply;
import models.BankInterestRequest;
import models.LoanRequest;
import models.RequestReply;
import service.BankAppGateway;

public class BankFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfReply;
    public DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>> listModel = new DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>>();
    public String name;
    private BankAppGateway BankGateway;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BankFrame ABNframe = new BankFrame("ABN", 200000, 300000, 20);
                    BankFrame INGframe = new BankFrame("ING", 0, 100000, 10);
                    BankFrame RABOframe = new BankFrame("RABO", 0, 250000, 15);
                     BankFrame testframe = new BankFrame("test", 0, 250000, 15);
                     testframe.setVisible(true);
                    ABNframe.setVisible(true);
                    INGframe.setVisible(true);
                    RABOframe.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public BankFrame(String bank, int min, int max, int time) {
        this.name = bank;
        setTitle(bank);
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

        JList<RequestReply<BankInterestRequest, BankInterestReply>> list = new JList<RequestReply<BankInterestRequest, BankInterestReply>>(listModel);
        scrollPane.setViewportView(list);

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

        JButton btnSendReply = new JButton("send reply");
        btnSendReply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RequestReply<BankInterestRequest, BankInterestReply> rr = list.getSelectedValue();
                double interest = Double.parseDouble((tfReply.getText()));
                LoanRequest lr = rr.getRequest().getLoanRequest();
                BankInterestReply reply = new BankInterestReply(interest, bank, lr);
                if (rr != null && reply != null) {
                    rr.setReply(reply);
                    list.repaint();
                }
                BankGateway.sendReply(rr);
            }
        });
        GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
        gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnSendReply.gridx = 4;
        gbc_btnSendReply.gridy = 1;
        contentPane.add(btnSendReply, gbc_btnSendReply);
        BankGateway = new BankAppGateway(this, min, max, time);
    }
}
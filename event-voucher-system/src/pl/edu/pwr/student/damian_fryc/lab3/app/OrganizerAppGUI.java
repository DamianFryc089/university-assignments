package pl.edu.pwr.student.damian_fryc.lab3.app;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OfferDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.UsersDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.OfferDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.OrderDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.UsersDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class OrganizerAppGUI extends JFrame {

    private final UsersDao usersDao = new UsersDaoDB();
    private final OfferDao offerDao = new OfferDaoDB();
    private final OrderDao orderDao = new OrderDaoDB();
    private Organizer self;
    private final Font bigFont = new Font("Arial", Font.PLAIN, 30);
    private final Font mediumFont = new Font("Arial", Font.PLAIN, 20);

    public OrganizerAppGUI() {
        setTitle("Organizer Application");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginScreen();
    }
    private void loginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        loginPanel.setPreferredSize(new Dimension(200,50));
        JLabel idLabel = new JLabel("Enter organizer ID:", SwingConstants.RIGHT);
        JTextField idField = new JTextField();
        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create new organizer");

        idLabel.setFont(bigFont);
        idField.setFont(bigFont);
        loginButton.setFont(bigFont);
        createButton.setFont(bigFont);

        loginPanel.add(idLabel);
        loginPanel.add(idField);
        loginPanel.add(loginButton);
        loginPanel.add(createButton);

        add(new JScrollPane(loginPanel));
        loginButton.addActionListener(e -> {
            try {
                int organizerId = Integer.parseInt(idField.getText());
                Organizer organizer = usersDao.getOrganizer(organizerId);
                if (organizer != null) {
                    self = organizer;
                    mainScreen();
                } else {
                    JOptionPane.showMessageDialog(this, "Organizer not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        });

        createButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Organizer Name:");
            if (name != null && !name.isEmpty()) {
                self = usersDao.addOrganizer(name);
                JOptionPane.showMessageDialog(this, "Organizer created!");
                mainScreen();
            }
        });

        revalidate();
        repaint();
    }
    private void mainScreen() {
        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome " + self.getName() + " ("+self.getId()+")!", SwingConstants.CENTER);
        welcomeLabel.setFont(bigFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(welcomeLabel, gbc);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable dataTable = new JTable(model);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 50;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(new JScrollPane(dataTable), gbc);

        JPanel optionInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.add(optionInput, gbc);


        JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JButton offersButton = new JButton("   Offers   ");
        offersButton.setFont(bigFont);
        JButton ordersButton = new JButton("   Orders   ");
        ordersButton.setFont(bigFont);
        JButton exitButton = new JButton("    Exit    ");
        exitButton.setFont(bigFont);

        mainButtons.add(offersButton);
        mainButtons.add(ordersButton);
        mainButtons.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.add(mainButtons, gbc);

        add(mainPanel);

        offersButton.addActionListener(e -> updateOffers((ArrayList<Offer>) offerDao.getAllOffers(), dataTable, optionInput));
        ordersButton.addActionListener(e -> updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput));
        exitButton.addActionListener(e -> System.exit(0));

        revalidate();
        repaint();
    }
    private void updateOffers(ArrayList<Offer> offers, JTable dataTable, JPanel optionInput){
        optionInput.removeAll();
        optionInput.revalidate();
        optionInput.repaint();
        for (MouseListener listener : dataTable.getMouseListeners()) {
            dataTable.removeMouseListener(listener);
        }

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("i");
        model.addColumn("Parameters");

        int i = 0;
        for (Offer offer : offers) {
            ArrayList<String> offerData = offer.toStringArray(false, true);
            ArrayList<Object> rowData = new ArrayList<>();
            rowData.add(i++);
            rowData.addAll(offerData);
            model.addRow(rowData.toArray(new Object[0]));
        }

//        for (Offer offer : offers) {
//            ArrayList<String> offerData = offer.toStringArray(true, true);
//            model.addRow(offerData.toArray(new Object[0]));
//        }
        dataTable.setModel(model);
        dataTable.repaint();
    }
    private void updateOrders(ArrayList<Order> orders, JTable dataTable, JPanel optionInput){
        optionInput.removeAll();
        optionInput.revalidate();
        optionInput.repaint();
        for (MouseListener listener : dataTable.getMouseListeners()) {
            dataTable.removeMouseListener(listener);
        }
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                optionInput.removeAll();
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow != -1) {
                    String orderI = dataTable.getValueAt(selectedRow, 0).toString();
                    int selected = Integer.parseInt(orderI);

                    if (Objects.equals(orders.get(selected).getStatus(), "Approved")){
                        JButton button1 = new JButton("Declare order execution");
                        button1.setFont(bigFont);
                        button1.addActionListener(x -> {
                            orders.get(selected).setStatus("In Progress");
                            orders.get(selected).setOrganizerId(self.getId());
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button1);
                    }
                    else if(Objects.equals(orders.get(selected).getStatus(), "In Progress") && orders.get(selected).getOrganizerId() == self.getId()){

                        JButton button1 = new JButton("Withdraw declaration of order execution");
                        button1.setFont(mediumFont);
                        button1.addActionListener(x -> {
                            orders.get(selected).setStatus("Approved");
                            orders.get(selected).setOrganizerId(-1);
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button1);


                        JPanel panel = new JPanel();
                        JLabel label = new JLabel("New parameters:");
                        label.setFont(mediumFont);
                        panel.add(label);

                        JTextField parametersField = new JTextField(dataTable.getValueAt(selectedRow, 4).toString());
                        parametersField.setPreferredSize(new Dimension(200,50));
                        parametersField.setFont(mediumFont);
                        panel.add(parametersField);

                        JButton button2 = new JButton("Update order and open customer registration");
                        button2.setFont(mediumFont);
                        button2.addActionListener(x -> {
                            orders.get(selected).setStatus("Awaiting Confirmation");
                            orders.get(selected).setOrderParameters(parametersField.getText());
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        panel.add(button2);
                        optionInput.add(panel);
                    }
                    else if(Objects.equals(orders.get(selected).getStatus(), "Awaiting Confirmation") && orders.get(selected).getOrganizerId() == self.getId()){

                        JButton button1 = new JButton("Close customer registration and clear order parameters");
                        button1.setFont(mediumFont);
                        button1.addActionListener(x -> {
                            orders.get(selected).setStatus("In Progress");
                            orders.get(selected).setOrderParameters("");
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button1);


                        JPanel panel = new JPanel();
                        JLabel label = new JLabel("New parameters:");
                        label.setFont(mediumFont);
                        panel.add(label);

                        JTextField parametersField = new JTextField(dataTable.getValueAt(selectedRow, 4).toString());
                        parametersField.setPreferredSize(new Dimension(200,50));
                        parametersField.setFont(mediumFont);
                        panel.add(parametersField);

                        JButton button2 = new JButton("Update order");
                        button2.setFont(mediumFont);
                        button2.addActionListener(x -> {
                            orders.get(selected).setOrderParameters(parametersField.getText());
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        panel.add(button2);
                        optionInput.add(panel);
                    }
                    else if(Objects.equals(orders.get(selected).getStatus(), "Confirmed") && orders.get(selected).getOrganizerId() == self.getId()){

                        JButton button1 = new JButton("Mark order as Completed");
                        button1.setFont(bigFont);
                        button1.addActionListener(x -> {
                            orders.get(selected).setStatus("Completed");
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button1);

                        JButton button2 = new JButton("Mark order as Did Not Attend");
                        button2.setFont(bigFont);
                        button2.addActionListener(x -> {
                            orders.get(selected).setStatus("Did Not Attend");
                            orderDao.updateOrder(orders.get(selected));
                            updateOrders((ArrayList<Order>) orderDao.getAllOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button2);
                    }

                    optionInput.revalidate();
                    optionInput.repaint();
                }

            }
        });
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("i");
        model.addColumn("customer id");
        model.addColumn("offer id");
        model.addColumn("offer parameters");
        model.addColumn("parameters");
        model.addColumn("status");


        int i = 0;
        for (Order order : orders) {
            ArrayList<String> orderData = order.toStringArray(false, true, false, true,true, true, true);
            ArrayList<Object> rowData = new ArrayList<>();
            rowData.add(i++);
            rowData.addAll(orderData);
            model.addRow(rowData.toArray(new Object[0]));
        }

        dataTable.setModel(model);
        dataTable.repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrganizerAppGUI app = new OrganizerAppGUI();
            app.setVisible(true);
        });
    }
}
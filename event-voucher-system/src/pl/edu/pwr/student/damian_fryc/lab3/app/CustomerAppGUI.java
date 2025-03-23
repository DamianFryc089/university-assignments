package pl.edu.pwr.student.damian_fryc.lab3.app;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OfferDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.UsersDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.OfferDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.OrderDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.dao.db.UsersDaoDB;
import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class CustomerAppGUI extends JFrame {

    private final UsersDao usersDao = new UsersDaoDB();
    private final OfferDao offerDao = new OfferDaoDB();
    private final OrderDao orderDao = new OrderDaoDB();
    private Customer self;
	private final Font bigFont = new Font("Arial", Font.PLAIN, 30);

    public CustomerAppGUI() {
        setTitle("Customer Application");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginScreen();
    }
    private void loginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        loginPanel.setPreferredSize(new Dimension(200,50));
        JLabel idLabel = new JLabel("Enter Customer ID:", JLabel.RIGHT);
        JTextField idField = new JTextField();
        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create New Customer");

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
                int customerId = Integer.parseInt(idField.getText());
                Customer customer = usersDao.getCustomer(customerId);
                if (customer != null) {
                    self = customer;
                    mainScreen();
                } else {
                    JOptionPane.showMessageDialog(this, "Customer not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        });

        createButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            if (name != null && !name.isEmpty()) {
                self = usersDao.addCustomer(name);
                JOptionPane.showMessageDialog(this, "Customer created!");
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
        gbc.weighty = 0;
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
        ordersButton.addActionListener(e -> updateOrders((ArrayList<Order>) orderDao.getCustomerOrders(self), dataTable, optionInput));
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
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                optionInput.removeAll();
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow != -1) {
                    String offerI = dataTable.getValueAt(selectedRow, 0).toString();
                    JButton button = new JButton("Create order for selected offer");
                    button.setFont(bigFont);
                    button.addActionListener(x -> orderDao.addOrder(new Order(self, offers.get(Integer.parseInt(offerI)))));
                    optionInput.add(button);
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

                    if (Objects.equals(orders.get(Integer.parseInt(orderI)).getStatus(), "New")) {

                        JButton button = new JButton("Cancel order");
                        button.setFont(bigFont);
                        button.addActionListener(x -> {
                            orderDao.deleteOrder(orders.get(Integer.parseInt(orderI)));
                            updateOrders((ArrayList<Order>) orderDao.getCustomerOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button);

                    } else if (Objects.equals(orders.get(Integer.parseInt(orderI)).getStatus(), "Awaiting Confirmation")) {
                        JButton button = new JButton("Confirm order");
                        button.setFont(bigFont);
                        button.addActionListener(x -> {
                            orders.get(Integer.parseInt(orderI)).setStatus("Confirmed");
                            orderDao.updateOrder(orders.get(Integer.parseInt(orderI)));
                            updateOrders((ArrayList<Order>) orderDao.getCustomerOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button);
                    } else if (Objects.equals(orders.get(Integer.parseInt(orderI)).getStatus(), "Confirmed")) {

                        JButton button = new JButton("Cancel order confirmation");
                        button.setFont(bigFont);
                        button.addActionListener(x -> {
                            orders.get(Integer.parseInt(orderI)).setStatus("Awaiting Confirmation");
                            orderDao.updateOrder(orders.get(Integer.parseInt(orderI)));
                            updateOrders((ArrayList<Order>) orderDao.getCustomerOrders(self), dataTable, optionInput);
                        });
                        optionInput.add(button);
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
        model.addColumn("offer id");
        model.addColumn("offer parameters");
        model.addColumn("parameters");
        model.addColumn("status");

        int i = 0;
        for (Order order : orders) {
            ArrayList<String> orderData = order.toStringArray(false, false, false, true, true, true, true);
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
            CustomerAppGUI app = new CustomerAppGUI();
            app.setVisible(true);
        });
    }
}
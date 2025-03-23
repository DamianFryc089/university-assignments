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

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CustomerAppTUI extends AppTUI {

    private static Customer logIn(Scanner scanner, UsersDao usersDao){
        Customer self = null;
        int id;
        while (self == null) {
            System.out.println("Input customer id");
            id = getInt(scanner, 0, -1);
            self = usersDao.getCustomer(id);

            if (self == null) {
                System.out.println("No customer found\n\n0 - go back\n1 - create new customer\n2 - exit application");
                int selected = getInt(scanner, 0, 2);
                if (selected == 1) {
                    System.out.println("Input customer name: ");
                    String name = scanner.nextLine();
                    self = usersDao.addCustomer(name);
                    System.out.println("User created!");
                }
                if (selected == 2)  return null;
            }
        }
        System.out.println("Welcome " + self.getId() + ": " + self.getName());
        return self;
    }
    private static void showOffers(OfferDao offerDao) {
        ArrayList<Offer> offers = (ArrayList<Offer>) offerDao.getAllOffers();
        if (offers.isEmpty()) {
            System.out.println("None");
            return;
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        ArrayList<String> header = new ArrayList<>();
        header.add("i");
        header.add("parameters");
        tableData.add(header);

        for (Offer offer : offers) {
            tableData.add(offer.toStringArray(false, true));
        }
        System.out.println("Offers");
        printTableWithBorders(tableData);

    }
    private static void showOrders(OrderDao orderDao, Customer self) {
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getCustomerOrders(self);
        if (orders.isEmpty()) {
            System.out.println("None");
            return;
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        ArrayList<String> header = new ArrayList<>();
        header.add("i");
        header.add("offer id");
        header.add("offer parameters");
        header.add("parameters");
        header.add("status");
        tableData.add(header);

        for (Order order : orders) {
            tableData.add(order.toStringArray(false, false, false, true, true, true, true));
        }
        System.out.println("Orders");
        printTableWithBorders(tableData);

    }
    private static void selectOffer(Scanner scanner, OfferDao offerDao, OrderDao orderDao, Customer self) {
//        ArrayList<Offer> offers = showOffers(offerDao);
        ArrayList<Offer> offers = (ArrayList<Offer>) offerDao.getAllOffers();
        if (offers.isEmpty()) {
            System.out.println("No offer to select");
            return;
        }

        System.out.println("Select offer");
        int selected = getInt(scanner, 0, offers.size() - 1);

        System.out.println("Selected offer " + selected);
        System.out.println("\n0 - go back\n1 - create order");

        if (getInt(scanner, 0, 1) == 1) {
            orderDao.addOrder(new Order(self, offers.get(selected)));
            System.out.println("Order added!");
        }
    }
    private static void selectOrder(Scanner scanner, OrderDao orderDao, Customer self) {
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getCustomerOrders(self);
        if (orders.isEmpty()) {
            System.out.println("No order to select");
            return;
        }

        System.out.println("Select order");
        int selected = getInt(scanner, 0, orders.size() - 1);

        System.out.println("Selected order " + selected);

        if (Objects.equals(orders.get(selected).getStatus(), "New")) {
            System.out.println("\n0 - go back\n1 - cancel order");
            if (getInt(scanner, 0, 1) == 1) {
                orderDao.deleteOrder(orders.get(selected));
                System.out.println("Order canceled!");
            }
        } else if (Objects.equals(orders.get(selected).getStatus(), "Awaiting Confirmation")) {
            System.out.println("\n0 - go back\n1 - confirm order");
            if (getInt(scanner, 0, 1) == 1) {
                orders.get(selected).setStatus("Confirmed");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order confirmed!");
            }
        } else if (Objects.equals(orders.get(selected).getStatus(), "Confirmed")) {
            System.out.println("\n0 - go back\n1 - cancel order confirmation");
            if (getInt(scanner, 0, 1) == 1) {
                orders.get(selected).setStatus("Awaiting Confirmation");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Canceled order confirmation!");
            }
        } else System.out.println("No action to perform");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UsersDao usersDao = new UsersDaoDB();
//        UsersDaoMock usersDaoMock = new UsersDaoMock();

        Customer self = logIn(scanner, usersDao);
        if (self == null) return;

//        OfferDao offerDao = new OfferDaoMock();
//        OrderDao orderDao = new OrderDaoMock();
        OfferDao offerDao = new OfferDaoDB();
        OrderDao orderDao = new OrderDaoDB();

        int selected;
        while (true) {
            System.out.println("\n0 - exit\n1 - offers\n2 - orders");
            selected = getInt(scanner, 0, 2);

            switch (selected) {
                case 0: return;
                case 1:
                    showOffers(offerDao);
                    System.out.println("\n0 - go back\n1 - select offer");
                    if (getInt(scanner, 0, 1) == 1)
                        selectOffer(scanner, offerDao, orderDao, self);
                    break;
                case 2:
                    showOrders(orderDao, self);
                    System.out.println("\n0 - go back\n1 - select order");
                    if (getInt(scanner, 0, 1) == 1)
                        selectOrder(scanner, orderDao, self);
                    break;
                default: break;
            }
        }
    }


}

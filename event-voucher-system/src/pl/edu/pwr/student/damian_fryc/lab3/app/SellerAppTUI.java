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
import pl.edu.pwr.student.damian_fryc.lab3.model.Seller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class SellerAppTUI extends AppTUI{

    private static Seller logIn(Scanner scanner, UsersDao usersDao){
        Seller self = null;
        int id;
        while (self == null) {
            System.out.println("Input seller id");
            id = getInt(scanner, 0, -1);
            self = usersDao.getSeller(id);

            if (self == null) {
                System.out.println("No seller found\n\n0 - go back\n1 - create new seller\n2 - exit application");
                int selected = getInt(scanner, 0, 2);
                if (selected == 1) {
                    System.out.println("Input seller name: ");
                    String name = scanner.nextLine();
                    self = usersDao.addSeller(name);
                    System.out.println("Seller created!");
                }
                if (selected == 2)  return null;
            }
        }
        System.out.println("Welcome " + self.getId() + ": " + self.getName());
        return self;
    }
    private static void showOffers(OfferDao offerDao){
        ArrayList<Offer> offers = (ArrayList<Offer>) offerDao.getAllOffers();
        if (offers.isEmpty()) {
            System.out.println("None");
            return;
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        ArrayList<String> header = new ArrayList<>();
        header.add("i"); header.add("parameters");
        tableData.add(header);

        for (Offer offer : offers) {
            tableData.add(offer.toStringArray(false,true));
        }
        System.out.println("Offers");
        printTableWithBorders(tableData);
    }
    private static void showOrders(OrderDao orderDao){
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("None");
            return;
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        ArrayList<String> header = new ArrayList<>();
        header.add("i"); header.add("customer id"); header.add("organizer id"); header.add("offer id"); header.add("offer parameters"); header.add("parameters"); header.add("status");
        tableData.add(header);

        System.out.println("Orders");
        for (Order order : orders) {
            tableData.add(order.toStringArray(false, true, true, true,true, true, true));
        }
        printTableWithBorders(tableData);

    }
    private static void selectOffer(Scanner scanner, OfferDao offerDao, OrderDao orderDao) {
//        ArrayList<Offer> offers = showOffers(offerDao);
        ArrayList<Offer> offers = (ArrayList<Offer>) offerDao.getAllOffers();
        if (offers.isEmpty()) {
            System.out.println("No offer to select");
            return;
        }

        System.out.println("Select offer");
        int selected = getInt(scanner, 0, offers.size()-1);

        System.out.println("Selected offer " + selected);
        System.out.println("\n0 - go back\n1 - delete offer\n2 - edit parameters");
        int input = getInt(scanner, 0, 2);
        if (input == 1) {
            offerDao.deleteOffer(offers.get(selected), orderDao);
            System.out.println("Offer deleted!");
        }
        else if(input == 2){
            System.out.println("Input new parameters");
            String parameters = scanner.nextLine();
            offers.get(selected).setParameters(parameters);
            offerDao.updateOffer(offers.get(selected), orderDao);
            System.out.println("Offer updated!");
        }
    }
    private static void selectOrder(Scanner scanner, OrderDao orderDao) {
//        ArrayList<Order> orders = showOrders(orderDao);
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No order to select");
            return;
        }

        System.out.println("Select order");
        int selected = getInt(scanner, 0, orders.size()-1);

        System.out.println("Selected order " + selected);

        if (Objects.equals(orders.get(selected).getStatus(), "New")){
            System.out.println("\n0 - go back\n1 - approve order\n2 - delete order");
            int input = getInt(scanner, 0, 2);
            if (input == 1) {
                orders.get(selected).setStatus("Approved");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order approved!");
            }
            if (input == 2) {
                orderDao.deleteOrder(orders.get(selected));
                System.out.println("Order deleted!");
            }
        }
        else if(Objects.equals(orders.get(selected).getStatus(), "Approved")){
            System.out.println("\n0 - go back\n1 - disapprove order\n2 - delete order");
            int input = getInt(scanner, 0, 2);
            if (input == 1) {
                orders.get(selected).setStatus("New");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order disapproved!");
            }
            if (input == 2) {
                orderDao.deleteOrder(orders.get(selected));
                System.out.println("Order deleted!");
            }
        }
        else {
            System.out.println("\n0 - go back\n1 - delete order");
            int input = getInt(scanner, 0, 1);
            if (input == 1) {
                orderDao.deleteOrder(orders.get(selected));
                System.out.println("Order deleted!");
            }
        }
    }
    private static void addOffer(Scanner scanner, OfferDao offerDao) {
        System.out.println("Input parameters");
        String offerParameters = scanner.nextLine();
        offerDao.addOffer(new Offer(0, offerParameters));
        System.out.println("Offer added!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UsersDao usersDao = new UsersDaoDB();
//        UsersDaoMock usersDaoMock = new UsersDaoMock();

        Seller self = logIn(scanner, usersDao);
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
                    System.out.println("\n0 - go back\n1 - select offer\n2 - add offer");
                    selected = getInt(scanner, 0, 2);
                    if (selected == 1) selectOffer(scanner, offerDao,orderDao);
                    if (selected == 2) addOffer(scanner, offerDao);
                    break;
                case 2:
                    showOrders(orderDao);
                    System.out.println("\n0 - go back\n1 - select order");
                    if (getInt(scanner, 0, 1) == 1) selectOrder(scanner, orderDao);
                    break;
                default: break;
            }
        }
    }


}

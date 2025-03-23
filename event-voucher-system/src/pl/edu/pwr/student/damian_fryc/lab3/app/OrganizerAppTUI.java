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

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class OrganizerAppTUI extends AppTUI{
    private static Organizer logIn(Scanner scanner, UsersDao usersDao){
        Organizer self = null;
        int id;
        while (self == null) {
            System.out.println("Input organizer id");
            id = getInt(scanner, 0, -1);
            self = usersDao.getOrganizer(id);

            if (self == null) {
                System.out.println("No organizer found\n\n0 - go back\n1 - create new organizer\n2 - exit application");
                int selected = getInt(scanner, 0, 2);
                if (selected == 1) {
                    System.out.println("Input organizer name: ");
                    String name = scanner.nextLine();
                    self = usersDao.addOrganizer(name);
                    System.out.println("Organizer created!");
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
    private static void showOrders(OrderDao orderDao, Organizer self){
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getAllOrders(self);
        if (orders.isEmpty()) {
            System.out.println("None");
            return;
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        ArrayList<String> header = new ArrayList<>();
        header.add("i"); header.add("customer id"); header.add("offer id"); header.add("offer parameters"); header.add("parameters"); header.add("status");
        tableData.add(header);

        for (Order order : orders) {
            tableData.add(order.toStringArray(false, true, false, true,true, true, true));
        }
        System.out.println("Orders");
        printTableWithBorders(tableData);
    }
    private static void selectOrder(Scanner scanner, OrderDao orderDao, Organizer self) {
//        ArrayList<Order> orders = showOrders(orderDao);
        ArrayList<Order> orders = (ArrayList<Order>) orderDao.getAllOrders(self);
        if (orders.isEmpty()) {
            System.out.println("No order to select");
            return;
        }

        System.out.println("Select order");
        int selected = getInt(scanner, 0, orders.size()-1);

        System.out.println("Selected order " + selected);

        if (Objects.equals(orders.get(selected).getStatus(), "Approved")){
            System.out.println("\n0 - go back\n1 - declare order execution");
            if (getInt(scanner, 0, 1) == 1) {
                orders.get(selected).setStatus("In Progress");
                orders.get(selected).setOrganizerId(self.getId());
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Declared order execution!");
            }
        }
        else if(Objects.equals(orders.get(selected).getStatus(), "In Progress") && orders.get(selected).getOrganizerId() == self.getId()){
            System.out.println("\n0 - go back\n1 - withdraw declaration of order execution\n2 - update order and open customer registration");
            int input = getInt(scanner, 0, 2);
            if (input == 1) {
                orders.get(selected).setStatus("Approved");
                orders.get(selected).setOrganizerId(-1);
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order execution withdrawn!");
            } else if (input == 2) {
                orders.get(selected).setStatus("Awaiting Confirmation");
                System.out.println("Input order parameters!");
                String parameters = scanner.nextLine();
                orders.get(selected).setOrderParameters(parameters);
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order updated!");
            }
        }
        else if(Objects.equals(orders.get(selected).getStatus(), "Awaiting Confirmation") && orders.get(selected).getOrganizerId() == self.getId()){
            System.out.println("\n0 - go back\n1 - close customer registration and clear order parameters \n2 - update order");
            int input = getInt(scanner, 0, 2);
            if (input == 1) {
                orders.get(selected).setStatus("In Progress");
                orders.get(selected).setOrderParameters("");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order parameters cleared and customer registration closed!");
            } else if (input == 2) {
                System.out.println("Input order parameters");
                String parameters = scanner.nextLine();
                orders.get(selected).setOrderParameters(parameters);
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Order updated!");
            }
        }
        else if(Objects.equals(orders.get(selected).getStatus(), "Confirmed") && orders.get(selected).getOrganizerId() == self.getId()){
            System.out.println("\n0 - go back\n1 - mark order as Completed \n2 - mark order as Did Not Attend");
            int input = getInt(scanner, 0, 2);
            if (input == 1) {
                orders.get(selected).setStatus("Completed");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Status updated");
            } else if (input == 2) {
                orders.get(selected).setStatus("Did Not Attend");
                orderDao.updateOrder(orders.get(selected));
                System.out.println("Status updated");
            }
        }
        else System.out.println("No action to perform");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UsersDao usersDao = new UsersDaoDB();
//        UsersDaoMock usersDaoMock = new UsersDaoMock();

        Organizer self = logIn(scanner, usersDao);
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
                    break;
                case 2:
                    showOrders(orderDao, self);
                    System.out.println("\n0 - go back\n1 - select order");
                    if (getInt(scanner, 0, 1) == 1) selectOrder(scanner, orderDao, self);
                    break;
                default: break;
            }
        }
    }


}

package pl.edu.pwr.student.damian_fryc.lab3.dao.mock;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMock implements OrderDao {
    @Override
    public void addOrder(Order order) {
        System.out.println("added order");
    }

    @Override
    public List<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(1,1,1,1,"asd","STATUS1"));
        orders.add(new Order(2,2,1,2,"asd","STATUS2"));
        orders.add(new Order(3,1,1,3,"asd","STATUS3"));
        return orders;
    }

    @Override
    public List<Order> getAllOrders(Organizer organizer) {
        return null;
    }

    @Override
    public List<Order> getCustomerOrders(Customer customer) {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(1,1,1,1,"asd","STATUS1"));
        orders.add(new Order(2,1,1,2,"asd","STATUS2"));
        return orders;
    }

    @Override
    public Order getOrder(int orderId) {
        return new Order(orderId,1,1,1,"asd","STATUS1");
    }

    @Override
    public void updateOrder(Order order) {
        System.out.println("updated order");
    }

    @Override
    public void updateOrders(int offerId, String offerParameters) {

    }

    @Override
    public void deleteOrder(Order order) {
        System.out.println("deleted order");
    }

    @Override
    public void deleteOrders(int offerId) {
        System.out.println("deleted orders");
    }
}

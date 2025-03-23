package pl.edu.pwr.student.damian_fryc.lab3.dao;

import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;

import java.util.List;

public interface OrderDao {
    void addOrder(Order order);
    List<Order> getAllOrders();
    List<Order> getAllOrders(Organizer organizer);
    List<Order> getCustomerOrders(Customer customer);
    Order getOrder(int orderId);
    void updateOrder(Order order);
    void updateOrders(int offerId, String offerParameters);
    void deleteOrder(Order order);
    void deleteOrders(int offerId);
}

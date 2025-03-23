package pl.edu.pwr.student.damian_fryc.lab3.dao.db;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoDB extends DaoDB implements OrderDao {

    @Override
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, organizer_id, offer_id, order_parameters, status, offer_parameters) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getOrganizerId());
            stmt.setInt(3, order.getOfferId());
            stmt.setString(4, order.getOrderParameters());
            stmt.setString(5, order.getStatus());
            stmt.setString(6, order.getOfferParameters());
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrganizerId(rs.getInt("organizer_id"));
                order.setOfferId(rs.getInt("offer_id"));
                order.setOfferParameters(rs.getString("offer_parameters"));
                order.setOrderParameters(rs.getString("order_parameters"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
    public List<Order> getAllOrders(Organizer organizer) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE organizer_id = ? OR organizer_id = -1";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, organizer.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrganizerId(rs.getInt("organizer_id"));
                order.setOfferId(rs.getInt("offer_id"));
                order.setOfferParameters(rs.getString("offer_parameters"));
                order.setOrderParameters(rs.getString("order_parameters"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getCustomerOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customer.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrganizerId(rs.getInt("organizer_id"));
                order.setOfferId(rs.getInt("offer_id"));
                order.setOfferParameters(rs.getString("offer_parameters"));
                order.setOrderParameters(rs.getString("order_parameters"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public Order getOrder(int orderId) {
        Order order = null;
        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("client_id"));
                order.setOrganizerId(rs.getInt("organizer_id"));
                order.setOfferId(rs.getInt("offer_id"));
                order.setOfferParameters(rs.getString("offer_parameters"));
                order.setOrderParameters(rs.getString("order_parameters"));
                order.setStatus(rs.getString("status"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET customer_id = ?, organizer_id = ?, offer_id = ?, order_parameters = ?, status = ?, offer_parameters = ? WHERE id = ?";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getOrganizerId());
            stmt.setInt(3, order.getOfferId());
            stmt.setString(4, order.getOrderParameters());
            stmt.setString(5, order.getStatus());
            stmt.setString(6, order.getOfferParameters());
            stmt.setInt(7, order.getId());
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOrders(int offerId, String offerParameters) {
        String sql = "UPDATE orders SET offer_parameters = ? WHERE offer_id = ? AND status = 'New'";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, offerParameters);
            stmt.setInt(2, offerId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(Order order) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrders(int offerId) {
        String sql = "DELETE FROM orders WHERE offer_id = ? AND status = 'New'";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, offerId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

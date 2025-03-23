package pl.edu.pwr.student.damian_fryc.lab3.dao.db;

import pl.edu.pwr.student.damian_fryc.lab3.dao.UsersDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Seller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDaoDB extends DaoDB implements UsersDao {
    private ResultSet getUserInfo(String tableName, int userId){

        ResultSet rs = null;
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

//            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
    @Override
    public Customer addCustomer(String name) {
        String sql = "INSERT INTO customers (name) VALUES (?)";
        Customer newCustomer = null;
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    newCustomer = new Customer(generatedKeys.getInt(1), name);
                generatedKeys.close();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newCustomer;
    }

    @Override
    public Seller addSeller(String name) {
        String sql = "INSERT INTO sellers (name) VALUES (?)";
        Seller newSeller = null;
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    newSeller = new Seller(generatedKeys.getInt(1), name);
                generatedKeys.close();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newSeller;
    }

    @Override
    public Organizer addOrganizer(String name) {
        String sql = "INSERT INTO organizers (name) VALUES (?)";
        Organizer newOrganizer = null;
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    newOrganizer = new Organizer(generatedKeys.getInt(1), name);
                generatedKeys.close();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newOrganizer;
    }

    @Override
    public Customer getCustomer(int id) {
        Customer customer = null;
        ResultSet resultSet = getUserInfo("customers", id);
        try {
            if (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"), resultSet.getString("name"));
                resultSet.close();
            }
        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public Seller getSeller(int id) {
        Seller seller = null;
        ResultSet resultSet = getUserInfo("sellers", id);
        try {
            if (resultSet.next()) {
                seller = new Seller(resultSet.getInt("id"), resultSet.getString("name"));
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return seller;

    }

    @Override
    public Organizer getOrganizer(int id) {
        Organizer organizer = null;
        ResultSet resultSet = getUserInfo("organizers", id);
        try {
            if (resultSet.next()) {
                organizer = new Organizer(resultSet.getInt("id"), resultSet.getString("name"));
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return organizer;

    }
}

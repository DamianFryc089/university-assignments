package pl.edu.pwr.student.damian_fryc.lab3.dao.db;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OfferDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDaoDB extends DaoDB implements OfferDao {

    @Override
    public void addOffer(Offer offer) {
        String sql = "INSERT INTO offers (parameters) VALUES (?);";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, offer.getParameters());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM offers";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Offer offer = new Offer(rs.getInt("id"), rs.getString("parameters"));
                offers.add(offer);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }

    @Override
    public Offer getOffer(int offerID) {
        Offer offer = null;
        String sql = "SELECT * FROM offers WHERE id = ?";

        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, offerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                offer = new Offer(rs.getInt("id"),rs.getString("parameters"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offer;
    }

    @Override
    public void updateOffer(Offer offer, OrderDao orderDao) {
        String sql = "UPDATE offers SET parameters = ? WHERE id = ?";
//        orderDao.updateOrders(offer.getId(), offer.getParameters());
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, offer.getParameters());
            stmt.setInt(2, offer.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteOffer(Offer offer, OrderDao orderDao) {
//        orderDao.deleteOrders(offer.getId());
        String sql = "DELETE FROM offers WHERE id=?";
        try {
            connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, offer.getId());
            stmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

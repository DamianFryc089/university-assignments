package pl.edu.pwr.student.damian_fryc.lab3.dao;

import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;

import java.util.List;

public interface OfferDao {
    void addOffer(Offer offer);
    List<Offer> getAllOffers();
    Offer getOffer(int offerID);
    void updateOffer(Offer offer, OrderDao orderDao);
    void deleteOffer(Offer offer, OrderDao orderDao);
}

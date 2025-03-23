package pl.edu.pwr.student.damian_fryc.lab3.dao.mock;

import pl.edu.pwr.student.damian_fryc.lab3.dao.OfferDao;
import pl.edu.pwr.student.damian_fryc.lab3.dao.OrderDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class OfferDaoMock implements OfferDao {
    @Override
    public void addOffer(Offer offer) {
        System.out.println("added offer");
    }

    @Override
    public List<Offer> getAllOffers() {
        ArrayList<Offer> offers = new ArrayList<>();
        offers.add(new Offer(1,"aaa"));
        offers.add(new Offer(2,"bbb"));
        offers.add(new Offer(3,"ccc"));
        return offers;
    }

    @Override
    public Offer getOffer(int offerID) {
        return new Offer(offerID,"aaa");
    }

    @Override
    public void updateOffer(Offer offer, OrderDao orderDao) {
        System.out.println("updated offer");
    }

    @Override
    public void deleteOffer(Offer offer, OrderDao orderDao) {
        System.out.println("deleted offer");
    }
}

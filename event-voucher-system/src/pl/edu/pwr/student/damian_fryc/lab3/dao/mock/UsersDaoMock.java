package pl.edu.pwr.student.damian_fryc.lab3.dao.mock;

import pl.edu.pwr.student.damian_fryc.lab3.dao.UsersDao;
import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Seller;

public class UsersDaoMock implements UsersDao {
    @Override
    public Customer addCustomer(String name) {
        System.out.println("added customer");
        return new Customer(0, "TEST Customer");
    }

    @Override
    public Seller addSeller(String name) {
        System.out.println("added seller");
        return new Seller(0, "TEST Seller");
    }

    @Override
    public Organizer addOrganizer(String name) {
        System.out.println("added organizer");
        return new Organizer(0, "TEST Organizer");
    }

    @Override
    public Customer getCustomer(int id) {
        return new Customer(1,"TEST Customer");
    }

    @Override
    public Seller getSeller(int id) {
        return new Seller(1,"TEST Seller");
    }

    @Override
    public Organizer getOrganizer(int id) {
        return new Organizer(1,"TEST Organizer");
    }
}

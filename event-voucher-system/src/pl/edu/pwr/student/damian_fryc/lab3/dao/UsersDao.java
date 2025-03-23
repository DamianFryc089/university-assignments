package pl.edu.pwr.student.damian_fryc.lab3.dao;

import pl.edu.pwr.student.damian_fryc.lab3.model.Customer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Order;
import pl.edu.pwr.student.damian_fryc.lab3.model.Organizer;
import pl.edu.pwr.student.damian_fryc.lab3.model.Seller;

import java.util.List;

public interface UsersDao {
    Customer addCustomer(String name);
    Seller addSeller(String name);
    Organizer addOrganizer(String name);

    Customer getCustomer(int id);
    Seller getSeller(int id);
    Organizer getOrganizer(int id);
}

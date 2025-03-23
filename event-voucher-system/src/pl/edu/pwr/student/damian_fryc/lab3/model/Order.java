package pl.edu.pwr.student.damian_fryc.lab3.model;

import java.util.ArrayList;

public class Order {
    private int id = -1;
    private int customerId = -1;
    private int organizerId = -1;
    private int offerId = -1;
    private String offerParameters = " ";
    private String orderParameters = " ";
    private String status = " ";

    public Order(int id, int customerID, int organizerId, int offerID, String offerParameters, String status) {
        this.id = id;
        this.customerId = customerID;
        this.organizerId = organizerId;
        this.offerId = offerID;
        this.offerParameters = offerParameters;
        this.status = status;
    }
    public Order(Customer customer, Offer offer){
        this.customerId = customer.getId();
        this.offerId = offer.getId();
        this.offerParameters = offer.getParameters();
        this.status = "New";
    }
    public Order(){}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public int getOrganizerId() {
        return organizerId;
    }
    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public int getOfferId() {
        return offerId;
    }
    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderParameters() {
        return orderParameters;
    }
    public void setOrderParameters(String orderParameters) {
        this.orderParameters = orderParameters;
    }

    public String getOfferParameters() {
        return offerParameters;
    }
    public void setOfferParameters(String offerParameters) {
        this.offerParameters = offerParameters;
    }

    public ArrayList<String> toStringArray(boolean includeId, boolean includeCustomerId, boolean includeOrganizerId, boolean includeOfferId, boolean includeOfferParameters, boolean includeOrderParameters, boolean includeStatus) {
        ArrayList<String> attributes = new ArrayList<>();

        if (includeId)              attributes.add(String.valueOf(id));
        if (includeCustomerId)      attributes.add(String.valueOf(customerId));
        if (includeOrganizerId)     attributes.add(String.valueOf(organizerId));
        if (includeOfferId)         attributes.add(String.valueOf(offerId));
        if (includeOfferParameters) attributes.add(offerParameters);
        if (includeOrderParameters) attributes.add(orderParameters);
        if (includeStatus)          attributes.add(status);

        return attributes;
    }
}

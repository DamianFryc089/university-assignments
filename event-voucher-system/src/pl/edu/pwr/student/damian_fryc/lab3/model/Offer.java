package pl.edu.pwr.student.damian_fryc.lab3.model;

import java.util.ArrayList;

public class Offer {
    private int id = -1;
    private String parameters = " ";

    public Offer(int id, String parameters) {
        this.id = id;
        this.parameters = parameters;
    }
    public Offer(){}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getParameters() {
        return parameters;
    }
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public ArrayList<String> toStringArray(boolean includeId, boolean includeParameters) {
        ArrayList<String> attributes = new ArrayList<>();

        if (includeId)          attributes.add(String.valueOf(id));
        if (includeParameters)  attributes.add(parameters);

        return attributes;
    }
}

package pl.edu.pwr.student.damian_fryc.lab3.model;

public class Seller {
    private int id = -1;
    private String name = "";
    public Seller(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Seller(){}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}

package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.rmi.Remote;

public class Socket extends Circle {

    public enum Mode{
        INPUT, OUTPUT, CONTROL
    }

    private final BlockModule blockModule;
    private final UpdateListener mainApp;

    private final Mode mode;
    private Connection connection = null;
    public Socket(BlockModule blockModule, UpdateListener mainApp, Mode mode){
        super(5);
        getStyleClass().add(mode+"-socket");

        this.blockModule = blockModule;
        this.mainApp = mainApp;
        this.mode = mode;

        this.setOnMouseClicked(this::socketClicked);
    }

    private void socketClicked(MouseEvent event){
        if(connection != null) connection.delete();
        else {
            boolean selected = mainApp.objectSelected(this);

            if(selected) setClicked();
            else setUnclicked();
        }
    }
    public void setClicked(){
        getStyleClass().set(0, mode+"-socket-clicked");
    }
    public void setUnclicked(){
        getStyleClass().set(0, mode+"-socket");
    }
    public Remote getRemote() {
        return blockModule.getRemote();
    }
    public Mode getMode() {
        return mode;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void removeConnection() {
        connection = null;
    }
    public Remote getConnectedRemote() {
        if (connection == null) return null;
        return connection.getSecondRemote(this);
    }
    public void updateConnectionPosition() {
        if (connection != null) connection.updatePosition();
    }
    public double getX(){
        return blockModule.getTranslateX() + getLayoutX() + getParent().getLayoutX() - getRadius()/2;
    }
    public double getY(){
        return blockModule.getTranslateY() + getLayoutY() + getParent().getLayoutY() - getRadius()/2;
    }

}

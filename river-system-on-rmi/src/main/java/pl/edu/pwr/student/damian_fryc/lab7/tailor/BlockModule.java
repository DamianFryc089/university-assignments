package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import interfaces.IControlCenter;
import interfaces.IEnvironment;
import interfaces.IRetensionBasin;
import interfaces.IRiverSection;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.rmi.Remote;
import java.util.ArrayList;

public class BlockModule extends Pane {
    private final Remote remote;
    private final String name;
    private final UpdateListener mainApp;

    Text nameText = new Text();

    private final HBox inputBoxes = new HBox(5);
    private final HBox outputBoxes = new HBox(5);
    private final int maxInputs;
    private final int maxOutputs;
    private final int maxControl;
    private final ArrayList<Socket> inputSockets = new ArrayList<>();
    private final ArrayList<Socket> outputSockets = new ArrayList<>();
    private final ArrayList<Socket> controlSockets = new ArrayList<>();


    private double dragStartX, dragStartY;

    public BlockModule(Remote remote, String name, UpdateListener mainApp){
        this.remote = remote;
        this.name = name;
        this.mainApp = mainApp;

        switch (remote){
            case IControlCenter ignored -> {
                maxInputs = 10;
                maxOutputs = 0;
                maxControl = 0;
                setStyle("-fx-background-color: rgba(255,200,200,0.75);");
            }
            case IEnvironment ignored -> {
                maxInputs = 10;
                maxOutputs = 0;
                maxControl = 0;
                setStyle("-fx-background-color: rgba(200,200,255,0.75);");
            }
            case IRetensionBasin ignored -> {
                maxInputs = 5;
                maxOutputs = 1;
                maxControl = 1;
                setStyle("-fx-background-color: rgba(255,220,220,0.75);");

            }
            case IRiverSection ignored -> {
                maxInputs = 1;
                maxOutputs = 1;
                maxControl = 1;
                setStyle("-fx-background-color: rgba(220,220,255,0.75);");
            }
            default -> {
                maxInputs = 0;
                maxOutputs = 0;
                maxControl = 0;
                setStyle("-fx-background-color: rgba(255,255,255,0.75);");
            }
        }

        buildBlock();
    }

    private void buildBlock(){
        getStyleClass().add("block-module");

        nameText.setText(name);
        nameText.setFill(Color.DARKBLUE);
        nameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        inputBoxes.setAlignment(Pos.CENTER);
        for (int i = 0; i < maxInputs; i++) {
            createNewInput();
        }
        inputBoxes.getChildren().addAll(inputSockets);

        outputBoxes.setAlignment(Pos.CENTER);
        for (int i = 0; i < maxOutputs; i++) {
            createNewOutput();
        }

        HBox middleBox = new HBox(5, nameText);
        middleBox.setAlignment(Pos.CENTER);
        if (maxControl > 0) {
            Socket controlSocket = new Socket(this, mainApp, Socket.Mode.CONTROL);
            controlSockets.add(controlSocket);
            middleBox.getChildren().add(controlSocket);
        }

        VBox vbox = new VBox(15, inputBoxes, middleBox, outputBoxes);
        vbox.setAlignment(Pos.CENTER);

        vbox.getStyleClass().add("block-inside");

        getChildren().add(vbox);
        setOnMousePressed(this::onBlockPressed);
        setOnMouseDragged(this::onBlockDragged);

    }

    private void createNewInput(){
        Socket newSocket = new Socket(this, mainApp, Socket.Mode.INPUT);
        inputSockets.add(newSocket);

    }
    private void createNewOutput(){
        Socket newSocket = new Socket(this, mainApp, Socket.Mode.OUTPUT);
        outputSockets.add(newSocket);
        outputBoxes.getChildren().add(newSocket);
    }
    private void onBlockPressed(MouseEvent event) {
        dragStartX = event.getSceneX() - getTranslateX();
        dragStartY = event.getSceneY() - getTranslateY();
    }

    private void onBlockDragged(MouseEvent event) {
        setTranslateX(event.getSceneX() - dragStartX);
        setTranslateY(event.getSceneY() - dragStartY);

        for(Socket socket : inputSockets){
            socket.updateConnectionPosition();
        }
        for(Socket socket : outputSockets){
            socket.updateConnectionPosition();
        }
        for(Socket socket : controlSockets){
            socket.updateConnectionPosition();
        }
    }
    public ArrayList<Remote> getConnectedInputs(){
        ArrayList<Remote> remotes = new ArrayList<>();
        for(Socket inputSocket : inputSockets){
            Remote remote = inputSocket.getConnectedRemote();
            if (remote != null) remotes.add(remote);
        }
        return remotes;
    }

    public Remote getRemote() {
        return remote;
    }
    public void remove(){}

    public Pane getNode() {
        return this;
    }
}

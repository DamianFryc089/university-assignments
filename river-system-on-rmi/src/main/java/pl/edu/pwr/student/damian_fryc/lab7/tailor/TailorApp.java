package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import interfaces.IControlCenter;
import interfaces.IEnvironment;
import interfaces.IRetensionBasin;
import interfaces.IRiverSection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class TailorApp extends Application implements UpdateListener{

    private Tailor tailor;
    private final StackPane mainPane = new StackPane();
    private final ArrayList<BlockModule> blockModules = new ArrayList<>();
    private Socket selectedSocket = null;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(mainPane, 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        tailorInput(stage);
        stage.setTitle("Tailor");
        stage.setScene(scene);
        stage.show();
    }

    private void tailorInput(Stage stage){
        mainPane.getChildren().clear();

        Text text = new Text("Tailor");

        TextField nameInput = new TextField("Tailor");
        HBox nameBox = new HBox(new Text("     Name: "), nameInput);
        nameBox.alignmentProperty().set(Pos.CENTER);

        TextField portInput = new TextField("2000");
        portInput.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getText();
            if (newText.matches("\\d*"))
                return change;
            return null;
        }));
        HBox portBox = new HBox(new Text("     Port: "), portInput);
        portBox.alignmentProperty().set(Pos.CENTER);

        Button acceptButton = new Button("Set");
        acceptButton.setOnAction(event -> {
            try {
                tailor = new Tailor(Integer.parseInt(portInput.getText()), nameInput.getText(), this);
                stage.setOnCloseRequest(event2 -> {
                    try {
                        tailor.closeServer();
                    } catch (NoSuchObjectException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("Tailor - " + nameInput.getText() + ":" + Integer.parseInt(portInput.getText()));
            connectingScreen();
        });
        VBox vbox = new VBox(5, text, nameBox, portBox, acceptButton);
        vbox.alignmentProperty().set(Pos.CENTER);
        mainPane.getChildren().add(vbox);
    }

    private void connectingScreen(){
        mainPane.getChildren().clear();
        mainPane.alignmentProperty().set(Pos.TOP_LEFT);
        Button updateButton = new Button("UPDATE CONNECTIONS");
        updateButton.setTranslateX(230);
        updateButton.setTranslateY(30);
        updateButton.idProperty().set("update-button");
        updateButton.setOnAction(event -> {
            try {
                updateAll();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        mainPane.getChildren().add(updateButton);
    }
    private void updateAll() throws RemoteException {
        for (BlockModule blockModule : blockModules){
            for (Remote remote : blockModule.getConnectedInputs()){
                System.out.println(remote);
            }
            switch (blockModule.getRemote()){
                case IControlCenter cc -> {
                    for (Remote iRetentionBasin : blockModule.getConnectedInputs()){
                        tailor.assignBasinToCenter((IRetensionBasin) iRetentionBasin, cc);
                    }
                }
                case IEnvironment e -> {
                    for (Remote iRiverSection : blockModule.getConnectedInputs()){
                        tailor.assignRiverToEnvironment((IRiverSection) iRiverSection, e);
                    }
                }
                case IRetensionBasin rb -> {
                    for (Remote iRiverSection : blockModule.getConnectedInputs()){
                        tailor.assignBasinToRiver(rb, (IRiverSection) iRiverSection);
                    }
                }
                case IRiverSection rs -> {
                    for (Remote iRetentionBasin : blockModule.getConnectedInputs()){
                        tailor.assignRiverToBasin(rs, (IRetensionBasin) iRetentionBasin);
                    }
                }
                default -> {}
            }
        }
    }

    @Override
    public void elementAdded(Remote r, String name) {
        Platform.runLater(() -> {
            BlockModule blockModule = new BlockModule(r, name, this);
            blockModules.add(blockModule);
            mainPane.getChildren().add(blockModule.getNode());
        });
    }
    @Override
    public void elementRemoved(Remote r, String name) {
        BlockModule blockModuleToRemove = null;
        for( BlockModule blockModule : blockModules){
            if(blockModule.getRemote() == r){
                blockModuleToRemove = blockModule;
                break;
            }
        }
        if(blockModuleToRemove != null) {
            blockModuleToRemove.remove();
            blockModules.remove(blockModuleToRemove);
            mainPane.getChildren().remove(blockModuleToRemove);
        }
    }
    @Override
    public boolean objectSelected(Socket socket) {
        if (selectedSocket == null) {
            selectedSocket = socket;
            System.out.println("Selected socket");
            return true;
        }
        else if(selectedSocket == socket){
            selectedSocket = null;
            System.out.println("Deselected socked");
        }
        else {
            Socket inputSocket;
            Socket secondarySocket;
            if (selectedSocket.getMode() == Socket.Mode.INPUT && socket.getMode() != Socket.Mode.INPUT) {
                inputSocket = selectedSocket;
                secondarySocket = socket;
            }
            else if (socket.getMode() == Socket.Mode.INPUT  && selectedSocket.getMode() != Socket.Mode.INPUT) {
                inputSocket = socket;
                secondarySocket = selectedSocket;
            }
            else{
                System.out.println("Wrong connection");
                return false;
            }

            Connection newConnection = null;
            //Basin to Center
            if(inputSocket.getRemote() instanceof IControlCenter && secondarySocket.getRemote() instanceof IRetensionBasin && secondarySocket.getMode() == Socket.Mode.CONTROL)
                newConnection = new Connection(selectedSocket, socket);

            //River to Environment
            if(inputSocket.getRemote() instanceof IEnvironment && secondarySocket.getRemote() instanceof IRiverSection && secondarySocket.getMode() == Socket.Mode.CONTROL)
                newConnection = new Connection(selectedSocket, socket);

            //River to Basin
            if(inputSocket.getRemote() instanceof IRetensionBasin && secondarySocket.getRemote() instanceof IRiverSection && secondarySocket.getMode() == Socket.Mode.OUTPUT)
                newConnection = new Connection(selectedSocket, socket);

            //Basin to River
            if(inputSocket.getRemote() instanceof IRiverSection && secondarySocket.getRemote() instanceof IRetensionBasin && secondarySocket.getMode() == Socket.Mode.OUTPUT)
                newConnection = new Connection(selectedSocket, socket);

            if(newConnection != null) {
                newConnection.updatePosition();
                mainPane.getChildren().add(newConnection);
                System.out.println("New connection created");
                selectedSocket = null;
            }
            else {
                System.out.println("Wrong connection");
            }
        }
        return false;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
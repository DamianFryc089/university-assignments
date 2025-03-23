package pl.edu.pwr.student.damian_fryc.lab5;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import pl.edu.pwr.student.damian_fryc.lab5.logic.*;
import pl.edu.pwr.student.damian_fryc.lab5.view.*;

import java.util.ArrayList;

public class SimulationController implements CarEndListener{
    private final Pane drawArea;
    private final Text carAmountText;

    private final ArrayList<CarQueue> carQueues = new ArrayList<>();
    private final ArrayList<WashBay> washBays = new ArrayList<>();
    private final ArrayList<Car> cars = new ArrayList<>();
    private Controller controller;

    private double carSpeedScale;
    private double controllerSpeedScale;
    private double washerSpeedScale;
    private boolean carLoop = true;

    public SimulationController(Pane drawArea, Text carAmount) {
        this(drawArea, carAmount, 2, 2, 1, 1, 1);
    }
    public SimulationController(Pane drawArea, Text carAmountText, int queuesCount, int washBaysCount, double carSpeedScale, double controllerSpeedScale, double washerSpeedScale) {
        CarQueue.CAPACITY = 10;
        this.carAmountText = carAmountText;
        this.drawArea = drawArea;

        createQueues(queuesCount);
        createWashBays(washBaysCount);
        createController();
        addCars(4);

        changeCarSpeedScale(carSpeedScale);
        changeControllerSpeedScale(controllerSpeedScale);
        changeWasherSpeedScale(washerSpeedScale);
    }

    private void createController() {
        PathTransition pathTransition = new PathTransition();
        Path path = new Path();
        ControllerUI controllerUI = new ControllerUI(path, pathTransition, controllerSpeedScale);
        drawArea.getChildren().addAll(controllerUI.getShape(), path);

        controller = new Controller(washBays, carQueues, controllerUI, controllerSpeedScale);
        controller.start();
    }
    private void createQueues(int amount){
        amount = Math.clamp(amount, 2, 100);
        for (int i = 0; i < amount; i++) {
            CarQueueUI carQueueUI = new CarQueueUI(i);
            carQueues.add(new CarQueue(i, carQueueUI));
            drawArea.getChildren().addAll(carQueueUI.getShape());
        }
    }
    private void createWashBays(int amount){
        amount = Math.clamp(amount, 2, 250);
        ArrayList<Washer> waterWashers = new ArrayList<>();
        ArrayList<Washer> soapWashers = new ArrayList<>();

        for (int i = 0; i < amount - 1; i++) {
            WasherUI soapWasherUI = new WasherUI(0, i);
            soapWashers.add(new Washer(soapWasherUI));
            WasherUI waterWasherUI = new WasherUI(1, i);
            waterWashers.add(new Washer(waterWasherUI));

            drawArea.getChildren().addAll(soapWasherUI.getShape(), waterWasherUI.getShape());
        }

        Washer[] waterWashersC;
        Washer[] soapWashersC;
        for (int i = 0; i < amount; i++) {
            if (i == 0) {
                waterWashersC = new Washer[]{null, waterWashers.get(i)};
                soapWashersC = new Washer[]{null, soapWashers.get(i)};
            }
            else if(i == amount - 1){
                waterWashersC = new Washer[]{waterWashers.get(i-1), null};
                soapWashersC = new Washer[]{soapWashers.get(i-1), null};
            }
            else {
                waterWashersC = new Washer[]{waterWashers.get(i-1), waterWashers.get(i)};
                soapWashersC = new Washer[]{soapWashers.get(i-1), soapWashers.get(i)};
            }
            WashBayUI washBayUI = new WashBayUI(i);
            drawArea.getChildren().addAll(washBayUI.getShape());

            washBays.add(new WashBay(i, waterWashersC, soapWashersC, washBayUI));
        }
    }
    public void addCars(int amount){
        synchronized (cars){
            for (int i = 0; i < amount; i++) {
                PathTransition pathTransition = new PathTransition();
                Path path = new Path();

                char letter = (char) (cars.size()+'a');
                CarUI carUI = new CarUI(path, pathTransition, letter, carSpeedScale);
                Car car = new Car(this, letter, carQueues, carUI, carSpeedScale, carLoop);

                drawArea.getChildren().addAll(carUI.getShape(), path);
                cars.add(car);
                Platform.runLater(car::start);
            }
            carAmountText.setText("Amount: " + cars.size());
        }
    }
    public double changeCarSpeedScale(double newSpeed){
        carSpeedScale = Math.clamp(newSpeed,0.001,100);
        synchronized (cars) {
            for (Car car : cars)
                car.setSpeedScale(newSpeed);
        }
        return carSpeedScale;
    }
    public double changeControllerSpeedScale(double newSpeed){
        controllerSpeedScale = Math.clamp(newSpeed,0.001,100);
        controller.setSpeedScale(newSpeed);
        return controllerSpeedScale;
    }
    public double changeWasherSpeedScale(double newSpeed){
        washerSpeedScale = Math.clamp(newSpeed,0.001,100);
        for (WashBay washBay : washBays){
            for (Washer washer : washBay.waterWashers)
                if(washer != null)
                    washer.setSpeedScale(newSpeed);

            for (Washer washer : washBay.soapWashers)
                if(washer != null)
                    washer.setSpeedScale(newSpeed);
        }
        return washerSpeedScale;
    }
    public double getCarSpeedScale() {
        return carSpeedScale;
    }
    public double getControllerSpeedScale() {
        return controllerSpeedScale;
    }
    public double getWasherSpeedScale() {
        return washerSpeedScale;
    }

    public boolean toggleCarLoop(){
        carLoop = !carLoop;
        for (Car car : cars)
            car.setLoop(carLoop);
        return carLoop;
    }

    @Override
    public void onThreadComplete(Car car) {
        synchronized (cars){
            cars.remove(car);
            carAmountText.setText("Amount: " + cars.size());
        }
    }

    public void removeSimulationController() {
        drawArea.getChildren().clear();

        synchronized (cars) {
            for (Car car : cars)
                car.interrupt();
        }
        controller.interrupt();
    }
}

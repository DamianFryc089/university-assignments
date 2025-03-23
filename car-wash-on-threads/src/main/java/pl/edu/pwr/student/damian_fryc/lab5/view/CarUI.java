package pl.edu.pwr.student.damian_fryc.lab5.view;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pl.edu.pwr.student.damian_fryc.lab5.logic.Car;
import pl.edu.pwr.student.damian_fryc.lab5.logic.CarQueue;
import pl.edu.pwr.student.damian_fryc.lab5.logic.WashBay;

import java.util.random.RandomGenerator;

public class CarUI {
    public double x = 10;
    public double y = -100;

    private final StackPane carShape = new StackPane();
    private final Path path;
    private final PathTransition pathTransition;
    private final Object animationStop = new Object();
    private boolean isPlaying = false;

    public CarUI(Path path, PathTransition pathTransition, char letter, double speedScale) {

        Text text = new Text(String.valueOf(letter));

        int color = RandomGenerator.getDefault().nextInt(0xFFFFFF + 1);
        Rectangle rectangle = new Rectangle(20, 20 , Paint.valueOf(String.format("#%06X", color)));
//        Rectangle rectangle = new Rectangle(20, 20 , Paint.valueOf("999999"));
        carShape.getChildren().addAll(rectangle, text);

        carShape.setTranslateX(x);
        carShape.setTranslateY(y);

        pathTransition.setNode(carShape);
        pathTransition.setDuration(Duration.millis(speedScale * 500));
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.setPath(path);

        path.setStrokeWidth(0);

        this.path = path;
        this.pathTransition = pathTransition;

        // after the end of the animation send signal
        pathTransition.setOnFinished(event -> {
            synchronized (animationStop) {
                isPlaying = false;
                animationStop.notify();
            }
        });
    }

    public Pane getShape() {
        return carShape;
    }

    public boolean moveToCarQueue(CarQueue carQueue, Car car) throws InterruptedException {

        if (isPlaying) {
            synchronized (animationStop) {
                animationStop.wait();
            }
        }

        if( x == carQueue.carQueueUI.x + (CarQueue.CAPACITY - carQueue.getSlotInQueue(car)) * CarQueueUI.LINE_LENGTH_PER_CAR - 10 ) {
            return false;
        }

        Platform.runLater(() -> {
            path.getElements().clear();
            path.getElements().add(new MoveTo(x, y)); // Start

            // to Y of queue
            if(y != carQueue.carQueueUI.y) {
                y = carQueue.carQueueUI.y;
                path.getElements().add(new LineTo(x, y));
            }

            // to X + pos and Y of queue
            x = carQueue.carQueueUI.x + (CarQueue.CAPACITY - carQueue.getSlotInQueue(car)) * CarQueueUI.LINE_LENGTH_PER_CAR - 10;
            path.getElements().add(new LineTo(x, y));

            isPlaying = true;
            pathTransition.play();
        });
        synchronized (animationStop) {
            animationStop.wait();
        }
        return true;
    }

    public void moveCarInQueue(int newPos, double queueX) {

        if(x == queueX + newPos * CarQueueUI.LINE_LENGTH_PER_CAR - 10) {
            return;
        }
        Platform.runLater(() -> {
            path.getElements().clear();
            path.getElements().add(new MoveTo(x, y)); // Start

            // to X + slot size in queue
            x =  queueX + newPos * CarQueueUI.LINE_LENGTH_PER_CAR - 10;
            path.getElements().add(new LineTo(x, y));

            isPlaying = true;
            pathTransition.play();
        });
    }

    public void moveToWashBay(WashBay washBay) throws InterruptedException {

        if (isPlaying) {
            synchronized (animationStop) {
                animationStop.wait();
            }
        }
        Platform.runLater(() -> {
            path.getElements().clear();
            path.getElements().add(new MoveTo(x, y));

            // to X of wash bay
            x = washBay.washBayUI.x;
            path.getElements().add(new LineTo(x, y));

            // to X and Y of wash bay
            y = washBay.washBayUI.y;
            path.getElements().add(new LineTo(x, y));

            isPlaying = true;
            pathTransition.play();
        });
        synchronized (animationStop) {
            animationStop.wait();
        }
    }

    public void moveToEdgeOfScreen() throws InterruptedException {

        if (isPlaying) {
            synchronized (animationStop) {
                animationStop.wait();
            }
        }

        Platform.runLater(() -> {
            path.getElements().clear();
            path.getElements().add(new MoveTo(x, y));

            // to -Y
            y = -100;
            path.getElements().add(new LineTo(x, y));

            // to starting position
            x = -10;
            path.getElements().add(new LineTo(x, y));

            isPlaying = true;
            pathTransition.play();
        });

        synchronized (animationStop) {
            animationStop.wait();
        }
    }

    public void changeAnimationSpeed(double speedScale){
        pathTransition.setDuration(Duration.millis(speedScale * 500));
    }
}

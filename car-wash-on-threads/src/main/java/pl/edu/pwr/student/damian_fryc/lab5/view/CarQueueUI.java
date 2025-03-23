package pl.edu.pwr.student.damian_fryc.lab5.view;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import pl.edu.pwr.student.damian_fryc.lab5.logic.Car;
import pl.edu.pwr.student.damian_fryc.lab5.logic.CarQueue;

public class CarQueueUI {
    public static final double QUEUE_HEIGHT = 30;
    public static final double LINE_LENGTH_PER_CAR = 25;
    public final double x;
    public final double y;

    private final double lineLength;

    public CarQueueUI(int i) {
        x = 50;
        y = 150 + i * QUEUE_HEIGHT;
        lineLength = CarQueue.CAPACITY * LINE_LENGTH_PER_CAR;
    }

    public Pane getShape() {
        Pane pane = new Pane();
        double startX = 0;
        double endX = lineLength;

        double y1 = -QUEUE_HEIGHT / 2;
        double y2 = QUEUE_HEIGHT / 2;

        pane.setTranslateX(x);
        pane.setTranslateY(y);

        for (int i = 1; i < CarQueue.CAPACITY ; i++) {
            Line line = new Line(LINE_LENGTH_PER_CAR * i + 2,-QUEUE_HEIGHT/2,LINE_LENGTH_PER_CAR * i + 2, QUEUE_HEIGHT/2);
            line.getStrokeDashArray().addAll(3.0, 3.0);
            pane.getChildren().add(line);
        }

        pane.getChildren().add(new Line(startX,  y1,  endX,  y1));
        pane.getChildren().add(new Line(startX,  y2,  endX,  y2));
        return pane;
    }

    public void moveCarsInQueue(Car[] queuedCars) {
        for (int i = 0; i < queuedCars.length; i++) {
            if(queuedCars[i] != null)
                queuedCars[i].carUI.moveCarInQueue((queuedCars.length - i), x);
        }
    }
}

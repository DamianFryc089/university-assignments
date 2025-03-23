package pl.edu.pwr.student.damian_fryc.lab5.view;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Scale;
import pl.edu.pwr.student.damian_fryc.lab5.logic.CarQueue;

public class WashBayUI {
    public static final double WIDTH_OF_WASHING_BAY = 35;
    public static final double HEIGHT_OF_WASHING_BAY = 40;
    public final double x;
    public final double y;

    public WashBayUI(int i) {
        x = 125 + CarQueueUI.LINE_LENGTH_PER_CAR * CarQueue.CAPACITY + i * WIDTH_OF_WASHING_BAY;
        y = 25;
    }

    public Pane getShape() {
        Pane washBayShape = new Pane();

        // creating [ shape
        Polyline leftWall = new Polyline();
        leftWall.getPoints().addAll(
                -WIDTH_OF_WASHING_BAY/2 + 5, -HEIGHT_OF_WASHING_BAY/2,
                -WIDTH_OF_WASHING_BAY/2, -HEIGHT_OF_WASHING_BAY/2,
                -WIDTH_OF_WASHING_BAY/2, HEIGHT_OF_WASHING_BAY/2,
                -WIDTH_OF_WASHING_BAY/2 + 5 , HEIGHT_OF_WASHING_BAY/2
        );

        Polyline rightWall = new Polyline();
        rightWall.getPoints().addAll(leftWall.getPoints());

        // flipping to create ] shape
        Scale flipY = new Scale(-1, 1, 0, 0);
        rightWall.getTransforms().add(flipY);

        washBayShape.setTranslateX(x);
        washBayShape.setTranslateY(y);
        washBayShape.getChildren().addAll(leftWall, rightWall);
        return washBayShape;
    }
}

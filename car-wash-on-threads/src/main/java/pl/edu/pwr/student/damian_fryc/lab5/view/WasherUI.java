package pl.edu.pwr.student.damian_fryc.lab5.view;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;
import pl.edu.pwr.student.damian_fryc.lab5.logic.CarQueue;

public class WasherUI {
    public final double x;
    public final double y;

    private final Pane washerShape = new Pane();
    private final Polyline leftLine;
    private final Polyline rightLine;
    public WasherUI(int posFromTop, int washBayId){
        x = 125 + CarQueueUI.LINE_LENGTH_PER_CAR * CarQueue.CAPACITY + WashBayUI.WIDTH_OF_WASHING_BAY * (washBayId + 0.5);
        y = 25 + posFromTop * WashBayUI.HEIGHT_OF_WASHING_BAY / 2 - WashBayUI.HEIGHT_OF_WASHING_BAY/2;

        // < and > shapes
        leftLine = new Polyline();
        leftLine.getPoints().addAll(
                0.0, 0.0,
                -5.0, WashBayUI.HEIGHT_OF_WASHING_BAY/4,
                0.0, WashBayUI.HEIGHT_OF_WASHING_BAY/2
        );
        rightLine = new Polyline();
        rightLine.getPoints().addAll(
                0.0, 0.0,
                5.0, WashBayUI.HEIGHT_OF_WASHING_BAY/4,
                0.0, WashBayUI.HEIGHT_OF_WASHING_BAY/2
        );

        if(posFromTop == 0) {
            leftLine.setStroke(Paint.valueOf("FF0000"));
            rightLine.setStroke(Paint.valueOf("FF0000"));
        }
        else {
            leftLine.setStroke(Paint.valueOf("0000FF"));
            rightLine.setStroke(Paint.valueOf("0000FF"));
        }

        washerShape.getChildren().addAll(leftLine, rightLine);
        washerShape.setTranslateX(x);
        washerShape.setTranslateY(y);
        hide();
    }
    public Pane getShape() {
        return washerShape;
    }
    public void hide(){
        Platform.runLater(() -> {
            leftLine.setVisible(false);
            rightLine.setVisible(false);
        });
    }
    public void showLeft(){
        Platform.runLater(() -> {
            leftLine.setVisible(true);
            rightLine.setVisible(false);
        });
    }
    public void showRight(){
        Platform.runLater(() -> {
            leftLine.setVisible(false);
            rightLine.setVisible(true);
        });
    }
}

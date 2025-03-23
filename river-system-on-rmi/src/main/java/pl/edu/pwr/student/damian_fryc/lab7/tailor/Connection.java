package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.rmi.Remote;

public class Connection extends Line {
    private final Socket socket1;
    private final Socket socket2;

    public Connection(Socket socket1, Socket socket2){
        strokeWidthProperty().set(5);
        setStrokeLineCap(StrokeLineCap.ROUND);
        strokeProperty().set(Paint.valueOf("Black"));
        setMouseTransparent(true);

        this.socket1 = socket1;
        socket1.setConnection(this);
        socket1.setUnclicked();

        this.socket2 = socket2;
        socket2.setConnection(this);
        socket2.setUnclicked();

        System.out.println("new connection between " + socket1 + " and " + socket2);
    }
    public void delete() {
        System.out.println("deleted connection between " + socket1 + " and " + socket2);
        socket1.removeConnection();
        socket2.removeConnection();
        if (getParent() instanceof Pane parent) {
            parent.getChildren().remove(this);
        }
    }

    public Remote getSecondRemote(Socket socket) {
        if (socket == socket1) return socket2.getRemote();
        else return socket1.getRemote();
    }

    public void updatePosition() {
        double x1 = socket1.getX();
        double x2 = socket2.getX();
        double y1 = socket1.getY();
        double y2 = socket2.getY();

        setStartX(x2 - x1);
        if(x2 - x1 >0)
            translateXProperty().set(x1);
        else
            translateXProperty().set(x2);

        setStartY(y2 - y1);
        if(y2 - y1 >0)
            translateYProperty().set(y1);
        else
            translateYProperty().set(y2);
    }
}

package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import java.rmi.Remote;

public interface UpdateListener {
    void elementAdded(Remote r, String name);
    void elementRemoved(Remote r, String name);
    boolean objectSelected(Socket socket);
}

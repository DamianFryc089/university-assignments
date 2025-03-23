package pl.edu.pwr.student.damian_fryc.lab5.logic;

import pl.edu.pwr.student.damian_fryc.lab5.view.ControllerUI;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread{
	private final ArrayList<WashBay> washBays;
	private final ArrayList<CarQueue> carQueues;
	private final ControllerUI controllerUI;
	private int lastPicked = 0;
	private double speedScale;

	public Controller(List<WashBay> washBays, List<CarQueue> carQueues, ControllerUI controllerUI, double speedScale) {
		this.washBays = (ArrayList<WashBay>) washBays;
		this.carQueues = (ArrayList<CarQueue>) carQueues;
        this.controllerUI = controllerUI;
		this.speedScale = speedScale;

		setName("Controller");
    }

	@Override
	public void run(){
		try {
			while (true)
			{
				sleep((long) (speedScale * 500));

				// find empty wash bay
				WashBay emptyWashBay = null;
				for (WashBay washBay : washBays){
					if(washBay.isEmpty()) {
						emptyWashBay = washBay;
						break;
					}
				}
				if(emptyWashBay == null) continue;

				controllerUI.goToNextQueue(carQueues.get(lastPicked));

				// get waiting car
				Car car = carQueues.get(lastPicked).getFirst();
				lastPicked = (lastPicked + 1) % carQueues.size();
				if (car == null) continue;

				car.setWashBay(emptyWashBay);
				emptyWashBay.setCar(car);

				sleep((long) (car.getSpeedScale() * 500));
			}
		} catch (InterruptedException ignored) {}
    }
	public void setSpeedScale(double speedScale) {
		this.speedScale = speedScale;
		controllerUI.changeAnimationSpeed(speedScale);
	}
}

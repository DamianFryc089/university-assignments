package pl.edu.pwr.student.damian_fryc.lab5.logic;

import pl.edu.pwr.student.damian_fryc.lab5.view.CarUI;

import java.util.ArrayList;
import java.util.random.RandomGenerator;

public class Car extends Thread {
	public final char letter;
	public final CarUI carUI;

	private final Object waitInQueueLock = new Object();
	private final ArrayList<CarQueue> carQueues;
	private final CarEndListener carEndListener;
	private WashBay washBay = null;
	private CarQueue carQueue = null;
	private double speedScale;
	private boolean carLoop;

	public Car(CarEndListener carEndListener, char letter, ArrayList<CarQueue> carQueues, CarUI carUI, double speedScale, boolean carLoop){
		this.carEndListener = carEndListener;
		this.letter = letter;
		this.carQueues = carQueues;
        this.carUI = carUI;
		this.speedScale = speedScale;
		this.carLoop = carLoop;

		setName("Car-"+letter);
    }

	@Override
	public void run(){
		try {
			do {
				sleep((long) (speedScale * RandomGenerator.getDefault().nextInt(500, 10000)));
				int slotInQueue;
				do {
					sleep((long) (speedScale * 1000));
					slotInQueue = reserveSlotInQueue();
				} while(slotInQueue == -1);

				while (carUI.moveToCarQueue(carQueue, this));

				carQueue.enqueue(this);

				synchronized (waitInQueueLock){
					waitInQueueLock.wait();
				}

				System.out.println(letter + " arrived to " + washBay.id);

				carUI.moveToWashBay(washBay);

				washPhase(Washer.WasherType.WATER);
				washPhase(Washer.WasherType.SOAP);
				washPhase(Washer.WasherType.WATER);

				washBay.removeCar();
				washBay = null;

				carUI.moveToEdgeOfScreen();
				System.out.println(letter + " DONE!!");
			}while (carLoop);
		} catch (InterruptedException ignored) {}
		carEndListener.onThreadComplete(this);
    }

	private int reserveSlotInQueue(){
		CarQueue minQueue = carQueues.getFirst();
		for (int i = 1; i < carQueues.size(); i++) {
			if (carQueues.get(i).getQueueCarCount() < minQueue.getQueueCarCount())
				minQueue = carQueues.get(i);
		}

		int reserved = minQueue.reserve(this);
		if(reserved != -1)
			carQueue = minQueue;

		return reserved;
	}

	private void washPhase(Washer.WasherType washerType) throws InterruptedException {
		while (true) {
			Washer washer = washBay.tryUseLeft(washerType, (long) (speedScale * 1000));
			if(washer != null) {
				washer.use(1);
				return;
			}

			washer = washBay.tryUseRight(washerType, (long) (speedScale * 1000));
			if(washer != null) {
				washer.use(0);
				return;
			}
		}
	}
	public void setSpeedScale(double speedScale) {
		this.speedScale = speedScale;
		carUI.changeAnimationSpeed(speedScale);
	}
	public double getSpeedScale() {
		return speedScale;
	}
	public void setLoop(boolean carLoop) {
		this.carLoop = carLoop;
	}

	public void setWashBay(WashBay washBay) {
		this.washBay = washBay;
		synchronized (waitInQueueLock) {
			waitInQueueLock.notify();
		}
	}
}

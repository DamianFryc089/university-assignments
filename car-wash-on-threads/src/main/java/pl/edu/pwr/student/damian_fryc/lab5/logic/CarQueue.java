package pl.edu.pwr.student.damian_fryc.lab5.logic;

import pl.edu.pwr.student.damian_fryc.lab5.view.CarQueueUI;

import java.util.Arrays;

public class CarQueue {
	public static int CAPACITY = 10;
	public final CarQueueUI carQueueUI;

	private final Car[] queuedCars;
	private final Car[] reservedQueuedCars;
	private final int id;
	private int size;
	private int reserved;

	public CarQueue(int id, CarQueueUI carQueueUI) {
		this.queuedCars = new Car[CAPACITY];
		this.reservedQueuedCars = new Car[CAPACITY];
		this.id = id;
        this.carQueueUI = carQueueUI;
        this.size = 0;
		this.reserved = 0;
	}

	public synchronized int reserve(Car car){
		if (reserved == CAPACITY)
			return -1;
		reservedQueuedCars[reserved] = car;
		reserved++;
		return reserved-1;
	}
	public synchronized void enqueue(Car car) {
		queuedCars[size] = car;
		size++;
		System.out.println(id + ": " + getQueueString() + " +" + car.letter);
	}


	public synchronized Car getFirst() {
		if (size == 0) return null;

		Car removedCar = queuedCars[0];

		for (int i = 1; i < size; i++)
			queuedCars[i - 1] = queuedCars[i];

		for (int i = 1; i < reserved; i++)
			reservedQueuedCars[i - 1] = reservedQueuedCars[i];


		queuedCars[size - 1] = null;
		reservedQueuedCars[reserved - 1] = null;

		size--;
		reserved--;
		carQueueUI.moveCarsInQueue(queuedCars);

		System.out.println(id + ": " + getQueueString() + " -" + removedCar.letter);
		return removedCar;
	}

	public synchronized int getQueueCarCount() {
		return reserved;
	}

	@Override
	public String toString() {
		return "CarQueue{" +
				"CAPACITY=" + CAPACITY +
				", queuedCars=" + Arrays.toString(queuedCars) +
				", size=" + size +
				'}';
	}

	private String getQueueString() {
		StringBuilder text = new StringBuilder();
		for (int i = CAPACITY - 1; i >= 0; i--) {
			if (queuedCars[i] == null) text.append(" - ");
			else text.append(" ").append(queuedCars[i].letter).append(" ");
		}
		return text.toString();
	}

	public synchronized int getSlotInQueue(Car car) {
		for (int i = 0; i < reserved; i++)
			if (reservedQueuedCars[i] == car) {
				return i;
			}
		return -1;
	}
}

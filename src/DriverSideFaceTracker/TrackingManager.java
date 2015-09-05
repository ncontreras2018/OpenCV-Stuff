package DriverSideFaceTracker;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import Util.Webcam;

public class TrackingManager extends Thread {

	private Rect[] locations;

	private CascadeClassifier objectDetector;

	private NetworkTableManager tableManager;

	private double sensitivity;

	private int sleepTime;
	
	private boolean hasNewLocations;

	public TrackingManager(String mode, double sensitivity,
			int trackRate) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String path = getClass().getResource("/" + mode).toString()
				.substring(6);

		while (path.indexOf("%20") != -1) {
			path = path.substring(0, path.indexOf("%20")) + " "
					+ path.substring(path.indexOf("%20") + 3);
		}

		System.out.println(path);

		objectDetector = new CascadeClassifier(path);

		tableManager = new NetworkTableManager();

		this.sensitivity = sensitivity;

		sleepTime = 1000 / trackRate;
	}

	public void run() {

		while (true) {

			if (tableManager.hasNewImage()) {

				Mat image = tableManager.getLatestImage();

				MatOfRect faceDetections = new MatOfRect();
				objectDetector.detectMultiScale(image, faceDetections,
						1 * sensitivity, 4, 0, new Size(10, 10), new Size(9999,
								9999));

				locations = faceDetections.toArray();
				
				hasNewLocations = true;
			}

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasNewLocations() {
		return hasNewLocations;
	}
	
	public Rect[] getLocations() {
		hasNewLocations = false;
		return locations;
	}
	
	public Size getImageSize() {
		return tableManager.getImageSize();
	}

	public void writeToTable(double forwardError, double rightError,
			double clockwiseError) {
		tableManager.write(forwardError, rightError, clockwiseError);
		
	}
}
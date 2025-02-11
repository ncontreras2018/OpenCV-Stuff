package CircleDetectionROI;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;

@SuppressWarnings("serial")
public class CircleDetectionROIPanel extends JPanel {

	Mat webcamFeed;

	public CircleDetectionROIPanel(int width, int height, String frameName) {
		JFrame frame = new JFrame(frameName);

		frame.add(this);

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);

		System.out.println("Panel Ready");
	}
}

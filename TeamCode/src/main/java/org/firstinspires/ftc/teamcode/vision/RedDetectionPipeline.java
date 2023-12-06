package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class RedDetectionPipeline extends OpenCvPipeline {
    private static final int NUM_RECTANGLES = 3;
    private static final String[] RECTANGLE_NAMES = {"left", "center", "right"};

    private Rect[] rectangles = new Rect[NUM_RECTANGLES];
    private double[] redConcentrations = new double[NUM_RECTANGLES];
    private String selectedRectangle = "";

    private Telemetry telemetry;

    public RedDetectionPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;

        // Define the positions and sizes of the rectangles
        rectangles[0] = new Rect(new Point(30, 220), new Size(100, 100));
        rectangles[1] = new Rect(new Point(250, 200), new Size(100, 100));
        rectangles[2] = new Rect(new Point(470, 220), new Size(100, 100));
    }

    @Override
    public Mat processFrame(Mat input) {
        // Process the frame to determine red concentration in each rectangle
        for (int i = 0; i < NUM_RECTANGLES; i++) {
            Mat roi = input.submat(rectangles[i]);
            redConcentrations[i] = calculateRedConcentration(roi);
        }

        // Find the index of the rectangle with the highest red concentration
        int maxIndex = findMaxIndex(redConcentrations);

        // Set the selected rectangle name
        selectedRectangle = RECTANGLE_NAMES[maxIndex];

        // Draw rectangles and information on the input frame for visualization
        drawRectangles(input);
        drawText(input);

        // Live telemetry feedback within the pipeline
//        for (int i = 0; i < NUM_RECTANGLES; i++) {
//            telemetry.addData("Red Concentration " + RECTANGLE_NAMES[i], redConcentrations[i]);
//        }
        telemetry.addData("Selected Rectangle", selectedRectangle);
        telemetry.update();

        // Return the input frame (you can modify this to return a processed frame if needed)
        return input;
    }

    public String getSelectedRectangle() {
        return selectedRectangle;
    }

    private double calculateRedConcentration(Mat roi) {
        // Convert the ROI to HSV color space
        Mat hsv = new Mat();
        Imgproc.cvtColor(roi, hsv, Imgproc.COLOR_BGR2HSV);

        // Define the lower and upper bounds of the red hue
        Scalar lowerRed1 = new Scalar(0,100, 20);  // Lower bound (red hues)
        Scalar upperRed1 = new Scalar(10, 255, 255); // Upper bound (red hues)
        Mat redMask1 = new Mat();
        Core.inRange(hsv, lowerRed1, upperRed1, redMask1);

        // Include another range for the red hue (since it wraps around)
        Scalar lowerRed2 = new Scalar(160, 100, 20); // Lower bound for the other range
        Scalar upperRed2 = new Scalar(179, 255, 255); // Upper bound for the other range
        Mat redMask2 = new Mat();
        Core.inRange(hsv, lowerRed2, upperRed2, redMask2);

        // Combine the masks to get the final red mask
        Mat redMask = new Mat();
        Core.bitwise_or(redMask1, redMask2, redMask);

        // Increase the saturation and value thresholds to filter out other colors
        Scalar lowerThreshold = new Scalar(0, 80, 80);
        Scalar upperThreshold = new Scalar(180, 255, 255);
        Core.inRange(hsv, lowerThreshold, upperThreshold, redMask);

        // Calculate the concentration of the color red in the given region of interest (ROI)
        double redPixels = Core.countNonZero(redMask);
        double totalPixels = roi.size().area();

        return (redPixels / totalPixels) * 100.0;
    }

    private int findMaxIndex(double[] array) {
        // Find the index of the maximum value in the array
        int maxIndex = 0;
        double maxValue = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxIndex = i;
                maxValue = array[i];
            }
        }

        return maxIndex;
    }

    private void drawRectangles(Mat frame) {
        // Find the index of the rectangle with the highest red concentration
        int maxIndex = findMaxIndex(redConcentrations);

        // Draw rectangles on the input frame for visualization
        for (int i = 0; i < rectangles.length; i++) {
            Scalar color = (i == maxIndex) ? new Scalar(0, 255, 0) : new Scalar(0, 255, 255);
            Imgproc.rectangle(frame, rectangles[i].tl(), rectangles[i].br(), color, 2);
        }
    }

    private void drawText(Mat frame) {
        // Draw text with the selected rectangle name on the input frame for visualization
        Point textPosition = new Point(30, 30);
        Imgproc.putText(frame, "Selected Rectangle: " + selectedRectangle, textPosition, Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(0, 0, 0), 4);
    }
}
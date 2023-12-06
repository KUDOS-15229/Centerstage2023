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

public class BlueDetectionPipeline extends OpenCvPipeline {
    private static final int NUM_RECTANGLES = 3;
    private static final String[] RECTANGLE_NAMES = {"left", "center", "right"};

    private Rect[] rectangles = new Rect[NUM_RECTANGLES];
    private double[] blueConcentrations = new double[NUM_RECTANGLES];
    private String selectedRectangle = "";

    Telemetry telemetry;

    public BlueDetectionPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;

        // Define the positions and sizes of the rectangles
        rectangles[0] = new Rect(new Point(30, 220), new Size(100, 100));
        rectangles[1] = new Rect(new Point(250, 200), new Size(100, 100));
        rectangles[2] = new Rect(new Point(470, 220), new Size(100, 100));
    }

    @Override
    public Mat processFrame(Mat input) {
        // Process the frame to determine blue concentration in each rectangle
        double[] blueConcentrations = new double[NUM_RECTANGLES];

        for (int i = 0; i < NUM_RECTANGLES; i++) {
            Mat roi = input.submat(rectangles[i]);
            blueConcentrations[i] = calculateBlueConcentration(roi);
        }

        // Update telemetry with blue concentration for each rectangle
//        for (int i = 0; i < NUM_RECTANGLES; i++) {
//            telemetry.addData("Blue Concentration " + RECTANGLE_NAMES[i], blueConcentrations[i]);
//        }

        // Find the index of the rectangle with the highest blue concentration
        int maxIndex = findMaxIndex(blueConcentrations);

        // Set the selected rectangle name
        selectedRectangle = RECTANGLE_NAMES[maxIndex];


        // Draw rectangles and information on the input frame for visualization
        drawRectangles(input);
        drawText(input);

        // Update telemetry with selected rectangle
        telemetry.addData("Selected Rectangle", selectedRectangle);
        telemetry.update();

        // Return the input frame (you can modify this to return a processed frame if needed)
        return input;
    }

    public String getSelectedRectangle() {
        return selectedRectangle;
    }

    public void printTelemetry() {
        for (int i = 0; i < NUM_RECTANGLES; i++) {
            telemetry.addData("Blue Concentration " + RECTANGLE_NAMES[i], blueConcentrations[i]);
        }
        telemetry.addData("Selected Rectangle", selectedRectangle);
        telemetry.update();
    }

    private double calculateBlueConcentration(Mat roi) {
        // Convert the ROI to HSV color space
        Mat hsv = new Mat();
        Imgproc.cvtColor(roi, hsv, Imgproc.COLOR_RGB2HSV);

        // Define the range of blue color in HSV
        Scalar lowerBlue = new Scalar(90, 50, 50);   // Adjust these values as needed
        Scalar upperBlue = new Scalar(130, 255, 255); // Adjust these values as needed

        // Create a binary mask based on the blue color range
        Mat blueMask = new Mat();
        Core.inRange(hsv, lowerBlue, upperBlue, blueMask);

        // Calculate the concentration of the color blue in the given region of interest (ROI)
        double bluePixels = Core.countNonZero(blueMask);
        double totalPixels = roi.size().area();

        return (bluePixels / totalPixels) * 100.0;
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
        for (int i = 0; i < NUM_RECTANGLES; i++) {
            Rect rect = rectangles[i];

            // Set the color based on whether the rectangle is selected
            Scalar color = (RECTANGLE_NAMES[i].equals(selectedRectangle)) ? new Scalar(0, 255, 0) : new Scalar(0, 0, 255);

            // Draw the rectangle on the input frame
            Imgproc.rectangle(frame, rect.tl(), rect.br(), color, 2);
        }
    }

    private void drawText(Mat frame) {
        // Draw text with the selected rectangle name on the input frame for visualization
        Point textPosition = new Point(30, 30);
        Imgproc.putText(frame, "Selected Rectangle: " + selectedRectangle, textPosition, Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(255, 255, 255), 2);
    }
}
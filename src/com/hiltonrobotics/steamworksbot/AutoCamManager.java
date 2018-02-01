package com.hiltonrobotics.steamworksbot;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class AutoCamManager {
	private static AutoCamManager instance = null;
	private Thread autoThread;
	
	private AutoCamManager() {
		CameraServer camServer = CameraServer.getInstance();
		UsbCamera cam = camServer.startAutomaticCapture();
		cam.setExposureAuto();
		cam.setResolution(640, 480);
		
		CvSink in = camServer.getVideo();
		CvSource out = camServer.putVideo("auto", 640, 480);
		
		Mat m1 = new Mat();
		Mat m2 = new Mat();
		Mat m3 = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		autoThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				in.grabFrame(m1);
				if (!m1.empty()) {
					Imgproc.cvtColor(m1, m2, Imgproc.COLOR_BGR2HSV);
					Core.inRange(m2, new Scalar(30, 100, 100), new Scalar(90, 255, 255), m3);
					Imgproc.cvtColor(m3, m2, Imgproc.COLOR_GRAY2BGR);
					//Core.bitwise_or(m1, m2, m3);
					Imgproc.GaussianBlur(m2, m3, new Size(5, 5), 0);
					Imgproc.threshold(m3, m2, 60, 255, Imgproc.THRESH_BINARY);
					Imgproc.cvtColor(m3, m2, Imgproc.COLOR_BGR2GRAY);
					//Imgproc.HoughLines(m2, m3, RHO_TRANSFORM_VALUE, 90, 90);
					Imgproc.findContours(m2, contours, m3, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
					Imgproc.drawContours(m1, contours, -1, new Scalar(255, 255, 255));
					out.putFrame(m1);
				}
			}
		});
		autoThread.setDaemon(true);
		autoThread.start();
	}
	
	public static AutoCamManager getInstance() {
		if (instance == null) instance = new AutoCamManager();
		return instance;
	}
}

package com.hiltonrobotics.steamworksbot;

import java.text.Format;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
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
	
	public static final int RHO_TRANSFORM_VALUE = (int) Math.ceil(Math.sqrt(640 * 640 + 480 * 480));
	
	public static final Scalar FILTER_LOW = new Scalar(0, 250, 0);
	public static final Scalar FILTER_HIGH = new Scalar(255, 255, 255);
	
	public static final Scalar COLOR_WHITE = new Scalar(255, 255, 255);
	public static final Scalar COLOR_RED = new Scalar(0, 0, 255);
	
	public static double RATIO_SCORE_THRESH = 0.5;
	
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
		//MatOfPoint2f cTemp2 = new MatOfPoint2f();
		ArrayList<MatOfPoint> contoursFilter = new ArrayList<>();
		autoThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				in.grabFrame(m1);
				if (!m1.empty()) {
					Imgproc.cvtColor(m1, m2, Imgproc.COLOR_BGR2HLS);
					Core.inRange(m2, FILTER_LOW, FILTER_HIGH, m3);
					Imgproc.cvtColor(m3, m2, Imgproc.COLOR_GRAY2BGR);
					//Core.bitwise_or(m1, m2, m3);
					Imgproc.GaussianBlur(m2, m3, new Size(5, 5), 0);
					Imgproc.threshold(m3, m2, 60, 255, Imgproc.THRESH_BINARY);
					Imgproc.cvtColor(m3, m2, Imgproc.COLOR_BGR2GRAY);
					//Imgproc.HoughLines(m2, m3, RHO_TRANSFORM_VALUE, 90, 90);
					Imgproc.findContours(m2, contours, m3, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
					Imgproc.rectangle(m1, new Point(0, 0), new Point(640, 480), new Scalar(0, 0, 0), -1);
					for (MatOfPoint c : contours) {
						//MatOfPoint2f cTemp1 = new MatOfPoint2f(c.toArray());
						//Imgproc.approxPolyDP(cTemp1, cTemp2, Imgproc.arcLength(cTemp1, true) / 1000, true);
						//if (Imgproc.contourArea(cTemp2) > 500 || true) contoursFilter.add(new MatOfPoint(cTemp2.toArray()));
						MatOfInt convex = new MatOfInt();
						if (Imgproc.contourArea(c) >= 50) {
							//contoursFilter.add(c);
							MatOfPoint convexM1 = new MatOfPoint();
							Imgproc.convexHull(c, convex, false);
							convexM1.create((int) convex.size().height, 1, CvType.CV_32SC2);
							for (int i = 0; i < convex.size().height; ++i) {
								int j = (int) convex.get(i, 0)[0];
								convexM1.put(i, 0, new double[] {c.get(j, 0)[0], c.get(j, 0)[1]});
							}
							//MatOfPoint convexM2 = new MatOfPoint();
							RotatedRect r = Imgproc.minAreaRect(new MatOfPoint2f(convexM1.toArray()));
							double score = scoreRectRatio(r);
							if (score <= RATIO_SCORE_THRESH) {
								Point[] box = new Point[4];
								r.points(box);
								for (int i = 0; i < 4; i++) {
									Imgproc.line(m1, box[i], box[(i + 1) % 4], COLOR_WHITE);//(score <= RATIO_SCORE_THRESH) ? COLOR_WHITE : COLOR_RED);
								}
								Imgproc.putText(m1, String.format("%f", score), r.center, 0, 1, COLOR_WHITE);
							}
						}
					}
					
					Imgproc.drawContours(m1, contoursFilter, -1, COLOR_WHITE);
					contours.clear();
					contoursFilter.clear();
					out.putFrame(m1);
				}
			}
		});
		autoThread.setDaemon(true);
		autoThread.start();
	}
	
	public static final double RECTANGLE_TARGET_RATIO = 8;
	
	public static double scoreRectRatio(RotatedRect r) {
		double rat = r.size.height / r.size.width;
		if (rat < 1) rat = 1 / rat;
		return Math.abs(rat / RECTANGLE_TARGET_RATIO - 1);
	}
	
	public static double scoreRectDual(RotatedRect r1, RotatedRect r2) {
		double a1 = r1.size.area(), a2 = r2.size.area();
		return Math.abs(a1 / a2 - 1);
	}
	
	/*public static double scoreRect(MatOfPoint c) {
		Point[] box = new Point[4];
		Imgproc.minAreaRect(new MatOfPoint2f(c.toArray())).points(box);
		for (int i = 0; i < c.size().height; ++i) {
			c.get(i, 0)
		}
		return 0;
	}
	
	public static double findDistToLine(Point p1, Point p2, Point loc) {
		if (p1.x == p2.x) {
			// Points have same x
			if (p1.y == p2.y) {
				// Points are identical
				return dist(loc, p2);
			} else if (p1.y > p2.y) {
				// Make p2.y > p1.y
				Point t = p1;
				p1 = p2;
				p2 = t;
			}
			if (loc.y > p2.y) {
				// Location is above top point
				return dist(loc, p2);
			} else if (loc.y < p1.y) {
				// Location is below bottom point
				return dist(p1, loc);
			} else {
				// Location is between both point's y positions
				if (p1.x > loc.x) {
					return p1.x - loc.x;
				} else {
					return loc.x - p1.x;
				}
			}
		} else if (p1.x > p2.x) {
			// Make p2.x > p1.x
			Point t = p1;
			p1 = p2;
			p2 = t;
		}
		double m = (p2.y - p1.y) / (p2.x - p1.x);
		double b = p1.y - p1.x * m;
		double mp = -(1 / m);
		double bp = loc.y - loc.x * mp;
		//mx + b = mpx + bp
		//mx - (mp)x = (bp) - b
		//(m - (mp))x = (bp) - b
		//x = [(bp) - b] / [m - (mp)]
		double iX = (bp - b) / (m - mp);
		double iY = m * iX;
		if (iX < p1.x) {
			// Intersect is to the left of line
			return dist(p1, loc);
		} else if (iX > p2.x) {
			// Intersect is to the right of line
			return dist(loc, p2);
		} else {
			// Intersect is on line
			return dist(loc, new Point(iX, iY));
		}
	}*/
	
	public static double distSq(Point p1, Point p2) {
		double dx = Math.abs(p1.x - p2.x);
		double dy = Math.abs(p1.y - p2.y);
		return dx * dx + dy * dy;
	}
	
	public static double dist(Point p1, Point p2) {
		return Math.sqrt(dist(p1, p2));
	}
	
	public static AutoCamManager getInstance() {
		if (instance == null) instance = new AutoCamManager();
		return instance;
	}
}

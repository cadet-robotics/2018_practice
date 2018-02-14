package com.hiltonrobotics.steamworksbot;

public enum CamType {
	CAM_LIFE_HD_3000(68.5),
	CAM_LIFE_CINEMA(73);
	
	double fd, fh, fv;
	
	CamType(double fdIn, double fhIn, double fvIn) {
		fd = fdIn;
		fh = fhIn;
		fv = fvIn;
	}
	
	public static final double RAD_TO_DEG = 360 / (Math.PI * 2);
	public static final double DEG_TO_RAD = (Math.PI * 2) / 360;
	public static final double CAM_ASPECT = 640 / 360;
	
	CamType(double fdIn, double fhIn) {
		this(fdIn, fhIn, 2*Math.atan((1/CAM_ASPECT) * Math.tan(fhIn/2)));
	}
	
	CamType(double fdIn) {
		this(fdIn, 2.0*Math.atan(Math.tan(fdIn/2)/Math.sqrt(1 + Math.pow(CAM_ASPECT, 2))));
	}
}

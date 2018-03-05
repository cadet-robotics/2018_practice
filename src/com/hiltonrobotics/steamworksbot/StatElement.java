package com.hiltonrobotics.steamworksbot;

public interface StatElement<T> {
	String getKey();
	T getValue();
	boolean isDone();
}
package frc.robot;

public interface StatElement<T> {
	String getKey();
	T getValue();
	boolean isDone();
}
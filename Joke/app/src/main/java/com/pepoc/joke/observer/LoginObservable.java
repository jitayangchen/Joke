package com.pepoc.joke.observer;

import java.util.Observable;

public class LoginObservable extends Observable {

	private static LoginObservable instance = null;
	
	private LoginObservable() {
		
	}
	
	public static LoginObservable getInstance() {
		if (null == instance) {
			instance = new LoginObservable();
		}
		return instance;
	}
	
	public void updateObserver(Object data) {
		setChanged();
		notifyObservers(data);
	}
	
}

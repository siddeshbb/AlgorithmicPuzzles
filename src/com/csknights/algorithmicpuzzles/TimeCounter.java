package com.csknights.algorithmicpuzzles;

public class TimeCounter {

	private long startTime;

	private long totalTime = 0;

	private boolean isStarted = false;

	private boolean isPaused = false;

	private boolean stopTimerOnPause = true;

	public void setStopTimerOnPause(boolean value) {
		stopTimerOnPause = value;
	}

	public long getTime() {
		if (!isStarted || isPaused) {
			return totalTime;
		}
		return totalTime + (System.currentTimeMillis() - startTime);
	}

	public int getTimeSeconds() {
		return (int) (getTime() / 1000);
	}

	public void start() {
		if (!isStarted) {
			startTime = System.currentTimeMillis();
			isStarted = true;
		}
	}

	public void stop() {
		totalTime += (System.currentTimeMillis() - startTime);
		isStarted = false;
	}

	public void pause() {
		if (isStarted && stopTimerOnPause && !isPaused) {
			totalTime += (System.currentTimeMillis() - startTime);
			isPaused = true;
		}
	}

	public void resume() {
		if (isPaused && isStarted) {
			startTime = System.currentTimeMillis();
			isPaused = false;
		}
	}

	public void reset() {
		totalTime = 0;
		isStarted = isPaused = false;
		stopTimerOnPause = true;
	}

}

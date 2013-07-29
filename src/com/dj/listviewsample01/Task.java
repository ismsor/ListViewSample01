package com.dj.listviewsample01;


import java.util.Random;
import java.util.Vector;

public class Task implements Runnable {
	public static interface taskListener {
		void onProgressChanged(final int progress);
	}
	private String desc;
	private int progress;
	
	private Vector<Task.taskListener> listenerList;

	public Task(String desc) {
		progress=0;
		listenerList=new Vector<Task.taskListener>();
		this.desc=desc;
	}
	
	@Override
	public void run() {
		Random random = new Random();
		try {
			Thread.sleep(random.nextInt(9)*1000);
			progress = 0;
			for (int i = 0; i < 10; i++) {				
				Thread.sleep(1000);
				incrementProgress(10);
			}
			setProgress(100);
		} catch (InterruptedException e) {
			setProgress(0);
		} catch (Exception generalEcc) {
			setProgress(0);
		} finally {
			Thread.interrupted();
		}

	}

	public void addListener(taskListener l) {
		listenerList.add(l);
	}
	public void removeListener(taskListener l){
		listenerList.remove(l);
	}
	public int getProgress() {
		return progress > 100 ? 100 : progress;
	}

	private void setProgress(int progress) {
		this.progress = progress > 100 ? 100 : progress;
		if (!listenerList.isEmpty()) {
			for (Task.taskListener listener : listenerList)
				listener.onProgressChanged(this.progress);
		}
	}

	private void incrementProgress(int increment) {
		setProgress(this.progress + increment);
	}

	public String getDesc() {
		return desc;
	}

	
}

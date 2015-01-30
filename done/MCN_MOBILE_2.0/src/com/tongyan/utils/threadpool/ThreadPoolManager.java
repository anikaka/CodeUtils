package com.tongyan.utils.threadpool;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.util.Log;

/**
 * 
 * @className ThreadPoolManager
 * @author wanghb
 * @date 2014-7-21 PM 07:37:05
 */
public class ThreadPoolManager {

	private static final String TAG = "ThreadPoolManager";
	
	/** 线程池的大小 */
	private int poolSize;
	private static final int MIN_POOL_SIZE = 1;
	private static final int MAX_POOL_SIZE = 10;
	
	/** 线程池 */
	private ExecutorService threadPool;
	
	/** 请求队列 */
	private LinkedList<ThreadPoolTask> asyncTasks;
	
	/** 工作方式 */
	private int type;
	public static final int TYPE_FIFO = 0;//what 栈头
	public static final int TYPE_LIFO = 1;// what 栈尾
	
	/** 轮询线程 */
	private Thread poolThread;
	/** 轮询时间 */
	private static final int SLEEP_TIME = 500;
	
	private boolean Running = false;
	
	
	public ThreadPoolManager(int type, int poolSize) {
		this.type = (type == TYPE_FIFO) ? TYPE_FIFO : TYPE_LIFO;
		
		if (poolSize < MIN_POOL_SIZE) poolSize = MIN_POOL_SIZE;
		if (poolSize > MAX_POOL_SIZE) poolSize = MAX_POOL_SIZE;
		this.poolSize = poolSize;
				
		threadPool = Executors.newFixedThreadPool(this.poolSize);
		
		asyncTasks = new LinkedList<ThreadPoolTask>();
	}
	
	/**
	 * 向任务队列中添加任务
	 * @param task
	 */
	public void addAsyncTask(ThreadPoolTask task) {
		synchronized (asyncTasks) {
			asyncTasks.addLast(task);
		}
	}
	
	/**
	 * 从任务队列中提取任务
	 * @return
	 */
	private ThreadPoolTask getAsyncTask() {
		synchronized (asyncTasks) {
			if (asyncTasks.size() > 0) {
				ThreadPoolTask task = (this.type == TYPE_FIFO) ? asyncTasks.removeFirst() : asyncTasks.removeLast();
				return task;
			}
		}
		return null;
	}
	
	
	public int getAsyncTaskSize() {
		if(asyncTasks != null) {
			return asyncTasks.size();
		} else {
			return 0;
		}
	}
	
	
	/**
	 * 开启线程池轮询
	 * @return
	 */
	public void start() {
		if (poolThread == null) {
			poolThread = new Thread(new PoolRunnable());
			poolThread.start();
			setRunning(true);
		}
	}
	
	/**
	 * 结束轮询，关闭线程池
	 */
	public void stop() {
		poolThread.interrupt();
		poolThread = null;
	}
	
	public void setRunning(boolean running) {
		Running = running;
	}

	public boolean isRunning() {
		return Running;
	}

	/**
	 * 实现轮询的Runnable
	 * @author carrey
	 *
	 */
	private class PoolRunnable implements Runnable {

		@Override
		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ThreadPoolTask task = getAsyncTask();
					if (task == null) {
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();//中断线程
						}
						continue;
					}
					threadPool.execute(task);
				}
			} finally {
				threadPool.shutdown();
			}
		}
		
	}
}

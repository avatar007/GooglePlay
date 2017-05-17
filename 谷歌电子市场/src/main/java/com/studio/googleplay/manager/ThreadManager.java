package com.studio.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理者
 */
public class ThreadManager {
	private static ThreadPool mThreadPool;
	public static ThreadPool getInstance() {
		if (mThreadPool == null){
			synchronized (ThreadManager.class){
				if (mThreadPool == null){
					// int cpu = Runtime.getRuntime().availableProcessors();//获取cpu个数
					// int corePoolSize = cpu * 2 + 1;
					int corePoolSize = 10;
					int maximumPoolSize = 10;
					mThreadPool = new ThreadPool(corePoolSize, maximumPoolSize, 1L);
				}
			}
		}
		return mThreadPool;
	}

	public static class ThreadPool {
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;
		private ThreadPoolExecutor pool;

		private ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		public void execute(Runnable runnable) {
			if (pool == null) {
				// 参1:核心线程数;参2:最大线程数;参3:线程休眠时间;参4:时间单位;参5:线程队列;参6:生产线程的工厂;参7:线程异常处理策略
				pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
						keepAliveTime, TimeUnit.SECONDS,
						new LinkedBlockingQueue<Runnable>(),
						Executors.defaultThreadFactory(),
						new ThreadPoolExecutor.AbortPolicy());
			}
			pool.execute(runnable);
		}
		public void cancel(Runnable runnable) {
			if (pool != null){
				pool.getQueue().remove(runnable);//移除线程队列中任务
			}
		}
	}


}

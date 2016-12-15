package com.EudyContreras.Snake.ThreadExecutors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eudy Contreras on 10/14/2016.
 */

public class MainThreadExecutor implements Executor {

	public final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    @Override
    public void execute(Runnable runnable) {
        queue.add(runnable);
    }
}
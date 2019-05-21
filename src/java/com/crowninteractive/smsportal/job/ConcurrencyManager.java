/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 *
 * @author Oladeji
 */
public class ConcurrencyManager {

    private final static Logger L = Logger.getLogger(ConcurrencyManager.class);
    private static final ExecutorService WORKERS;

    static {
        WORKERS = Executors.newCachedThreadPool();
    }

    private ConcurrencyManager() {
    }

    public static void execute(Runnable task) {
        WORKERS.execute(task);
    }

    /**
     * Executes a task at intervals.
     *
     * @param task the task to be executed.
     * @param intervals intervals within which tasks are meant to be executed.
     * Allow enough interval for a task run to finish executing before another
     * is fired.
     * @return A handle to the task execution that can for example be used to
     * kill it.
     */
    public static Object executePeriodically(Runnable task, long intervals) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static void shutdown() {
        L.info("Shutting down workers in ConcurrencyManager.");
        WORKERS.shutdown();
    }
}

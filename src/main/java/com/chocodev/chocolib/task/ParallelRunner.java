package com.chocodev.chocolib.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ParallelRunner {

    public ParallelRunner() {

    }

    public <T, Q> List<Q> run(List<T> data, Executor<T, Q> executor, int nthreads) {
        List<List<T>> chopped=chopIntoParts(data, nthreads);
        final CountDownLatch semaphore = new CountDownLatch(1);
        final CountDownLatch countDown = new CountDownLatch(chopped.size());
        List<ParallelTask<T, Q>> tasks = new ArrayList<>();
        
        
        for (int i = 0; i != chopped.size(); i++) {
            ParallelTask<T, Q> task = new ParallelTask<T, Q>(executor, chopped.get(i), semaphore, countDown);
            new Thread(task).start();
            tasks.add(task);
        }
        try {
            semaphore.countDown();
            countDown.await();
        } catch (Exception ex) {
               ex.printStackTrace();
        }
        List<Q> results = new ArrayList<>();
        for (int i = 0; i != chopped.size(); i++) {
            results.addAll(tasks.get(i).getOutput());
        }
        return results;
    }

    public <T, Q> List<Q> run(List<T> data, Executor<T, Q> executor) {
        final CountDownLatch semaphore = new CountDownLatch(1);
        final CountDownLatch countDown = new CountDownLatch(data.size());
        List<ParallelTask<T, Q>> tasks = new ArrayList<>();
        for (int i = 0; i != data.size(); i++) {
            ParallelTask<T, Q> task = new ParallelTask<T, Q>(executor, data.get(i), semaphore, countDown);
            new Thread(task).start();
            tasks.add(task);
        }
        try {
            semaphore.countDown();
            countDown.await();
        } catch (Exception ex) {

        }
        List<Q> results = new ArrayList<>();
        for (int i = 0; i != data.size(); i++) {
            results.addAll(tasks.get(i).getOutput());
        }
        return results;
    }

    public static <T> List<List<T>> chopIntoParts(final List<T> ls, final int iParts) {
        final List<List<T>> lsParts = new ArrayList<List<T>>();
        final int iChunkSize = ls.size() / iParts;
        int iLeftOver = ls.size() % iParts;
        int iTake = iChunkSize;

        for (int i = 0, iT = ls.size(); i < iT; i += iTake) {
            if (iLeftOver > 0) {
                iLeftOver--;

                iTake = iChunkSize + 1;
            } else {
                iTake = iChunkSize;
            }

            lsParts.add(new ArrayList<T>(ls.subList(i, Math.min(iT, i + iTake))));
        }

        return lsParts;
    }
}
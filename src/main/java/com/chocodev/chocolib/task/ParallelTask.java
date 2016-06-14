package com.chocodev.chocolib.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ParallelTask<T,Q> implements Runnable
{
    List<T> input;
    List<Q> output;
    Exception exception;
    Executor<T,Q> executor;
    CountDownLatch semaphore = new CountDownLatch(1);
    CountDownLatch countDown;

    public ParallelTask(Executor<T,Q> executor,T item, CountDownLatch semaphore, CountDownLatch countDown)
    {
        this.executor=executor;
        this.input=new ArrayList<>();
        this.input.add(item);
        this.countDown=countDown;
        this.semaphore=semaphore;
    }
    public ParallelTask(Executor<T, Q> executor, List<T> input, CountDownLatch semaphore, CountDownLatch countDown) {
        this.executor=executor;
        this.input=input;
        this.countDown=countDown;
        this.semaphore=semaphore;
    }
    public void run()
    {
        try
        {
            semaphore.await();
            this.output=new ArrayList<>();
            for(int i=0;i!=input.size();i++){
                this.output.add(executor.execute(input.get(i)));    
            }            
        }
        catch(Exception ex)
        {
            this.output=null;
            this.exception=ex;
        }
        countDown.countDown();
    }
    public List<Q> getOutput()
    {
        return output;
    }
    public Exception getException()
    {
        return exception;
    }
}
package com.example.lly.queue;

import com.example.lly.entity.pojo.Seckill;
import com.example.lly.entity.pojo.SuccessSeckill;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SeckillQueue {

    private static final int MAX_SIZE = 150;

    private SeckillQueue(){}

    /**
     *静态内部类，进程被调用时候才会加载出来，有一定的延迟性
     */
    private static class QueueHolder {
        private static SeckillQueue seckillQueue = new SeckillQueue();
    }

    public static SeckillQueue getSeckillQueue() {
        return QueueHolder.seckillQueue;
    }

    /**
    *ArrayBlockingQueue 一个对象数组 + 一把锁 + 两个条件
    *LinkedBlockingQueue
     */
    //public BlockingQueue<SuccessSeckill> successSeckillsQueue = new LinkedBlockingQueue<>(MAX_SIZE);
    public static BlockingQueue<SuccessSeckill> blockingQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    /**
     * offer方法是队列未满插入返回true，满了则返回false不插入，适用于秒杀情景，还可以设定等待时间，其余add会抛异常，put正常等待唤醒
     * 生产者入队操作
     */
    public Boolean produce(SuccessSeckill seckill) {
        return blockingQueue.offer(seckill);
    }

    /**
     *消费者出队操作
     */
    public Seckill consume(Seckill seckill) throws InterruptedException {
        return blockingQueue.take();
    }

    public int size() {
        return blockingQueue.size();
    }


}

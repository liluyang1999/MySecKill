package com.example.lly.queue;

import com.example.lly.entity.pojo.SeckillInfo;
import com.example.lly.entity.pojo.SuccessInfo;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SeckillQueue {

    private static final int MAX_SIZE = 150;

    /**
     * 构造函数私有化防止被实例化
     */
    private SeckillQueue(){}

    /**
     *静态内部类，延迟加载节省开销
     */
    private static class Singleton {
        private static SeckillQueue seckillQueue = new SeckillQueue();
    }

    /**
     * 得到实例唯一接口
     */
    public static SeckillQueue getSeckillQueue() {
        return Singleton.seckillQueue;
    }

    /**
    *ArrayBlockingQueue 一个对象数组 + 一把锁 + 两个条件
    *LinkedBlockingQueue   链表，两把锁
     */
    //public BlockingQueue<SuccessSeckill> successSeckillsQueue = new LinkedBlockingQueue<>(MAX_SIZE);
    public static BlockingQueue<SuccessInfo> blockingQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    /**
     * offer方法是队列未满插入返回true，满了则返回false不插入，适用于秒杀情景，还可以设定等待时间，其余add会抛异常，put正常等待唤醒
     * 生产者入队操作
     */
    public Boolean produce(SuccessInfo seckill) {
        return blockingQueue.offer(seckill);
    }

    /**
     *消费者出队操作
     */
    public SeckillInfo consume(SeckillInfo seckillInfo) throws InterruptedException {
        return blockingQueue.take();
    }

    /**
     * 阻塞队列大小
     */
    public int size() {
        return blockingQueue.size();
    }


}

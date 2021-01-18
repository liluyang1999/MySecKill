package com.example.lly.queue;

import com.example.lly.object.entity.OrderInfo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderInfoQueue {

    private static final int MAX_SIZE = 150;

    /**
     * 构造函数私有化防止被实例化
     */
    private OrderInfoQueue(){}

    /**
     *静态内部类，调用时再加载实现延迟以节省开销
     */
    private static class Singleton {
        private static OrderInfoQueue orderInfoQueue = new OrderInfoQueue();
    }

    /**
     * 得到实例唯一接口
     */
    public static OrderInfoQueue getSeckillQueue() {
        return Singleton.orderInfoQueue;
    }

    /**
     *ArrayBlockingQueue 一个对象数组 + 一把锁 + 两个条件
     *LinkedBlockingQueue   链表，两把锁
     */
    //public BlockingQueue<SuccessSeckill> successSeckillsQueue = new LinkedBlockingQueue<>(MAX_SIZE);
    public static BlockingQueue<OrderInfo> blockingQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    /**
     * offer方法是队列未满插入返回true，满了则返回false不插入，适用于秒杀情景，还可以设定等待时间，其余add会抛异常，put正常等待唤醒
     * 生产者入队操作
     */
    public Boolean produce(OrderInfo seckill) {
        return blockingQueue.offer(seckill);
    }

    /**
     *消费者出队操作
     */
    public OrderInfo consume(OrderInfo orderInfo) throws InterruptedException {
        return blockingQueue.take();
    }

    /**
     * 阻塞队列大小
     */
    public int size() {
        return blockingQueue.size();
    }


}


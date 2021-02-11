package com.example.lly.module.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁案例测试
 */
public class LockDemo {

    private static final Lock lock = new ReentrantLock(true);

    private static int num1 = 0;
    private static int num2 = 0;

    public static void main(String[] args) {
        lockDemo();
        syncDemo();
    }

    /**
     * 测试下20万自增基本能确定性能
     */
    public static void lockDemo() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ((Runnable) () -> lock()).run();
        }
        long end = System.currentTimeMillis();
        System.out.println("累加：" + num1);
        System.out.println("ReentrantLock锁：" + (end - start));
    }

    public static void syncDemo() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ((Runnable) () -> sync()).run();
        }
        long end = System.currentTimeMillis();
        System.out.println("累加：" + num2);
        System.out.println("Synchronized锁：" + (end - start));
    }

    public static void lock() {
        lock.lock();
        num1++;
        lock.unlock();
    }

    public static synchronized void sync() {
        num2++;
    }
}

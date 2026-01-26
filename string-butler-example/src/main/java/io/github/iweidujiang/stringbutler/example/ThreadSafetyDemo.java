package io.github.iweidujiang.stringbutler.example;

import java.util.Date;

/**
 * ┌───────────────────────────────────────────────
 * │ 📦 线程安全测试
 * │
 * │ 👤 作者：苏渡苇
 * │ 🔗 公众号：苏渡苇
 * │ 💻 GitHub：https://github.com/iweidujiang
 * │ 📅 @date 2026/1/26
 * └───────────────────────────────────────────────
 */
public class ThreadSafetyDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 可变版本（有并发问题） ===");
        testMutable();

        System.out.println("\n=== 不可变版本（线程安全） ===");
        testImmutable();
    }

    static void testMutable() throws InterruptedException {
        MutableDateFormatter formatter = new MutableDateFormatter();

        Thread t1 = new Thread(() -> {
            formatter.setPattern("yyyy-MM-dd");
            System.out.println("Thread-1: " + formatter.format(new Date()));
        });

        Thread t2 = new Thread(() -> {
            formatter.setPattern("dd/MM/yyyy");
            System.out.println("Thread-2: " + formatter.format(new Date()));
        });

        t1.start(); t2.start();
        t1.join(); t2.join();
    }

    static void testImmutable() throws InterruptedException {
        // 每个线程创建自己的不可变实例
        Thread t1 = new Thread(() -> {
            ImmutableDateFormatter f = new ImmutableDateFormatter("yyyy-MM-dd");
            System.out.println("Thread-1: " + f.format(new Date()));
        });

        Thread t2 = new Thread(() -> {
            ImmutableDateFormatter f = new ImmutableDateFormatter("dd/MM/yyyy");
            System.out.println("Thread-2: " + f.format(new Date()));
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}

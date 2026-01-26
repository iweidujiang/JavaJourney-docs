package io.github.iweidujiang.stringbutler.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ┌───────────────────────────────────────────────
 * │ 📦 可变的时间格式化对象
 * │
 * │ 👤 作者：苏渡苇
 * │ 🔗 公众号：苏渡苇
 * │ 💻 GitHub：https://github.com/iweidujiang
 * │ 📅 @date 2026/1/26
 * └───────────────────────────────────────────────
 */
public class MutableDateFormatter {
    private String pattern = "yyyy-MM-dd";

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String format(Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static void main(String[] args) throws InterruptedException {
        MutableDateFormatter formatter = new MutableDateFormatter();

        Thread t1 = new Thread(() -> {
            formatter.setPattern("yyyy-MM-dd");
            System.out.println("Thread-1: " + formatter.format(new Date()));
        });

        Thread t2 = new Thread(() -> {
            formatter.setPattern("dd/MM/yyyy");
            System.out.println("Thread-2: " + formatter.format(new Date()));
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}

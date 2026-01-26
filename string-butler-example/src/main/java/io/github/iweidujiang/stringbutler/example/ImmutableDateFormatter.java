package io.github.iweidujiang.stringbutler.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ┌───────────────────────────────────────────────
 * │ 📦 不可变时间格式化
 * │
 * │ 👤 作者：苏渡苇
 * │ 🔗 公众号：苏渡苇
 * │ 💻 GitHub：https://github.com/iweidujiang
 * │ 📅 @date 2026/1/26
 * └───────────────────────────────────────────────
 */
public final class ImmutableDateFormatter {
    private final String pattern;

    public ImmutableDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    public String format(Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    // 返回新实例，原对象不变
    public ImmutableDateFormatter withPattern(String newPattern) {
        return new ImmutableDateFormatter(newPattern);
    }
}

package com.xxmicloxx.NoteBlockAPI.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final String baseName;
    private final AtomicInteger threadCount = new AtomicInteger(1);

    public NamedThreadFactory(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(baseName + "-Thread-" + threadCount.getAndIncrement());
        return thread;
    }
}

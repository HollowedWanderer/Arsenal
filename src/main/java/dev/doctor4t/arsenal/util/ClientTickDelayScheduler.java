package dev.doctor4t.arsenal.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientTickDelayScheduler {
    public static final Map<Integer, DelayHandler> scheduledTasks = new ConcurrentHashMap<>();
    private static int taskIdCounter = 0;
    public static boolean run = false;

    public static void schedule(int ticks, Runnable task) {
        int taskId = taskIdCounter++;
        scheduledTasks.put(taskId, new DelayHandler(ticks, task, taskId));
    }

    public static void tick() {
        if (!scheduledTasks.isEmpty()) {
            for(int i = 0; i < scheduledTasks.size(); ++i) {
                DelayHandler handler = scheduledTasks.values().stream().toList().get(i);
                if (handler.ticks == -1) {
                    if (run) {
                        handler.task.run();
                        scheduledTasks.remove(handler.id);
                        if (scheduledTasks.isEmpty()) {
                            break;
                        }
                    }
                } else if (handler.ticks > 0) {
                    --handler.ticks;
                } else {
                    handler.task.run();
                    scheduledTasks.remove(handler.id);
                    if (scheduledTasks.isEmpty()) {
                        break;
                    }
                }
            }
        }

    }

    public static class DelayHandler {
        public int ticks;
        public Runnable task;
        public int id;

        public DelayHandler(int ticks, Runnable task, int id) {
            this.ticks = ticks;
            this.task = task;
            this.id = id;
        }
    }
}
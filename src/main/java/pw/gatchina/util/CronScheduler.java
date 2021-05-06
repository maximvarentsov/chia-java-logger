package pw.gatchina.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class CronScheduler {
    private class InternalTask implements Callable<Void> {
        private final Object runnableOrCallable;
        private final SchedulingPattern schedulingPattern;

        public InternalTask(final @NotNull Object runnableOrCallable, final @NotNull SchedulingPattern schedulingPattern) {
            this.runnableOrCallable = runnableOrCallable;
            this.schedulingPattern = schedulingPattern;
        }

        @Override
        public Void call() throws Exception {
            try {
                if (runnableOrCallable instanceof Runnable) {
                    ((Runnable) runnableOrCallable).run();
                } else if (runnableOrCallable instanceof Callable) {
                    ((Callable) runnableOrCallable).call();
                }
            } catch (Throwable th) {
                logger.error(th.getMessage(), th);
            }

            startImpl(this);

            return null;
        }

        public Object getRunnableOrCallable() {
            return runnableOrCallable;
        }

        public SchedulingPattern getSchedulingPattern() {
            return schedulingPattern;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(CronScheduler.class);
    private final Map<InternalTask, Future<?>> tasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService;

    public CronScheduler() {
        this(Integer.MAX_VALUE);
    }

    public CronScheduler(final int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
    }

    public CronScheduler(final @NotNull ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public long start(final @NotNull String pattern, final @NotNull Runnable runnable) {
        var schedulingPattern = new SchedulingPattern(pattern);
        return startImpl(new InternalTask(runnable, schedulingPattern));
    }

    public long start(final @NotNull String pattern, final @NotNull Callable<?> callable) {
        var schedulingPattern = new SchedulingPattern(pattern);
        return startImpl(new InternalTask(callable, schedulingPattern));
    }

    public void stop(final @NotNull Callable<?> callable) {
        stopImpl(callable);
    }

    public void stop(final @NotNull Runnable runnable) {
        stopImpl(runnable);
    }

    private long startImpl(final @NotNull InternalTask internalTask) {
        var schedulingPattern = internalTask.getSchedulingPattern();

        var millis = System.currentTimeMillis();
        var next = schedulingPattern.next(millis);

        var diff = next - millis;

        var future = executorService.schedule(internalTask, diff, TimeUnit.MILLISECONDS);

        tasks.put(internalTask, future);
        return next;
    }

    private void stopImpl(final @NotNull Object runnableOrCallable) {
        for (var entry : tasks.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (key.getRunnableOrCallable() == runnableOrCallable) {
                value.cancel(false);
                break;
            }
        }
    }

    public void shutdown() {
        for (var future : tasks.values()) {
            future.cancel(false);
        }
    }
}


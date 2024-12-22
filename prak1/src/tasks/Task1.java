package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


public class Task1 {
    // Ильин Владислав Викторович ИКБО-01-21
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final int capacity = 10000;
        List<Integer> list = randomList(capacity);
        System.out.println("Sequential execution");
        long startTime = System.currentTimeMillis();
        sumInList(list);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        System.out.println("Using Executors");
        startTime = System.currentTimeMillis();
        sumInListUsingExecutors(list);
        endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        System.out.println("Using ForkJoin and Recursive tasks");
        startTime = System.currentTimeMillis();
        sumInListUsingForkJoin(list);
        endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    public static List<Integer> randomList(int countElement) {
        Random rand = new Random();
        List<Integer> list = new ArrayList<>(countElement);
        for (int i = 0; i < countElement; i++) {
            list.add(rand.nextInt());
        }
        return list;
    }
    // Ильин Владислав Викторович ИКБО-01-21
    private static int sumInList(List<Integer> list) throws InterruptedException {
        int sum = 0;
        for (int i : list) {
            sum += i;
            Thread.sleep(1);
        }
        return sum;
    }

    // Ильин Владислав Викторович ИКБО-01-21
    private static int sumInListUsingExecutors(List<Integer> list) throws InterruptedException, ExecutionException {
        int sum = 0;
        int availableThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableThreads);
        List<Callable<Integer>> tasks = new ArrayList<>(availableThreads);
        int batchSize = list.size() / availableThreads;

        for (int i = 0; i < availableThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == availableThreads - 1) ? list.size() : (i + 1) * batchSize;
            tasks.add(() -> sumInList(list.subList(startIndex, endIndex)));
        }
        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        for (Future<Integer> future : futures) {
            sum += future.get();
        }
        executorService.shutdown();
        return sum;
    }

    // Ильин Владислав Викторович ИКБО-01-21
    private static int sumInListUsingForkJoin(List<Integer> list) throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        SumFinderTask task = new SumFinderTask(list, 0, list.size());

        return forkJoinPool.invoke(task);
    }


    // Ильин Владислав Викторович ИКБО-01-21
    static class SumFinderTask extends RecursiveTask<Integer> {
        private final List<Integer> list;
        private final int start;
        private final int end;

        public SumFinderTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 1000) {
                try {
                    return sumInList(list.subList(start, end));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            int middle = start + (end - start) / 2;
            SumFinderTask left = new SumFinderTask(list, start, middle);
            SumFinderTask right = new SumFinderTask(list, middle, end);

            left.fork();
            int rightSum = right.compute();
            int leftSum = left.join();
            return leftSum + rightSum;
        }
    }
}
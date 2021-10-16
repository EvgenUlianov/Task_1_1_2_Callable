import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
     public static void main(String[] args) {
          final int timeOut15 = 15_000;
          final int timeOut5 = 5_000;
          final int numberOfTreads = 4;
          System.out.println("Задача 2. Межпоточный диалог со счетчиком");

          final ExecutorService threadPool = Executors.newFixedThreadPool(numberOfTreads);


          List<MyCallable> treads = new ArrayList<>();
          for (int i = 0; i < numberOfTreads; i++)
               treads.add(new MyCallable());
          List<Future<Integer>> tasks = new ArrayList<>();


          // invokeAll запускает и ждет, мне так не нравится...
          // tasks = threadPool.invokeAll(treads, timeOut15 + timeOut5, TimeUnit.MILLISECONDS);
          for (Callable<Integer> tread: treads) {
              // final Future<Integer> task = new F{uture<Integer>()

               final Future<Integer> task = threadPool.submit(tread);

               tasks.add(task);
          }

          try {
               Thread.sleep(timeOut5);
          } catch (InterruptedException e) {
               e.printStackTrace();
          }

          ThreadsBuffer buffer = ThreadsBuffer.get();
          buffer.setSleepComand(treads.get(0).getCurrentName(), Boolean.TRUE);
          buffer.setSleepComand(treads.get(1).getCurrentName(), Boolean.TRUE);
          buffer.setSleepComand(treads.get(2).getCurrentName(), Boolean.TRUE);

          try {
               Thread.sleep(timeOut5 + timeOut15);
          } catch (InterruptedException e) {
               e.printStackTrace();
          }

          threadPool.shutdownNow();
          try {
               threadPool.awaitTermination(1_000,TimeUnit.MILLISECONDS);
          } catch (InterruptedException e) {
               e.printStackTrace();
          }

          for (Future<Integer> task: tasks) {
                    final Integer resultOfTask;
                    try {
                         resultOfTask = task.get();
                         System.out.println(resultOfTask);
                    } catch (InterruptedException | ExecutionException e) {
                         e.printStackTrace();
                    }
          }

     }
}

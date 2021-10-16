import java.util.concurrent.Callable;

class MyCallable extends Thread implements Callable<Integer> {

    public String getCurrentName() {
        String currentName = super.getName();
        return currentName;
    }

    @Override
    public Integer call() {
        final int timeOut15 = 15_000;
        final int timeOut = 2500;
        int counter = 0;
        String currentName = super.getName();
        ThreadsBuffer buffer = ThreadsBuffer.get();

        try {
            while(!isInterrupted()) {
                Thread.sleep(timeOut);
                System.out.printf("Я поток %s. Всем привет! (сообщение № %d)\n", currentName, ++counter);
                buffer.setCounter(currentName, counter);

                if (buffer.getSleepComand(currentName)){
                    boolean needToSleep = false;
                    synchronized (buffer) {
                        // мы этот момент еще не изучали, но судя по всему, правильно делать именно так
                        if (buffer.getSleepComand(currentName)){
                            needToSleep = true;
                            buffer.setSleepComand(currentName, false);
                        }
                    }
                    if (needToSleep) {
                        System.out.printf("Я поток %s. Я ложусь спать!\n", currentName);
                        Thread.sleep(timeOut15);
                        System.out.printf("Я поток %s. Я долго спал!\n", currentName);
                    }
                }
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } finally{

                System.out.printf("%s завершен\n", getName());

        }
        return counter;
    }
}

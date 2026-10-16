import java.util.LinkedList;

public class ProducerConsumer {
    static class PC {
        private final LinkedList<Integer> list = new LinkedList<>();
        private final int capacity;

        public PC(int capacity) {
            this.capacity = capacity;
        }

        public void produce() {
            int value = 0;
            while (true) {
                synchronized (this) {
                    while (list.size() == capacity) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    list.add(value);
                    System.out.println("Producer produced-" + value);
                    value++;
                    notify();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        public void consume() {
            while (true) {
                synchronized (this) {
                    while (list.isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    int value = list.removeFirst();
                    System.out.println("Consumer consumed-" + value);
                    notify();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PC pc = new PC(2);

        Thread producer = new Thread(pc::produce, "Producer");
        Thread consumer = new Thread(pc::consume, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}

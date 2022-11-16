import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Philosopher implements Runnable {

    static int counter = 0;

     Object leftFork;
     Object rightFork;
    private boolean isFull;
    private int id = 0;
    private Philosopher[] philosophers;

    public Philosopher(Object leftFork, Object rightFork, Philosopher[] tab) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.isFull = false;
        id = ++counter;
        philosophers = tab;
    }

    private void think() throws InterruptedException {
        System.out.println("Wątek " + id + " myśli.");
        Thread.sleep(((int) (Math.random() * 100)));
    }

    static synchronized private void eat(Philosopher tmp) throws InterruptedException {
        System.out.println("Wątek " + tmp.id + " zaczyna posiłek.");
        Thread.sleep(((int) (Math.random() * 100)));
    }


    @Override
    public void run() {
        try {
            while (!isFull) {
                think();
                eat(this);
                isFull = true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}

class PhilosopherOnLock implements Runnable {

    static int counter = 0;

    private Lock leftFork;
    private Lock rightFork;
    private boolean isFull;
    private int id = 0;

    public PhilosopherOnLock(Boolean leftFork, Boolean rightFork) {
        this.leftFork = new ReentrantLock(leftFork);
        this.rightFork = new ReentrantLock(rightFork);
        this.isFull = false;
        id = ++counter;
    }

    private void think() throws InterruptedException {
        System.out.println("Wątek " + id + " czeka na widelec.");
        Thread.sleep(((int) (Math.random() * 100)));
    }

    private void eat() throws InterruptedException {
        System.out.println("Wątek " + id + " zaczyna posiłek.");
        Thread.sleep(((int) (Math.random() * 100)));
    }

    @Override
    public void run() {
        try {
            while (!isFull) {
                think();
                if (leftFork.tryLock()) {
                    if (rightFork.tryLock()) {
                        eat();
                        isFull = true;
                    } else {
                        leftFork.unlock();
                        think();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}

class Dining {

    public static void main(String[] args) throws Exception {

        int numberOfPhil = 5;

        final Philosopher[] philosophers = new Philosopher[numberOfPhil];
        final PhilosopherOnLock[] philosophersOnLock = new PhilosopherOnLock[numberOfPhil];
        final Thread[] threads = new Thread[numberOfPhil];
        Boolean[] forks = new Boolean[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Boolean(false);
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];
            philosophers[i] = new Philosopher( leftFork, rightFork, philosophers);

            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }



//        for (int i = 0; i < philosophers.length; i++) {
//            Boolean leftFork = forks[i];
//            Boolean rightFork = forks[(i + 1) % forks.length];
//            philosophersOnLock[i] = new PhilosopherOnLock(rightFork, leftFork);
//
//            Thread t = new Thread(philosophersOnLock[i]);
//            t.start();
//        }
    }
}


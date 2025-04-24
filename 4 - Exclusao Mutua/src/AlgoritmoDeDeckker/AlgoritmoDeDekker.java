package AlgoritmoDeDeckker;

public class AlgoritmoDeDekker {
    public static void main(String[] args) {
        Lock lock = new LockImpl();
        Thread t1 = new MyLockThread(lock, 0);
        Thread t2 = new MyLockThread(lock, 1);

        t1.start();
        t2.start();
    }

    static class MyLockThread extends Thread {
        Lock lock;
        int id;

        public MyLockThread(Lock lock, int id) {
            this.lock = lock;
            this.id = id;
        }

        public void run() {
            while (true) {
                lock.requestCS(id);
                System.out.println("Thread " + id + " entrou na seção crítica");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id);
            }
        }
    }
}

interface Lock {
    public void requestCS(int id);
    public void releaseCS(int id);
}

class LockImpl implements Lock {
    boolean wantCS[] = {false, false};
    int turn = 1;

    public void requestCS(int id) {
        int other = 1 - id;
        wantCS[id] = true;
        while ((wantCS[other])) {

            if (turn == other) {
                wantCS[id] = false;
                while (turn == other) Thread.yield();
                wantCS[id] = true;
            }
        }
    }

    public void releaseCS(int id) {
        wantCS[id] = false;

        turn = 1 - id;
    }
}

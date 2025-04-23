package VariavelBooleana;

// Não garante a exclusão mútua
//devido a condição de corrida, duas threads podem ler ao mesmo tempo tempo
//   public void requestCS(){
//        while (busy)
//        busy = true;
//   }

public class Boolean {
    public static void main(String[] args) {
        Lock lock = new LockImpl();
        Thread t1 = new MyLockThread(lock);
        Thread t2 = new MyLockThread(lock);

        t1.start();
        t2.start();

    }

    static class MyLockThread extends Thread{
        Lock lock;

        public MyLockThread(Lock lock) {
            this.lock = lock;
        }

        public void run(){
            while(true){
                lock.requestCS();
                // Critical section
                System.out.println("Thread " + Thread.currentThread().getName() + " in critical section");
                try {
                    Thread.sleep(2000); // Simulate work in critical section
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.releaseCS();
            }
        }
    }
}


interface Lock {
    public void requestCS();
    public void releaseCS();
}

class LockImpl implements Lock {
    private boolean busy = false;

    public void requestCS(){
        while (busy)
        busy = true;
    }

    public void releaseCS() {
        busy = false;
    }
}
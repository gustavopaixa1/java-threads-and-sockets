package VariavelDeSelecao;


// Não garante a exclusão mútua

import java.sql.Time;

public class Variavel {
    public static void main(String[] args) {
        Lock lock = new LockImpl();
        Thread t1 = new MyLockThread(lock,0, 3000L);
        Thread t2 = new MyLockThread(lock,1, 500L);

        t1.start();
        t2.start();

    }

    static class MyLockThread extends Thread{
        Lock lock;
        int id;
        Long ms;

        public MyLockThread(Lock lock, Integer id, Long ms) {
            this.lock = lock;
            this.id = id;
            this.ms = ms;
        }

        public void run(){
            while(true){
                lock.requestCS(id);
                // Critical section
                System.out.println("Thread " + Thread.currentThread().getName() + " in critical section");
                try {
                    Thread.sleep(ms); // Simulate work in critical section
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
    volatile int turn = 0;

    public void requestCS(int id){
        //System.out.println("Thread " + id + " requesting critical section");
        while(turn == 1 - id){
            Thread.yield();
        }
    }

    public void releaseCS(int id) {
        //System.out.println("Thread " + id + " releasing critical section");
        turn = 1 - id;
    }
}
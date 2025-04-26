package InstrucoesAtomicas;

import java.util.concurrent.atomic.AtomicBoolean;

public class AlgoritmoTestAndSet {
    public static void main(String[] args) {
        int numThreads = 5; // Número de processos
        Lock lock = new LockImpl();

        for (int i = 0; i < numThreads; i++) {
            new MyLockThread(lock, i).start();
        }
    }

    static class MyLockThread extends Thread {
        private final Lock lock;
        private final int id;

        public MyLockThread(Lock lock, int id) {
            this.lock = lock;
            this.id = id;
        }

        public void run() {
            while (true) {
                lock.requestCS(id);
                System.out.println("🟢 Thread " + id + " entrou na seção crítica");

                try {
                    Thread.sleep(1000); // Simula trabalho na seção crítica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("🔴 Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id);
            }
        }
    }
}

// Interface Lock padrão
interface Lock {
    public void requestCS(int id);
    public void releaseCS(int id);
}

// Implementação de Lock usando TestAndSet
class LockImpl implements Lock {
    private boolean lock = false; // false = livre, true = ocupado

    // Implementação do testAndSet
    private synchronized boolean testAndSet() {
        boolean old = lock;
        if(!lock) lock = true;
        return old;
    }

    public void requestCS(int id) {
        while (testAndSet()) {
            // Busy waiting até conseguir pegar o lock
        }
    }

    public void releaseCS(int id) {
        lock = false; // Libera o lock ao sair da seção crítica
    }
}

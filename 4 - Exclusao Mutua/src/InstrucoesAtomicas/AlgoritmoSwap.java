package InstrucoesAtomicas;

public class AlgoritmoSwap {
    public static void main(String[] args) {
        int numThreads = 5; // Número de processos
        Lock lock = new LockImpl2();

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

// Classe Swap conforme o slide
class Swap {
    volatile private boolean value = false;

    public synchronized boolean swap(boolean newValue) {
        boolean temp = value;
        value = newValue;
        return temp;
    }
}

// Implementação de Lock usando Swap
class LockImpl2 implements Lock {
    Swap flag = new Swap(); // usa a classe Swap

    public void requestCS(int id) {
        while (flag.swap(true)) {
            // Espera até conseguir entrar (key ficar false)
        }
    }

    public void releaseCS(int id) {
        // Ao liberar, devemos setar o value do Swap para false
        // Como a swap(newValue) sempre muda value, aqui fazemos:
        flag.swap(false);
    }
}

package AlgoritmoDeDijkstra;

public class AlgoritmoDeDijkstra {
    public static void main(String[] args) {
        int numThreads = 5; // Número de processos
        Lock lock = new LockImpl(numThreads);

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
                    Thread.sleep(1000); // Simula tempo de uso da seção crítica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("🔴 Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id);

                try {
                    Thread.sleep(500); // Simula tempo de "pensando"
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// Interface Lock (padrão)
interface Lock {
    public void requestCS(int id);
    public void releaseCS(int id);
}

// Implementação sem synchronized
class LockImpl implements Lock {
    private final int[] status; // 0 = pensando, 1 = tentando entrar, 2 = na seção crítica
    private int turn;
    private int nproc;

    public LockImpl(int nproc) {
        this.nproc = nproc;
        this.status = new int[nproc];
        for (int i = 0; i < nproc; i++) {
            status[i] = 0; // Inicialmente todos estão pensando
        }
        turn = 0; // Inicia com processo 0
    }

    public void requestCS(int id) {
        do {
            status[id] = 1; // Tentando entrar

            // Espera até ser sua vez
            while (turn != id) {
                if (status[turn] == 0) {
                    turn = id;
                }
                Thread.yield(); // Melhora um pouco a eficiência do busy waiting
            }

            status[id] = 2; // Entra na seção crítica

            // Verifica se há algum outro processo na seção crítica
            // O código do slide nao funcionou, utilizei esse aqui
            boolean otherInCritical = false;
            for (int j = 0; j < nproc; j++) {
                if (j != id && status[j] == 2) {
                    otherInCritical = true;
                    break;
                }
            }

            if (!otherInCritical) {
                break; // Pode entrar na seção crítica
            }
        } while (true);
    }

    public void releaseCS(int id) {
        status[id] = 0; // Sai da seção crítica
        turn = (id + 1) % nproc; // Passa a vez
    }
}

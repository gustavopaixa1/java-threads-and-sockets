package AlgoritmoDeBakery;

public class AlgoritmoDeBakery {
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
                    Thread.sleep(1000); // Simula uso da seção crítica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("🔴 Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id);

                try {
                    Thread.sleep(500); // Simula tempo "pensando"
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// Interface padrão
interface Lock {
    public void requestCS(int id);
    public void releaseCS(int id);
}

// Implementação do Bakery com log dos tickets
class LockImpl implements Lock {
    volatile private boolean[] choosing; // choosing[i] = true se o processo i está escolhendo seu número
    volatile private int[] number;        // number[i] = ticket do processo i
    volatile private int nproc;           // Número de processos

    public LockImpl(int nproc) {
        this.nproc = nproc;
        this.choosing = new boolean[nproc];
        this.number = new int[nproc];
        for (int i = 0; i < nproc; i++) {
            choosing[i] = false;
            number[i] = 0;
        }
    }

    public void requestCS(int id) {
        choosing[id] = true;

        // Escolhe o maior número atual + 1
        int max = 0;
        for (int i = 0; i < nproc; i++) {
            if (number[i] > max) {
                max = number[i];
            }
        }
        number[id] = max + 1;
        choosing[id] = false;

        // 📝 Adicionado: LOG dos tickets escolhidos
        printTickets(id);

        // Espera sua vez
        for (int j = 0; j < nproc; j++) {
            if (j == id) continue;

            // Espera o processo j terminar de escolher
            while (choosing[j]) ;

            // Espera enquanto:
            // 1. j tenha um número diferente de zero (quer entrar)
            // 2. (number[j], j) < (number[id], id)
            while (number[j] != 0 && (number[j] < number[id] || (number[j] == number[id] && j < id))) ;
        }
    }

    public void releaseCS(int id) {
        number[id] = 0; // Libera a seção crítica
    }

    // 🔵 Método para imprimir o vetor de tickets
    private void printTickets(int id) {
        System.out.print("🎟️ Tickets após escolha da Thread " + id + ": [ ");
        for (int i = 0; i < nproc; i++) {
            System.out.print(number[i] + " ");
        }
        System.out.println("]");
    }
}

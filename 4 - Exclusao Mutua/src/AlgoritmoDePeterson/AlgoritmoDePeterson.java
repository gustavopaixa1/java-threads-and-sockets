package AlgoritmoDePeterson;


public class AlgoritmoDePeterson {
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
                lock.requestCS(id); // requisita entrada na seção crítica
                System.out.println("Thread " + id + " entrou na seção crítica");

                try {
                    Thread.sleep(1000); // simula trabalho na seção crítica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id); // libera a seção crítica
            }
        }
    }
}

// Interface Lock (igual à sua)
interface Lock {
    public void requestCS(int id); // Requisita a seção crítica
    public void releaseCS(int id); // Libera a seção crítica
}

// Implementação do Lock usando Peterson
class LockImpl implements Lock {
    private final boolean[] wantCS = {false, false}; // Flags de intenção de entrar
    private int turn = 0; // De quem é a vez (0 ou 1)

    public void requestCS(int id) {
        int other = 1 - id; // Calcula o id da outra thread

        wantCS[id] = true;     // Diz que quer entrar
        turn = other;          // Dá a vez ao outro

        // Espera enquanto o outro quiser entrar E for a vez dele
        while (wantCS[other] && turn == other) {
            Thread.yield(); // Libera CPU para outras threads
        }
    }

    public void releaseCS(int id) {
        wantCS[id] = false; // Diz que não quer mais a seção crítica
    }
}


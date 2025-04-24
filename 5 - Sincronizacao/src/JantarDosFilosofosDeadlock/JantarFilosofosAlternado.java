package JantarDosFilosofosDeadlock;

import java.util.concurrent.Semaphore;

public class JantarFilosofosAlternado {

    static class Mesa {
        private final Semaphore[] garfos; // Um semáforo para cada garfo

        public Mesa(int numFilosofos) {
            garfos = new Semaphore[numFilosofos];
            for (int i = 0; i < numFilosofos; i++) {
                garfos[i] = new Semaphore(1); // Cada garfo só pode ser usado por um filósofo por vez
            }
        }

        // Métdo onde o filósofo tenta pegar os dois garfos
        public void pegarGarfos(int id) throws InterruptedException {
            int primeiro, segundo;

            // 🧠 Alternância para evitar deadlock:
            // Filosofias com ID par: pegam garfo da esquerda primeiro, depois da direita.
            // Filosofias com ID ímpar: pegam da direita primeiro, depois da esquerda.
            if (id % 2 == 0) {
                primeiro = id;
                segundo = (id + 1) % garfos.length;
            } else {
                primeiro = (id + 1) % garfos.length;
                segundo = id;
            }

            // 🔐 Espera para pegar o primeiro garfo (semaforo de garfo)
            garfos[primeiro].acquire();

            // 🔐 Espera para pegar o segundo garfo (semaforo de garfo)
            garfos[segundo].acquire();
        }

        // Métdo onde o filósofo devolve os dois garfos
        public void devolverGarfos(int id) {
            // Libera os semáforos dos dois garfos que estavam com o filósofo

            garfos[id].release(); // libera o garfo da esquerda
            garfos[(id + 1) % garfos.length].release(); // libera o garfo da direita
        }
    }

    static class Filosofo extends Thread {
        private final int id;
        private final Mesa mesa;

        public Filosofo(int id, Mesa mesa) {
            this.id = id;
            this.mesa = mesa;
        }

        public void run() {
            try {
                while (true) {
                    pensar();
                    mesa.pegarGarfos(id);
                    comer();
                    mesa.devolverGarfos(id);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void pensar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está pensando...");
            Thread.sleep((int) (1000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está comendo!");
            Thread.sleep((int) (1000));
        }
    }

    public static void main(String[] args) {
        int numFilosofos = 5;
        Mesa mesa = new Mesa(numFilosofos);

        for (int i = 0; i < numFilosofos; i++) {
            new Filosofo(i, mesa).start();
        }
    }
}

package Monitores.JantarDosFilosofos;

public class JantarFilosofosMonitor {

    // Classe que representa a mesa com os garfos
    static class Mesa {
        private final boolean[] garfos;
        private final int lugares;

        public Mesa(int lugares) {
            this.lugares = lugares;
            this.garfos = new boolean[lugares];
            for (int i = 0; i < lugares; i++) {
                garfos[i] = false; // todos os garfos estão livres no início
            }
        }

        // Métdo synchronized: filósofo tenta pegar os dois garfos
        public synchronized void acquire(int id) {
            int i = id;
            int j = (id + 1) % lugares;

            // Espera até que os dois garfos estejam livres
            // Esse código já está corrigido para previnir deadlock
            // caso tente pegar um garfo de casa vez entra em deadlock
            while (garfos[i] || garfos[j]) {
                try {
                    wait(); // suspende a thread e libera o monitor
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Marca os garfos como ocupados
            garfos[i] = true;
            garfos[j] = true;
        }

        // Métdo synchronized: filósofo devolve os garfos
        public synchronized void release(int id) {
            int i = id;
            int j = (id + 1) % lugares;

            garfos[i] = false;
            garfos[j] = false;

            notifyAll(); // acorda outros filósofos que podem estar esperando
        }
    }

    // Classe filósofo que pensa, tenta pegar os garfos, come, devolve e repete
    static class Filosofo extends Thread {
        private final int id;
        private final Mesa mesa;

        public Filosofo(int id, Mesa mesa) {
            this.id = id;
            this.mesa = mesa;
        }

        public void run() {
            while (true) {
                pensar();
                mesa.acquire(id);
                comer();
                mesa.release(id);
            }
        }

        private void pensar() {
            System.out.println("🤔 Filósofo " + id + " está pensando...");
            try {
                Thread.sleep(500 + (int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void comer() {
            System.out.println("🍝 Filósofo " + id + " está comendo!");
            try {
                Thread.sleep(1000 + (int) (Math.random() * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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


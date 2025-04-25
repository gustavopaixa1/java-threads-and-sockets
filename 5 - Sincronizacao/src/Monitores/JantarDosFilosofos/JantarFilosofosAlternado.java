package Monitores.JantarDosFilosofos;

public class JantarFilosofosAlternado {

    // Monitor da mesa com array de garfos
    static class Mesa {
        private final boolean[] garfos;
        private final int lugares;

        public Mesa(int lugares) {
            this.lugares = lugares;
            this.garfos = new boolean[lugares];
            for (int i = 0; i < lugares; i++) {
                garfos[i] = false; // todos os garfos estão livres inicialmente
            }
        }

        // Método synchronized: o filósofo tenta pegar seus dois garfos
        public synchronized void pegarGarfos(int id) throws InterruptedException {
            int garfoEsquerda = id;
            int garfoDireita = (id + 1) % lugares;

            // 🧠 Alterna a ordem de aquisição dos garfos
            if (id % 2 == 0) {
                // Filosofos com ID par: pegam primeiro o da esquerda
                while (garfos[garfoEsquerda]) wait();
                garfos[garfoEsquerda] = true;

                while (garfos[garfoDireita]) wait();
                garfos[garfoDireita] = true;
            } else {
                // Filosofos com ID ímpar: pegam primeiro o da direita
                while (garfos[garfoDireita]) wait();
                garfos[garfoDireita] = true;

                while (garfos[garfoEsquerda]) wait();
                garfos[garfoEsquerda] = true;
            }
        }

        // Método synchronized: o filósofo devolve seus dois garfos
        public synchronized void devolverGarfos(int id) {
            int garfoEsquerda = id;
            int garfoDireita = (id + 1) % lugares;

            garfos[garfoEsquerda] = false;
            garfos[garfoDireita] = false;

            notifyAll(); // acorda filósofos que podem estar esperando
        }
    }

    // Classe Filósofo
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
            System.out.println("🤔 Filósofo " + id + " está pensando...");
            Thread.sleep(500 + (int) (Math.random() * 500));
        }

        private void comer() throws InterruptedException {
            System.out.println("🍝 Filósofo " + id + " está comendo!");
            Thread.sleep(1000 + (int) (Math.random() * 500));
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


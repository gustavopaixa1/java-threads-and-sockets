package JantarDosFilosofosDeadlock;

import java.util.concurrent.Semaphore;

public class JantarFilosofosComMutex {

    static class Mesa {
        private final Semaphore[] garfos;
        private final Semaphore mutex; // protege a seção onde os garfos são adquiridos

        public Mesa(int numFilosofos) {
            garfos = new Semaphore[numFilosofos];
            for (int i = 0; i < numFilosofos; i++) {
                garfos[i] = new Semaphore(1);
            }
            mutex = new Semaphore(1); // garante exclusividade no ato de pegar os dois garfos
        }

        // Versão limitante, pois apenas um dos filosofos pega os garfos por vez.
        public void pegarGarfos(int id) throws InterruptedException {
            mutex.acquire(); // impede que outros filósofos entrem nessa parte
            garfos[id].acquire(); // pega garfo da esquerda
            garfos[(id + 1) % garfos.length].acquire(); // pega garfo da direita
            mutex.release(); // libera acesso para outro filósofo tentar pegar garfos
        }

        public void devolverGarfos(int id) {
            garfos[id].release();
            garfos[(id + 1) % garfos.length].release();
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
            Thread.sleep((int) (Math.random() * 1000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está comendo!");
            Thread.sleep((int) (Math.random() * 1000));
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
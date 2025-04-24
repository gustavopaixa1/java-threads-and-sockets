package JantarDosFilosofosDeadlock;

import java.util.concurrent.Semaphore;

/**
 * Classe que implementa o problema do Jantar dos Filósofos utilizando semáforos.
 * Este código pode levar a um deadlock, pois todos os filósofos podem pegar um garfo
 * e ficar esperando pelo outro, sem liberar os recursos.
 */
public class JantarFilosofos {

    /**
     * Classe que representa a mesa com os garfos compartilhados entre os filósofos.
     */
    static class Mesa {
        private final Semaphore[] garfos; // Array de semáforos representando os garfos


        public Mesa(int numFilosofos) {
            garfos = new Semaphore[numFilosofos];
            for (int i = 0; i < numFilosofos; i++) {
                garfos[i] = new Semaphore(1); // Cada garfo pode ser usado por um filósofo por vez
            }
        }

        /**
         * Métdo para um filósofo pegar os garfos necessários para comer.
         *
         */

        // O DEADLOCK ACONTECE AQUI
        //
        public void pegarGarfos(int id) throws InterruptedException {
            garfos[id].acquire(); // Pega o garfo da esquerda
            garfos[(id + 1) % garfos.length].acquire(); // Pega o garfo da direita
        }

        /**
         * Métdo para um filósofo devolver os garfos após comer.
         *
         * @param id Identificador do filósofo.
         */
        public void devolverGarfos(int id) {
            garfos[id].release(); // Devolve o garfo da esquerda
            garfos[(id + 1) % garfos.length].release(); // Devolve o garfo da direita
        }
    }

    static class Filosofo extends Thread {
        private final int id; // Identificador do filósofo
        private final Mesa mesa; // Referência à mesa compartilhada

        public Filosofo(int id, Mesa mesa) {
            this.id = id;
            this.mesa = mesa;
        }

        /**
         * O filósofo alterna entre pensar e comer.
         */
        public void run() {
            try {
                while (true) {
                    pensar(); // Simula o filósofo pensando
                    mesa.pegarGarfos(id); // Tenta pegar os garfos para comer
                    comer(); // Simula o filósofo comendo
                    mesa.devolverGarfos(id); // Devolve os garfos após comer
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrompe a thread em caso de erro
            }
        }

        /**
         * Métdo que simula o filósofo pensando.
         */
        private void pensar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está pensando...");
            Thread.sleep((int) (Math.random() * 500)); // Simula o tempo de pensamento
        }

        /**
         * Métdo que simula o filósofo comendo.
         */
        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está comendo!");
            Thread.sleep((int) (Math.random() * 500)); // Simula o tempo de alimentação
        }
    }

    public static void main(String[] args) {
        int numFilosofos = 5; // Número de filósofos e garfos
        Mesa mesa = new Mesa(numFilosofos); // Cria a mesa compartilhada

        // Cria e inicia as threads dos filósofos
        for (int i = 0; i < numFilosofos; i++) {
            new Filosofo(i, mesa).start();
        }
    }
}
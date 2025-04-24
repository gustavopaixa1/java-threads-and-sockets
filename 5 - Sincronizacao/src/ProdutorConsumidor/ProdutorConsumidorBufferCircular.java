package ProdutorConsumidor;

import java.util.concurrent.Semaphore;

// Com vaios produtores ou consumidores
// O buffer é circular e dá voltas graças ao código in = (in + 1) % tamanho;
public class ProdutorConsumidorBufferCircular {

    static class BufferCircular {
        private final String[] buffer;
        private final int tamanho;
        private int in = 0, out = 0;

        private final Semaphore empty;
        private final Semaphore full;
        // O mutex (mutual exclusion) garante que apenas uma thread acesse a sessão crítica por vez
        private final Semaphore mutex;

        public BufferCircular(int tamanho) {
            this.tamanho = tamanho;
            buffer = new String[tamanho];
            empty = new Semaphore(tamanho); // inicialmente todas posições livres
            full = new Semaphore(0);        // nenhum item disponível
            mutex = new Semaphore(1);       // exclusão mútua
        }

        public void depositar(String item) throws InterruptedException {
            empty.acquire(); // espera espaço
            mutex.acquire(); // entra na seção crítica
            buffer[in] = item;
            System.out.println("Produtor colocou: " + item + " na posição " + in);
            in = (in + 1) % tamanho;
            mutex.release(); // sai da seção crítica
            full.release();  // sinaliza item disponível
        }

        public String retirar() throws InterruptedException {
            full.acquire(); // espera item
            mutex.acquire(); // entra na seção crítica
            String item = buffer[out];
            System.out.println("Consumidor pegou: " + item + " da posição " + out);
            out = (out + 1) % tamanho;
            mutex.release(); // sai da seção crítica
            empty.release(); // sinaliza espaço livre
            return item;
        }
    }

    static class Produtor extends Thread {
        private final BufferCircular buffer;
        private final String nome;

        public Produtor(BufferCircular buffer, String nome) {
            this.buffer = buffer;
            this.nome = nome;
        }

        // Cada produtor tem seu próprio contador de item
        public void run() {
            int i = 0;
            while (true) {
                try {
                    buffer.depositar(nome + ": Item " + i++);

                    Thread.sleep(500); // produção mais rápida
                } catch (InterruptedException e) {
                }
            }
        }
    }

    static class Consumidor extends Thread {
        private final BufferCircular buffer;

        public Consumidor(BufferCircular buffer) {
            this.buffer = buffer;
        }

        public void run() {
            while (true) {
                try {
                    buffer.retirar();
                    Thread.sleep(1000); // consumo mais lento
                } catch (InterruptedException e) {
                }
            }
        }
    }

    // Cria o buffer e passa a referencia em memoria a cada thread
    public static void main(String[] args) {
        int tamanhoBuffer = 5;
        int numProdutores = 3;
        int numConsumidores = 2;

        BufferCircular buffer = new BufferCircular(tamanhoBuffer);

        for (int i = 0; i < numProdutores; i++) {
            new Produtor(buffer, "P" + i).start();
        }

        for (int i = 0; i < numConsumidores; i++) {
            new Consumidor(buffer).start();
        }
    }

}

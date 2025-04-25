package Monitores.ObjetosLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProdutorConsumidorMultiploLock {

    // Buffer circular para múltiplos produtores e consumidores
    static class BufferCircular {
        private final String[] buffer;
        private final int capacidade;
        private int in = 0, out = 0, count = 0;

        // Lock explícito em vez de synchronized
        private final ReentrantLock lock = new ReentrantLock();
        // Condição para quando o buffer não está cheio (produtor pode produzir)
        private final Condition notFull = lock.newCondition();
        // Condição para quando o buffer não está vazio (consumidor pode consumir)
        private final Condition notEmpty = lock.newCondition();

        public BufferCircular(int capacidade) {
            this.capacidade = capacidade;
            this.buffer = new String[capacidade];
        }

        // Métdo para produtores depositarem itens
        public void depositar(String item, int idProdutor) throws InterruptedException {
            lock.lock(); // adquire o lock (substitui synchronized)
            try {
                // enquanto o buffer estiver cheio, aguarda
                while (count == capacidade) {
                    System.out.println("⚠️ Produtor " + idProdutor + " esperando (buffer cheio)");
                    notFull.await(); // libera o lock e espera notificação
                }
                // insere o item
                buffer[in] = item;
                in = (in + 1) % capacidade;
                count++;
                System.out.println("🟩 Produtor " + idProdutor + " colocou: " + item +
                        " (itens no buffer: " + count + ")");
                notEmpty.signal(); // acorda um consumidor, se houver
            } finally {
                lock.unlock(); // sempre libera o lock no finally
            }
        }

        // Métdo para consumidores retirarem itens
        public String retirar(int idConsumidor) throws InterruptedException {
            lock.lock(); // adquire o lock
            try {
                // enquanto o buffer estiver vazio, aguarda
                while (count == 0) {
                    System.out.println("⚠️ Consumidor " + idConsumidor + " esperando (buffer vazio)");
                    notEmpty.await(); // libera o lock e espera notificação
                }
                // retira o item
                String item = buffer[out];
                out = (out + 1) % capacidade;
                count--;
                System.out.println("🟥 Consumidor " + idConsumidor + " pegou: " + item +
                        " (itens no buffer: " + count + ")");
                notFull.signal(); // acorda um produtor, se houver
                return item;
            } finally {
                lock.unlock(); // libera o lock
            }
        }
    }

    // Thread Produtor
    static class Produtor extends Thread {
        private final BufferCircular buffer;
        private final int id;

        public Produtor(BufferCircular buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        @Override
        public void run() {
            int i = 0;
            try {
                while (true) {
                    String item = "P" + id + "-Item" + i++;
                    buffer.depositar(item, id);
                    Thread.sleep(400 + (int)(Math.random() * 400)); // varia a produção
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Thread Consumidor
    static class Consumidor extends Thread {
        private final BufferCircular buffer;
        private final int id;

        public Consumidor(BufferCircular buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    buffer.retirar(id);
                    Thread.sleep(600 + (int)(Math.random() * 600)); // varia o consumo
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Programa principal
    public static void main(String[] args) {
        final int CAPACIDADE = 5;
        final int NUM_PRODUTORES = 3;
        final int NUM_CONSUMIDORES = 10;

        BufferCircular buffer = new BufferCircular(CAPACIDADE);

        // Inicia produtores
        for (int i = 0; i < NUM_PRODUTORES; i++) {
            new Produtor(buffer, i).start();
        }
        // Inicia consumidores
        for (int i = 0; i < NUM_CONSUMIDORES; i++) {
            new Consumidor(buffer, i).start();
        }
    }
}

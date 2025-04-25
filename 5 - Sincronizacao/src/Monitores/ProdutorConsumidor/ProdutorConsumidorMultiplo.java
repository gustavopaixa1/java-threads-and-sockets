package Monitores.ProdutorConsumidor;

public class ProdutorConsumidorMultiplo {

    // 🌀 Buffer circular com controle sincronizado
    static class BufferCircular {
        private final String[] buffer;
        private final int capacidade;
        private int in = 0;
        private int out = 0;
        private int count = 0;

        public BufferCircular(int capacidade) {
            this.capacidade = capacidade;
            this.buffer = new String[capacidade];
        }

        // 🔼 Métdo sincronizado para produtores
        public synchronized void depositar(String item, int idProdutor) throws InterruptedException {
            while (count == capacidade) {
                System.out.println("⚠️ Produtor " + idProdutor + " esperando (buffer cheio)");
                wait(); // Libera o monitor e entra na fila de espera
            }

            buffer[in] = item;
            in = (in + 1) % capacidade;
            count++;

            System.out.println("🟩 Produtor " + idProdutor + " colocou: " + item);

            notifyAll(); // Acorda todos consumidores que podem estar esperando
        }

        // 🔽 Métdo sincronizado para consumidores
        public synchronized String retirar(int idConsumidor) throws InterruptedException {
            while (count == 0) {
                System.out.println("⚠️ Consumidor " + idConsumidor + " esperando (buffer vazio)");
                wait(); // Libera o monitor e entra na fila de espera
            }

            String item = buffer[out];
            out = (out + 1) % capacidade;
            count--;

            System.out.println("🟥 Consumidor " + idConsumidor + " pegou: " + item);

            notifyAll(); // Acorda todos produtores que podem estar esperando
            return item;
        }
    }

    // 🧵 Classe Produtor
    static class Produtor extends Thread {
        private final BufferCircular buffer;
        private final int id;

        public Produtor(BufferCircular buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        public void run() {
            int i = 0;
            try {
                while (true) {
                    String item = "Item-P" + id + "-" + i++;
                    buffer.depositar(item, id);
                    Thread.sleep(500 + (int)(Math.random() * 300)); // simula tempo de produção variável
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 🧵 Classe Consumidor
    static class Consumidor extends Thread {
        private final BufferCircular buffer;
        private final int id;

        public Consumidor(BufferCircular buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        public void run() {
            try {
                while (true) {
                    buffer.retirar(id);
                    Thread.sleep(800 + (int)(Math.random() * 400)); // simula tempo de consumo variável
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 🚀 Ponto de entrada
    public static void main(String[] args) {
        int capacidadeBuffer = 3;
        int numProdutores = 3;
        int numConsumidores = 4;

        BufferCircular buffer = new BufferCircular(capacidadeBuffer);

        // Inicia produtores
        for (int i = 0; i < numProdutores; i++) {
            new Produtor(buffer, i).start();
        }

        // Inicia consumidores
        for (int i = 0; i < numConsumidores; i++) {
            new Consumidor(buffer, i).start();
        }
    }
}

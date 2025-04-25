package Monitores.ProdutorConsumidor;

public class ProdutorConsumidorBufferCircular {

    // 🔁 BUFFER CIRCULAR: estrutura compartilhada entre produtor e consumidor
    static class BufferCircular {
        private final String[] buffer;
        private final int capacidade;
        private int in = 0;       // índice de inserção
        private int out = 0;      // índice de remoção
        private int count = 0;    // número de itens no buffer

        public BufferCircular(int capacidade) {
            this.capacidade = capacidade;
            this.buffer = new String[capacidade];
        }

        // 🔼 PRODUTOR: deposita item no buffer
        public synchronized void depositar(String item) throws InterruptedException {
            // ❗ Enquanto o buffer estiver cheio, o produtor espera
            while (count == capacidade) {
                wait(); // 🔄 Libera o monitor e entra na fila de espera
            }

            // 💾 Insere item no buffer na posição 'in'
            buffer[in] = item;
            in = (in + 1) % capacidade;
            count++;

            System.out.println("🟩 Produtor colocou: " + item + " na posição: " + in);

            // 🔔 Acorda todos os consumidores que possam estar esperando
            notifyAll(); // usamos notifyAll por segurança com múltiplos consumidores
        }

        // 🔽 CONSUMIDOR: retira item do buffer
        public synchronized String retirar() throws InterruptedException {
            // ❗ Enquanto o buffer estiver vazio, o consumidor espera
            while (count == 0) {
                wait(); // 🔄 Libera o monitor e espera notificação
            }

            // 🔄 Retira item da posição 'out'
            String item = buffer[out];
            out = (out + 1) % capacidade;
            count--;

            System.out.println("🟥 Consumidor pegou: " + item + " da posição: " + out);

            // 🔔 Acorda todos os produtores que possam estar esperando
            notifyAll(); // usamos notifyAll por segurança com múltiplos produtores
            return item;
        }
    }

    // 🧵 THREAD PRODUTORA
    static class Produtor extends Thread {
        private final BufferCircular buffer;

        public Produtor(BufferCircular buffer) {
            this.buffer = buffer;
        }

        public void run() {
            int i = 0;
            try {
                while (true) {
                    buffer.depositar("Item " + i++);
                    Thread.sleep(500); // simula produção
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 🧵 THREAD CONSUMIDORA
    static class Consumidor extends Thread {
        private final BufferCircular buffer;

        public Consumidor(BufferCircular buffer) {
            this.buffer = buffer;
        }

        public void run() {
            try {
                while (true) {
                    buffer.retirar();
                    Thread.sleep(1000); // simula consumo
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 🚀 MAIN: ponto de entrada do programa
    public static void main(String[] args) {
        BufferCircular buffer = new BufferCircular(5); // buffer com 5 posições

        // Cria e inicia produtor e consumidor
        new Produtor(buffer).start();
        new Consumidor(buffer).start();
    }
}

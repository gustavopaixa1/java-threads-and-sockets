package Monitores.ProdutorConsumidor;

public class ProdutorConsumidorBufferSimples {

    // Classe que representa o buffer com capacidade de 1 item
    static class Buffer {
        private String item = null;       // espaço do buffer
        private boolean vazio = true;     // controla a condição de espera

        // Métdo do produtor: deposita um item no buffer
        public synchronized void depositar(String valor) throws InterruptedException {
            // Enquanto o buffer não estiver vazio, o produtor espera
            while (!vazio) {
                wait(); // Libera o monitor e espera notificação
            }

            item = valor;
            vazio = false;
            System.out.println("🟩 Produtor colocou: " + valor);

            notify(); // Acorda o consumidor que pode estar esperando
        }

        // Métdo do consumidor: retira um item do buffer
        public synchronized String retirar() throws InterruptedException {
            // Enquanto o buffer estiver vazio, o consumidor espera
            while (vazio) {
                wait(); // Libera o monitor e espera notificação
            }

            String valor = item;
            item = null;
            vazio = true;
            System.out.println("🟥 Consumidor pegou: " + valor);

            notify(); // Acorda o produtor que pode estar esperando
            return valor;
        }
    }

    // Thread que representa o produtor
    static class Produtor extends Thread {
        private final Buffer buffer;

        public Produtor(Buffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            int i = 0;
            try {
                while (true) {
                    buffer.depositar("Item " + i++);
                    Thread.sleep(1000); // Simula tempo de produção
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Thread que representa o consumidor
    static class Consumidor extends Thread {
        private final Buffer buffer;

        public Consumidor(Buffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            try {
                while (true) {
                    buffer.retirar();
                    Thread.sleep(1500); // Simula tempo de consumo
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Métdo principal para executar o programa
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // Inicia o produtor e o consumidor
        new Produtor(buffer).start();
        new Consumidor(buffer).start();
    }
}

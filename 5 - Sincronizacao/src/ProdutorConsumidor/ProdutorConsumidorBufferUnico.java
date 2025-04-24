package ProdutorConsumidor;

import java.util.concurrent.Semaphore;

// Problema em que um produtor gera dados e um consumidor os consome
// O produtor deve esperar o consumidor retirar os dados antes de produzir mais
// O consumidor deve esperar o produtor produzir dados antes de consumir
public class ProdutorConsumidorBufferUnico {

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        new Produtor(buffer).start();
        new Consumidor(buffer).start();
    }

    // Buffer com apenas uma posição
    static class Buffer {
        private String valor;
        private Semaphore vazio = new Semaphore(2); // começa vazio
        private Semaphore cheio = new Semaphore(0); // começa sem dados

        public void depositar(String item) throws InterruptedException {
            // Verifica se o buffer está vazio, se sim, vai enviar algo
            vazio.acquire();
            valor = item;
            System.out.println("Produtor colocou: " + item);
            // Esse semáforo indica que o buffer está cheio
            cheio.release();
        }

        public String retirar() throws InterruptedException {
            // Verifica se o buffer está cheio, se sim, vai retirar algo
            cheio.acquire();
            String item = valor;
            System.out.println("Consumidor pegou: " + item);
            // Indica que o buffer está vazio
            vazio.release();
            return item;
        }
    }

    static class Produtor extends Thread {
        private Buffer buffer;

        public Produtor(Buffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            int i = 0;
            while (true) {
                try {
                    buffer.depositar("Item " + i++);
                    Thread.sleep(1000); // simula tempo de produção
                } catch (InterruptedException e) {}
            }
        }
    }

    static class Consumidor extends Thread {
        private Buffer buffer;

        public Consumidor(Buffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            while (true) {
                try {
                    buffer.retirar();
                    Thread.sleep(1500); // simula tempo de consumo
                } catch (InterruptedException e) {}
            }
        }
    }
}

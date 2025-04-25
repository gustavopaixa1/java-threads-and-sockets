package Monitores.ObjetosLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LeitorEscritorLock {

    // Classe que implementa o banco de dados compartilhado
    // usando ReentrantLock e duas Condition variables.
    static class SharedDB {
        private int nr;                 // número de leitores ativos
        private boolean turnWR;         // controla prioridade entre leitores/escritores
        private final ReentrantLock lock;
        private final Condition okRD;   // fila de leitores
        private final Condition okWR;   // fila de escritores
        private String dataBase;        // dado compartilhado

        public SharedDB(String db) {
            this.nr = 0;
            this.turnWR = false;
            // lock justo: respeita ordem de chegada
            this.lock = new ReentrantLock(true);
            this.okRD = lock.newCondition();
            this.okWR = lock.newCondition();
            this.dataBase = db;
        }

        // Método de leitura
        public String read(int id) throws InterruptedException {
            String value;
            lock.lock(); // adquire o lock
            try {
                // se há escritor esperando, ou já foi escritor, aguarda
                while (turnWR && lock.hasWaiters(okWR)) {
                    System.out.println("⏳ Leitor " + id + " aguardando (prioridade escritor)");
                    okRD.await();
                }
                // agora o leitor entra
                nr++;
                System.out.println("📖 Leitor " + id + " entrou para ler (Leitores ativos: " + nr + ")");

                // se ainda há leitores aguardando, acorda todos
                if (lock.hasWaiters(okRD)) {
                    okRD.signalAll();
                } else {
                    // se não, passa a vez ao escritor
                    turnWR = true;
                }
            } finally {
                lock.unlock(); // libera para leitura/extras
            }

            // se��o de leitura fora do lock
            value = dataBase;
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            lock.lock(); // reentra para decrementar leitores
            try {
                nr--;
                System.out.println("✅ Leitor " + id + " saiu de ler (Leitores restantes: " + nr + ")");
                if (nr == 0) {
                    // se foi o último, acorda um escritor
                    okWR.signal();
                }
            } finally {
                lock.unlock();
            }
            return value;
        }

        // Métdo de escrita
        public void write(String v, int id) throws InterruptedException {
            lock.lock();
            try {
                // espera enquanto houver leitores ativos
                while (nr != 0) {
                    System.out.println("⏳ Escritor " + id + " aguardando (leitores ativos)");
                    okWR.await();
                }
                // entrou para escrever
                System.out.println("✍️ Escritor " + id + " escrevendo: " + v);
                dataBase = v;
                turnWR = false; // libera vez para leitores

                // se há leitores esperando, acorda um deles
                if (lock.hasWaiters(okRD)) {
                    okRD.signal();
                } else {
                    // senão, acorda outro escritor
                    okWR.signal();
                }
            } finally {
                lock.unlock();
            }
            // simula tempo de escrita
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("✅ Escritor " + id + " terminou de escrever");
        }
    }

    // Thread Leitor
    static class Reader extends Thread {
        private final SharedDB db;
        private final int id;

        public Reader(SharedDB db, int id) {
            this.db = db;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String v = db.read(id);
                    // opcional: exibir o valor lido
                    System.out.println("    Leitor " + id + " leu valor: " + v);
                    Thread.sleep(800 + (int)(Math.random() * 400));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Thread Escritor
    static class Writer extends Thread {
        private final SharedDB db;
        private final int id;
        private int counter = 0;

        public Writer(SharedDB db, int id) {
            this.db = db;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String novo = "Valor-" + id + "-" + counter++;
                    db.write(novo, id);
                    Thread.sleep(1500 + (int)(Math.random() * 500));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Ponto de entrada
    public static void main(String[] args) {
        SharedDB banco = new SharedDB("inicial");

        // inicia leitores
        for (int i = 0; i < 4; i++) {
            new Reader(banco, i).start();
        }

        // inicia escritores
        for (int i = 0; i < 2; i++) {
            new Writer(banco, i).start();
        }
    }
}

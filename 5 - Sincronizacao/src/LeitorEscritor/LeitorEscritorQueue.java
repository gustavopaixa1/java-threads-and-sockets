package LeitorEscritor;

import java.util.concurrent.Semaphore;

/**
 * Classe que implementa o problema de leitores e escritores com prioridade de equidade,
 * utilizando uma fila para gerenciar a ordem de chegada.
 */
public class LeitorEscritorQueue {


    static class BancoDeDados {
        private final Semaphore mutex = new Semaphore(1);   // Protege o contador de leitores
        private final Semaphore wlock = new Semaphore(1);   // Garante exclusividade de escrita
        private final Semaphore queue = new Semaphore(1);   // Fila de chegada (garante equidade)
        private int numLeitores = 0; // Contador de leitores ativos
        private String dado = "valor inicial"; // Dados armazenados no banco

        public void ler(String nome) throws InterruptedException {
            queue.acquire();  // Entra na fila geral
            mutex.acquire();
            numLeitores++;
            if (numLeitores == 1) {
                wlock.acquire(); // O primeiro leitor bloqueia os escritores
            }
            mutex.release();
            queue.release(); // Libera a fila

            // Seção crítica (leitura)
            System.out.println(nome + " está lendo: " + dado);
            Thread.sleep(1000);

            mutex.acquire();
            numLeitores--;
            if (numLeitores == 0) {
                wlock.release(); // O último leitor libera os escritores
            }
            mutex.release();
        }

        public void escrever(String nome, String novoValor) throws InterruptedException {
            queue.acquire(); // Entra na fila geral
            wlock.acquire(); // Garante exclusividade total
            queue.release(); // Libera a fila após entrar

            // Seção crítica (escrita)
            System.out.println(nome + " está escrevendo: " + novoValor);
            Thread.sleep(1000);
            dado = novoValor;
            System.out.println(nome + " terminou de escrever.");
            wlock.release();
        }
    }

    static class Leitor extends Thread {
        private final BancoDeDados banco; // Referência ao banco de dados compartilhado
        private final String nome; // Nome do leitor

        public Leitor(BancoDeDados banco, String nome) {
            this.banco = banco;
            this.nome = nome;
        }

        public void run() {
            try {
                while (true) {
                    banco.ler(nome); // Realiza a leitura
                    Thread.sleep(1500); // Simula nova tentativa de leitura
                }
            } catch (InterruptedException e) {
                // Trata interrupções da thread
            }
        }
    }

    static class Escritor extends Thread {
        private final BancoDeDados banco; // Referência ao banco de dados compartilhado
        private final String nome; // Nome do escritor
        private int cont = 0; // Contador para gerar novos valores

        public Escritor(BancoDeDados banco, String nome) {
            this.banco = banco;
            this.nome = nome;
        }

        public void run() {
            try {
                while (true) {
                    banco.escrever(nome, "Valor " + cont++); // Realiza a escrita
                    Thread.sleep(3000); // Simula tempo até próxima escrita
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        BancoDeDados banco = new BancoDeDados();

        // Criação de múltiplos leitores e escritores
        new Leitor(banco, "Leitor-1").start();
        new Leitor(banco, "Leitor-2").start();
        new Leitor(banco, "Leitor-3").start();

        new Escritor(banco, "Escritor-1").start();
        new Escritor(banco, "Escritor-2").start();
    }
}
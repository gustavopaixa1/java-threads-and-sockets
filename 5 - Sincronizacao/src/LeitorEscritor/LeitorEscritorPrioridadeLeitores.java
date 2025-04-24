package LeitorEscritor;

import java.util.concurrent.Semaphore;

/**
 * Classe que implementa o problema de leitores e escritores com prioridade para leitores.
 * Esse exemplo pode ter starvation do escritor pois tem muitos leitores. Que podem não parar de chegar.
 */
public class LeitorEscritorPrioridadeLeitores {

    /**
     * Classe que simula um banco de dados compartilhado entre leitores e escritores.
     */
    static class BancoDeDados {
        private final Semaphore mutex = new Semaphore(1); // Controla o acesso ao contador de leitores
        private final Semaphore wlock = new Semaphore(1); // Controla o acesso exclusivo para escritores
        private int numLeitores = 0; // Contador de leitores ativos
        private String dados = "valor inicial"; // Dados armazenados no banco

        /**
         * @param nome Nome do leitor que está acessando os dados.
         */
        public void ler(String nome) throws InterruptedException {
            mutex.acquire(); // Garante acesso exclusivo ao contador de leitores
            numLeitores++;
            if (numLeitores == 1) {
                wlock.acquire(); // O primeiro leitor bloqueia os escritores
            }
            mutex.release();

            // Simula a leitura dos dados
            System.out.println(nome + " está lendo: " + dados);
            Thread.sleep(1000);

            mutex.acquire();
            numLeitores--;
            if (numLeitores == 0) {
                wlock.release(); // O último leitor libera os escritores
            }
            mutex.release();
        }

        /**

         * @param nome Nome do escritor que está acessando os dados.
         * @param novoValor Novo valor a ser escrito no banco de dados.
         */
        public void escrever(String nome, String novoValor) throws InterruptedException {
            wlock.acquire(); // Garante acesso exclusivo para o escritor
            System.out.println(nome + " está escrevendo: " + novoValor);
            Thread.sleep(1000);
            dados = novoValor; // Atualiza os dados
            System.out.println(nome + " terminou de escrever.");
            wlock.release();
        }
    }

    /**
     * Classe que representa um leitor.
     */
    static class Leitor extends Thread {
        private final BancoDeDados banco; // Referência ao banco de dados compartilhado
        private final String nome; // Nome do leitor

        /**
         * @param banco Referência ao banco de dados.
         * @param nome Nome do leitor.
         */
        public Leitor(BancoDeDados banco, String nome) {
            this.banco = banco;
            this.nome = nome;
        }

        /**
         * Métdo executado pela thread do leitor.
         */
        public void run() {
            try {
                while (true) {
                    banco.ler(nome); // Realiza a leitura
                    Thread.sleep(1500); // Simula a chegada constante de leitores
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Classe que representa um escritor.
     */
    static class Escritor extends Thread {
        private final BancoDeDados banco; // Referência ao banco de dados compartilhado
        private final String nome; // Nome do escritor
        private int cont = 0; // Contador para gerar novos valores

        /**
         * Construtor do escritor.
         *
         * @param banco Referência ao banco de dados.
         * @param nome Nome do escritor.
         */
        public Escritor(BancoDeDados banco, String nome) {
            this.banco = banco;
            this.nome = nome;
        }

        public void run() {
            try {
                while (true) {
                    banco.escrever(nome, "Novo valor " + cont++); // Realiza a escrita
                    Thread.sleep(3000); // Tenta escrever periodicamente
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        BancoDeDados banco = new BancoDeDados();

        // Criação de múltiplos leitores e um escritor
        new Leitor(banco, "Leitor-1").start();
        new Leitor(banco, "Leitor-2").start();
        new Leitor(banco, "Leitor-3").start();
        new Escritor(banco, "Escritor-1").start();
    }
}
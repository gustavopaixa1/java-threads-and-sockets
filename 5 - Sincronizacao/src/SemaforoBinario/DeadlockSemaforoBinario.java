package SemaforoBinario;

import java.util.concurrent.Semaphore;

class SemaforoBinario {

    // Semáforo binário: 1 permissão
    static Semaphore semaforo = new Semaphore(1);

    public static void main(String[] args) {
        Thread t1 = new Thread(new Tarefa("Thread A"));
        Thread t2 = new Thread(new Tarefa("Thread B"));

        t1.start();
        t2.start();
    }

    static class Tarefa implements Runnable {
        private String nome;

        public Tarefa(String nome) {
            this.nome = nome;
        }

        @Override
        public void run() {
            try {
                System.out.println(nome + " tentando entrar na seção crítica...");
                semaforo.acquire(); // tenta entrar
                semaforo.acquire(); // tenta entrar

                System.out.println(nome + " entrou na seção crítica.");
                Thread.sleep(2000); // simula trabalho
                System.out.println(nome + " saindo da seção crítica.");
                semaforo.release(); // libera
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

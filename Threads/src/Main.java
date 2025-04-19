import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExampleThread thread1 = new ExampleThread("Thread 1");
        ExampleThread thread2 = new ExampleThread("Thread 2");
        MyRun runnableImplementation = new MyRun("Thread 3");

//      Passa o objeto runnable para o construtor do thread
        Thread thread3 = new Thread(runnableImplementation);
        System.out.println(thread1.getPriority());
        System.out.println(thread2.getPriority());

//      O isAlive serve para verificar se a thread ainda está em execução
        System.out.println("Thread1 is alive: " + thread1.isAlive());

//      Definir prioridades das threads, sendo 1 a menor e 10 a maior
        thread1.setPriority(5);
        thread2.setPriority(5);
        thread3.setPriority(5);

//      Thread inicia com o start() e não com o run()
        thread3.start();

//      O join() faz com que a thread principal aguarde a execução da thread 3
//       A thread 3 vai finalizar e as demais threads vão continuar executando
//       thread3.join();

        thread1.start();
        thread2.start();

//      Invocar o start() de um thread que já foi iniciado gera uma IllegalThreadStateException
//      thread.start();
        System.out.println("Hello from main thread " + Thread.currentThread().getName());
        System.out.println("Thread1 is alive: " + thread1.isAlive());
    }
}

// Estender a classe thread impossibilita a herança de outra classe
class ExampleThread extends Thread {
    private String message;

    public ExampleThread(String message) {
        this.message = message;
    }

    // O métdo run() é o que vai executar quando a thread for iniciada
    @Override
    public void run() {

        for (int i = 0; i < 50; i++) {
            System.out.println(message + "- " + i);
        }
        System.out.println("Thread " + message + " finished.");
    }

}

// Permite a herança de outra classe
class MyRun implements Runnable {
    private String message;

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println(message + " -" + i);
//          Pausa a execução da thread atual e permite que outras threads sejam executadas
            Thread.yield();
        }
        try {
            // Pausa a execução da thread 1 segundo.
            // Neste exemplo, a thread irá mostrar a mensagem de término depois das demais threads
            sleep(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Thread " + message + " finished.");

    }

    public MyRun(String message) {
        this.message = message;
    }
}

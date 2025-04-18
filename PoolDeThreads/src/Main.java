public class Main {
    public static void main(String[] args) {
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
        thread1.setPriority(10);
        thread2.setPriority(1);
        thread3.setPriority(1);

//      Thread inicia com o start() e não com o run()
        thread1.start();
        thread2.start();
        thread3.start();

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

    // This method is called when the thread is started
    // Must be overridden to define the thread's behavior
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
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
        for (int i = 0; i < 10; i++) {
            System.out.println(message + " -" + i);
        }
        System.out.println("Thread " + message + " finished.");

    }

    public MyRun(String message) {
        this.message = message;
    }
}

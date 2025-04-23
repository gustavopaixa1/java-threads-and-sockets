package Tasks;

public class ThreadTask extends Thread {
    private final String name;

    public ThreadTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Tarefa " + name + " executada por " + Thread.currentThread().getName());
    }
}

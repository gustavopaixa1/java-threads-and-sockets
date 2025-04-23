import java.util.concurrent.Callable;

public class CallableTask implements Callable<String> {
    private final String name;

    public CallableTask(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        //System.out.println("Tarefa " + name + " executada por " + Thread.currentThread().getName());
        return "Tarefa " + name + " executada com sucesso!";
    }
}

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InterfaceExecutor {

    public static void main(String[] args) {
//      O objeto executor pode receber uma thread ou um objeto runnable
        Executor executor = Executors.newSingleThreadExecutor();

        RunnableTask runnableTask = new RunnableTask("'executando um objeto runnable'");
        ThreadTask threadTask = new ThreadTask("'executando um objeto thread'");

        // Executa uma tarefa anônima
        executor.execute(() -> {
            System.out.println("Tarefa executada por" + Thread.currentThread().getName());
        });

        // Executa tarefas passando um objeto runnable e um objeto thread
        // A Interface executor tem acesso apenas ao métdo execute
        executor.execute(runnableTask);
        executor.execute(threadTask);

    }
}


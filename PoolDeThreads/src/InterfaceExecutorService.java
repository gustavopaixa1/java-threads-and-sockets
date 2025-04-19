import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class InterfaceExecutorService {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Pode executar Runnable e Callable
        // Retorna valor após a execução da tarefa
        // Retorno é efetivado por um objeto Future
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        RunnableTask runnableTask = new RunnableTask("'executando um objeto runnable'");
        CallableTask callableTask = new CallableTask("'executando um objeto callable'");

        // O objeto callable retorna um Future de determinado tipo, definido no construtor
        Future<String> resultCallable = executorService.submit(callableTask);
        try {
            System.out.println("Resultado da tarefa callable: " + resultCallable.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // O objeto Runnable não retorna valor
        Future resultRunnable = executorService.submit(runnableTask);
        try {
            System.out.println("Resultado da tarefa runnable: " + resultRunnable.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        List<Callable<String>> tasks = Arrays.asList(
                new CallableTask("Tarefa 1"),
                new CallableTask("Tarefa 2"),
                new CallableTask("Tarefa 3"),
                new CallableTask("Tarefa 4"),
                new CallableTask("Tarefa 5")
        );

        // É possível passar uma lista de tarefas para o invokeAll
        List<Future<String>> results = executorService.invokeAll(tasks);
        for (Future<String> result : results) {
            System.out.println("Resultado: " + result.get());
        }

        // È possível encerrer o executorService com shutdown, nao mata as tarefas em andamento
        // Nao aceita novas tarefas
        // executorService.shutdown();

        // O shutdownNow tenta interromper as tarefas em andamento
    }
}

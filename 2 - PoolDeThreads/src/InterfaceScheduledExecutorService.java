import Tasks.CallableTask;
import Tasks.RunnableTask;

import java.util.concurrent.*;

public class InterfaceScheduledExecutorService {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Adiciona a temporarização de tarefas, executa as tarefas depois de determinado tempo
        // Em vez de retornar futures, retorna um objeto ScheduledFuture
        // O retorno do runnable é null, o retorno do callable é o mesmo do callable
        ScheduledExecutorService scheduleExecutor = Executors.newScheduledThreadPool(5);
        CallableTask callableTask = new CallableTask("Tarefa 1");
        RunnableTask runnableTask = new RunnableTask("Tarefa 2");

        // O métdo schedule excuta uma tarefa depois de um tempo determinado
        ScheduledFuture<String> scheduleResult = scheduleExecutor.schedule(callableTask, 2, TimeUnit.SECONDS);

        // Código feito para parar a execução do ScheduledExecutorService após 10 segundos
        scheduleExecutor.schedule(() -> {
            scheduleExecutor.shutdown();
        }, 5, TimeUnit.SECONDS);

        // Pega o resultado do ScheduledFuture com o métdo get()
        System.out.println(scheduleResult.get());

        // initial delay: tempo que a tarefa demora a começar - period: tempo que a tarefa demora a repetir
        // Independe da duração da tarefa, se a tarefa demora mais a terminar, atrasa as demais
        scheduleExecutor.scheduleAtFixedRate(runnableTask, 1, 1, TimeUnit.SECONDS);

        // A proxima execução começa depois que a anterior acabar
        scheduleExecutor.scheduleWithFixedDelay(runnableTask, 2, 4, TimeUnit.SECONDS);


    }
}

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ObjetosExecutor {
    public static void main(String[] args) {

        // THREADS FIXAS
        // newFixedThreadPool cria um pool de threads fixo
        // Se nao houver threads livres, a tarefa fica em espera
        // Threads ficam vivas aguardando novas tarefas
        Executor executor = Executors.newFixedThreadPool(5);

        // Cria e executa 10 tarefas
        for (int i = 0; i < 10; i++) {
            Runnable task = new RunnableTask("Fixed - Tarefa " + i);
            executor.execute(task);
        }

        // THREADS COM CACHE
        // Threads sao criadas sob demanda e reusadas quando disponiveis
        // Se o tempo de inatividade for maior que 60 segundos, a thread é encerrada
        // Não adota fila de espera
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            Runnable task = new RunnableTask("Cache -" + i);
            executorService.execute(task);
        }

        // THREADS COM AGENDAMENTO
        // Possui um numero fixo de threads
        // Pode agendar tarefas com delay ou repetição, com o uso dos métodos schedule
        // Exemplos do uso de Schedule estão na classe InterfaceScheduledExecutorService.java, bjs
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    }
}

class RunnableTask implements Runnable {
    private final String name;

    public RunnableTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Tarefa " + name + " executada por " + Thread.currentThread().getName());
    }
}

class RunnableTaskTill100 implements Runnable{
    private final String name;

    public RunnableTaskTill100(String name){
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Tarefa " + name + " executada por " + Thread.currentThread().getName() + " - " + i);
        }
        System.out.println("Tarefa " + name + " finalizada.");
    }
}
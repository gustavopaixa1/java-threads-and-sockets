package Monitores;

public class BadSyncClass extends Thread {
    private final BadSynchClass a, b;

    public BadSyncClass(BadSynchClass a, BadSynchClass b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        a.swap(b); // ou b.swap(a)
        System.out.println("A: " + a.get() + " B: " + b.get());
    }

    public static void main(String[] args) {
        BadSynchClass a = new BadSynchClass(1);
        BadSynchClass b = new BadSynchClass(2);

        // Thread 1: troca de a com b
        new BadSyncClass(a, b).start();

        // Thread 2: troca de b com a (inverso)
        new BadSyncClass(b, a).start();
    }
}
class BadSynchClass {
    private int value;

    public BadSynchClass(int v) {
        this.value = v;
    }

    // 🔐 MÉTDO SINCRONIZADO
    // Usa o monitor (lock) do próprio objeto. Somente uma thread pode executar este métdo por vez no mesmo objeto.
    public synchronized int get() {
        return value;
    }

    // 🔐 MESMA COISA: usa o monitor do objeto
    public synchronized void set(int i) {
        value = i;
    }

    // 🔐 AQUI ESTÁ O PERIGO: métdo sincronizado que tenta acessar outro objeto sincronizado
    public synchronized void swap(BadSynchClass other) {
        // Neste momento, a thread já obteve o lock (monitor) do objeto atual (this)
        int tmp = this.get(); // Tranquilo, ainda estamos usando apenas o monitor de "this"

        // 🚨 PERIGO: chamamos um métdo sincronizado de "other"
        // Isso exige que a thread consiga o monitor de "other"
        // Mas se "other" já estiver com outra thread... deadlock!
        this.set(other.get());

        // 🚨 E novamente usamos "other"
        other.set(tmp);
    }
}

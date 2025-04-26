import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

// 🔵 Classe que implementa a lógica da interface remota
public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

    // 🔹 Construtor: deve lançar RemoteException para permitir a exportação automática
    protected CalculatorImpl() throws RemoteException {
        super(); // 🔵 exporta o objeto para que ele aceite chamadas remotas
    }

    // 🔵 Métodos remotos reais, que fazem os cálculos
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        return a - b;
    }

    @Override
    public int mul(int a, int b) throws RemoteException {
        return a * b;
    }

    @Override
    public int div(int a, int b) throws RemoteException {
        if (b == 0) throw new ArithmeticException("Divisão por zero");
        return a / b;
    }
}

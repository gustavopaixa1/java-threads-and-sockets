import javax.naming.Context;
import javax.naming.InitialContext;

public class CalculadoraClient {
    public static void main(String[] args) {
        try {
            // 🔵 Obtém o contexto de nomes para fazer o lookup
            Context nameContext = new InitialContext();

            // 🔵 Procura o serviço remoto registrado com nome específico
            Calculator calc = (Calculator) nameContext.lookup("rmi://127.0.0.1:1099/calculator");

            // 🔵 Chama métodos remotos como se fossem locais
            System.out.println("123 + 456 = " + calc.add(123, 456));
            System.out.println("20 * 7 = " + calc.mul(20, 7));
            System.out.println("50 - 15 = " + calc.sub(50, 15));
            System.out.println("30 / 5 = " + calc.div(30, 5));

        } catch (Exception e) {
            // ❗ Captura qualquer erro de comunicação ou lookup
            e.printStackTrace();
        }
    }
}

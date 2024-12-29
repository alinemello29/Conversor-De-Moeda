import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorDeMoedas {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double valor;

        while (true) {
            System.out.println("\nEscolha uma opção de conversão:");
            System.out.println("1. Dólar para Real");
            System.out.println("2. Real para Dólar");
            System.out.println("3. Sair");
            System.out.print("Digite sua opção: ");

            int opcao = scanner.nextInt();

            if (opcao == 3) {
                System.out.println("Obrigado por usar o conversor de moedas! Até logo! 👋");
                break;
            }

            System.out.print("Digite o valor a ser convertido: ");
            valor = scanner.nextDouble();

            double taxaConversao = obterTaxaDeConversao(opcao);
            if (taxaConversao > 0) {
                double resultado = valor * taxaConversao;
                System.out.printf("Resultado: %.2f%n", resultado);
            } else {
                System.out.println("Não foi possível obter a taxa de conversão.");
            }
        }

        scanner.close();
    }

    private static double obterTaxaDeConversao(int opcao) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseStr = response.toString();
            double taxa = 0;

            if (opcao == 1) { // Dólar para Real
                taxa = Double.parseDouble(responseStr.split("\"BRL\":")[1].split(",")[0]);
            } else if (opcao == 2) { // Real para Dólar
                taxa = 1 / Double.parseDouble(responseStr.split("\"BRL\":")[1].split(",")[0]);
            }

            return taxa; // Retorna a taxa de conversão
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Retorna -1 em caso de erro
        }
    }
}
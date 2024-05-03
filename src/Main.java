import java.util.Scanner;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Seja bem-vindo/a ao Conversor de Moeda z)");
            System.out.println("1. Dólar > Peso argentino");
            System.out.println("2. Peso argentino > Dólar");
            System.out.println("3. Dólar > Real brasileiro");
            System.out.println("4. Real brasileiro > Dólar");
            System.out.println("5. Dólar > Peso colombiano");
            System.out.println("6. Peso colombiano > Dólar");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção válida: ");

            int opcao = scanner.nextInt();
            if (opcao == 7) {
                break;
            }

            switch (opcao) {
                case 1:
                    converterMoeda("USD", "ARS");
                    break;
                case 2:
                    converterMoeda("ARS", "USD");
                    break;
                case 3:
                    converterMoeda("USD", "BRL");
                    break;
                case 4:
                    converterMoeda("BRL", "USD");
                    break;
                case 5:
                    converterMoeda("USD", "COP");
                    break;
                case 6:
                    converterMoeda("COP", "USD");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha novamente.");
                    break;
            }
        }

        scanner.close();
    }

    private static void converterMoeda(String moedaDe, String moedaPara) {
        String apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("API Key não encontrada. Certifique-se de definir a variável de ambiente EXCHANGE_RATE_API_KEY com sua chave de API.");
            return;
        }

        try {
            URL url = new URL("https://api.exchangerate-api.com/v4/latest/" + moedaDe + "?api_key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            double taxaCambio = jsonObject.getJSONObject("rates").getDouble(moedaPara);
            System.out.printf("Digite o valor em %s: ", moedaDe);
            Scanner scanner = new Scanner(System.in);
            double valor = scanner.nextDouble();
            double valorConvertido = valor * taxaCambio;
            System.out.println();
            System.out.println("-------------------------------");
            System.out.printf("%.2f %s equivale a %.2f %s\n", valor, moedaDe, valorConvertido, moedaPara);
            System.out.println("-------------------------------");
            System.out.println();

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao converter a moeda: " + e.getMessage());
        }
    }
}
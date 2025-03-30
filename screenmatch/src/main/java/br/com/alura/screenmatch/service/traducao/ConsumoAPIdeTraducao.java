package br.com.alura.screenmatch.service.traducao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPIdeTraducao {
    public static String endereco2 = "https://api.mymemory.translated.net/get?q=Hello+world&langpair=en|pt";

    public void testeApi(){

        try {
            // URL da API
            String apiUrl = "https://api.mymemory.translated.net/get?q=Hello+world&langpair=en|pt";

            // Criar uma URL e abrir conexão
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configurar método GET
            connection.setRequestMethod("GET");

            // Checar o código de resposta
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // 200 = OK
                // Ler a resposta
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Exibir o resultado
                System.out.println("Resposta da API:");
                System.out.println(response.toString());
            } else {
                System.out.println("Erro na requisição. Código de resposta: " + responseCode);
            }

            // Fechar conexão
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

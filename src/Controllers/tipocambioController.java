package Controllers;

import Models.Moneda;
import Models.RMoneda;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class tipocambioController {
    public List<RMoneda> obtenerTasasDeCambio(String tipo) throws IOException {
        Moneda moneda = new Moneda(tipo, 0.0);
        URL url = new URL(moneda.obtenerClave(tipo));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(content.toString(), JsonObject.class);
            JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");

            List<RMoneda> exchangeRates = new ArrayList<>();
            for (String key : rates.keySet()) {
                exchangeRates.add(new RMoneda(key, rates.get(key).getAsDouble()));
            }

            return exchangeRates;
        } finally {
            conn.disconnect();
        }
    }
}
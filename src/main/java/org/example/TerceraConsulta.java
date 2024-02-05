package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que realiza consultas a una API para obtener información específica sobre una serie de televisión.
 * Enfocada en obtener el título y el argumento del primer episodio de la tercera saga (temporada) de la serie:
 * Saga del ejército de la Patrulla Roja.
 */
public class TerceraConsulta {
    // Consulta: El título y el argumento del primer episodio de la tercera saga (temporada) de la serie:
    // Saga del ejército de la Patrulla Roja.

    /**
     * Método principal que imprime en la consola el resultado de obtener los datos de la serie.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        System.out.println(obtenerDatosSerie(obtenerIdSerie()));
    }

    /**
     * Método que realiza una consulta para obtener el ID de la serie "Dragon Ball".
     *
     * @return El ID de la serie "Dragon Ball".
     */
    private static String obtenerIdSerie() {
        String seriesId = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/idbyTitle/Dragon%20Ball/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray jsonArray = jsonResponse.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject serie = jsonArray.getJSONObject(i);
                String title = serie.getString("title");

                // Verificar si el título es "Dragon Ball"
                if ("Dragon Ball".equalsIgnoreCase(title)) {
                    seriesId = serie.getString("imdb_id"); // Almacena el ID de la serie
                    break;  // Termina la iteración una vez que se encuentra la serie "Dragon Ball"
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return seriesId;
    }

    /**
     * Método que realiza una consulta para obtener los datos del primer episodio de la tercera temporada de la serie.
     *
     * @param seriesId El ID de la serie para la cual se desea obtener los datos.
     * @return Una cadena de texto con los datos del primer episodio de la tercera temporada.
     */
    private static String obtenerDatosSerie (String seriesId) {
        String res = "";
        String temporada = "3";
        String episodio = "1";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/id/"+ seriesId +
                            "/season/" + temporada + "/episode/" + episodio + "/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            System.out.println(jsonResponse.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}

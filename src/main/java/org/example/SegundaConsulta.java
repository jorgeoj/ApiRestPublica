package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que realiza consultas a una API para obtener información sobre series de televisión.
 * Se enfoca en obtener la URL del poster de la primera serie registrada del año 1995.
 */
public class SegundaConsulta {
    // Consulta: La URL del poster de la primera serie que aparece registrada (del 1995)

    /**
     * Método principal que obtiene el ID de la serie "Dragon Ball" del año 1995
     * y luego imprime la URL del poster de esa serie.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        imprimirURLPoster(obtenerIdSerie());
    }

    /**
     * Método que realiza una consulta para obtener el ID de la serie "Dragon Ball" del año 1995.
     *
     * @return El ID de la serie "Dragon Ball" del año 1995.
     */
    private static String obtenerIdSerie() {
        String idSerie = "";
        try {
            /*
            Esto es otra forma de sacarlo con otra request (solo con el nombre de la serie)

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/idbyTitle/Dragon%20Ball/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            */
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/byYear/1995/"))
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
                    // Almacena el ID de la serie
                    idSerie = serie.getString("imdb_id");
                    break;  // Termina la iteración una vez que se encuentra la serie "Dragon Ball"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idSerie;
    }

    /**
     * Método que realiza una consulta para obtener la URL del poster de la serie utilizando su ID.
     * Imprime la URL del poster en la consola.
     *
     * @param idSerie El ID de la serie para la cual se desea obtener la URL del poster.
     * @return Un objeto JSONObject que representa la respuesta de la consulta.
     */
    private static JSONObject imprimirURLPoster(String idSerie) {
        JSONObject resultObject = new JSONObject();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/id/" + idSerie + "/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            String bannerUrl = jsonResponse.getJSONObject("results").getString("banner");

            System.out.print("URL del poster: " +  bannerUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }
}

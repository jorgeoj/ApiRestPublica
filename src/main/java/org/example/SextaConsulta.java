package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SextaConsulta {
    // Consulta: De ese mismo año es la película Postergeist, el primer gran éxito de Steven Spielberg como productor,
    // que participó en los premios Oscars al año siguiente sin ser ganadora. ¿A qué apartado fue nominada?
    // ¿Que otro premio ganó ese año?
    public static void main(String[] args) {
        System.out.println(getPeliculaId());
        System.out.println(obtenerPremiosPoltergeist(getPeliculaId()));
    }

    /**
     * Método para obtener el ID de la película con título "Poltergeist" registrada en el año 1982.
     *
     * @return El ID de la película "Poltergeist" o una cadena vacía si no se encuentra.
     */
    public static String getPeliculaId() {
        String peliculaid = "";
        try {
            // Realizar una solicitud HTTP para obtener la información sobre las películas registradas en el año 1982
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/byYear/1982/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());


            // Obtener el ID de la película con título "Poltergeist"
            JSONArray resultsArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movie = resultsArray.getJSONObject(i);
                if ("Poltergeist".equals(movie.getString("title"))) {
                    peliculaid = movie.getString("imdb_id");
                    System.out.println("ID de la película 'Poltergeist': " + peliculaid);
                    break;  // Terminar la búsqueda una vez que se encuentra la película "Poltergeist"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return peliculaid;
    }

    public static String obtenerPremiosPoltergeist(String idPelicula) {
        String winner = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/id/" + idPelicula + "/awards/"))
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
        return winner;
    }
}

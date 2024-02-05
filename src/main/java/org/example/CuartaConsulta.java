package org.example;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CuartaConsulta {
    // Consulta: Lugar de nacimiento y signo del zodiaco del actor que hacía de Goku en la pelicula "Dragonball Evolution"
    public static void main(String[] args) {
        System.out.println(obtenerPeliculaPorNombre());
    }

    public static String obtenerPeliculaPorNombre() {
        String res = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/imdb_id/byTitle/Dragonball%20Evolution/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            // TODO esto da error no se porque
            String movieId = jsonResponse.getJSONObject("results").getString("imdb_id");
            System.out.println(movieId);

            res = movieId;

            System.out.println(jsonResponse.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
    retrievegetMovieIdByTitle ->
    imdb_id:"tt1098327"
    title:"Dragonball Evolution"

    listgetCastByMovieids (con el id que sacamos antes) -> donde el "role" = "Goku"
    imdb_id:"nm0154226"
    name:"Justin Chatwin"

    retrievegetActorDetailsById (con el id del paso anterior) ->
    birth_place:"Nanaimo, British Columbia, Canada"
    star_sign:"Scorpio"
    */

}

package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que realiza consultas relacionadas con la película "Dragonball Evolution" y su elenco,
 * obteniendo información sobre el actor que interpreta el papel de Goku.
 */
public class CuartaConsulta {
    // Consulta: Lugar de nacimiento y signo del zodiaco del actor que hacía de Goku en la pelicula "Dragonball Evolution"
    /**
     * Método principal que ejecuta las consultas y muestra información sobre el actor que interpreta a Goku.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        System.out.println(obtenerDatosActor(obtenerActorGoku(obtenerPeliculaPorNombre())));
    }

    /**
     * Realiza una consulta para obtener el ID de la película "Dragonball Evolution" por su nombre.
     *
     * @return El ID de la película obtenido.
     */
    public static String obtenerPeliculaPorNombre() {
        String movieId = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/imdb_id/byTitle/Dragonball%20Evolution/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            JSONArray resultsArray = jsonResponse.getJSONArray("results");
            if (resultsArray.length() > 0) {
                JSONObject movieInfo = resultsArray.getJSONObject(0);
                movieId = movieInfo.getString("imdb_id");

                System.out.println("id de la película: " + movieId);
            } else {
                System.out.println("No se encontraron resultados para la película.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieId;
    }

    /**
     * Realiza una consulta para obtener el ID del actor que interpreta a Goku en la película especificada por su ID.
     *
     * @param idPelicula El ID de la película.
     * @return El ID del actor que interpreta a Goku.
     */
    public static String obtenerActorGoku(String idPelicula) {
        String actor = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/id/"+ idPelicula +"/cast/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (jsonResponse.has("results")) {
                JSONArray rolesArray = jsonResponse.getJSONObject("results").getJSONArray("roles");

                // Buscar el actor que interpreta el papel de Goku
                for (int i = 0; i < rolesArray.length(); i++) {
                    JSONObject role = rolesArray.getJSONObject(i);

                    if ("Goku".equalsIgnoreCase(role.getString("role"))) {
                        actor = role.getJSONObject("actor").getString("imdb_id");
                        System.out.println("id del actor de goku: " + actor);
                        break;  // Terminar la búsqueda una vez que se encuentra a Goku
                    }
                }
            } else {
                System.out.println("La respuesta no contiene resultados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

    /**
     * Realiza una consulta para obtener información sobre el actor especificado por su ID.
     *
     * @param idActor El ID del actor.
     * @return Un objeto JSON con información sobre el actor.
     */
    public static JSONObject obtenerDatosActor(String idActor) {
        JSONObject resultObject = new JSONObject();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/actor/id/" + idActor + "/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            String lugarNacimiento = jsonResponse.getJSONObject("results").getString("birth_place");
            String cumple = jsonResponse.getJSONObject("results").getString("birth_date");
            String signo = jsonResponse.getJSONObject("results").getString("star_sign");

            System.out.println("lugar de nacimiento: " + lugarNacimiento);
            System.out.println("fecha de nacimiento: " + cumple);
            System.out.println("signo del zodiaco: " +  signo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }
}

// System.out.println(jsonResponse.toString(2)); <- Devuelve siempre el resultado completo de la consulta

package org.gs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
@Tag(name="Movie Resources", description = "Movie Rest API")
public class MovieResources {
        public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId ="getMovies" , summary = "Movies list", description = "Get all the movies")
    @APIResponse(responseCode = "200",description = "Operation completed", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId ="countMovies" , summary = "Count Movies ", description = "Size of the list movies")
    @APIResponse(responseCode = "200",description = "Operation completed", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    @Path("/size")
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId ="createMovie" , summary = "Create a new movie ", description = "Create a new movie")
    @APIResponse(responseCode = "201",description = "movie created", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    public Response addMovie(Movie newMovie) {
        movies.add(newMovie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId ="updateMovie" , summary = "update a movie", description = "update a movie")
    @APIResponse(responseCode = "200",description = "Operation completed", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    public Response updateMovie(
            @Parameter(required = true)
            @PathParam("id") Long id,
            @Parameter(required = true)
            @PathParam("title") String title) {
        movies=movies.stream().map( movie -> {
            if (movie.getId().equals(id)) {
                 movie.setTitle(title);
            }
            return movie;
        }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }

 @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId ="deleteMovie" , summary = "delete a movie", description = "delete a movie")
    @APIResponse(responseCode = "204",description = "Movie deleted", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404",description = "Movie not valid", content=@Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteMovie(@PathParam("id") Long id){
        Optional<Movie> movieToDelete=movies.stream().filter(movie->movie.getId().equals(id)).findFirst();
     boolean removed=false;
        if(movieToDelete.isPresent()){
            removed=movies.remove(movieToDelete.get());
        }
        return removed?Response.noContent().build():
                Response.status(Response.Status.BAD_REQUEST).build();
    }


}
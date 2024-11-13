package org.gs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")

public class MovieResources {
        public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation()
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/size")
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMovie(Movie newMovie) {
        movies.add(newMovie);
        return Response.ok(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(
            @PathParam("id") Long id,
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
package nl.meul.rpiservices;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** The rest interface class.
 */
@Path("v1")
public class RestInterface {

    private static final Logger LOGGER = Logger.getLogger(RestInterface.class.getName());

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status() {

        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();

        ServicesManager.getInstance().getServices().forEach((service) -> {
            Service s;
            try {
                s = service.newInstance();
                resultBuilder.add(s.getDescription(), s.getStatus());
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        resultBuilder.add("result", "ok");

        return Response.ok(resultBuilder.build().toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update() {

        Kodi kodi = new Kodi();
        JsonObjectBuilder resultBuilder = kodi.updateLibrary();
        resultBuilder.add("result", "ok");

        return Response.ok(resultBuilder.build().toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response start() {

        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();

        ServicesManager.getInstance().getServices().forEach((service) -> {
            try {
                ((Service)service.newInstance()).start();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                throw new WebApplicationException(ex, 500);
            }
        });
        resultBuilder.add("result", "ok");

        return Response.ok(resultBuilder.build().toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {

        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        ServicesManager.getInstance().getServices().forEach((service) -> {
            try {
                ((Service)service.newInstance()).stop();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                throw new WebApplicationException(ex, 500);
            }
        });
        resultBuilder.add("result", "ok");

        return Response.ok(resultBuilder.build().toString(), MediaType.APPLICATION_JSON).build();
    }
}

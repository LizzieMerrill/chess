package handlers;

import com.google.gson.Gson;
import dataAccess.dao.*;
import requests.ErrorObject;
import server.Server;
import server.StandardResponse;
import service.DataService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler extends Server implements Route {

    DataService dataService;
    public ClearHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        dataService = new DataService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            // Call the service method to clear chess data
                ErrorObject returnValue = dataService.clear();

                // Return success response
                response.status(200);
                return new Gson().toJson(returnValue);

        } catch (Exception e) {
            response.status(500);
            return new Gson().toJson(new StandardResponse("Error: " + e.getMessage()));
        }
    }
}

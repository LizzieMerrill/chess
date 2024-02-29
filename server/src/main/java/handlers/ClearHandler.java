//package handlers;
//
//import server.Server;
//import server.StandardResponse;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
////HANDLERS SHOULDNT DIRECTLY ACCESS DATABASE
//public class ClearHandler extends Server implements Route {
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//        try {
//            gameDAO.clearChessData();
//            return gson.toJson(new StandardResponse(200, "Chess data cleared successfully"));
//        } catch (Exception e) {
//            response.status(500);
//            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
//        }
//    }
//}
package handlers;

import com.google.gson.Gson;
import dataAccess.dao.*;
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
            //if(authDAO.isValidAuthToken(request.headers("Authorization"))){
                dataService.clear();

                // Return success response
                response.status(200);
//            }
//            else{
//                response.status(401);
//                //return "";
//            }
                return "{}";

        } catch (Exception e) {
            response.status(500);
            return new Gson().toJson(new StandardResponse("Error: " + e.getMessage()));
        }
    }
}

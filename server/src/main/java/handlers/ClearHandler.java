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

import server.Server;
import server.StandardResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler extends Server implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            // Call the service method to clear chess data
            dataService.clear();

            // Return success response
            return gson.toJson(new StandardResponse(200, "Chess data cleared successfully"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new StandardResponse(500, "Error: " + e.getMessage()));
        }
    }
}

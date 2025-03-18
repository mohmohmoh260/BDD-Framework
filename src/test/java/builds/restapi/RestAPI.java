package builds.restapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import workDirectory.stepDefinitions.Hooks;

import java.util.HashMap;
import java.util.Map;

public abstract class RestAPI extends Hooks {

    public void GETRequest(){

    }

    public void POSTRequest(){
        // Create JSON body using Map
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("title", "foo");
        requestBody.put("body", "bar");
        requestBody.put("userId", "1");

        // Send POST request
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://jsonplaceholder.typicode.com/posts")
                .then()
                .statusCode(201)  // Validate success status
                .extract().response();

        getScenario().log("Response Body: " + response.asString());
    }

    public void PUTRequest(){

    }
}

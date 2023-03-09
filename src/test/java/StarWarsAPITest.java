import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class StarWarsAPITest {

    @Test
    public void testGetPlanetWithLargestDiameter() {
        RestAssured.baseURI = "https://swapi.dev/api";

        // Retrieve the total number of planets in the API
        Response countResponse = given()
                .when()
                .get("/planets/")
                .then()
                .statusCode(200)
                .extract().response();

        int totalCount = countResponse.path("count");

        // Calculate the number of pages required to retrieve all the planets
        int pageSize = 10;
        int pageCount = (int) Math.ceil((double) totalCount / pageSize);

        int largestDiameter = 0;
        String largestDiameterPlanetName = "";

        // Loop through all the pages and retrieve all the planets
        for (int page = 1; page <= pageCount; page++) {
            Response response = given()
                    .queryParam("page", page)
                    .when()
                    .get("/planets")
                    .then()
                    .statusCode(200)
                    .extract().response();

            List<Map<String, Object>> results = response.path("results");

            // Loop through the planets on the current page
            for (Map<String, Object> planet : results) {
                String diameterString = (String) planet.get("diameter");

                int diameter = 0;
                if (!diameterString.equals("unknown")) {
                    diameter = Integer.parseInt(diameterString);
                }

                // Update the largest diameter and planet name if necessary
                if (diameter > largestDiameter) {
                    largestDiameter = diameter;
                    largestDiameterPlanetName = (String) planet.get("name");
                }
            }
        }

        // Print the name of the planet with the largest diameter
        System.out.println("The planet with the largest diameter is " + largestDiameterPlanetName);
    }
}

package com.techsmart.restassureddemo;


import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import  static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.equalTo;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
public class GooglePlaceApiTests {


    @DisplayName("Test Google Add Place Rest API Endpoint")
    @Test
    @Disabled
    void addPlaces() throws Exception{
        System.out.println("#######################################Adding places in Google with RestAssured#####################################\n");

        String jsonBody = generateStringFromResource("C:\\Users\\subar\\Documents\\RestAssuredDemo\\rest-assured-demo\\rest-assured-demo\\src\\test\\resources\\body.json");
        RestAssured.baseURI = "http://216.10.245.166" ;
        given()
                .param("key","qaclick123")
                .body(jsonBody)
                .when()
                .get("/maps/api/place/add/json")
                .then()
                .assertThat()
                .statusCode(200).and()
                .body("scope",equalTo("APP")).and()
                .contentType("application/json;charset=UTF-8");

    }

    @DisplayName("Test Google Add Place Rest API Endpoint and Then Delete Them")
    @Test
    void addPlaceAndDeletePlace() throws Exception {
        System.out.println("#######################################Adding places in Google with RestAssured#####################################\n");

        String jsonBody = generateStringFromResource("C:\\Users\\subar\\Documents\\RestAssuredDemo\\rest-assured-demo\\rest-assured-demo\\src\\test\\resources\\body.json");
        RestAssured.baseURI = "http://216.10.245.166" ;
        Response response = given()
                .queryParam("key", "qaclick123")
                .body(jsonBody)
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat()
                .statusCode(200).and()
                .contentType("application/json;charset=UTF-8").and()
                .extract().response();

        //Just print the output
        System.out.println(response.asString());

        //Now we will convert output into JSON
        JsonPath jsonPath = new JsonPath(response.asString());
        assertEquals(jsonPath.get("scope"),"APP");
        String placeId = jsonPath.get("place_id");
        System.out.println("The place id is " + placeId);
        //Now we will delete the place with teh placeId
String deleteRequestBody = "{\n" +
        "\"place_id\": \"" + placeId + "\"\n" +
        "}\n";
        Response responseDeleteRequest = given().
                queryParam("key", "qaclick123")
                .body(deleteRequestBody)
                .contentType("application/json;charset=UTF-8")
                .when()
                .post("/maps/api/place/delete/json")
                .then()
                .assertThat()
                .statusCode(200).extract().response();
        System.out.println(responseDeleteRequest.asString());
    }


    public String generateStringFromResource(String path) throws IOException {
        File file = new File(path);
        return new String(Files.readAllBytes(Paths.get(file.getPath())));

    }
}

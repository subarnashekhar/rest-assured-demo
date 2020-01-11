package com.techsmart.restassureddemo;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JiraApiRestAssuredTest {

      static String jSessionId  ;

    @BeforeAll
    @DisplayName("Getting teh jsession Id first")
   public static  void  getJessionId(){
        RestAssured.baseURI = "http://192.168.34.10:8080";
        ///rest/auth/1/session
        System.out.println("##########login in to Jira to get JsessionId");
        String jsonBody = "{ \"username\": \"subarnagaine\", \"password\": \"Hello@2019\" }";
        Response response = given()
                .header("Content-Type","application/json")
                .body(jsonBody)
                .when()
                .post("/rest/auth/1/session")
                .then()
                .assertThat()
                .statusCode(200).and()
                .extract().response();
JsonPath jsonPath = new JsonPath(response.asString());
        jSessionId = jsonPath.get("session.value");

        System.out.println("the sesiion id is" + jSessionId);
    }

    @DisplayName("Creating issues")
    @Test
    public void createIssues(){
        System.out.println("#############Now Creating Issues############");
        String jsonBodyCreateIssue = "{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \"RES\"\n" +
                "       },\n" +
                "       \"summary\": \"REST ye merry gentlemen.2367\",\n" +
                "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API22\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Task\"\n" +
                "       }\n" +
                "   }\n" +
                "}";
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + jSessionId + "")
                .body(jsonBodyCreateIssue)
                .when()
                .post("/rest/api/2/issue")
                .then()
                .assertThat().statusCode(201).and()
                .extract().response();

        System.out.println(response.asString());
    }

}

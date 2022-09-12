package beerBrew.step_definitions;

import beerBrew.pojo.ABV;
import beerBrew.utilities.ConfigurationReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.text.DecimalFormat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Brew_stepDefinitions {

    Response response;
    String actualNameOfBeerStyle;
    String expectedNameOfBeerStyle;

    ABV abv = new ABV();


    @When("User search a valid {string}")
    public void user_search_style(String beerStyles) {
        response = given().accept(ContentType.JSON)
                .and().baseUri(ConfigurationReader.getProperty("api_base_url"))
                .and().queryParam("name", beerStyles)
                .when().get("/styles");

        JsonPath jsonPath = response.jsonPath();


        actualNameOfBeerStyle = response.path("name[0]");
        expectedNameOfBeerStyle = beerStyles;

    }

    @Then("Status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        assertThat(expectedStatusCode, is(equalTo(response.statusCode())));
    }

    @Then("User should see matching infos")
    public void user_should_see_matching_infos() {

        assertThat(actualNameOfBeerStyle, is(equalTo(expectedNameOfBeerStyle)));

    }

    @Then("Response should match with json schema")
    public void response_should_match_json_schema() {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/getOneBeerStyle_JSONSchema.json")));
    }

    @Then("status code should not be {int}")
    public void status_code_should_not_be(Integer expectedStatusCode) {

        assertThat(200, is(not(expectedStatusCode)));
    }


    @When("User posts {string} and {string}")
    public void user_posts_and(String og, String fg) {


        abv.setOg(og);
        abv.setFg(fg);

        response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .body(abv)
                .when().post(ConfigurationReader.getProperty("api_base_url") + "/calculate/abv");

        response.prettyPrint();
    }


    @Then("{string} should be {string}")
    public void verifyHeader(String header, String value) {

        assertThat(response.header(header), is(equalTo(value)));

    }

    @Then("User should see correct abv result")
    public void user_should_see_correct() {

        double og = Double.parseDouble(abv.getOg());
        double fg = Double.parseDouble(abv.getFg());

        double expectedABV= ((og - fg) * 131.25);


        Float abvResult = response.path("abv");


        DecimalFormat df = new DecimalFormat("#.###");
        Float format = Float.valueOf(df.format(expectedABV));


        assertThat( abvResult, is(equalTo(format)));


    }

}

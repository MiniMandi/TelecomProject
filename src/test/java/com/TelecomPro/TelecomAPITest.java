package com.TelecomPro;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Listeners.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TelecomAPITest {
	
	 private static String baseUrl = "https://thinking-tester-contact-list.herokuapp.com";
	 private static String token;
	 //private static ExtentReports extent = ExtentManager.getInstance(); 
	 //private ExtentTest test;

	 ExtentReports extent;
	 ExtentTest test;
	    
     @BeforeClass
    public void setup() {
    	extent = ExtentManager.createInstance();
    	
        RestAssured.baseURI = baseUrl;
    }

    @Test(priority = 1)
    public void testAddUser() {
    	test = extent.createTest("Test Add User");
        Response response = given()
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Mini\", \"lastName\": \"Mandi\", \"email\": \"mandimini10@gmail.com\", \"password\": \"myPassword\" }")
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .extract()
            .response();

        token = response.path("token");
        //test.log(Status.PASS, "User added successfully, token generated: " + token);
        test.pass("User added successfully, token generated");
    }

    @Test(priority = 2)
    public void testGetUserProfile() {
    	test = extent.createTest("Test Get User Profile");	
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/users/me")
            .then()
            .statusCode(200)
            .body("email", equalTo("mandimini10@gmail.com"));
        System.out.println("token is: "+token);
        //test.log(Status.PASS, "User profile retrieved successfully");
        test.pass("User profile retrieved successfully");
    }

    @Test(priority = 3)
    public void testUpdateUser() {
    	test = extent.createTest("Test Update User");
        given()
            .header("Authorization", "Bearer " + token)
            .body("{ \"firstName\": \"Mini\", \"lastName\": \"Mandi1\", \"email\": \"mandimini10@gmail.com\", \"password\": \"myPassword\" }")
            .when()
            .patch("/users/me")
            .then()
            .statusCode(200);
        //test.log(Status.PASS, "User updated successfully");
        test.pass("User updated successfully");
    }

    @Test(priority = 4)
    public void testLoginUser() {
    	test = extent.createTest("Test Login User");
        Response response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"email\": \"mandimini10@gmail.com\", \"password\": \"myPassword\" }")
            .when()
            .post("/users/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

        token = response.path("token");
        //test.log(Status.PASS, "User logged in successfully, token generated: " + token);
        test.pass("User logged in successfully");
    }

    @Test(priority = 5)
    public void testAddContact() {
    	test = extent.createTest("Test Add Contact");
        given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"birthdate\": \"1970-01-01\", \"email\": \"jdoe@gmail.com\", \"phone\": \"8005555555\", \"street1\": \"1 Main St.\", \"street2\": \"Apartment A\", \"city\": \"Anytown\", \"stateProvince\": \"KS\", \"postalCode\": \"12345\", \"country\": \"USA\" }")
            .when()
            .post("/contacts")
            .then()
            .statusCode(201);
        //test.log(Status.PASS, "Contact added successfully");
        test.pass("Contact added successfully");
    }

    @Test(priority = 6)
    public void testGetContactList() {
    	test = extent.createTest("Test Get Contact List");
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/contacts")
            .then()
            .statusCode(200);
    	
    	 //test.log(Status.PASS,"Contact list retrieved successfully");
    	 test.pass("Contact list retrieved successfully");
    }

    @Test(priority = 7)
    public void testGetContact() {
    	test = extent.createTest("Test Get Contact");
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/contacts/")
            .then()
            .statusCode(200);
        //test.log(Status.PASS, "Contact retrieved successfully");
        test.pass("Contact retrieved successfully");
    }

    @Test(priority = 8)
    public void testUpdateFullContact() {
    	test = extent.createTest("Test Update Full Contact");   	
        given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Amy\", \"lastName\": \"Miller\", \"birthdate\": \"1992-02-02\", \"email\": \"amiller@gmail.com\", \"phone\": \"8005554242\", \"street1\": \"13 School St.\", \"street2\": \"Apt. 5\", \"city\": \"Washington\", \"stateProvince\": \"QC\", \"postalCode\": \"A1A1A1\", \"country\": \"Canada\" }")
            .when()
            .put("/contacts/")
            .then()
            .statusCode(200)
            .body("email", equalTo("amiller@gmail.com"));
        //test.log(Status.PASS, "Full contact updated successfully");
        test.pass("Full contact updated successfully");
    }

    @Test(priority = 9)
    public void testUpdatePartialContact() {
    	test = extent.createTest("Test Update Partial Contact");
        given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Anna1\" }")
            .when()
            .patch("/contacts/")
            .then()
            .statusCode(200)
            .body("firstName", equalTo("Anna1"));
        //test.log(Status.PASS, "Partial contact updated successfully");
        test.pass("Partial contact updated successfully");
    }

    @Test(priority = 10)
    public void testLogoutUser() {
    	test = extent.createTest("Test Logout User");
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .post("/users/logout")
            .then()
            .statusCode(200);
        //test.log(Status.PASS, "User logged out successfully");
        test.pass("User logged out successfully");
    }
    
    /**public void onFinish(ITestContext context) 
    { 
    	extent.flush();
    	
    }**/
    
    @AfterClass
    public void tearDown() {
        extent.flush();
    }
}

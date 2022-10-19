import Constants.Messages;
import Constants.TestStandEndpoints;
import TestData.CreatingRandomData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserUpdateTest {
    private String email;
    private String password;
    private String name;
    private UserResponse userResponse;
    private UserRequest user;

    // Перед каждым тестом формируем случайные тестовые данные и создаем пользователя
    @Before
    public void setUp() {
        RestAssured.baseURI = TestStandEndpoints.BASE_URL;
        this.email = CreatingRandomData.getRandomAlexeyKolyaevEmail();
        this.password = CreatingRandomData.getRandomAlexeyKolyaevString();
        this.name = CreatingRandomData.getRandomAlexeyKolyaevString();
        this.user = new UserRequest(email,password,name);
        this.userResponse = UserResponse.getRegisterUserResponse(user);
    }

    // После окончания теста удаляем созданного пользователя
    @After
    public void deleteCreatedUser() {
        this.user = new UserRequest(email,password);
        this.userResponse = UserResponse.getLoginUserResponse(user);
        UserResponse.deleteUser(userResponse);
    }

    @Test
    @DisplayName("Checking the ability to update a name")
    public void checkUpdateNameRegisteredUserTest() {
        this.user = new UserRequest(email,password,name + new Random().nextInt(10));
        Response response = userResponse.getResponseUpdateUser(userResponse);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Checking the ability to update an email")
    public void checkUpdateEmailRegisteredUserTest() {
        this.user = new UserRequest(new Random().nextInt(10) + email,password,name);
        Response response = userResponse.getResponseUpdateUser(userResponse);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Checking the ability to update a password")
    public void checkUpdatePasswordRegisteredUserTest() {
        this.user = new UserRequest(email,password + new Random().nextInt(10),name);
        Response response = userResponse.getResponseUpdateUser(userResponse);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Checking the inability to update a password of an unauthorised user")
    public void checkUpdatePasswordUnauthorisedUserUnableTest() {
        this.user = new UserRequest(email,password + new Random().nextInt(10),name);
        Response response = userResponse.getResponseUpdateUnauthorisedUser(userResponse);
        response.then().assertThat().body("message", equalTo(Messages.UNAUTHORIZED))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Checking the inability to update an email of an unauthorised user")
    public void checkUpdateEmailUnauthorisedUserUnableTest() {
        this.user = new UserRequest(new Random().nextInt(10) + email,password,name);
        Response response = userResponse.getResponseUpdateUnauthorisedUser(userResponse);
        response.then().assertThat().body("message", equalTo(Messages.UNAUTHORIZED))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Checking the inability to update a name of an unauthorised user")
    public void checkUpdateNameUnauthorisedUserUnableTest() {
        this.user = new UserRequest(email,password,new Random().nextInt(10) + name);
        Response response = userResponse.getResponseUpdateUnauthorisedUser(userResponse);
        response.then().assertThat().body("message", equalTo(Messages.UNAUTHORIZED))
                .and()
                .statusCode(401);
    }
}

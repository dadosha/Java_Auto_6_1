package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;

import static com.codeborne.selenide.Selenide.$;

public class LogInPage {
    private SelenideElement usernameField = $("[data-test-id='login'] input");
    private SelenideElement passwordField = $("[data-test-id='password'] input");
    private SelenideElement acceptButton = $("[data-test-id='action-login']");

    public VerificationCodePage validLogin(DataGenerator.UserInfo info) {
        usernameField.setValue(info.getUsername());
        passwordField.setValue(info.getPassword());
        acceptButton.click();

        return new VerificationCodePage();
    }
}

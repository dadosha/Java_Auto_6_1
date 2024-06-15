package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationCodePage {
    private SelenideElement codeField = $("[data-test-id='code'] input");
    private SelenideElement acceptButton = $("[data-test-id='action-verify']");

    public VerificationCodePage() {
        acceptButton.shouldBe(visible);
    }

    public CreditCardsPage validCodeEnter(DataGenerator.VerificationCode code) {
        codeField.setValue(code.getCode());
        acceptButton.click();

        return new CreditCardsPage();
    }
}

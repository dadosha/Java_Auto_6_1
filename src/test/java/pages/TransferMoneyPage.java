package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferMoneyPage {
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement toField = $("[data-test-id='to'] input");
    private SelenideElement acceptButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public TransferMoneyPage() {
        amountField.shouldBe(visible);
    }

    public TransferMoneyPage(String hiddenCard) {
        toField.shouldHave(Condition.value(hiddenCard));
    }

    public SelenideElement transferToCardFieldGet() {
        return toField;
    }

    public void transferMoney(int amount, String cardFrom) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(cardFrom);
        acceptButton.click();
    }

    public CreditCardsPage successTransferMoney(int amount, String cardFrom) {
        transferMoney(amount, cardFrom);
        return new CreditCardsPage();
    }

    public SelenideElement sendMoneyWithError(int amount, String cardFrom) {
        transferMoney(amount, cardFrom);
        return errorNotification;
    }

    public SelenideElement sendMoneyWithoutInfo() {
        acceptButton.click();
        return errorNotification;
    }

    public SelenideElement sendMoneyWithoutAmount(String cardFrom) {
        fromField.setValue(cardFrom);
        acceptButton.click();
        return errorNotification;
    }
}

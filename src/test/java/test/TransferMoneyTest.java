package test;

import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CreditCardsPage;
import pages.LogInPage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static data.DataGenerator.CardInfo.*;
import static data.DataGenerator.LogIn.*;
import static data.DataGenerator.LogInCode.*;

public class TransferMoneyTest {
    CreditCardsPage creditCardPage;
    Card cardTo;
    Card cardFrom;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LogInPage();
        var validAuthInfo = getCorrectUserLogInInfo();
        var validVerificationCode = getCorrectCode();
        var verificationCodePage = loginPage.validLogin(validAuthInfo);
        creditCardPage = verificationCodePage.validCodeEnter(validVerificationCode);
        List<DataGenerator.Card> cards = ChooseCard(List.of(getCorrectCard1(), getCorrectCard2()));
        cardTo = cards.get(0);
        cardFrom = cards.get(1);
    }

    @Test
    @DisplayName("Should successfully transfer money")
    void shouldSuccessfullyTransferMoney () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());
        String hiddenFromCard = hiddenCard(cardFrom.getNumber());

        int cardToBalance = creditCardPage.getCardBalance(hiddenToCard);
        int cardFromBalance = creditCardPage.getCardBalance(hiddenFromCard);
        int transferAmount = getTransferAmount(cardFromBalance);
        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var creditCardPageAfterPay = transferPage.successTransferMoney(transferAmount, cardFrom.getNumber());
        creditCardPageAfterPay.cardInfo(hiddenToCard)
                .shouldHave(Condition.exactText(hiddenToCard + ", баланс: " + String.valueOf(cardToBalance + transferAmount) + " р.\nПополнить"));
        creditCardPageAfterPay.cardInfo(hiddenFromCard)
                .shouldHave(Condition.exactText(hiddenFromCard + ", баланс: " + String.valueOf(cardFromBalance - transferAmount) + " р.\nПополнить"));
    }

    @Test
    @DisplayName("Should successfully transfer all money")
    void shouldSuccessfullyTransferAllMoney () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());
        String hiddenFromCard = hiddenCard(cardFrom.getNumber());

        int cardToBalance = creditCardPage.getCardBalance(hiddenToCard);
        int cardFromBalance = creditCardPage.getCardBalance(hiddenFromCard);
        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var creditCardPageAfterPay = transferPage.successTransferMoney(cardFromBalance, cardFrom.getNumber());
        creditCardPageAfterPay.cardInfo(hiddenToCard)
                .shouldHave(Condition.exactText(hiddenToCard + ", баланс: " + String.valueOf(cardToBalance + cardFromBalance) + " р.\nПополнить"));
        creditCardPageAfterPay.cardInfo(hiddenFromCard)
                .shouldHave(Condition.exactText(hiddenFromCard + ", баланс: 0 р.\nПополнить"));
    }

    @Test
    @DisplayName("Should error transfer money when send money without info")
    void shouldErrorTransferMoneyWhenNoInfoTransfer () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());

        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var errorNotification = transferPage.sendMoneyWithoutInfo();
        errorNotification
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Произошла ошибка"));
    }

    @Test
    @DisplayName("Should error transfer money when no exists card")
    void shouldErrorTransferMoneyWhenNoExistsCard() {
        cardFrom = getIncorrectCard2();
        String hiddenToCard = hiddenCard(cardTo.getNumber());

        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var errorNotification = transferPage.sendMoneyWithError(1, cardFrom.getNumber());
        errorNotification
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Произошла ошибка"));
    }

    @Test
    @DisplayName("Should error transfer money when send money without amount")
    void shouldErrorTransferMoneyWhenWithoutAmount () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());

        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var errorNotification = transferPage.sendMoneyWithoutAmount(cardFrom.getNumber());
        errorNotification
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Произошла ошибка"));
    }

    @Test
    @DisplayName("Should error transfer money when send money without card")
    void shouldErrorTransferMoneyWhenWithoutCard () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());

        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var errorNotification = transferPage.sendMoneyWithError(1, "");
        errorNotification
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Произошла ошибка"));
    }

    @Test
    @DisplayName("Should error transfer money when amount more balance")
    void shouldErrorTransferMoneyWhenAmountMoreBalance () {
        String hiddenToCard = hiddenCard(cardTo.getNumber());
        String hiddenFromCard = hiddenCard(cardFrom.getNumber());

        int cardFromBalance = creditCardPage.getCardBalance(hiddenFromCard);
        int transferAmount = cardFromBalance + 1;
        var transferPage = creditCardPage.openTransferCardPage(hiddenToCard);
        transferPage.transferToCardFieldGet().shouldHave(Condition.value(hiddenToCard));
        var errorNotification = transferPage.sendMoneyWithError(transferAmount, cardFrom.getNumber());
        errorNotification
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Произошла ошибка"));
    }
}

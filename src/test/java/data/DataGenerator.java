package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));
    private static Random rnd = new Random();

    private DataGenerator() {
    }

    public static String generateCreditCardNumber() {
        return faker.business().creditCardNumber();
    }

    public static String hiddenCard(String cardNumber) {
        return String.format("**** **** **** %s", cardNumber.substring(cardNumber.length() - 4));
    }

    public static int getTransferAmount(int amount) {
        if (amount < 0) {
            throw new AmountLessZeroException(
                    "На карте баланс меньше 0"
            );
        }
        return rnd.nextInt(amount);
    }

    public static List<Card> chooseCard(List<Card> cards) {
        int rand = rnd.nextInt(2);
        Card cardTo = cards.get(rand);
        Card cardFrom = cards.get(1 - rand);
        return List.of(cardTo, cardFrom);
    }

    @Value
    public static class UserInfo {
        String username;
        String password;
    }

    public static class LogIn {
        private LogIn() {
        }

        public static UserInfo getCorrectUserLogInInfo() {
            return new UserInfo("vasya", "qwerty123");
        }

        public static UserInfo getIncorrectUserLogInInfo() {
            return new UserInfo("vasya", "qwerty12345");
        }
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static class LogInCode {
        private LogInCode() {
        }

        public static VerificationCode getCorrectCode() {
            return new VerificationCode("12345");
        }

        public static VerificationCode getIncorrectCode() {
            return new VerificationCode("00000");
        }
    }

    @Value
    public static class Card {
        String number;
    }

    public static class CardInfo {
        private CardInfo() {
        }

        public static Card getCorrectCard1() {
            return new Card("5559 0000 0000 0001");
        }

        public static Card getCorrectCard2() {
            return new Card("5559 0000 0000 0002");
        }

        public static Card getIncorrectCard2() {
            return new Card(generateCreditCardNumber());
        }
    }
}

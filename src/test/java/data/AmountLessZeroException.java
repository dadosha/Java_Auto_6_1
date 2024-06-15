package data;

public class AmountLessZeroException extends RuntimeException {
    public AmountLessZeroException(String msg) {
        super(msg);
    }
}

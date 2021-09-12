package pw.gatchina.okex.client;

public class APIException extends RuntimeException {
    private int code;

    public APIException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public APIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        if (this.code != 0) {
            return this.code + " : " + super.getMessage();
        }
        return super.getMessage();
    }
}

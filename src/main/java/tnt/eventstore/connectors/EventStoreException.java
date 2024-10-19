package tnt.eventstore.connectors;


public class EventStoreException extends Exception {

    private final ErrorCode errorCode;

    public enum ErrorCode {
        CONNECTION_ERROR,
        STORAGE_ERROR,
        RETRIEVAL_ERROR,
        SCOPE_NOT_FOUND,
        UNKNOWN_ERROR
    }

    /**
     * Konstruktor für eine allgemeine EventStoreException mit einer Fehlermeldung.
     * @param message Die Fehlermeldung
     */
    public EventStoreException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    /**
     * Konstruktor für eine EventStoreException mit einer Fehlermeldung und einem Fehlercode.
     * @param message Die Fehlermeldung
     * @param errorCode Der spezifische Fehlercode
     */
    public EventStoreException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Konstruktor für eine EventStoreException mit einer Fehlermeldung und der zugrunde liegenden Ursache.
     * @param message Die Fehlermeldung
     * @param cause Die zugrunde liegende Ausnahme
     */
    public EventStoreException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    /**
     * Konstruktor für eine EventStoreException mit einer Fehlermeldung, einem Fehlercode und der zugrunde liegenden Ursache.
     * @param message Die Fehlermeldung
     * @param errorCode Der spezifische Fehlercode
     * @param cause Die zugrunde liegende Ausnahme
     */
    public EventStoreException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gibt den spezifischen Fehlercode für die Exception zurück.
     * @return Der Fehlercode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
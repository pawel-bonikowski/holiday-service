package bluestone.task.holiday.domain;

public class ExternalServiceError extends RuntimeException {
    public ExternalServiceError(Exception e) {
        super(e);
    }
}

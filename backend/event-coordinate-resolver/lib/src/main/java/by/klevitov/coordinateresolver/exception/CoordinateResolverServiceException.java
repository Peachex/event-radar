package by.klevitov.coordinateresolver.exception;

public class CoordinateResolverServiceException extends RuntimeException {
    public CoordinateResolverServiceException(String message) {
        super(message);
    }

    public CoordinateResolverServiceException(Throwable cause) {
        super(cause);
    }
}

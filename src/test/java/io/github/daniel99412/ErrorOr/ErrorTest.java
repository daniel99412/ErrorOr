import io.github.daniel99412.Error.Error;
import io.github.daniel99412.Error.ErrorType;
import org.junit.jupiter.api.Assertions;

import io.github.daniel99412.Error.Error;
import io.github.daniel99412.Error.ErrorType;

import org.junit.jupiter.api.Test;

public class ErrorTest {

    @Test
    public void testError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        Assertions.assertNotNull(error);
    }

    @Test
    public void testFailureErrorType() {
        Error error = Error.failure("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Failure, error.type());
    }

    @Test
    public void testUnexpectedErrorType() {
        Error error = Error.unexpected("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Unexpected, error.type());
    }

    @Test
    public void testValidationErrorType() {
        Error error = Error.validation("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Validation, error.type());
    }

    @Test
    public void testConflictErrorType() {
        Error error = Error.conflict("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Conflict, error.type());
    }

    @Test
    public void testNotFoundErrorType() {
        Error error = Error.notFound("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.NotFound, error.type());
    }

    @Test
    public void testUnauthorizedErrorType() {
        Error error = Error.unauthorized("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Unauthorized, error.type());
    }

    @Test
    public void testForbiddenErrorType() {
        Error error = Error.forbidden("Test error", "Test description", null);
        Assertions.assertEquals(ErrorType.Forbidden, error.type());
    }
}

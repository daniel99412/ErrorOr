package com.dpardo.ErrorOr;

import com.dpardo.Error.Error;
import com.dpardo.Error.ErrorType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorTest {

    @Test
    public void testError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        assertNotNull(error);
    }

    @Test
    public void testFailureErrorType() {
        Error error = Error.failure("Test error", "Test description", null);
        assertEquals(ErrorType.Failure, error.type());
    }

    @Test
    public void testUnexpectedErrorType() {
        Error error = Error.unexpected("Test error", "Test description", null);
        assertEquals(ErrorType.Unexpected, error.type());
    }

    @Test
    public void testValidationErrorType() {
        Error error = Error.validation("Test error", "Test description", null);
        assertEquals(ErrorType.Validation, error.type());
    }

    @Test
    public void testConflictErrorType() {
        Error error = Error.conflict("Test error", "Test description", null);
        assertEquals(ErrorType.Conflict, error.type());
    }

    @Test
    public void testNotFoundErrorType() {
        Error error = Error.notFound("Test error", "Test description", null);
        assertEquals(ErrorType.NotFound, error.type());
    }

    @Test
    public void testUnauthorizedErrorType() {
        Error error = Error.unauthorized("Test error", "Test description", null);
        assertEquals(ErrorType.Unauthorized, error.type());
    }

    @Test
    public void testForbiddenErrorType() {
        Error error = Error.forbidden("Test error", "Test description", null);
        assertEquals(ErrorType.Forbidden, error.type());
    }
}

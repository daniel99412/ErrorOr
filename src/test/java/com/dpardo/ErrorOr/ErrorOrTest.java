package com.dpardo.ErrorOr;

import com.dpardo.Error.Error;
import com.dpardo.Error.ErrorType;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ErrorOrTest {

    @Test
    public void testFromError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        ErrorOr<String> errorOr = ErrorOr.from(error);
        assertTrue(errorOr.isError());
    }

    @Test
    public void testFromValue() {
        String value = "Text value";
        ErrorOr<String> errorOr = ErrorOr.fromValue(value);
        assertFalse(errorOr.isError());
    }

    @Test
    public void testIsError() {
        ErrorOr<String> errorOr = ErrorOr.from(new Error("Test error", "Test description", ErrorType.Failure, null));
        assertTrue(errorOr.isError());

        errorOr = ErrorOr.fromValue("Test value");
        assertFalse(errorOr.isError());
    }

    @Test
    public void testGetValue() {
        String value = "Test value";
        ErrorOr<String> errorOr = ErrorOr.fromValue(value);
        assertEquals(value, errorOr.getValue());
    }

    @Test
    public void testGetError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        ErrorOr<String> errorOr = ErrorOr.from(error);
        assertEquals(List.of(error), errorOr.getErrors());
    }

    @Test
    public void testMatch_WithValue_ShouldExecuteOnValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.fromValue("Success");

        // Act
        String result = errorOr.match(
            value -> value + " processed",
            errors -> "Error processed"
        );

        // Assert
        assertEquals("Success processed", result);
    }

    @Test
    public void testMatch_WithError_ShouldExecuteOnError() {
        // Arrange
        Error error = Error.failure("Test.Code", "Test error", null);
        ErrorOr<String> errorOr = ErrorOr.from(error);

        // Act
        String result = errorOr.match(
            value -> value + " processed",
            errors -> errors.get(0).code() + " processed"
        );

        // Assert
        assertEquals("Test.Code processed", result);
    }

    @Test
    public void testMap_WithValue_ShouldTransformValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.fromValue("123");

        // Act
        ErrorOr<Integer> result = errorOr.map(Integer::parseInt);

        // Assert
        assertFalse(result.isError());
        assertEquals(123, result.getValue());
    }

    @Test
    public void testMap_WithError_ShouldPropagateError() {
        // Arrange
        Error error = Error.failure("Test.Code", "Test error", null);
        ErrorOr<String> errorOr = ErrorOr.from(error);

        // Act
        ErrorOr<Integer> result = errorOr.map(Integer::parseInt);

        // Assert
        assertTrue(result.isError());
        assertEquals(error, result.getFirstError());
    }

    @Test
    public void testThen_WithValue_ShouldChainToNextErrorOr() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.fromValue("Success");

        // Act
        ErrorOr<String> result = errorOr.then(value -> ErrorOr.fromValue(value + " chained"));

        // Assert
        assertFalse(result.isError());
        assertEquals("Success chained", result.getValue());
    }

    @Test
    public void testThen_WithValue_ShouldChainToNextErrorOrWithError() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.fromValue("Success");
        Error nextError = Error.failure("Next.Error", "Next error", null);

        // Act
        ErrorOr<String> result = errorOr.then(value -> ErrorOr.from(nextError));

        // Assert
        assertTrue(result.isError());
        assertEquals(nextError, result.getFirstError());
    }

    @Test
    public void testThen_WithError_ShouldPropagateErrorAndNotChain() {
        // Arrange
        Error initialError = Error.failure("Initial.Error", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.from(initialError);

        // Act
        ErrorOr<String> result = errorOr.then(value -> {
            fail("The 'then' function should not be called when there is an error.");
            return ErrorOr.fromValue("Should not happen");
        });

        // Assert
        assertTrue(result.isError());
        assertEquals(initialError, result.getFirstError());
    }
}
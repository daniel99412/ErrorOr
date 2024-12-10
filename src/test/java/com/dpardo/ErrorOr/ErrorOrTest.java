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
}
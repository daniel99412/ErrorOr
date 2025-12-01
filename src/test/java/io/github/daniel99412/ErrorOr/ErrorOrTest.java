import io.github.daniel99412.ErrorOr.ErrorOr;
import io.github.daniel99412.Error.Error;
import io.github.daniel99412.Error.ErrorType;
import org.junit.jupiter.api.Assertions;

import io.github.daniel99412.Error.Error;
import io.github.daniel99412.Error.ErrorType;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ErrorOrTest {

    @Test
    public void testFromError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        ErrorOr<String> errorOr = ErrorOr.error(error);
        Assertions.assertTrue(errorOr.isError());
    }

    @Test
    public void testFromValue() {
        String value = "Text value";
        ErrorOr<String> errorOr = ErrorOr.of(value);
        Assertions.assertFalse(errorOr.isError());
    }

    @Test
    public void testIsError() {
        ErrorOr<String> errorOr = ErrorOr.error(new Error("Test error", "Test description", ErrorType.Failure, null));
        Assertions.assertTrue(errorOr.isError());

        errorOr = ErrorOr.of("Test value");
        Assertions.assertFalse(errorOr.isError());
    }

    @Test
    public void testGetValue() {
        String value = "Test value";
        ErrorOr<String> errorOr = ErrorOr.of(value);
        Assertions.assertEquals(value, errorOr.getValue());
    }

    @Test
    public void testGetError() {
        Error error = new Error("Test error", "Test description", ErrorType.Failure, null);
        ErrorOr<String> errorOr = ErrorOr.error(error);
        Assertions.assertEquals(List.of(error), errorOr.getErrors());
    }

    @Test
    public void testMatch_WithValue_ShouldExecuteOnValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");

        // Act
        String result = errorOr.match(
            value -> value + " processed",
            errors -> "Error processed"
        );

        // Assert
        Assertions.assertEquals("Success processed", result);
    }

    @Test
    public void testMatch_WithError_ShouldExecuteOnError() {
        // Arrange
        Error error = Error.failure("Test.Code", "Test error", null);
        ErrorOr<String> errorOr = ErrorOr.error(error);

        // Act
        String result = errorOr.match(
            value -> value + " processed",
            errors -> errors.get(0).code() + " processed"
        );

        // Assert
        Assertions.assertEquals("Test.Code processed", result);
    }

    @Test
    public void testMap_WithValue_ShouldTransformValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("123");

        // Act
        ErrorOr<Integer> result = errorOr.map(Integer::parseInt);

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(123, result.getValue());
    }

    @Test
    public void testMap_WithError_ShouldPropagateError() {
        // Arrange
        Error error = Error.failure("Test.Code", "Test error", null);
        ErrorOr<String> errorOr = ErrorOr.error(error);

        // Act
        ErrorOr<Integer> result = errorOr.map(Integer::parseInt);

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(error, result.getFirstError());
    }

    @Test
    public void testThen_WithValue_ShouldChainToNextErrorOr() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");

        // Act
        ErrorOr<String> result = errorOr.then(value -> ErrorOr.of(value + " chained"));

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals("Success chained", result.getValue());
    }

    @Test
    public void testThen_WithValue_ShouldChainToNextErrorOrWithError() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");
        Error nextError = Error.failure("Next.Error", "Next error", null);

        // Act
        ErrorOr<String> result = errorOr.then(value -> ErrorOr.error(nextError));

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(nextError, result.getFirstError());
    }

    @Test
    public void testThen_WithError_ShouldPropagateErrorAndNotChain() {
        // Arrange
        Error initialError = Error.failure("Initial.Error", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);

        // Act
        ErrorOr<String> result = errorOr.then(value -> {
            Assertions.fail("The 'then' function should not be called when there is an error.");
            return ErrorOr.of("Should not happen");
        });

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(initialError, result.getFirstError());
    }

    @Test
    public void testFilter_WithValueAndPassingPredicate_ShouldReturnValue() {
        // Arrange
        ErrorOr<Integer> errorOr = ErrorOr.of(20);
        Error validationError = Error.validation("Test.Code", "User is underage", null);

        // Act
        ErrorOr<Integer> result = errorOr.filter(age -> age >= 18, validationError);

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(20, result.getValue());
    }

    @Test
    public void testFilter_WithValueAndFailingPredicate_ShouldReturnError() {
        // Arrange
        ErrorOr<Integer> errorOr = ErrorOr.of(15);
        Error validationError = Error.validation("Test.Code", "User is underage", null);

        // Act
        ErrorOr<Integer> result = errorOr.filter(age -> age >= 18, validationError);

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(validationError, result.getFirstError());
    }

    @Test
    public void testFilter_WithError_ShouldPropagateError() {
        // Arrange
        Error initialError = Error.notFound("Test.Code", "User not found", null);
        ErrorOr<Integer> errorOr = ErrorOr.error(initialError);
        Error validationError = Error.validation("Test.Code", "User is underage", null);

        // Act
        ErrorOr<Integer> result = errorOr.filter(age -> age >= 18, validationError);

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(initialError, result.getFirstError());
        Assertions.assertNotEquals(validationError, result.getFirstError());
    }

    @Test
    public void testGetOrElse_WithValue_ShouldReturnValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("actual_value");

        // Act
        String result = errorOr.getOrElse("default_value");

        // Assert
        Assertions.assertEquals("actual_value", result);
    }

    @Test
    public void testGetOrElse_WithError_ShouldReturnDefaultValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.error(Error.unexpected("Test.Code", "Unexpected error", null));

        // Act
        String result = errorOr.getOrElse("default_value");

        // Assert
        Assertions.assertEquals("default_value", result);
    }

    @Test
    public void testOrElse_WithValue_ShouldReturnValue() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("actual_value");

        // Act
        String result = errorOr.orElse(() -> {
            Assertions.fail("Supplier should not be called when there is a value.");
            return "default_value";
        });

        // Assert
        Assertions.assertEquals("actual_value", result);
    }

    @Test
    public void testOrElse_WithError_ShouldReturnDefaultValueFromSupplier() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.error(Error.unexpected("Test.Code", "Unexpected error", null));
        final boolean[] supplierCalled = {false};

        // Act
        String result = errorOr.orElse(() -> {
            supplierCalled[0] = true;
            return "default_value";
        });

        // Assert
        Assertions.assertEquals("default_value", result);
        Assertions.assertTrue(supplierCalled[0], "Supplier should be called when there is an error.");
    }

    @Test
    public void testPeek_WithValue_ShouldExecuteAction() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("test");
        final boolean[] actionExecuted = {false};

        // Act
        ErrorOr<String> result = errorOr.peek(value -> {
            actionExecuted[0] = true;
            Assertions.assertEquals("test", value);
        });

        // Assert
        Assertions.assertTrue(actionExecuted[0]);
        Assertions.assertSame(errorOr, result, "Peek should return the same instance for chaining.");
    }

    @Test
    public void testPeek_WithError_ShouldNotExecuteAction() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.error(Error.unexpected("Test.Code", "Unexpected error", null));

        // Act
        ErrorOr<String> result = errorOr.peek(value -> Assertions.fail("Action should not be executed for an error."));

        // Assert
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testPeekError_WithError_ShouldExecuteAction() {
        // Arrange
        Error error = Error.unexpected("Test.Code", "Unexpected error", null);
        ErrorOr<String> errorOr = ErrorOr.error(error);
        final boolean[] actionExecuted = {false};

        // Act
        ErrorOr<String> result = errorOr.peekError(errors -> {
            actionExecuted[0] = true;
            Assertions.assertEquals(1, errors.size());
            Assertions.assertEquals(error, errors.get(0));
        });

        // Assert
        Assertions.assertTrue(actionExecuted[0]);
        Assertions.assertSame(errorOr, result, "PeekError should return the same instance for chaining.");
    }

    @Test
    public void testPeekError_WithValue_ShouldNotExecuteAction() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("test");

        // Act
        ErrorOr<String> result = errorOr.peekError(errors -> Assertions.fail("Action should not be executed for a value."));

        // Assert
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testMapError_WithError_ShouldTransformError() {
        // Arrange
        Error initialError = Error.unexpected("Initial.Error", "Initial description", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);

        // Act
        ErrorOr<String> result = errorOr.mapError(error -> Error.validation(
            "Transformed.Error",
            "Transformed " + error.description(),
            null
        ));

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertNotEquals(initialError, result.getFirstError());
        Assertions.assertEquals("Transformed.Error", result.getFirstError().code());
        Assertions.assertEquals("Transformed Initial description", result.getFirstError().description());
    }

    @Test
    public void testMapError_WithValue_ShouldDoNothing() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("test_value");

        // Act
        ErrorOr<String> result = errorOr.mapError(error -> {
            Assertions.fail("mapError should not be called for a value.");
            return Error.unexpected("Test.Code", "Unexpected error", null);
        });

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
        Assertions.assertEquals("test_value", result.getValue());
    }

    @Test
    public void testMatchConsumer_WithValue_ShouldExecuteOnValueConsumer() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");
        final boolean[] consumerExecuted = {false};

        // Act
        errorOr.match(
            value -> {
                consumerExecuted[0] = true;
                Assertions.assertEquals("Success", value);
            },
            errors -> Assertions.fail("onError consumer should not be called.")
        );

        // Assert
        Assertions.assertTrue(consumerExecuted[0]);
    }

    @Test
    public void testMatchConsumer_WithError_ShouldExecuteOnErrorConsumer() {
        // Arrange
        Error error = Error.failure("Test.Code", "Test error", null);
        ErrorOr<String> errorOr = ErrorOr.error(error);
        final boolean[] consumerExecuted = {false};

        // Act
        errorOr.match(
            value -> Assertions.fail("onValue consumer should not be called."),
            errors -> {
                consumerExecuted[0] = true;
                Assertions.assertEquals(1, errors.size());
                Assertions.assertEquals(error, errors.get(0));
            }
        );

        // Assert
        Assertions.assertTrue(consumerExecuted[0]);
    }

    @Test
    public void testMatch_WithMultipleErrors_ShouldPassAllErrorsToFunction() {
        // Arrange
        Error error1 = Error.failure("Test.Code1", "First error", null);
        Error error2 = Error.validation("Test.Code2", "Second error", null);
        ErrorOr<String> errorOr = ErrorOr.errors(List.of(error1, error2));

        // Act
        String result = errorOr.match(
            value -> "Should not be called",
            errors -> {
                Assertions.assertEquals(2, errors.size());
                Assertions.assertTrue(errors.contains(error1));
                Assertions.assertTrue(errors.contains(error2));
                return "Correctly handled multiple errors";
            }
        );

        // Assert
        Assertions.assertEquals("Correctly handled multiple errors", result);
    }

    // --- Tests for elseFunc ---
    @Test
    public void testElseFunc_WithValue_ShouldReturnOriginal() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("original");

        // Act
        ErrorOr<String> result = errorOr.elseFunc(errors -> {
            Assertions.fail("Else function should not be called with a value.");
            return Error.unexpected("ShouldNotBeCalled", "", null);
        });

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testElseFunc_WithError_ShouldApplyFunctionAndReturnNewError() {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);
        Error newError = Error.validation("Transformed.Code", "Transformed error", null);

        // Act
        ErrorOr<String> result = errorOr.elseFunc(errors -> {
            Assertions.assertEquals(1, errors.size());
            Assertions.assertEquals(initialError, errors.get(0));
            return newError;
        });

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(newError, result.getFirstError());
    }

    // --- Tests for elseFuncList ---
    @Test
    public void testElseFuncList_WithValue_ShouldReturnOriginal() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("original");

        // Act
        ErrorOr<String> result = errorOr.elseFuncList(errors -> {
            Assertions.fail("Else function list should not be called with a value.");
            return List.of(Error.unexpected("ShouldNotBeCalled", "", null));
        });

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testElseFuncList_WithError_ShouldApplyFunctionAndReturnNewErrors() {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);
        Error newError1 = Error.validation("Transformed.Code1", "Transformed error 1", null);
        Error newError2 = Error.validation("Transformed.Code2", "Transformed error 2", null);
        List<Error> newErrorList = List.of(newError1, newError2);

        // Act
        ErrorOr<String> result = errorOr.elseFuncList(errors -> {
            Assertions.assertEquals(1, errors.size());
            Assertions.assertEquals(initialError, errors.get(0));
            return newErrorList;
        });

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(newErrorList, result.getErrors());
    }

    // --- Tests for elseError ---
    @Test
    public void testElseError_WithValue_ShouldReturnOriginal() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("original");

        // Act
        ErrorOr<String> result = errorOr.elseError(Error.unexpected("ShouldNotBeUsed", "", null));

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testElseError_WithError_ShouldReturnSpecifiedError() {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);
        Error replacementError = Error.conflict("Replacement.Code", "Replacement error", null);

        // Act
        ErrorOr<String> result = errorOr.elseError(replacementError);

        // Assert
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(replacementError, result.getFirstError());
        Assertions.assertNotEquals(initialError, result.getFirstError());
    }

    // --- Tests for elseFuncValue ---
    @Test
    public void testElseFuncValue_WithValue_ShouldReturnOriginal() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("original");

        // Act
        ErrorOr<String> result = errorOr.elseFuncValue(errors -> {
            Assertions.fail("Else function value should not be called with a value.");
            return "should_not_be_returned";
        });

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testElseFuncValue_WithError_ShouldApplyFunctionAndReturnNewValue() {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);
        String replacementValue = "fallback_value";

        // Act
        ErrorOr<String> result = errorOr.elseFuncValue(errors -> {
            Assertions.assertEquals(1, errors.size());
            Assertions.assertEquals(initialError, errors.get(0));
            return replacementValue;
        });

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(replacementValue, result.getValue());
    }

    // --- Tests for elseValue ---
    @Test
    public void testElseValue_WithValue_ShouldReturnOriginal() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("original");

        // Act
        ErrorOr<String> result = errorOr.elseValue("fallback_value");

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertSame(errorOr, result);
    }

    @Test
    public void testElseValue_WithError_ShouldReturnSpecifiedValue() {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);
        String replacementValue = "fallback_value";

        // Act
        ErrorOr<String> result = errorOr.elseValue(replacementValue);

        // Assert
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(replacementValue, result.getValue());
    }

    @Test
    public void testMapAsync_WithValue_ShouldTransformValue() throws Exception {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("123");

        // Act
        CompletableFuture<ErrorOr<Integer>> resultFuture = errorOr.mapAsync(value ->
            CompletableFuture.supplyAsync(() -> Integer.parseInt(value))
        );

        // Assert
        ErrorOr<Integer> result = resultFuture.get();
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(123, result.getValue());
    }

    @Test
    public void testMapAsync_WithError_ShouldPropagateError() throws Exception {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);

        // Act
        CompletableFuture<ErrorOr<Integer>> resultFuture = errorOr.mapAsync(value -> {
            Assertions.fail("mapAsync function should not be called when there is an error.");
            return CompletableFuture.supplyAsync(() -> 0);
        });

        // Assert
        ErrorOr<Integer> result = resultFuture.get();
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(initialError, result.getFirstError());
    }

    @Test
    public void testMapAsync_WithValueFunctionThrowsException_ShouldCompleteExceptionally() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("abc");
        RuntimeException exception = new RuntimeException("Test Exception");

        // Act
        CompletableFuture<ErrorOr<Integer>> resultFuture = errorOr.mapAsync(value ->
            CompletableFuture.supplyAsync(() -> {
                throw exception;
            })
        );

        // Assert
        Assertions.assertThrows(Exception.class, resultFuture::get);
        Assertions.assertTrue(resultFuture.isCompletedExceptionally());
    }

    @Test
    public void testThenAsync_WithValue_ShouldChainToNextSuccessfulErrorOr() throws Exception {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");

        // Act
        CompletableFuture<ErrorOr<String>> resultFuture = errorOr.thenAsync(value ->
            CompletableFuture.supplyAsync(() -> ErrorOr.of(value + " chained"))
        );

        // Assert
        ErrorOr<String> result = resultFuture.get();
        Assertions.assertFalse(result.isError());
        Assertions.assertEquals("Success chained", result.getValue());
    }

    @Test
    public void testThenAsync_WithValue_ShouldChainToNextFailedErrorOr() throws Exception {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("Success");
        Error nextError = Error.failure("Next.Error", "Next error", null);

        // Act
        CompletableFuture<ErrorOr<String>> resultFuture = errorOr.thenAsync(value ->
            CompletableFuture.supplyAsync(() -> ErrorOr.error(nextError))
        );

        // Assert
        ErrorOr<String> result = resultFuture.get();
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(nextError, result.getFirstError());
    }

    @Test
    public void testThenAsync_WithError_ShouldPropagateError() throws Exception {
        // Arrange
        Error initialError = Error.failure("Initial.Code", "Initial error", null);
        ErrorOr<String> errorOr = ErrorOr.error(initialError);

        // Act
        CompletableFuture<ErrorOr<String>> resultFuture = errorOr.thenAsync(value -> {
            Assertions.fail("thenAsync function should not be called when there is an error.");
            return CompletableFuture.supplyAsync(() -> ErrorOr.of("some value"));
        });

        // Assert
        ErrorOr<String> result = resultFuture.get();
        Assertions.assertTrue(result.isError());
        Assertions.assertEquals(initialError, result.getFirstError());
    }

    @Test
    public void testThenAsync_WithValueFunctionCompletesExceptionally_ShouldCompleteExceptionally() {
        // Arrange
        ErrorOr<String> errorOr = ErrorOr.of("abc");
        RuntimeException exception = new RuntimeException("Test Exception");

        // Act
        CompletableFuture<ErrorOr<Integer>> resultFuture = errorOr.thenAsync(value ->
            CompletableFuture.supplyAsync(() -> {
                throw exception;
            })
        );

        // Assert
        Assertions.assertThrows(Exception.class, resultFuture::get);
        Assertions.assertTrue(resultFuture.isCompletedExceptionally());
    }
}

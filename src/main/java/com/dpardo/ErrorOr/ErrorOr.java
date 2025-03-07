package com.dpardo.ErrorOr;

import com.dpardo.Error.Error;
import com.dpardo.Error.utils.ErrorComparer;
import com.dpardo.ErrorOr.extensions.Else;
import com.dpardo.ErrorOr.extensions.FailIf;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Represents a result that can either be a valid value or contain a list of errors.
 *
 * This class provides a way to encapsulate the result of an operation that can either
 * succeed with a value or fail with one or more errors. It provides methods to check if
 * the operation has errors, access the value when there are no errors, and retrieve the
 * errors if present.
 *
 * @param <TValue> The type of the value that is returned when no errors are present.
 */
public class ErrorOr<TValue> implements IErrorOr.WithValue<TValue> {
    private final TValue value;
    private final List<Error> errors;

    /**
     * Default constructor is not supported. Use the provided factory methods to instantiate.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    private ErrorOr() {
        throw new UnsupportedOperationException("Default construction of ErrorOr<TValue> is invalid. Please use provided factory methods to instantiate.");
    }

    /**
     * Creates an instance of ErrorOr with a single error.
     *
     * @param error The error to associate with the instance.
     * @throws IllegalArgumentException if the error is null.
     */
    private ErrorOr(Error error) {
        if (error == null) {
            throw new IllegalArgumentException("Error cannot be null");
        }
        this.errors = Collections.singletonList(error);
        this.value = null;
    }

    /**
     * Creates an instance of ErrorOr with a list of errors.
     *
     * @param errors The list of errors to associate with the instance.
     * @throws IllegalArgumentException if the errors list is null or empty.
     */
    private ErrorOr(List<Error> errors) {
        if (errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException("Errors cannot be null or empty");
        }
        this.errors = List.copyOf(errors);
        this.value = null;
    }

    /**
     * Creates an instance of ErrorOr with a value.
     *
     * @param value The value to associate with the instance.
     * @throws IllegalArgumentException if the value is null.
     */
    private ErrorOr(TValue value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
        this.errors = Collections.emptyList();
    }

    /**
     * Checks if the current instance contains errors.
     *
     * @return {@code true} if there are errors, {@code false} otherwise.
     */
    @Override
    public Boolean isError() {
        return (errors != null && !errors.isEmpty());
    }

    /**
     * Retrieves the value if no errors are present.
     *
     * @return The value associated with this instance.
     * @throws IllegalStateException if errors are present, and the value is not accessible.
     */
    @Override
    public TValue getValue() {
        if (!isError()) {
            return value;
        }
        throw new IllegalStateException("The Value property cannot be accessed when errors have been recorded. Check isError() before accessing Value.");
    }

    /**
     * Retrieves the list of errors if present.
     *
     * @return An unmodifiable list of errors.
     * @throws IllegalStateException if no errors are present, and the errors are not accessible.
     */
    @Override
    public List<Error> getErrors() {
        if (isError()) {
            return Collections.unmodifiableList(errors);
        }
        throw new IllegalStateException("The Errors property cannot be accessed when no errors have been recorded. Check isError() before accessing Errors.");
    }

    /**
     * Compares this {@link ErrorOr} instance with another object for equality.
     *
     * This method first checks if the two objects are the same instance. If they are not, it checks if the
     * objects are of the same class. If the objects are of the same class, it compares them based on whether
     * they represent errors or values. If both objects are not errors, their values are compared for equality.
     * If both objects are errors, their error lists are checked for equality using the {@link ErrorComparer#checkIfErrorsAreEqual} method.
     *
     * @param obj The object to compare with this {@link ErrorOr} instance.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ErrorOr<?> other = (ErrorOr<?>) obj;

        if (!isError()) {
            return !other.isError() && Objects.equals(this.value, other.value);
        }

        return other.isError() && ErrorComparer.checkIfErrorsAreEqual(this.errors, other.errors);
    }

    /**
     * Generates a hash code for this {@link ErrorOr} instance.
     *
     * This method computes the hash code for the instance based on whether the instance represents an error
     * or a value. If the instance represents a value, the hash code is generated from the value. If the instance
     * represents errors, the hash code is calculated by combining the hash codes of the individual errors in the list.
     *
     * @return The hash code for this {@link ErrorOr} instance.
     */
    @Override
    public int hashCode() {
        if (!isError()) {
            return Objects.hash(value);
        }

        int hashCode = 17;
        for (Error error : errors) {
            hashCode = 31 * hashCode + Objects.hash(error);
        }
        return hashCode;
    }

    /**
     * Retrieves the errors if present or returns an empty list.
     *
     * @return An unmodifiable list of errors or an empty list if no errors are present.
     */
    public List<Error> getErrorsOrEmptyList() {
        return isError()
            ? Collections.unmodifiableList(errors)
            : EmptyErrors.getInstance();
    }

    /**
     * Retrieves the first error from the list of errors.
     *
     * @return The first error if errors are present.
     * @throws IllegalStateException if no errors are present, and the first error is not accessible.
     */
    public Error getFirstError() {
        if (isError()) {
            return errors.get(0);
        }
        throw new IllegalStateException("The FirstError property cannot be accessed when no errors have been recorded. Check isError() before accessing FirstError.");
    }

    /**
     * Factory method to create an instance of ErrorOr with a single error.
     *
     * @param <TValue> The type of the value.
     * @param error The error to associate with the instance.
     * @return A new instance of ErrorOr containing the error.
     */
    public static <TValue> ErrorOr<TValue> from(Error error) {
        return new ErrorOr(error);
    }

    /**
     * Factory method to create an instance of ErrorOr with a list of errors.
     *
     * @param <TValue> The type of the value.
     * @param errors The list of errors to associate with the instance.
     * @return A new instance of ErrorOr containing the errors.
     */
    public static <TValue> ErrorOr<TValue> from(List<Error> errors) {
        return new ErrorOr(errors);
    }

    /**
     * Factory method to create an instance of ErrorOr with a value.
     *
     * @param <TValue> The type of the value.
     * @param value The value to associate with the instance.
     * @return A new instance of ErrorOr containing the value.
     * @throws IllegalArgumentException if the value is null.
     */
    public static <TValue> ErrorOr<TValue> fromValue(TValue value) {
        return new ErrorOr(value);
    }

    /**
     * Delegates the failure check to the {@link FailIf#failIf(ErrorOr, Function, Error)} method.
     *
     * Evaluates the specified condition on the current value. If the condition is met, this method
     * fails the current {@link ErrorOr} instance and returns a new instance containing the specified error.
     * The actual logic is implemented in the {@link FailIf} utility class.
     *
     * @param onValue A {@link Function} that evaluates the current value and returns {@code true} if the condition is met.
     * @param error The {@link Error} to include if the condition is met.
     * @return The current instance if the condition is not met, or a new {@link ErrorOr} instance containing the error if it is.
     */
    public ErrorOr<TValue> failIf(Function<TValue, Boolean> onValue, Error error) {
        return FailIf.failIf(this, onValue, error);
    }

    /**
     * Delegates the failure check to the {@link FailIf#failIf(ErrorOr, Function, Function)} method.
     *
     * Evaluates the specified condition on the current value. If the condition is met, this method
     * fails the current {@link ErrorOr} instance and returns a new instance containing the error
     * generated by the provided error builder. The actual logic is implemented in the {@link FailIf} utility class.
     *
     * @param onValue A {@link Function} that evaluates the current value and returns {@code true} if the condition is met.
     * @param errorBuilder A {@link Function} that builds an {@link Error} based on the current value.
     * @return The current instance if the condition is not met, or a new {@link ErrorOr} instance containing the generated error if it is.
     */
    public ErrorOr<TValue> failIf(Function<TValue, Boolean> onValue, Function<TValue, Error> errorBuilder) {
        return FailIf.failIf(this, onValue, errorBuilder);
    }

    /**
     * Delegates the asynchronous failure check to the {@link FailIf#failIfAsync(ErrorOr, Function, Error)} method.
     *
     * Asynchronously evaluates the specified condition on the current value. If the condition is met, this method
     * fails the current {@link ErrorOr} instance and returns a {@link CompletableFuture} containing a new instance with the specified error.
     * The actual logic is implemented in the {@link FailIf} utility class.
     *
     * @param onValue A {@link Function} that asynchronously evaluates the current value and returns a {@link CompletableFuture}
     *                containing {@code true} if the condition is met.
     * @param error The {@link Error} to include if the condition is met.
     * @return A {@link CompletableFuture} containing the current instance if the condition is not met, or a new {@link ErrorOr}
     *         instance containing the error if it is.
     */
    public CompletableFuture<ErrorOr<TValue>> failIfAsync(Function<TValue, CompletableFuture<Boolean>> onValue, Error error) {
        return FailIf.failIfAsync(this, onValue, error);
    }

    /**
     * Delegates the asynchronous failure check to the {@link FailIf#failIfAsync(ErrorOr, Function, Function)} method.
     *
     * Asynchronously evaluates the specified condition on the current value. If the condition is met, this method
     * fails the current {@link ErrorOr} instance and returns a {@link CompletableFuture} containing a new instance with the error
     * generated asynchronously by the provided error builder. The actual logic is implemented in the {@link FailIf} utility class.
     *
     * @param onValue A {@link Function} that asynchronously evaluates the current value and returns a {@link CompletableFuture}
     *                containing {@code true} if the condition is met.
     * @param errorBuilder A {@link Function} that asynchronously builds an {@link Error} based on the current value.
     * @return A {@link CompletableFuture} containing the current instance if the condition is not met, or a new {@link ErrorOr}
     *         instance containing the generated error if it is.
     */
    public CompletableFuture<ErrorOr<TValue>> failIfAsync(Function<TValue, CompletableFuture<Boolean>> onValue, Function<TValue, CompletableFuture<Error>> errorBuilder) {
        return FailIf.failIfAsync(this, onValue, errorBuilder);
    }

    /**
     * Delegates the failure handling to the {@link Else#elseFunc} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseFunc} method
     * with the provided function that processes the list of errors. This method returns a new {@link ErrorOr}
     * containing the result of the error handling function. If no error is present, the original {@link ErrorOr} is returned.
     *
     * @param onError A {@link Function} that processes the list of errors and returns a new {@link Error} if the error condition is met.
     * @return A new {@link ErrorOr} containing the result of the error handling function if an error is present, or the original {@link ErrorOr} if not.
     */
    public ErrorOr<TValue> elseFunc(Function<List<Error>, Error> onError) {
        return Else.elseFunc(this, onError);
    }

    /**
     * Delegates the failure handling to the {@link Else#elseFuncList} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseFuncList} method
     * with the provided function that processes the list of errors. This method returns a new {@link ErrorOr}
     * containing the transformed list of errors. If no error is present, the original {@link ErrorOr} is returned.
     *
     * @param onError A {@link Function} that processes the list of errors and returns a new list of errors if the error condition is met.
     * @return A new {@link ErrorOr} containing the transformed list of errors if an error is present, or the original {@link ErrorOr} if not.
     */
    public ErrorOr<TValue> elseFuncList(Function<List<Error>, List<Error>> onError) {
        return Else.elseFuncList(this, onError);
    }

    /**
     * Delegates the failure handling to the {@link Else#elseError} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseError} method
     * and returns a new {@link ErrorOr} containing the specified error. If no error is present, the original {@link ErrorOr} is returned.
     *
     * @param error The error to return if the {@link ErrorOr} contains an error.
     * @return A new {@link ErrorOr} containing the specified error if an error is present, or the original {@link ErrorOr} if not.
     */
    public ErrorOr<TValue> elseError(Error error) {
        return Else.elseError(this, error);
    }

    /**
     * Delegates the failure handling to the {@link Else#elseFuncValue} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseFuncValue} method
     * with the provided function that processes the list of errors. This method returns a new {@link ErrorOr}
     * containing the result as a value. If no error is present, the original {@link ErrorOr} is returned.
     *
     * @param onError A {@link Function} that processes the list of errors and returns a value if the error condition is met.
     * @return A new {@link ErrorOr} containing the result if an error is present, or the original {@link ErrorOr} if not.
     */
    public ErrorOr<TValue> elseFuncValue(Function<List<Error>, TValue> onError) {
        return Else.elseFuncValue(this, onError);
    }

    /**
     * Delegates the failure handling to the {@link Else#elseValue} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseValue} method
     * and returns a new {@link ErrorOr} containing the specified value. If no error is present, the original {@link ErrorOr} is returned.
     *
     * @param value The value to return if the {@link ErrorOr} contains an error.
     * @return A new {@link ErrorOr} containing the specified value if an error is present, or the original {@link ErrorOr} if not.
     */
    public ErrorOr<TValue> elseValue(TValue value) {
        return Else.elseValue(this, value);
    }

    /**
     * Delegates the asynchronous failure handling to the {@link Else#elseAsync} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseAsync} method
     * with the provided asynchronous function that processes the list of errors. This method returns a {@link CompletableFuture}
     * containing a new {@link ErrorOr} instance with the result of the error handling function. If no error is present, the
     * original {@link ErrorOr} is returned in a completed {@link CompletableFuture}.
     *
     * @param onError A {@link Function} that asynchronously processes the list of errors and returns a {@link CompletableFuture}
     *                containing the result.
     * @return A {@link CompletableFuture} containing the result of the error handling function if an error is present,
     *         or a {@link CompletableFuture} with the original {@link ErrorOr} if not.
     */
    public CompletableFuture<ErrorOr<TValue>> elseAsync(Function<List<Error>, CompletableFuture<TValue>> onError) {
        return Else.elseAsync(this, onError);
    }

    /**
     * Delegates the asynchronous failure handling to the {@link Else#elseAsyncFuncError} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseAsyncFuncError} method
     * with the provided asynchronous function that processes the list of errors. This method returns a {@link CompletableFuture}
     * containing a new {@link ErrorOr} instance with the error generated asynchronously. If no error is present, the
     * original {@link ErrorOr} is returned in a completed {@link CompletableFuture}.
     *
     * @param onError A {@link Function} that asynchronously processes the list of errors and returns a {@link CompletableFuture}
     *                containing the error.
     * @return A {@link CompletableFuture} containing the error if an error is present, or a {@link CompletableFuture} with
     *         the original {@link ErrorOr} if not.
     */
    public CompletableFuture<ErrorOr<TValue>> elseAsyncFuncError(Function<List<Error>, CompletableFuture<Error>> onError) {
        return Else.elseAsyncFuncError(this, onError);
    }

    /**
     * Delegates the asynchronous failure handling to the {@link Else#elseAsyncList} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseAsyncList} method
     * with the provided asynchronous function that processes the list of errors. This method returns a {@link CompletableFuture}
     * containing a new {@link ErrorOr} instance with the transformed list of errors. If no error is present, the
     * original {@link ErrorOr} is returned in a completed {@link CompletableFuture}.
     *
     * @param onError A {@link Function} that asynchronously processes the list of errors and returns a {@link CompletableFuture}
     *                containing the transformed list of errors.
     * @return A {@link CompletableFuture} containing the transformed list of errors if an error is present, or a
     *         {@link CompletableFuture} with the original {@link ErrorOr} if not.
     */
    public CompletableFuture<ErrorOr<TValue>> elseAsyncList(Function<List<Error>, CompletableFuture<List<Error>>> onError) {
        return Else.elseAsyncList(this, onError);
    }

    /**
     * Delegates the asynchronous failure handling to the {@link Else#elseAsyncError} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseAsyncError} method
     * with the provided asynchronous error handling function. This method returns a {@link CompletableFuture} containing
     * a new {@link ErrorOr} instance with the specified error. If no error is present, the original {@link ErrorOr}
     * is returned in a completed {@link CompletableFuture}.
     *
     * @param onError A {@link CompletableFuture} representing the error to return if the {@link ErrorOr} contains an error.
     * @return A {@link CompletableFuture} containing the specified error if an error is present, or a
     *         {@link CompletableFuture} with the original {@link ErrorOr} if not.
     */
    public CompletableFuture<ErrorOr<TValue>> elseAsyncError(CompletableFuture<Error> onError) {
        return Else.elseAsyncError(this, onError);
    }

    /**
     * Delegates the asynchronous failure handling to the {@link Else#elseAsyncValue} method.
     *
     * If the current {@link ErrorOr} instance contains an error, it delegates to the {@link Else#elseAsyncValue} method
     * with the provided asynchronous value handling function. This method returns a {@link CompletableFuture} containing
     * a new {@link ErrorOr} instance with the specified value. If no error is present, the original {@link ErrorOr}
     * is returned in a completed {@link CompletableFuture}.
     *
     * @param onError A {@link CompletableFuture} representing the value to return if the {@link ErrorOr} contains an error.
     * @return A {@link CompletableFuture} containing the specified value if an error is present, or a
     *         {@link CompletableFuture} with the original {@link ErrorOr} if not.
     */
    public CompletableFuture<ErrorOr<TValue>> elseAsyncValue(CompletableFuture<TValue> onError) {
        return Else.elseAsyncValue(this, onError);
    }
}

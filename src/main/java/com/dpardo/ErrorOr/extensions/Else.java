package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Utility class that provides various extension methods for handling errors in the {@link ErrorOr} class.
 * These methods offer ways to handle or transform the error state of an {@link ErrorOr} instance,
 * depending on the specific operation required. They allow users to manage errors in a more flexible
 * and functional way by applying functions when an error is present.
 */
public class Else {
    /**
     * If the {@link ErrorOr} contains an error, applies the provided {@link Function} to the list of errors.
     * Otherwise, returns the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function to apply to the list of errors if an error is present.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return The original {@link ErrorOr} if no error is present, or a new {@link ErrorOr} with the result of the function.
     */
    public static <TValue> ErrorOr<TValue> elseFunc(ErrorOr<TValue> errorOr, Function<List<Error>, Error> onError) {
        if (!errorOr.isError()) {
            return errorOr;
        }
        return ErrorOr.from(onError.apply(errorOr.getErrors()));
    }

    /**
     * If the {@link ErrorOr} contains an error, applies the provided {@link Function} to the list of errors
     * and returns the resulting list of errors. Otherwise, returns the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function to apply to the list of errors if an error is present.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return The original {@link ErrorOr} if no error is present, or a new {@link ErrorOr} with the transformed errors.
     */
    public static <TValue> ErrorOr<TValue> elseFuncList(ErrorOr<TValue> errorOr, Function<List<Error>, List<Error>> onError) {
        if (!errorOr.isError()) {
            return errorOr;
        }
        return ErrorOr.from(onError.apply(errorOr.getErrors()));
    }

    /**
     * If the {@link ErrorOr} contains an error, returns a new {@link ErrorOr} with the specified error.
     * Otherwise, returns the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param error The error to return if the {@link ErrorOr} contains an error.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return The original {@link ErrorOr} if no error is present, or a new {@link ErrorOr} with the provided error.
     */
    public static <TValue> ErrorOr<TValue> elseError(ErrorOr<TValue> errorOr, Error error) {
        if (!errorOr.isError()) {
            return errorOr;
        }
        return ErrorOr.from(List.of(error));
    }

    /**
     * If the {@link ErrorOr} contains an error, applies the provided function to the list of errors and returns
     * a new {@link ErrorOr} containing the resulting value. Otherwise, returns the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function to apply to the list of errors if an error is present.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return The original {@link ErrorOr} if no error is present, or a new {@link ErrorOr} with the transformed value.
     */
    public static <TValue> ErrorOr<TValue> elseFuncValue(ErrorOr<TValue> errorOr, Function<List<Error>, TValue> onError) {
        if (!errorOr.isError()) {
            return errorOr;
        }
        return ErrorOr.fromValue(onError.apply(errorOr.getErrors()));
    }

    /**
     * If the {@link ErrorOr} contains an error, returns a new {@link ErrorOr} with the specified value.
     * Otherwise, returns the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The value to return if the {@link ErrorOr} contains an error.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return The original {@link ErrorOr} if no error is present, or a new {@link ErrorOr} with the provided value.
     */
    public static <TValue> ErrorOr<TValue> elseValue(ErrorOr<TValue> errorOr, TValue onError) {
        if (!errorOr.isError()) {
            return errorOr;
        }
        return ErrorOr.fromValue(onError);
    }

    /**
     * Asynchronously checks if the {@link ErrorOr} contains an error. If it does, applies the provided function
     * to the list of errors and returns a {@link CompletableFuture} with the result. Otherwise, returns a completed
     * {@link CompletableFuture} with the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function that asynchronously handles the errors and returns a {@link CompletableFuture} of the result.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return A {@link CompletableFuture} that either completes with the original {@link ErrorOr} or with the result of the error handling function.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> elseAsync(ErrorOr<TValue> errorOr, Function<List<Error>, CompletableFuture<TValue>> onError) {
        if (!errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }
        return onError.apply(errorOr.getErrors()).thenApply(ErrorOr::fromValue);
    }

    /**
     * Asynchronously checks if the {@link ErrorOr} contains an error. If it does, applies the provided function
     * to the list of errors and returns a {@link CompletableFuture} with the resulting error. Otherwise, returns a completed
     * {@link CompletableFuture} with the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function that asynchronously handles the errors and returns a {@link CompletableFuture} of the error.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return A {@link CompletableFuture} that either completes with the original {@link ErrorOr} or with the error generated by the function.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> elseAsyncFuncError(ErrorOr<TValue> errorOr, Function<List<Error>, CompletableFuture<Error>> onError) {
        if (!errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }
        return onError.apply(errorOr.getErrors()).thenApply(err -> ErrorOr.from(List.of(err)));
    }

    /**
     * Asynchronously checks if the {@link ErrorOr} contains an error. If it does, applies the provided function
     * to the list of errors and returns a {@link CompletableFuture} with the resulting list of errors. Otherwise, returns
     * a completed {@link CompletableFuture} with the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError The function that asynchronously handles the errors and returns a {@link CompletableFuture} of the list of errors.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return A {@link CompletableFuture} that either completes with the original {@link ErrorOr} or with the list of errors.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> elseAsyncList(ErrorOr<TValue> errorOr, Function<List<Error>, CompletableFuture<List<Error>>> onError) {
        if (!errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }
        return onError.apply(errorOr.getErrors()).thenApply(ErrorOr::from);
    }

    /**
     * Asynchronously checks if the {@link ErrorOr} contains an error. If it does, returns a {@link CompletableFuture}
     * with the specified error. Otherwise, returns a completed {@link CompletableFuture} with the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param error A {@link CompletableFuture} representing the error to return if the {@link ErrorOr} contains an error.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return A {@link CompletableFuture} that either completes with the original {@link ErrorOr} or with the specified error.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> elseAsyncError(ErrorOr<TValue> errorOr, CompletableFuture<Error> error) {
        if (!errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }
        return error.thenApply(err -> ErrorOr.from(List.of(err)));
    }

    /**
     * Asynchronously checks if the {@link ErrorOr} contains an error. If it does, returns a {@link CompletableFuture}
     * with the specified value. Otherwise, returns a completed {@link CompletableFuture} with the original {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to check for errors.
     * @param onError A {@link CompletableFuture} representing the value to return if the {@link ErrorOr} contains an error.
     * @param <TValue> The type of the value stored in the {@link ErrorOr}.
     * @return A {@link CompletableFuture} that either completes with the original {@link ErrorOr} or with the specified value.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> elseAsyncValue(ErrorOr<TValue> errorOr, CompletableFuture<TValue> onError) {
        if (!errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }
        return onError.thenApply(ErrorOr::fromValue);
    }
}

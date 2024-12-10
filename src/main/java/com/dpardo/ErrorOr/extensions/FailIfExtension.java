package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FailIfExtension {

    /**
     * If the state is value, the provided function {@code onValue} is invoked asynchronously.
     * If {@code onValue} returns true, the given {@code error} will be returned, and the state will be error.
     *
     * @param errorOr The {@link CompletableFuture} of {@link ErrorOr} instance.
     * @param onValue The function to execute if the state is a value.
     * @param error   The {@link Error} to return if the {@code onValue} function returns true.
     * @param <TValue> The type of the underlying value in the {@link ErrorOr} instance.
     * @return A {@link CompletableFuture} of {@link ErrorOr} containing the error if {@code onValue} returns true; otherwise, the original instance.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIf(
            CompletableFuture<ErrorOr<TValue>> errorOr,
            Function<TValue, Boolean> onValue,
            Error error
    ) {
        return errorOr.thenApply(result -> result.failIf(onValue, error));
    }

    /**
     * If the state is value, the provided function {@code onValue} is invoked asynchronously.
     * If {@code onValue} returns true, the given {@code errorBuilder} will be executed to generate an error.
     *
     * @param errorOr     The {@link CompletableFuture} of {@link ErrorOr} instance.
     * @param onValue     The function to execute if the state is a value.
     * @param errorBuilder The error builder function to generate an {@link Error} if {@code onValue} returns true.
     * @param <TValue>     The type of the underlying value in the {@link ErrorOr} instance.
     * @return A {@link CompletableFuture} of {@link ErrorOr} containing the error if {@code onValue} returns true; otherwise, the original instance.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIf(
            CompletableFuture<ErrorOr<TValue>> errorOr,
            Function<TValue, Boolean> onValue,
            Function<TValue, Error> errorBuilder
    ) {
        return errorOr.thenApply(result -> result.failIf(onValue, errorBuilder));
    }

    /**
     * If the state is value, the provided function {@code onValue} is invoked asynchronously.
     * If {@code onValue} returns true, the given {@code error} will be returned, and the state will be error.
     *
     * @param errorOr The {@link CompletableFuture} of {@link ErrorOr} instance.
     * @param onValue The function to execute asynchronously if the state is a value.
     * @param error   The {@link Error} to return if the {@code onValue} function returns true.
     * @param <TValue> The type of the underlying value in the {@link ErrorOr} instance.
     * @return A {@link CompletableFuture} of {@link ErrorOr} containing the error if {@code onValue} returns true; otherwise, the original instance.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIfAsync(
            CompletableFuture<ErrorOr<TValue>> errorOr,
            Function<TValue, CompletableFuture<Boolean>> onValue,
            Error error
    ) {
        return errorOr.thenCompose(result -> result.failIfAsync(onValue, error));
    }

    /**
     * If the state is value, the provided function {@code onValue} is invoked asynchronously.
     * If {@code onValue} returns true, the given {@code errorBuilder} will be executed asynchronously to generate an error.
     *
     * @param errorOr      The {@link CompletableFuture} of {@link ErrorOr} instance.
     * @param onValue      The function to execute asynchronously if the state is a value.
     * @param errorBuilder The error builder function to generate an {@link Error} asynchronously if {@code onValue} returns true.
     * @param <TValue>      The type of the underlying value in the {@link ErrorOr} instance.
     * @return A {@link CompletableFuture} of {@link ErrorOr} containing the error if {@code onValue} returns true; otherwise, the original instance.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIfAsync(
            CompletableFuture<ErrorOr<TValue>> errorOr,
            Function<TValue, CompletableFuture<Boolean>> onValue,
            Function<TValue, CompletableFuture<Error>> errorBuilder
    ) {
        return errorOr.thenCompose(result -> result.failIfAsync(onValue, errorBuilder));
    }
}

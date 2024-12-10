package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FailIf {
    /**
     * Fails the current {@link ErrorOr} instance if the specified condition is met.
     *
     * If the current instance is already an error, it is returned as-is. Otherwise, the provided condition is evaluated
     * on the current value. If the condition returns {@code true}, a new {@link ErrorOr} instance is created containing
     * the specified error. If the condition is not met, the current instance is returned.
     *
     * @param onValue A {@link Function} that evaluates the current value and returns {@code true} if the condition is met.
     * @param error The {@link Error} to include if the condition is met.
     * @return The current instance if the condition is not met, or a new {@link ErrorOr} instance containing the error if it is.
     */
    public static <TValue> ErrorOr<TValue> failIf(ErrorOr<TValue> errorOr, Function<TValue, Boolean> onValue, Error error) {
        if (errorOr.isError()) {
            return errorOr;
        }

        return onValue.apply(errorOr.getValue()) ? ErrorOr.from(List.of(error)) : errorOr;
    }

    /**
     * Fails the current {@link ErrorOr} instance if the specified condition is met, using a dynamic error builder.
     *
     * If the current instance is already an error, it is returned as-is. Otherwise, the provided condition is evaluated
     * on the current value. If the condition returns {@code true}, a new {@link ErrorOr} instance is created containing
     * the error generated by the provided error builder. If the condition is not met, the current instance is returned.
     *
     * @param onValue A {@link Function} that evaluates the current value and returns {@code true} if the condition is met.
     * @param errorBuilder A {@link Function} that builds an {@link Error} based on the current value.
     * @return The current instance if the condition is not met, or a new {@link ErrorOr} instance containing the generated error if it is.
     */
    public static <TValue> ErrorOr<TValue> failIf(ErrorOr<TValue> errorOr, Function<TValue, Boolean> onValue, Function<TValue, Error> errorBuilder) {
        if (errorOr.isError()) {
            return errorOr;
        }

        return onValue.apply(errorOr.getValue()) ? ErrorOr.from(errorBuilder.apply(errorOr.getValue())) : errorOr;
    }

    /**
     * Asynchronously fails the current {@link ErrorOr} instance if the specified condition is met.
     *
     * If the current instance is already an error, a completed {@link CompletableFuture} containing the current instance
     * is returned. Otherwise, the provided condition is evaluated asynchronously on the current value. If the condition
     * returns {@code true}, a new {@link ErrorOr} instance containing the specified error is returned. If the condition
     * is not met, the current instance is returned.
     *
     * @param onValue A {@link Function} that asynchronously evaluates the current value and returns a {@link CompletableFuture}
     *                containing {@code true} if the condition is met.
     * @param error The {@link Error} to include if the condition is met.
     * @return A {@link CompletableFuture} containing the current instance if the condition is not met, or a new {@link ErrorOr}
     *         instance containing the error if it is.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIfAsync(ErrorOr<TValue> errorOr, Function<TValue, CompletableFuture<Boolean>> onValue, Error error) {
        if (errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }

        return onValue.apply(errorOr.getValue()).thenApply(result -> result ? ErrorOr.from(List.of(error)) : errorOr);
    }

    /**
     * Asynchronously fails the current {@link ErrorOr} instance if the specified condition is met, using a dynamic error builder.
     *
     * If the current instance is already an error, a completed {@link CompletableFuture} containing the current instance
     * is returned. Otherwise, the provided condition is evaluated asynchronously on the current value. If the condition
     * returns {@code true}, a new {@link ErrorOr} instance is created containing the error generated asynchronously by the
     * provided error builder. If the condition is not met, the current instance is returned.
     *
     * @param onValue A {@link Function} that asynchronously evaluates the current value and returns a {@link CompletableFuture}
     *                containing {@code true} if the condition is met.
     * @param errorBuilder A {@link Function} that asynchronously builds an {@link Error} based on the current value.
     * @return A {@link CompletableFuture} containing the current instance if the condition is not met, or a new {@link ErrorOr}
     *         instance containing the generated error if it is.
     */
    public static <TValue> CompletableFuture<ErrorOr<TValue>> failIfAsync(ErrorOr<TValue> errorOr, Function<TValue, CompletableFuture<Boolean>> onValue, Function<TValue, CompletableFuture<Error>> errorBuilder) {
        if (errorOr.isError()) {
            return CompletableFuture.completedFuture(errorOr);
        }

        return onValue.apply(errorOr.getValue()).thenCompose(result -> {
            if (result) {
                return errorBuilder.apply(errorOr.getValue()).thenApply(error -> ErrorOr.from(List.of(error)));
            }
            return CompletableFuture.completedFuture(errorOr);
        });
    }
}

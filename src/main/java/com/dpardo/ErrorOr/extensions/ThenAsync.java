package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Provides an extension method to asynchronously chain operations on an ErrorOr instance (flatMap).
 */
public class ThenAsync {

    private ThenAsync() {
        // Private constructor to prevent instantiation
    }

    /**
     * Asynchronously chains a function that returns a {@link CompletableFuture} of an {@link ErrorOr},
     * effectively flat-mapping the result.
     *
     * @param errorOr The initial {@link ErrorOr} instance.
     * @param onValue The asynchronous function to apply to the value.
     * @param <TValue> The value type of the initial {@link ErrorOr}.
     * @param <TNextValue> The value type of the new {@link ErrorOr} returned by the function.
     * @return A {@link CompletableFuture} representing the result of the chained operation.
     */
    public static <TValue, TNextValue> CompletableFuture<ErrorOr<TNextValue>> thenAsync(
        ErrorOr<TValue> errorOr,
        Function<TValue, CompletableFuture<ErrorOr<TNextValue>>> onValue) {

        if (errorOr.isError()) {
            return CompletableFuture.completedFuture(ErrorOr.errors(errorOr.getErrors()));
        }

        return onValue.apply(errorOr.getValue());
    }
}

package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Provides an extension method to asynchronously map the value of an ErrorOr instance.
 */
public class MapAsync {

    private MapAsync() {
        // Private constructor to prevent instantiation
    }

    /**
     * Asynchronously transforms the value of an {@link ErrorOr} if it is not an error.
     * If the instance contains an error, the error is propagated into the resulting {@link CompletableFuture}.
     *
     * @param errorOr The {@link ErrorOr} instance to map.
     * @param onValue The asynchronous function to apply to the value if it exists.
     * @param <TValue> The current value type.
     * @param <TNextValue> The next value type after the asynchronous transformation.
     * @return A {@link CompletableFuture} containing the new {@link ErrorOr} with the transformed value,
     *         or an {@link ErrorOr} with the original errors.
     */
    public static <TValue, TNextValue> CompletableFuture<ErrorOr<TNextValue>> mapAsync(
        ErrorOr<TValue> errorOr,
        Function<TValue, CompletableFuture<TNextValue>> onValue) {

        if (errorOr.isError()) {
            return CompletableFuture.completedFuture(ErrorOr.errors(errorOr.getErrors()));
        }

        return onValue.apply(errorOr.getValue())
                .thenApply(ErrorOr::of);
    }
}

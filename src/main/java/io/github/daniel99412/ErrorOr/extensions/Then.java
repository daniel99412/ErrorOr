package io.github.daniel99412.ErrorOr.extensions;

import io.github.daniel99412.ErrorOr.ErrorOr;

import java.util.function.Function;

/**
 * Provides an extension method to chain operations on an ErrorOr instance (flatMap).
 */
public class Then {
    private Then() {
        // Private constructor to prevent instantiation
    }

    /**
     * Chains a function that returns an {@link ErrorOr}, effectively flat-mapping the result.
     * If the initial {@link ErrorOr} instance contains an error, that error is propagated and the function is not executed.
     * If the initial instance contains a value, the function is executed, and its resulting {@link ErrorOr} is returned.
     *
     * @param errorOr The initial {@link ErrorOr} instance.
     * @param onValue The function to apply to the value, which returns a new {@link ErrorOr}.
     * @param <TValue> The value type of the initial {@link ErrorOr}.
     * @param <TNextValue> The value type of the new {@link ErrorOr} returned by the function.
     * @return The result of the function if the initial {@link ErrorOr} has a value, or an {@link ErrorOr} with the original errors.
     */
    public static <TValue, TNextValue> ErrorOr<TNextValue> then(ErrorOr<TValue> errorOr, Function<TValue, ErrorOr<TNextValue>> onValue) {
        if (errorOr.isError()) {
            return ErrorOr.errors(errorOr.getErrors());
        }
        return onValue.apply(errorOr.getValue());
    }
}

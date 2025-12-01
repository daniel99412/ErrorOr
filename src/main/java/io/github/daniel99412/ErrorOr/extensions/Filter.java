package io.github.daniel99412.ErrorOr.extensions;

import io.github.daniel99412.Error.Error;
import io.github.daniel99412.ErrorOr.ErrorOr;

import java.util.function.Predicate;

/**
 * Provides an extension method to conditionally filter the value of an ErrorOr instance.
 */
public class Filter {

    private Filter() {
        // Private constructor to prevent instantiation
    }

    /**
     * If the {@link ErrorOr} has a value and the value satisfies the predicate, returns the original {@link ErrorOr}.
     * If the value does not satisfy the predicate, returns a new {@link ErrorOr} with the provided error.
     * If the {@link ErrorOr} already contains an error, it is returned as-is.
     *
     * @param errorOr The {@link ErrorOr} instance to filter.
     * @param predicate The predicate to apply to the value if it exists.
     * @param error The error to return if the predicate is not satisfied.
     * @param <TValue> The type of the value.
     * @return The original {@link ErrorOr} if the value satisfies the predicate or if it already has an error,
     *         otherwise a new {@link ErrorOr} containing the provided error.
     */
    public static <TValue> ErrorOr<TValue> filter(
        ErrorOr<TValue> errorOr,
        Predicate<TValue> predicate,
        Error error) {

        if (errorOr.isError()) {
            return errorOr;
        }

        if (!predicate.test(errorOr.getValue())) {
            return ErrorOr.error(error);
        }

        return errorOr;
    }
}

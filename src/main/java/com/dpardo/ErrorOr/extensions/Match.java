package com.dpardo.ErrorOr.extensions;

import com.dpardo.Error.Error;
import com.dpardo.ErrorOr.ErrorOr;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides extension methods for matching on ErrorOr instances.
 * These methods allow for handling both success and error paths in a single, expressive call.
 */
public class Match {

    private Match() {
        // Private constructor to prevent instantiation
    }

    /**
     * Processes an {@link ErrorOr} by executing one of two provided functions, based on whether it's a value or an error.
     * This method allows for a functional approach to handling results where both paths return a value.
     *
     * @param errorOr The {@link ErrorOr} instance to match against.
     * @param onValue The function to execute if the {@link ErrorOr} contains a value.
     * @param onError The function to execute if the {@link ErrorOr} contains errors.
     * @param <TValue> The type of the value held by the {@link ErrorOr}.
     * @param <TResult> The return type of the functions.
     * @return The result of the executed function (either {@code onValue} or {@code onError}).
     */
    public static <TValue, TResult> TResult match(ErrorOr<TValue> errorOr, Function<TValue, TResult> onValue, Function<List<Error>, TResult> onError) {
        if (errorOr.isError()) {
            return onError.apply(errorOr.getErrors());
        }
        return onValue.apply(errorOr.getValue());
    }

    /**
     * Processes an {@link ErrorOr} by executing one of two provided consumers, based on whether it's a value or an error.
     * This method is useful for side-effects where no return value is needed from the match operation.
     *
     * @param errorOr The {@link ErrorOr} instance to match against.
     * @param onValue The consumer to execute if the {@link ErrorOr} contains a value.
     * @param onError The consumer to execute if the {@link ErrorOr} contains errors.
     * @param <TValue> The type of the value held by the {@link ErrorOr}.
     */
    public static <TValue> void match(ErrorOr<TValue> errorOr, Consumer<TValue> onValue, Consumer<List<Error>> onError) {
        if (errorOr.isError()) {
            onError.accept(errorOr.getErrors());
            return;
        }
        onValue.accept(errorOr.getValue());
    }
}
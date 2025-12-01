package com.dpardo.ErrorOr.extensions;

import com.dpardo.Error.Error;
import com.dpardo.ErrorOr.ErrorOr;

import java.util.List;
import java.util.function.Consumer;

/**
 * Provides extension methods for performing side-effects on an ErrorOr instance.
 */
public class Peek {

    private Peek() {
        // Private constructor to prevent instantiation
    }

    /**
     * If the {@link ErrorOr} has a value, performs the given action on it.
     * This method is useful for side-effects like logging.
     *
     * @param errorOr The {@link ErrorOr} instance.
     * @param action The action to perform on the value.
     * @param <TValue> The type of the value.
     * @return The original {@link ErrorOr} instance, allowing for method chaining.
     */
    public static <TValue> ErrorOr<TValue> peek(ErrorOr<TValue> errorOr, Consumer<TValue> action) {
        if (!errorOr.isError()) {
            action.accept(errorOr.getValue());
        }
        return errorOr;
    }

    /**
     * If the {@link ErrorOr} has an error, performs the given action on the list of errors.
     * This method is useful for side-effects like logging errors.
     *
     * @param errorOr The {@link ErrorOr} instance.
     * @param action The action to perform on the list of errors.
     * @param <TValue> The type of the value.
     * @return The original {@link ErrorOr} instance, allowing for method chaining.
     */
    public static <TValue> ErrorOr<TValue> peekError(ErrorOr<TValue> errorOr, Consumer<List<Error>> action) {
        if (errorOr.isError()) {
            action.accept(errorOr.getErrors());
        }
        return errorOr;
    }
}

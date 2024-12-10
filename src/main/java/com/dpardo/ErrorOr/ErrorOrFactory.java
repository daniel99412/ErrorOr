package com.dpardo.ErrorOr;

/**
 * A utility class that provides factory methods for creating instances of {@link ErrorOr}.
 *
 * This class is designed to simplify the creation of {@link ErrorOr} instances. It cannot be instantiated
 * as it only contains static methods for creating {@link ErrorOr} objects.
 */
public final class ErrorOrFactory {
    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException Always thrown, as the class cannot be instantiated.
     */
    private ErrorOrFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Factory method to create an instance of {@link ErrorOr} with a value.
     *
     * @param <TValue> The type of the value.
     * @param value The value to associate with the instance.
     * @return A new instance of {@link ErrorOr} containing the value.
     * @throws IllegalArgumentException if the value is null.
     */
    public static <TValue> ErrorOr<TValue> from(TValue value) {
        return ErrorOr.fromValue(value);
    }
}

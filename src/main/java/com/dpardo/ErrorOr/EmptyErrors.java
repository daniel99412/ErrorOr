package com.dpardo.ErrorOr;

import com.dpardo.Error.Error;

import java.util.Collections;
import java.util.List;

/**
 * A utility class that provides an empty, unmodifiable list of errors.
 *
 * This class is used to provide a consistent, reusable instance of an empty list of errors
 * that can be returned when no errors are present. It is designed to be used in conjunction
 * with the {@link ErrorOr} class, where an empty list of errors might be needed.
 *
 * This class cannot be instantiated as it only contains static methods and fields.
 */

public final class EmptyErrors {
    /**
     * A static, unmodifiable list representing an empty set of errors.
     */
    private static final List<Error> EMPTY_ERRORS = Collections.emptyList();

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException Always thrown, as the class cannot be instantiated.
     */
    private EmptyErrors() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Retrieves the singleton instance of the empty list of errors.
     *
     * @return An unmodifiable empty list of errors.
     */
    public static List<Error> getInstance() {
        return EMPTY_ERRORS;
    }
}

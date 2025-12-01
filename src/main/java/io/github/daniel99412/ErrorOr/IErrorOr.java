package io.github.daniel99412.ErrorOr;

import io.github.daniel99412.Error.Error;

import java.util.List;

/**
 * Interface that represents a result that can either be a valid value or contain errors.
 *
 * This interface defines the contract for classes that can either hold a value or errors.
 * The classes implementing this interface must provide methods to check for errors and retrieve
 * the list of errors when present. Additionally, for classes that hold a value, the method to retrieve
 * the value is also defined.
 */
public interface IErrorOr {
    /**
     * Retrieves the list of errors associated with the instance.
     *
     * @return A list of errors, or an empty list if no errors are present.
     */
    List<Error> getErrors();

    /**
     * Checks if the current instance contains errors.
     *
     * @return {@code true} if there are errors, {@code false} otherwise.
     */
    Boolean isError();

    /**
     * A sub-interface that extends {@link IErrorOr} to provide a method for retrieving a value.
     *
     * This interface should be implemented by classes that can contain a valid value in addition
     * to errors. It extends {@link IErrorOr} and provides a method to retrieve the value when no
     * errors are present.
     *
     * @param <TValue> The type of the value that is returned when no errors are present.
     */
    interface WithValue<TValue> extends IErrorOr {
        /**
         * Retrieves the value associated with the instance.
         *
         * @return The value associated with the instance.
         * @throws IllegalStateException if errors are present, and the value is not accessible.
         */
        TValue getValue();
    }
}

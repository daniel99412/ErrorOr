package io.github.daniel99412.ErrorOr.extensions;

import io.github.daniel99412.Error.Error;
import io.github.daniel99412.ErrorOr.ErrorOr;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Provides an extension method to transform errors within an ErrorOr instance.
 */
public class MapError {

    private MapError() {
        // Private constructor to prevent instantiation
    }

    /**
     * If the {@link ErrorOr} instance contains errors, this method applies the given mapping function
     * to each error and returns a new {@link ErrorOr} containing the transformed errors.
     * If the instance contains a value, it is returned as-is.
     *
     * @param errorOr The {@link ErrorOr} instance.
     * @param mapping The function to transform each {@link Error}.
     * @param <TValue> The type of the value.
     * @return A new {@link ErrorOr} with transformed errors if it was an error, otherwise the original instance.
     */
    public static <TValue> ErrorOr<TValue> mapError(ErrorOr<TValue> errorOr, Function<Error, Error> mapping) {
        if (!errorOr.isError()) {
            return errorOr;
        }

        List<Error> newErrors = errorOr.getErrors().stream()
                .map(mapping)
                .collect(Collectors.toList());

        return ErrorOr.errors(newErrors);
    }
}

package com.dpardo.ErrorOr.extensions;

import com.dpardo.ErrorOr.ErrorOr;
import java.util.function.Function;

/**
 * Provides an extension method to map the value of an ErrorOr instance if it is not an error.
 */
public class Map {

    private Map() {
        // Private constructor to prevent instantiation
    }

    /**
     * Transforms the value of an {@link ErrorOr} if it is not an error.
     * If the instance contains an error, the error is propagated to the new {@link ErrorOr} instance.
     *
     * @param errorOr The {@link ErrorOr} instance to map.
     * @param onValue The function to apply to the value if it exists.
     * @param <TValue> The current value type.
     * @param <TNextValue> The next value type after transformation.
     * @return A new {@link ErrorOr} with the transformed value, or an {@link ErrorOr} containing the original errors.
     */
    public static <TValue, TNextValue> ErrorOr<TNextValue> map(ErrorOr<TValue> errorOr, Function<TValue, TNextValue> onValue) {
        if (errorOr.isError()) {
            return ErrorOr.errors(errorOr.getErrors());
        }
        return ErrorOr.of(onValue.apply(errorOr.getValue()));
    }
}

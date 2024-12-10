package com.dpardo.Error.utils;

import java.lang.Error;
import java.util.List;

public class ErrorComparer {
    /**
     * Compares two lists of errors to determine if they are equal.
     *
     * This method checks if the two lists of errors are the same instance. If they are not, it compares their
     * sizes. If the sizes are different, the lists are not equal. If the sizes are the same, it iterates over the
     * elements in both lists and compares each error using their {@link java.lang.Error#equals} method.
     *
     * @param errors1 The first list of errors.
     * @param errors2 The second list of errors.
     * @return {@code true} if the error lists are equal, {@code false} otherwise.
     */
    public static Boolean checkIfErrorsAreEqual(List<java.lang.Error> errors1, List<Error> errors2) {
        if (errors1 == errors2) {
            return true;
        }

        if (errors1.size() != errors2.size()) {
            return false;
        }

        for (int i = 0; i < errors1.size(); i++) {
            if (!errors1.get(i).equals(errors2.get(i))) {
                return false;
            }
        }
        return true;
    }
}

package com.dpardo.Error;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an error with a code, description, type, and optional metadata.
 *
 * The {@link Error} class encapsulates information about an error, including a code, description,
 * type, and any additional metadata that might be associated with the error. It provides various
 * static methods to create different types of errors such as failure, validation, conflict, etc.
 *
 * This record is immutable and provides methods to compare errors and generate a hash code based
 * on its properties.
 */
public record Error(
    String code,
    String description,
    ErrorType type,
    Map<String, Object> metadata
) {
    /**
     * Constructs an instance of {@link Error} with the provided parameters.
     *
     * @param code The error code, typically used for identifying the error.
     * @param description A description of the error, providing additional details.
     * @param type The type of the error, indicating its category.
     * @param metadata Additional data related to the error, or null if no extra data is needed.
     */
    public Error(String code, String description, ErrorType type, Map<String, Object> metadata) {
        this.code = code;
        this.description = description;
        this.type = type;
        this.metadata = metadata != null ? Map.copyOf(metadata) : null;
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Failure}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the failure type.
     */
    public static Error failure(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Failure, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Unexpected}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the unexpected type.
     */
    public static Error unexpected(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Unexpected, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Validation}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the validation type.
     */
    public static Error validation(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Validation, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Conflict}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the conflict type.
     */
    public static Error conflict(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Conflict, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#NotFound}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the not found type.
     */
    public static Error notFound(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.NotFound, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Unauthorized}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the unauthorized type.
     */
    public static Error unauthorized(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Unauthorized, metadata);
    }

    /**
     * Creates a {@link Error} of type {@link ErrorType#Forbidden}.
     *
     * @param code The error code.
     * @param description The error description.
     * @param metadata Additional metadata related to the error.
     * @return A new {@link Error} instance with the forbidden type.
     */
    public static Error forbidden(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.Forbiden, metadata);
    }

    /**
     * Compares this {@link Error} instance with another object for equality.
     *
     * @param obj The object to compare.
     * @return {@code true} if the object is equal to this error, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Error other = (Error) obj;
        return Objects.equals(type, other.type) &&
                Objects.equals(code, other.code) &&
                Objects.equals(description, other.description) &&
                type == other.type &&
                compareMetadata(metadata, other.metadata);
    }

    /**
     * Compares two maps of metadata for equality.
     *
     * @param metadata The first metadata map.
     * @param otherMetadata The second metadata map.
     * @return {@code true} if the maps are equal, {@code false} otherwise.
     */
    private static boolean compareMetadata(Map<String, Object> metadata, Map<String, Object> otherMetadata) {
        if (metadata == otherMetadata) return true;
        if (metadata == null || otherMetadata == null) return false;
        if (metadata.size() != otherMetadata.size()) return false;

        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            if (!Objects.equals(entry.getValue(), otherMetadata.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a hash code for this {@link Error} instance.
     *
     * @return A hash code based on the properties of the error.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(code, description, type);
        if (metadata != null) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                result = 31 * result + Objects.hash(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}

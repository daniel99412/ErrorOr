package io.github.daniel99412.Results;

public enum ResultTypes {
    Success,
    Created,
    Deleted,
    Updated;

    @Override
    public String toString() {
        return name();
    }
}

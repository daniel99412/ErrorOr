package com.dpardo.Results;

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

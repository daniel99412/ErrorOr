package com.dpardo.Results;

final class Success {
    protected ResultTypes resultType = ResultTypes.Success;

    @Override
    public String toString() {
        return resultType.toString();  // Imprime "SUCCESS"
    }
}

final class Created {
    protected ResultTypes resultType = ResultTypes.Created;

    @Override
    public String toString() {
        return resultType.toString();  // Imprime "CREATED"
    }
}

final class Deleted {
    protected ResultTypes resultType = ResultTypes.Deleted;

    @Override
    public String toString() {
        return resultType.toString();  // Imprime "DELETED"
    }
}

final class Updated {
    protected ResultTypes resultType = ResultTypes.Updated;

    @Override
    public String toString() {
        return resultType.toString();  // Imprime "UPDATED"
    }
}

public final class Results {
    public static final Success success = new Success();
    public static final Created created = new Created();
    public static final Deleted deleted = new Deleted();
    public static final Updated updated = new Updated();

    private Results() {}
}


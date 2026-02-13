package io.github.daniel99412.Results;

final class Success implements Result {
    @Override
    public ResultTypes type() {
        return ResultTypes.Success;
    }
}

final class Created implements Result {
    @Override
    public ResultTypes type() {
        return ResultTypes.Created;
    }
}

final class Deleted implements Result {
    @Override
    public ResultTypes type() {
        return ResultTypes.Deleted;
    }
}

final class Updated implements Result {
    @Override
    public ResultTypes type() {
        return ResultTypes.Deleted;
    }
}

public final class Results {
    public static final Success success = new Success();
    public static final Created created = new Created();
    public static final Deleted deleted = new Deleted();
    public static final Updated updated = new Updated();

    private Results() {}
}


# ErrorOr for Java

An implementation of the **ErrorOr** library from C# for Java, providing a safe and explicit way to handle both successful results and errors in applications. This library allows you to return results that can be either successful or errors, similar to the `Result` or `Either` pattern used in other languages. With **ErrorOr**, exceptions are avoided for error handling, promoting a more functional and robust programming style.

## Features:
- Explicitly represents successful results and errors.
- Avoids the use of exceptions for error handling.
- Facilitates chaining operations with results and errors.

## Installation

You can add **ErrorOr for Java** to your project by including it in your `pom.xml` if you're using Maven:

```xml
<dependency>
    <groupId>com.yourname</groupId>
    <artifactId>erroror-java</artifactId>
    <version>1.0.0</version>
</dependency>
```
Or in your build.gradle if you're using Gradle:
```code
implementation 'com.yourname:erroror-java:1.0.0'
```
## Usage
Here's an example of how to use the ErrorOr library in your Java project:
```java
import com.dpardo.ErrorOr.ErrorOr;
import com.dpardo.Error.Error;

public class Example {

    public ErrorOr<Integer> divide(int a, int b) {
        if (b == 0) {
            return ErrorOr.from(Error.validation("Division.ByZero", "Cannot divide by zero.", null));
        }
        return ErrorOr.fromValue(a / b);
    }
}
```
In this example:

If the division is successful, it returns the result (`a / b`).
If there is an error (in this case, division by zero), it returns an `Error` object.

## Advanced Usage: `match`, `map`, and `then`

The true power of `ErrorOr` comes from its fluent, functional API.

### `match()`
The `match` method allows you to handle both the success and error cases in a single, expressive statement.

```java
ErrorOr<Integer> result = divide(10, 2);

result.match(
    value -> System.out.println("Result is: " + value),
    errors -> errors.forEach(error -> System.err.println(error.description()))
);
```

### `map()`
The `map` method transforms the value inside an `ErrorOr` if it exists. If the `ErrorOr` contains an error, the error is simply passed along.

```java
ErrorOr<String> resultString = divide(100, 5)
    .map(value -> "The result is " + value);

// resultString will be an ErrorOr containing "The result is 20"
```

### `then()`
The `then` method (also known as `flatMap`) chains multiple operations that each return an `ErrorOr`. This is perfect for sequencing steps where any step can fail. If any step returns an error, the subsequent steps are skipped.

```java
public ErrorOr<String> process(int a, int b, int c) {
    return divide(a, b)
        .then(result1 -> divide(result1, c))
        .map(finalResult -> "Final result is: " + finalResult);
}

// Calling process(100, 2, 5) will return an ErrorOr with "Final result is: 10".
// Calling process(100, 0, 5) will return an ErrorOr with the "Cannot divide by zero." error.
```

### `failIf()`
The `failIf` method allows you to introduce a failure condition into a chain of operations. If the condition is met, the chain will stop and return the specified error.

```java
public ErrorOr<String> checkLength(String input) {
    return ErrorOr.fromValue(input)
        .failIf(s -> s.length() < 5, Error.validation("Length.TooShort", "Input must be at least 5 characters.", null))
        .map(s -> "Input is valid");
}

// checkLength("123") will return an ErrorOr with the "Length.TooShort" error.
// checkLength("12345") will return an ErrorOr with "Input is valid".
```

### `else()`
The `else` methods provide a way to recover from an error state. You can use it to provide a default value, a new `ErrorOr` instance, or execute some logic to handle the error.

```java
// Recover with a default value
ErrorOr<String> result = ErrorOr.from(Error.notFound("User.NotFound", "User was not found.", null))
    .elseValue("Default User");

// result will be an ErrorOr containing "Default User".

// Recover by executing a function
ErrorOr<String> result2 = ErrorOr.from(Error.unexpected("Db.Error", "Database connection failed.", null))
    .elseFuncValue(errors -> {
        // Log the errors
        System.err.println("Recovering from error: " + errors.get(0).description());
        return "Recovered Value";
    });

// result2 will be an ErrorOr containing "Recovered Value".
```

## Contributing
Feel free to fork the repository, create a branch, and submit a pull request. Please make sure your code follows the project’s conventions and includes tests.

## License
This project is licensed under the GNU General Public License v3 (GPL-3.0) - see the LICENSE file for details.

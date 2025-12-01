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
import com.dpardo.Error.ErrorType;

public class Example {

    public ErrorOr<Integer> divide(int a, int b) {
        if (b == 0) {
            return ErrorOr.from(Error.validation("Division.ByZero", "Cannot divide by zero.", null));
        }
        return ErrorOr.fromValue(a / b);
    }

    public void demonstrateNewFeatures() {
        // --- demonstrate match ---
        System.out.println("--- Demonstrating Match ---");
        ErrorOr<String> successResult = ErrorOr.fromValue("Hello ErrorOr!");
        successResult.match(
            value -> System.out.println("Success: " + value),
            errors -> errors.forEach(error -> System.err.println("Error: " + error.description()))
        );

        ErrorOr<String> errorResult = ErrorOr.from(Error.failure("Test.Fail", "Something went wrong.", null));
        errorResult.match(
            value -> System.out.println("Success: " + value),
            errors -> errors.forEach(error -> System.err.println("Error: " + error.description()))
        );

        // --- demonstrate map ---
        System.out.println("\n--- Demonstrating Map ---");
        ErrorOr<Integer> mappedSuccess = ErrorOr.fromValue("123").map(Integer::parseInt);
        mappedSuccess.match(
            value -> System.out.println("Mapped Success: " + value),
            errors -> errors.forEach(error -> System.err.println("Mapped Error: " + error.description()))
        );

        ErrorOr<Integer> mappedError = ErrorOr.fromValue("abc").map(Integer::parseInt); // This will cause a NumberFormatException if not handled
        mappedError.match(
            value -> System.out.println("Mapped Success: " + value),
            errors -> errors.forEach(error -> System.err.println("Mapped Error: " + error.description()))
        );


        // --- demonstrate then ---
        System.out.println("\n--- Demonstrating Then ---");
        ErrorOr<String> initialValue = ErrorOr.fromValue("start");
        ErrorOr<String> chainedResult = initialValue
            .then(s -> ErrorOr.fromValue(s + " -> step 1"))
            .then(s -> ErrorOr.fromValue(s + " -> step 2"))
            .then(s -> {
                // Simulate an error in the chain
                if (s.contains("step 1")) {
                    return ErrorOr.from(Error.conflict("Chain.Error", "An error occurred during chaining.", null));
                }
                return ErrorOr.fromValue(s + " -> step 3");
            });

        chainedResult.match(
            value -> System.out.println("Chained Success: " + value),
            errors -> errors.forEach(error -> System.err.println("Chained Error: " + error.description()))
        );
    }
}
```

In this example:

- `divide` now uses `Error.validation` and `ErrorOr.fromValue` for correct API usage.
- `demonstrateNewFeatures` shows how `match` can handle both success and error states.
- `map` is used to transform the value if no errors are present.
- `then` (flatMap) is used to chain operations that themselves return `ErrorOr`, demonstrating how errors propagate through the chain.

The `ErrorOr` library promotes a more functional and robust programming style by explicitly handling results and errors.

## Contributing
Feel free to fork the repository, create a branch, and submit a pull request. Please make sure your code follows the project’s conventions and includes tests.

## License
This project is licensed under the GNU General Public License v3 (GPL-3.0) - see the LICENSE file for details.
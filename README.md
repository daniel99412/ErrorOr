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
```code
import com.yourname.erroror.ErrorOr;

public class Example {

    public ErrorOr<Integer> divide(int a, int b) {
        if (b == 0) {
            return ErrorOr.error("Cannot divide by zero.");
        }
        return ErrorOr.success(a / b);
    }
}
```
In this example:

If the division is successful, it returns the result (a / b).
If there is an error (in this case, division by zero), it returns an error message.
Contributing
Feel free to fork the repository, create a branch, and submit a pull request. Please make sure your code follows the project’s conventions and includes tests.

## License
This project is licensed under the GNU General Public License v3 (GPL-3.0) - see the LICENSE file for details.
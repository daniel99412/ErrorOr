# ErrorOr for Java

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)
[![Maven Central](https://img.shields.io/maven-central/v/com.dpardo/erroror.svg?label=Maven%20Central)](#)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

A robust and fluent utility for functional-style error handling in Java, inspired by the popular C# library of the same name.

## Motivation

Why use `ErrorOr`?

*   **Against Exceptions for Control Flow:** While powerful, exceptions are for *exceptional* circumstances. Using them for predictable errors (like validation failures or "not found" scenarios) can be inefficient and clunky. `ErrorOr` treats errors as expected data, allowing you to handle them functionally.
*   **Beyond `Optional<T>`:** `Optional` is excellent for representing the potential absence of a value (`Some` or `None`). However, it can't tell you *why* a value is absent. `ErrorOr` fills this gap by carrying detailed error information in the failure case.

`ErrorOr` allows you to write cleaner, more predictable, and more expressive code by making successes and failures part of your return types.

## Installation

Add the library to your project using your favorite build tool.

**Gradle:**
```groovy
implementation 'io.github.daniel99412:ErrorOr:1.0-SNAPSHOT'
```

**Maven:**
```xml
<dependency>
    <groupId>io.github.daniel99412</groupId>
    <artifactId>ErrorOr</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Core Concepts

### Creating an `ErrorOr`
-   **For a success:** Use `ErrorOr.of()`
    ```java
    ErrorOr<String> success = ErrorOr.of("Everything went well!");
    ```
-   **For a failure:** Use `ErrorOr.error()` for a single error or `ErrorOr.errors()` for a list.
    ```java
    ErrorOr<User> notFound = ErrorOr.error(Error.notFound("User.NotFound", "User with ID 123 was not found.", null));
    ErrorOr<User> invalid = ErrorOr.errors(List.of(
        Error.validation("User.Name", "Name cannot be empty.", null),
        Error.validation("User.Email", "Email is not valid.", null)
    ));
    ```

## Usage Examples

### Basic Chaining: `map` and `then`
-   `map()` transforms the value inside a successful `ErrorOr`.
-   `then()` (or `flatMap`) chains another function that returns an `ErrorOr`.

If any step fails, the chain short-circuits and the error is propagated.

```java
public ErrorOr<Integer> parseAndDouble(String input) {
    return ErrorOr.of(input)
        .map(Integer::parseInt)
        .then(this::doubleIfEven);
}

private ErrorOr<Integer> doubleIfEven(int number) {
    if (number % 2 != 0) {
        return ErrorOr.error(Error.validation("Number.NotEven", "Number must be even.", null));
    }
    return ErrorOr.of(number * 2);
}

// parseAndDouble("10") -> Success: ErrorOr.of(20)
// parseAndDouble("7")  -> Failure: ErrorOr containing "Number must be even."
// parseAndDouble("abc")-> Failure: An error from Integer.parseInt is propagated.
```

### Handling Results: `match`
Use `match` to elegantly handle both the success and error cases.

```java
String message = parseAndDouble("10").match(
    value -> "Success! The result is " + value,
    errors -> "Errors occurred: " + errors.get(0).description()
);
// message: "Success! The result is 20"
```

### Validation: `filter`
Use `filter` to turn a success into a failure if a condition is not met.

```java
public ErrorOr<User> getAdultUser(String id) {
    return userRepository.findById(id)
        .filter(
            user -> user.getAge() >= 18,
            Error.validation("User.NotAdult", "User must be 18 or older.", null)
        );
}
```

### Side-Effects: `peek` and `peekError`
Perform an action without changing the `ErrorOr` instance, perfect for logging.

```java
userRepository.findById("user-123")
    .peek(user -> log.info("User {} found, proceeding...", user.getId()))
    .peekError(errors -> log.error("Failed to find user: {}", errors))
    .then(this::grantAccess);
```

### Error Recovery: `getOrElse` and `elseValue`
- `getOrElse()` unwraps the value or provides a default if it's an error.
- `elseValue()` recovers from an error by wrapping a default value in a new successful `ErrorOr`, allowing the chain to continue.

```java
// Get a value or a default
String username = userRepository.findById("user-404")
    .map(User::getName)
    .getOrElse("Guest"); // username is now "Guest"

// Recover a chain
ErrorOr<Session> session = sessionService.createSessionFor("user-404") // This fails
    .elseValue(Session.guestSession()); // Continues with a guest session

// session is now a successful ErrorOr containing a guest session.
```

### Transforming Errors: `mapError`
Add context to errors as they bubble up through your application layers.

```java
// In a lower layer
public ErrorOr<User> findUser() {
    // ... database logic fails ...
    return ErrorOr.error(Error.unexpected("Db.Timeout", "Database timed out.", null));
}

// In a higher service layer
public ErrorOr<User> registerUser() {
    return findUser()
        .mapError(error -> Error.failure(
            "Registration.Failed",
            "Could not register user due to a database problem: " + error.description(),
            null
        ));
}
```

### Asynchronous Operations
Use `mapAsync` and `thenAsync` to compose `CompletableFuture`s within your `ErrorOr` chains.

```java
// Asynchronously find a user, then charge them, then notify them.
CompletableFuture<ErrorOr<Void>> asyncResult = userRepository.findByIdAsync("user-123")
    .thenAsync(user -> paymentGateway.chargeAsync(user, 100.00))
    .thenAsync(receipt -> notificationService.sendReceiptAsync(receipt));
```

## Acknowledgements
This library is heavily inspired by the C# `ErrorOr` library created by [amantinband](https://github.com/amantinband). His original MIT-licensed project at [github.com/amantinband/error-or](https://github.com/amantinband/error-or) served as the primary reference and inspiration for this Java implementation.

## Contributing
Feel free to fork the repository, create a branch, and submit a pull request. Please make sure your code follows the project’s conventions and includes tests.

## License
This project is licensed under the GNU General Public License v3 (GPL-3.0) - see the LICENSE file for details.

# Unipi.Fifa Application Documentation

[Linked Table of Contents](#table-of-contents)


## Table of Contents <a name="table-of-contents"></a>

* [1. Overview](#overview)
* [2. Main Application Class: `FifaApplication`](#fifaapplication)


## 1. Overview <a name="overview"></a>

This document provides internal code documentation for the `Unipi.Fifa` Spring Boot application.  The application's primary function is currently limited to initialization, as evidenced by the minimal codebase.  Future development will likely expand its functionality.  The application is configured to exclude Spring Boot's default security auto-configuration, as indicated by the commented-out line `//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})`. This suggests a plan to implement custom security mechanisms later.


## 2. Main Application Class: `FifaApplication` <a name="fifaapplication"></a>

The `FifaApplication` class serves as the entry point for the application.  It leverages Spring Boot's `@SpringBootApplication` annotation, which combines several other annotations: `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.

| Annotation       | Description                                                                     |
|-----------------|---------------------------------------------------------------------------------|
| `@Configuration` | Marks the class as a source of bean definitions for the Spring IoC container. |
| `@EnableAutoConfiguration` | Enables Spring Boot's auto-configuration mechanism.                       |
| `@ComponentScan` | Enables component scanning to discover and register Spring-managed components. |

The `main` method is responsible for launching the Spring Boot application context:

```java
public static void main(String[] args) {
	SpringApplication.run(FifaApplication.class, args);
}
```

The `SpringApplication.run()` method performs the following actions:

1. **Creates an application context:** It instantiates a Spring `ApplicationContext`, which manages the application's beans and their dependencies.
2. **Registers beans:** It registers beans defined within the application and its dependencies based on component scanning and auto-configuration.
3. **Starts the application:** It initializes and starts the application, making it ready to handle requests (if applicable).  In this case, because there is no further logic in this class, the application will simply start and remain idle awaiting further development.

The commented-out line `//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})` indicates that the application initially excluded the default Spring Security auto-configuration. This was likely done to allow for more control over security implementation and potentially to avoid conflicts with custom security configurations planned for the future.  This is a common practice when building robust and secure applications.

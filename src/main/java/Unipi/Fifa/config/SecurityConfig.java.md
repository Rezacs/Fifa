# Unipi.Fifa.config.SecurityConfig Internal Documentation

[Linked Table of Contents](#linked-table-of-contents)

## Linked Table of Contents

* [1. Overview](#1-overview)
* [2. Class `SecurityConfig`](#2-class-securityconfig)
    * [2.1 Constructor `SecurityConfig(NeoUserDetailService neoUserDetailService)`](#21-constructor-securityconfigneouserdetailserviceneouserdetailservice)
    * [2.2 Method `configure(HttpSecurity http, HttpSecurity httpSecurity)`](#22-method-configurehttpsecurity-http-httpsecurity-httpsecurity)
    * [2.3 Method `passwordEncoder()`](#23-method-passwordencoder)


## 1. Overview

This document provides internal documentation for the `Unipi.Fifa.config.SecurityConfig` class, which configures Spring Security for the Unipi Fifa application.  The configuration focuses on stateless authentication using HTTP Basic and defines access control rules for specific API endpoints.


## 2. Class `SecurityConfig`

This class is annotated with `@Configuration` and `@EnableWebSecurity`, indicating it's a Spring configuration class enabling Spring Security features. It utilizes a custom `NeoUserDetailService` for user authentication.


### 2.1 Constructor `SecurityConfig(NeoUserDetailService neoUserDetailService)`

This constructor initializes the `SecurityConfig` object with an instance of `NeoUserDetailService`. This service is crucial for fetching user details during authentication.  Dependency injection ensures that Spring manages the creation and injection of this service.

| Parameter | Type                     | Description                                         |
|-----------|--------------------------|-----------------------------------------------------|
| `neoUserDetailService` | `NeoUserDetailService` | The service used to load user details for authentication. |


### 2.2 Method `configure(HttpSecurity http, HttpSecurity httpSecurity)`

This method is the core of the Spring Security configuration. It uses a fluent builder pattern to configure various aspects of security.

**Algorithm/Explanation:**

The `configure` method chains several security configurations within a `HttpSecurity` object.  Let's break down the key aspects:

1. **`sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`**: This disables session management, making the authentication stateless.  Each request is treated independently, improving scalability and reducing security risks associated with session hijacking.

2. **`csrf(AbstractHttpConfigurer::disable)`**: This disables Cross-Site Request Forgery (CSRF) protection.  This is typically done when using stateless authentication; however, care must be taken to ensure other appropriate security measures are in place to mitigate CSRF attacks in such scenarios.

3. **`cors(Customizer.withDefaults())`**:  This enables Cross-Origin Resource Sharing (CORS) with default settings. This allows requests from different origins (domains, protocols, ports) to access the application's resources.

4. **`authorizeHttpRequests(auth -> auth.requestMatchers("api/v1/auth/me","api/v1/enrolments/**").authenticated().anyRequest().permitAll())`**: This defines access control rules:
    *  `api/v1/auth/me` and `api/v1/enrolments/**` require authentication.  Only authenticated users can access these endpoints.
    *  All other requests (`anyRequest().permitAll()`) are permitted without authentication.

5. **`userDetailsService(neoUserDetailService)`**: This sets the `NeoUserDetailService` as the user details service. Spring Security uses this service to load user details (username, password, roles, etc.) for authentication.

6. **`httpBasic(Customizer.withDefaults())`**:  This enables HTTP Basic authentication, a simple authentication scheme where the username and password are sent in the request header.

7. **`build()`**: This builds the final `SecurityFilterChain`, which is a chain of security filters responsible for processing incoming requests.

The method returns the configured `SecurityFilterChain`.


### 2.3 Method `passwordEncoder()`

This method creates and returns a `BCryptPasswordEncoder` bean.  The `@Bean` annotation indicates that this method returns a Spring bean.

| Return Type | Description                                                              |
|-------------|--------------------------------------------------------------------------|
| `PasswordEncoder` | A `BCryptPasswordEncoder` object used for password hashing.  BCrypt is a strong one-way hashing algorithm that protects against rainbow table attacks. |


This method ensures that passwords are securely stored and protected using a strong hashing algorithm.

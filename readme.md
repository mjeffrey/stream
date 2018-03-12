# Demonstration of problem with using interceptors with streaming rest template

## Instructions

```mvn verify```

There are 4 tests, see StreamResourceTest and one fails (I expected them all to succeed)

The failure is caused by 

```java.lang.UnsupportedOperationException: getBody not supported```

It does not seem to be possible to use a ClientHttpRequestInterceptor with HttpComponentsClientHttpRequestFactory when buffering is turned off.


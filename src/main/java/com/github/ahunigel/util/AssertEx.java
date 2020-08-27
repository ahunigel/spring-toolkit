package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

/**
 * Enhance the spring {@link Assert}, provide some filters for copy properties
 * <p>
 * Created by nigel on 8/27/2020.
 *
 * @author nigel
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertEx extends Assert {
  /**
   * Assert a boolean expression, throwing an {@code UnsupportedOperationException}
   * if the expression evaluates to {@code false}.
   * <p>Call {@link #isTrue} if you wish to throw an {@code IllegalArgumentException}
   * on an assertion failure.
   * <pre class="code">AssertEx.supported("drink".equals(operation), "The operation is not supported yet");</pre>
   *
   * @param expression a boolean expression
   * @param message    the exception message to use if the assertion fails
   * @throws UnsupportedOperationException if {@code expression} is {@code false}
   */
  public static void supported(boolean expression, String message) {
    if (!expression) {
      throw new UnsupportedOperationException(message);
    }
  }

  /**
   * Assert a boolean expression, throwing an {@code AccessDeniedException}
   * if the expression evaluates to {@code false}.
   * <p>Call {@link #isTrue} if you wish to throw an {@code IllegalArgumentException}
   * on an assertion failure.
   * <pre class="code">AssertEx.hasPermission(permissions.contains("drink"), "The operation is not permitted");</pre>
   *
   * @param expression a boolean expression
   * @param message    the exception message to use if the assertion fails
   * @throws AccessDeniedException if {@code expression} is {@code false}
   */
  public static void hasPermission(boolean expression, String message) {
    if (!expression) {
      throw new AccessDeniedException(message);
    }
  }
}

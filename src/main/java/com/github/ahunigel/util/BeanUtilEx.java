package com.github.ahunigel.util;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.*;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * Enhance the spring {@link BeanUtils}, provide some filters for copy properties
 * <p>
 * Created by nigel on 2020/1/17.
 *
 * @author nigel
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtilEx extends BeanUtils {

  /**
   * provide name/value filter to find out which properties should be copied.
   * <pre>
   *   BeanUtilEx.copyProperties(foo, target, name -> name.length() > 1, Objects::nonNull);
   * </pre>
   *
   * @param source
   * @param target
   * @param nameFilter
   * @param valueFilter
   */
  public static void copyProperties(Object source, Object target, Predicate<String> nameFilter, Predicate valueFilter) {
    List<String> ignoreProperties = Lists.newArrayList();
    PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
    for (PropertyDescriptor sourcePd : sourcePds) {
      if (sourcePd != null) {
        Method readMethod = sourcePd.getReadMethod();
        if (readMethod != null) {
          if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
          }
          try {
            Object value = readMethod.invoke(source);
            if (nonNull(nameFilter) && !nameFilter.test(sourcePd.getName())) {
              ignoreProperties.add(sourcePd.getName());
            }
            if (nonNull(valueFilter) && !valueFilter.test(value)) {
              ignoreProperties.add(sourcePd.getName());
            }
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("Could not read property '" + sourcePd.getName() + "' from source", e);
          }
        }
      }
    }
    BeanUtils.copyProperties(source, target, ignoreProperties.toArray(new String[0]));
  }

  public static <T> T copyNonNullPropertiesForSameClass(T source, T target) {
    if (source == null || target == null || target.getClass() != source.getClass()) {
      return null;
    }

    final BeanWrapper src = new BeanWrapperImpl(source);
    final BeanWrapper trg = new BeanWrapperImpl(target);
    ReflectionUtils.doWithFields(target.getClass(), field -> {
          Object providedObject = src.getPropertyValue(field.getName());
          if (providedObject != null && !(providedObject instanceof Collection<?>)) {
            trg.setPropertyValue(
                field.getName(),
                providedObject);
          }
        }
        , field -> !Modifier.isStatic(field.getModifiers()));
    return target;
  }

  /**
   * Copy non-null property values of the given source bean into the target bean.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>This is just a convenience method. For more complex transfer needs,
   * consider using a full BeanWrapper.
   * <p>
   * null property values from given source would be add to ignore properties list
   *
   * @param source the source bean
   * @param target the target bean
   * @throws BeansException if the copying failed
   * @see BeanWrapper
   */
  public static void copyNonNullProperties(Object source, Object target) {
    copyProperties(source, target, null, Objects::nonNull);
  }

  /**
   * @param source
   * @param target
   * @param valueIgnoreFilter
   * @deprecated use {@link #copyPropertiesIgnoreValue(Object, Object, Predicate)} instead
   */
  @Deprecated
  public static void copyProperties(Object source, Object target, Predicate valueIgnoreFilter) {
    copyProperties(source, target, null, valueIgnoreFilter.negate());
  }

  public static void copyPropertiesFilterName(Object source, Object target, Predicate<String> nameFilter) {
    copyProperties(source, target, nameFilter, null);
  }

  public static void copyPropertiesFilterValue(Object source, Object target, Predicate valueFilter) {
    copyProperties(source, target, null, valueFilter);
  }

  public static void copyPropertiesIgnore(Object source, Object target, Predicate<String> nameIgnoreFilter,
                                          Predicate valueIgnoreFilter) {
    copyProperties(source, target, nameIgnoreFilter.negate(), valueIgnoreFilter.negate());
  }

  public static void copyPropertiesIgnoreName(Object source, Object target, Predicate<String> nameIgnoreFilter) {
    copyProperties(source, target, nameIgnoreFilter.negate(), null);
  }

  public static void copyPropertiesIgnoreValue(Object source, Object target, Predicate valueIgnoreFilter) {
    copyProperties(source, target, null, valueIgnoreFilter.negate());
  }

}

package com.anotherdev.firebase.auth.internal.org.immutables.value;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE,
        overshadowImplementation = true,
        strictBuilder = true
)
public @interface FarImmutableValueStyle {
}

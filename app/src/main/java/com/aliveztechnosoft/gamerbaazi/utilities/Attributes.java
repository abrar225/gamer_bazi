package com.aliveztechnosoft.gamerbaazi.utilities;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD})
public @interface Attributes {
    String key() default "";
    DataTypes dataType() default DataTypes.STRING;
}

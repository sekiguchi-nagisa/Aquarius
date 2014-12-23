package aquarius.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate user defined parsing rule(must be interface default method) in parser interface
 * @author skgchxngsxyz-opensuse
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RuleDefinition {	// just a marker annotation
}

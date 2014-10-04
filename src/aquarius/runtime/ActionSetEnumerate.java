package aquarius.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * represent for grammar action set enum
 * @author skgchxngsxyz-opensuse
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ActionSetEnumerate {
	/**
	 * @return
	 * grammar name
	 */
	String value();
}

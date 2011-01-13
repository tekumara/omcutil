package org.omancode.util.beans;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Static utility class for working with POJO beans.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public final class BeanUtil {

	private BeanUtil() {
		// no instantiation
	}

	/**
	 * Get bean property. Convenience method that returns any reflection errors
	 * as {@link IntrospectionException}s.
	 * 
	 * @param bean
	 *            Bean whose property is to be extracted
	 * @param name
	 *            Possibly indexed and/or nested name of the property to be
	 *            extracted
	 * @return the property value
	 * @throws IntrospectionException
	 *             if reflection problem getting property
	 */
	public static Object getProperty(Object bean, String name)
			throws IntrospectionException {
		try {
			return PropertyUtils.getProperty(bean, name);
		} catch (IllegalAccessException e) {
			throw new IntrospectionException("Oh no, "
					+ "couldn't get property [" + name + "]: " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IntrospectionException("Oh no, "
					+ "couldn't get property [" + name + "]: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IntrospectionException("Oh no, "
					+ "couldn't get property [" + name + "]: " + e.getMessage());
		}
	}

}

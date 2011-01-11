package org.omancode.util.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Inspects a bean and provides its property names and types. If the property
 * method generates an exception it will silently ignore the property. Provides
 * separate lists of property names and property types, as well as an iterator
 * that returns {@link Property} objects containing a name and type. Can be
 * directed to return the boxed equivalent of primitive types for properties
 * that return primitive types.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public class BeanPropertyInspector implements
		Iterable<BeanPropertyInspector.Property> {

	/**
	 * Iterator which returns BeanPropertyInspector.Property objects.
	 * 
	 */
	public final class PropertyIterator implements
			Iterator<BeanPropertyInspector.Property> {

		private final Iterator<String> propNamesIterator;
		private final Iterator<Class<?>> propTypesIterator;

		PropertyIterator() {
			propNamesIterator = propNames.iterator();
			propTypesIterator = propTypes.iterator();
		}

		@Override
		public boolean hasNext() {
			return propNamesIterator.hasNext();
		}

		@Override
		public Property next() {
			return new Property(propNamesIterator.next(),
					propTypesIterator.next());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Wraps a property name and type in one object.
	 */
	public static class Property {
		private final String name;
		private final Class<?> type;

		/**
		 * Construct a {@link Property}.
		 * 
		 * @param name
		 *            name
		 * @param type
		 *            type
		 */
		public Property(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}

		/**
		 * Get this property's name.
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Get this property's type.
		 * 
		 * @return type
		 */
		public Class<?> getPropertyType() {
			return type;
		}
	}

	private final Object bean;

	/**
	 * Columns are created for all getter methods that are defined by
	 * {@code stopClass}'s subclasses. {@code stopClass}'s getter methods and
	 * superclass getter methods are not converted to columns in the dataframe.
	 */
	private final Class<?> stopClass;

	private final List<String> propNames = new ArrayList<String>();
	private final List<Class<?>> propTypes = new ArrayList<Class<?>>();
	private int propCount = 0;
	private final boolean reportBoxedPrimitiveType;

	/**
	 * Construct a property inspector for the supplied bean. Primitive types are
	 * returned as primitive types, not boxed types.
	 * 
	 * @param bean
	 *            bean to inspect.
	 * @param stopClass
	 *            Columns are created for all getter methods that are defined by
	 *            {@code stopClass}'s subclasses. {@code stopClass}'s getter
	 *            methods and superclass getter methods are not converted to
	 *            columns in the dataframe.
	 * @throws IntrospectionException
	 *             if problem during reflection.
	 */
	public BeanPropertyInspector(Object bean, Class<?> stopClass)
			throws IntrospectionException {
		this(bean, stopClass, false);
	}

	/**
	 * Construct a property inspector for the supplied bean.
	 * 
	 * @param bean
	 *            bean to inspect.
	 * @param stopClass
	 *            Columns are created for all getter methods that are defined by
	 *            {@code stopClass}'s subclasses. {@code stopClass}'s getter
	 *            methods and superclass getter methods are not converted to
	 *            columns in the dataframe.
	 * @param reportBoxedPrimitive
	 *            whether bean properties returning a primitive type should
	 *            report these as a boxed type, eg: java.lang.Integer instead of
	 *            int.
	 * @throws IntrospectionException
	 *             if problem during reflection.
	 */
	public BeanPropertyInspector(Object bean, Class<?> stopClass,
			boolean reportBoxedPrimitive) throws IntrospectionException {
		this.bean = bean;
		this.stopClass = stopClass;
		this.reportBoxedPrimitiveType = reportBoxedPrimitive;
		generateProperties();
	}

	/**
	 * Get number of properties.
	 * 
	 * @return number of properties.
	 */
	public int getCount() {
		return propCount;
	}

	/**
	 * A list of property names that are assignable from the supplied class type
	 * {@code cls}. ie: those properties that are either the same as, or are a
	 * superclass or superinterface of, the class or interface represented by
	 * the specified <code>cls</code> parameter.
	 * 
	 * @see Class#isAssignableFrom(Class)
	 * @param cls
	 *            class to test for
	 * @return list of property names assignable from {@code cls}.
	 */
	public List<String> getNamesAssignableFrom(Class<?> cls) {
		List<String> assignable = new ArrayList<String>();
		for (int i = 0; i < propTypes.size(); i++) {
			Class<?> klass = propTypes.get(i);
			if (klass.isAssignableFrom(cls)) {
				assignable.add(propNames.get(i));
			}
		}
		return assignable;
	}

	/**
	 * Get property names.
	 * 
	 * @return list of property names.
	 */
	public List<String> getNames() {
		return propNames;
	}

	/**
	 * Get property class types.
	 * 
	 * @return list of property class types.
	 */
	public List<Class<?>> getTypes() {
		return propTypes;
	}

	/**
	 * Generate the lists of property names and types. Attempts to fetch each
	 * property on the bean. If the property method generates an exception it
	 * will silent ignore the property.
	 * 
	 * @throws IntrospectionException
	 *             if problem during reflection.
	 */
	private void generateProperties() throws IntrospectionException {
		// get all the bean's properties
		// via introspection
		PropertyDescriptor[] props;
		props = Introspector.getBeanInfo(bean.getClass(), stopClass)
				.getPropertyDescriptors();

		// check and add each property
		// to the propNames and propTypes lists
		for (PropertyDescriptor prop : props) {
			Class<?> klass = prop.getPropertyType();

			String propName = prop.getName();

			try {

				// try and get the property
				// if an exception is generated
				// we'll fall out and this
				// property won't be added to the list
				PropertyUtils.getProperty(bean, propName);

				propNames.add(propName);
				propCount++;

				if (klass.isPrimitive() && reportBoxedPrimitiveType) {
					// add boxed type instead of
					// primitive type for the
					// container metadata
					propTypes.add(boxedPrimitiveType(klass));
				} else {
					propTypes.add(klass);
				}

			} catch (IllegalAccessException e) { // NOPMD
				// ignore, we won't get this property
			} catch (InvocationTargetException e) { // NOPMD
				// ignore, we won't get this property
			} catch (NoSuchMethodException e) { // NOPMD
				// ignore, we won't get this property
			}

		}
	}

	/**
	 * Returns the boxed equivalent of a primitive type class. If the supplied
	 * class is not a primitive class, returns it as is.
	 * 
	 * @param primitiveClass
	 *            a primitive type class, eg: int.
	 * @return boxed version of the primitive type, eg: if primitiveType is int,
	 *         returns java.lang.Integer.
	 */
	public final Class<?> boxedPrimitiveType(Class<?> primitiveClass) {

		if (primitiveClass == byte.class) {
			return Byte.class;
		} else if (primitiveClass == short.class) {
			return Short.class;
		} else if (primitiveClass == int.class) {
			return Integer.class;
		} else if (primitiveClass == long.class) {
			return Long.class;
		} else if (primitiveClass == float.class) {
			return Float.class;
		} else if (primitiveClass == double.class) {
			return Double.class;
		} else if (primitiveClass == boolean.class) {
			return Boolean.class;
		} else if (primitiveClass == char.class) {
			return Character.class;
		}
		return primitiveClass;
	}

	@Override
	public Iterator<BeanPropertyInspector.Property> iterator() {
		return new PropertyIterator();
	}

}
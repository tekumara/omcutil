package org.omancode.util.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.omancode.util.DateUtil;

/**
 * Static utility class of general purpose file functions.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public final class FileUtil {

	private FileUtil() {
		// no instantiation
	}

	/**
	 * Add a trailing slash to a string if it does not have one.
	 * 
	 * @param str
	 *            string to test
	 * @return {@code str} with a trailing slash, or {@code null} if {@code str}
	 *         is {@code null}
	 */
	public static String addTrailingSlash(String str) {
		if (str == null) {
			return null;
		}
		return str.substring(str.length() - 1).equals("\\") ? str : str
				+ "\\";
	}

	/**
	 * Adds an extension to a file name if it doesn't already have the
	 * extension.
	 * 
	 * @param fileName
	 *            file name
	 * @param ext
	 *            extension, eg: ".csv"
	 * @return {@code fileName} with the extension {@code ext}, or {@code null}
	 *         if {@code fileName} is {@code null}
	 */
	public static String addExtension(String fileName, String ext) {

		if (fileName == null || ext == null) {
			return fileName;
		}

		return fileName.substring(fileName.length() - 1).equals(ext) ? fileName
				: fileName + ext;
	}

	/**
	 * Gets the extension of a filename.
	 * <p>
	 * This method returns the textual part of the filename after the last dot.
	 * There must be no directory separator after the dot.
	 * 
	 * <pre>
	 * foo.txt      --> "txt"
	 * a/b/c.jpg    --> "jpg"
	 * a/b.txt/c    --> ""
	 * a/b/c        --> ""
	 * </pre>
	 * <p>
	 * The output will be the same irrespective of the machine that the code is
	 * running on.
	 * 
	 * @param filename
	 *            the filename to retrieve the extension of.
	 * @return the extension of the file or an empty string if none exists.
	 */
	public static String getExtenstion(String filename) {
		return FilenameUtils.getExtension(filename);
	}

	/**
	 * Return contents of a file as a String. Closes file after reading.
	 * 
	 * @param file
	 *            file to read
	 * @return contents of the file
	 * @throws IOException
	 *             if file not found or problem reading the file
	 */
	public static String readFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		String contents = IOUtils.toString(reader);

		// release file after loading,
		// instead of waiting for VM exit/garbage collection
		reader.close();

		return contents;

	}

	/**
	 * Read contents of a class resource as a String. Closes file after reading.
	 * 
	 * @param klass
	 *            class
	 * @param filename
	 *            resource name. Usually this will be the name of a file in same
	 *            directory as the class file.
	 * @return contents of the resource
	 * @throws IOException
	 *             if problem locating or reading resource
	 */
	public static String readResource(Class<?> klass, String filename)
			throws IOException {
		InputStream stream = klass.getResourceAsStream(filename);
		String contents = IOUtils.toString(stream);

		// release file after loading,
		// instead of waiting for VM exit/garbage collection
		stream.close();

		return contents;
	}

	/**
	 * Write string to a file.
	 * 
	 * @param file
	 *            file
	 * @param contents
	 *            contents
	 * @throws IOException
	 *             if problem writing
	 */
	public static void writeFile(File file, String contents)
			throws IOException {
		FileWriter writer = new FileWriter(file);
		IOUtils.write(contents, writer);
	}

	/**
	 * Method that will close an {@link InputStream} ignoring it if it is null
	 * and ignoring exceptions.
	 * 
	 * @param in
	 *            the InputStream to close.
	 */
	public static void closeQuietly(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/**
	 * Load a file resource into a {@link Properties} object.
	 * 
	 * @param resourceClass
	 *            class the file resource is associated with
	 * @param fileName
	 *            file resource to load. Usually needs to exist in the same
	 *            directory as the class it is associated with.
	 * @return properties
	 * @throws IOException
	 *             if problem reading file
	 */
	public static Properties loadProperties(Class<?> resourceClass,
			String fileName) throws IOException {
		Properties props = new Properties();
		InputStream ins = resourceClass.getResourceAsStream(fileName);

		if (ins == null) {
			throw new IOException("Could not find resource \"" + fileName
					+ "\"");
		}

		try {
			props.load(ins);
		} finally {
			closeQuietly(ins);
		}
		return props;
	}

	/**
	 * Regex of invalid Windows file name characters.
	 */
	private static final String INVALID_FILENAME_CHARS_REGEX =
			"[\\\\/:*?<>\"|]";

	/**
	 * Compiled regex pattern of invalid file name characters.
	 */
	private static final Pattern INVALID_FILENAME_CHARS_PATTERN =
			Pattern.compile(INVALID_FILENAME_CHARS_REGEX);

	/**
	 * Remove invalid Windows filename characters (including directory slashes)
	 * from the string.
	 * 
	 * @param filename
	 *            filename, without the path
	 * @return cleaned filename
	 */
	public static String stripInvalidFileNameChars(String filename) {
		Matcher m = INVALID_FILENAME_CHARS_PATTERN.matcher(filename);

		return m.replaceAll("");
	}

	/**
	 * Takes a file name, removes any illegal characters and prefixes a date.
	 * 
	 * @param fileName
	 *            file name
	 * @return date + filename (cleaned)
	 */
	public static String cleanDatedName(String fileName) {

		// strip runName of any illegal characters
		String cleanedRunName = FileUtil.stripInvalidFileNameChars(fileName);
		String datedfileName =
				DateUtil.nowToSortableUniqueDateString() + " "
						+ cleanedRunName;
		return datedfileName;

	}

	/**
	 * Makes the path, including any necessary but nonexistent parent
	 * directories, for the given file name.
	 * 
	 * @param fileName
	 *            file name, eg: "c:\a\b\c\myfile.csv". Will make directory
	 *            "c:\a\b\c\".
	 * 
	 */
	public static void mkdirs(String fileName) {
		File file = new File(FilenameUtils.getFullPath(fileName));
		file.mkdirs();
	}
}

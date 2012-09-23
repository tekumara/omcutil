/*
 * @(#)ExensionFileFilter.java	1.17 09/08/04
 * 
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * @(#)ExampleFileFilter.java	1.16 04/07/26
 */

package org.omancode.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out all files except
 * for those type extensions that it knows about.
 * 
 * Extensions are of the type ".foo", which is typically found on Windows and
 * Unix boxes, but not on Macinthosh. Case is ignored.
 * 
 * Example - create a new filter that filters out all files but gif and jpg
 * image files:
 * 
 * JFileChooser chooser = new JFileChooser(); ExampleFileFilter filter = new
 * ExtFileFilter( new String{"gif", "jpg"}, "JPEG & GIF Images")
 * chooser.addChoosableFileFilter(filter); chooser.showOpenDialog(this);
 * 
 * @version $Revision$ 06/01/11 Oliver Mannion <br>
 *          replaced Hashtable with List and Enumeration with Iterator
 * @version 09/08/10 Oliver Mannion
 *          <ul>
 *          <li>renamed from ExampleFileFilter to ExtFileFilter
 *          <li>removed unused fields
 *          <li>made addExtension and setDescription final (because called
 *          during construction)
 *          </ul>
 * @version 1.16 07/26/04 Jeff Dinkins
 * @author Jeff Dinkins, Oliver Mannion
 */
public class ExtFileFilter extends FileFilter {

	private List<String> filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	/**
	 * Creates a file filter. If no filters are added, then all files are
	 * accepted.
	 * 
	 * @see #addExtension
	 */
	public ExtFileFilter() {
		this.filters = new ArrayList<String>(1);
	}

	/**
	 * Creates a file filter that accepts files with the given extension.
	 * Example: new ExtFileFilter("jpg");
	 * 
	 * @see #addExtension
	 */
	public ExtFileFilter(String extension) {
		this(extension, null);
	}

	/**
	 * Creates a file filter that accepts the given file type. Example: new
	 * ExtFileFilter("jpg", "JPEG Image Images");
	 * 
	 * Note that the "." before the extension is not needed. If provided, it
	 * will be ignored.
	 * 
	 * @see #addExtension
	 */
	public ExtFileFilter(String extension, String description) {
		this();
		if (extension != null) {
			addExtension(extension);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	/**
	 * Creates a file filter from the given string array. Example: new
	 * ExtFileFilter(String {"gif", "jpg"});
	 * 
	 * Note that the "." before the extension is not needed and will be ignored.
	 * 
	 * @see #addExtension
	 */
	public ExtFileFilter(String[] filters) {
		this(filters, null);
	}

	/**
	 * Creates a file filter from the given string array and description.
	 * Example: new ExtFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
	 * 
	 * Note that the "." before the extension is not needed and will be ignored.
	 * 
	 * @see #addExtension
	 */
	public ExtFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
			// add filters one by one
			addExtension(filters[i]);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	/**
	 * Return true if this file should be shown in the directory pane, false if
	 * it shouldn't.
	 * 
	 * Files that begin with "." are ignored.
	 * 
	 * @see #getExtension
	 * @see FileFilter#accept
	 */
	@Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.contains(getExtension(f))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the extension portion of the file's name .
	 * 
	 * @see #getExtension
	 * @see FileFilter#accept
	 */
	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}

	/**
	 * Adds a filetype "dot" extension to filter against.
	 * 
	 * For example: the following code will create a filter that filters out all
	 * files except those that end in ".jpg" and ".tif":
	 * 
	 * ExtFileFilter filter = new ExtFileFilter(); filter.addExtension("jpg");
	 * filter.addExtension("tif");
	 * 
	 * Note that the "." before the extension is not needed and will be ignored.
	 */
	public final void addExtension(String extension) {
		filters.add(extension.toLowerCase());
		fullDescription = null;
	}

	/**
	 * Returns the human readable description of this filter. For example:
	 * "JPEG and GIF Image Files (*.jpg, *.gif)"
	 * 
	 * @see #setDescription(String)
	 * @see #setExtensionListInDescription(boolean)
	 * @see #isExtensionListInDescription()
	 * @see FileFilter#getDescription
	 */
	@Override
	public String getDescription() {
		if (fullDescription == null) {
			if (description == null || isExtensionListInDescription()) {
				fullDescription = description == null ? "(" : description
						+ " (";
				// build the description from the extension list
				Iterator<String> extensions = filters.iterator();
				if (extensions != null) {
					fullDescription += "." + extensions.next();
					while (extensions.hasNext()) {
						fullDescription += ", ." + extensions.next();
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	/**
	 * Sets the human readable description of this filter. For example:
	 * filter.setDescription("Gif and JPG Images");
	 * 
	 * @see #setDescription(String)
	 * @see #setExtensionListInDescription(boolean)
	 * @see #isExtensionListInDescription()
	 */
	public final void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	/**
	 * Determines whether the extension list (.jpg, .gif, etc) should show up in
	 * the human readable description.
	 * 
	 * Only relevent if a description was provided in the constructor or using
	 * setDescription();
	 * 
	 * @see #getDescription()
	 * @see #setDescription(String)
	 * @see #isExtensionListInDescription()
	 */
	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	/**
	 * Returns whether the extension list (.jpg, .gif, etc) should show up in
	 * the human readable description.
	 * 
	 * Only relevant if a description was provided in the constructor or using
	 * setDescription();
	 * 
	 * @see #getDescription()
	 * @see #setDescription(String)
	 * @see #setExtensionListInDescription(boolean)
	 */
	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}
}

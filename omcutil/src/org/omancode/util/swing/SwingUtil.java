package org.omancode.util.swing;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Static utility class of general purpose file functions.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public final class SwingUtil {

	private static final int DEFAULT_COLUMN_MARGIN = 5;

	/**
	 * Private constructor to prevent instantiation.
	 */
	private SwingUtil() {
	}

	/**
	 * Resize a {@link JSplitPane} to its maximum or minimum. Silently exits
	 * without resize if the split pane is not visible. Adapted from
	 * {@code OneTouchActionHandler.actionPerformed} in
	 * {@link BasicSplitPaneDivider}.
	 * 
	 * @param toMinimum
	 *            {@code true} indicates the resize should go the minimum (top
	 *            or left) vs {@code false} which indicates the resize should go
	 *            to the maximum.
	 * @param splitPane
	 *            the split pane to adjust
	 */
	public static void resizeSplit(boolean toMinimum, JSplitPane splitPane) {
		BasicSplitPaneUI splitPaneUI = (BasicSplitPaneUI) splitPane.getUI();
		BasicSplitPaneDivider divider = splitPaneUI.getDivider();
		Insets insets = splitPane.getInsets();
		int lastLoc = splitPane.getLastDividerLocation();
		int currentLoc = splitPaneUI.getDividerLocation(splitPane);
		int newLoc;

		if (!splitPane.isVisible()) {
			// if split pane is not visible then
			// splitPane.getHeight() will be -1
			// and we can't get the max/min value to resize to
			// so return
			return;
		}

		// We use the location from the UI directly, as the location the
		// JSplitPane itself maintains is not necessarily correct.
		if (toMinimum) {
			if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
				if (currentLoc >= (splitPane.getHeight() - insets.bottom - divider
						.getHeight())) {
					int maxLoc = splitPane.getMaximumDividerLocation();
					newLoc = Math.min(lastLoc, maxLoc);
					// splitPaneUI.setKeepHidden(false);
				} else {
					newLoc = insets.top;
					// splitPaneUI.setKeepHidden(true);
				}
			} else {
				if (currentLoc >= (splitPane.getWidth() - insets.right - divider
						.getWidth())) {
					int maxLoc = splitPane.getMaximumDividerLocation();
					newLoc = Math.min(lastLoc, maxLoc);
					// splitPaneUI.setKeepHidden(false);
				} else {
					newLoc = insets.left;
					// splitPaneUI.setKeepHidden(true);
				}
			}
		} else {
			if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
				if (currentLoc == insets.top) {
					int maxLoc = splitPane.getMaximumDividerLocation();
					newLoc = Math.min(lastLoc, maxLoc);
					// splitPaneUI.setKeepHidden(false);
				} else {
					newLoc =
							splitPane.getHeight() - divider.getHeight()
									- insets.top;
					// splitPaneUI.setKeepHidden(true);
				}
			} else {
				if (currentLoc == insets.left) {
					int maxLoc = splitPane.getMaximumDividerLocation();
					newLoc = Math.min(lastLoc, maxLoc);
					// splitPaneUI.setKeepHidden(false);
				} else {
					newLoc =
							splitPane.getWidth() - divider.getWidth()
									- insets.left;
					// splitPaneUI.setKeepHidden(true);
				}
			}
		}
		if (currentLoc != newLoc) {
			splitPane.setDividerLocation(newLoc);
			// We do this in case the dividers notion of the location
			// differs from the real location.
			splitPane.setLastDividerLocation(currentLoc);
		}
	}

	/**
	 * Pack all columns. Sets the preferred width of all columns so they will be
	 * just wide enough to show the column head and the widest cell in the
	 * column + a margin of 5 each side.
	 * 
	 * NB: If a table has more than 100 rows, then only the width of the column
	 * header will be checked and not the widest cell value. Otherwise it could
	 * take too long.
	 * 
	 * @param table
	 *            table with columns to pack
	 */
	public static void packAllColumns(JTable table) {
		packAllColumns(table, DEFAULT_COLUMN_MARGIN);
	}

	/**
	 * Pack all columns. Sets the preferred width of all columns so they will be
	 * just wide enough to show the column head and the widest cell in the
	 * column + {@code margin} each side.
	 * 
	 * NB: If a table has more than 100 rows, then only the width of the column
	 * header will be checked and not the widest cell value. Otherwise it could
	 * take too long.
	 * 
	 * @param table
	 *            table with columns to pack
	 * @param margin
	 *            margin to leave at each side of each column (resulting in an
	 *            additional width of 2*margin pixels).
	 */
	public static void packAllColumns(JTable table, int margin) {
		for (int c = 0; c < table.getColumnCount(); c++) {
			packColumn(table, c, margin);
		}
	}

	/**
	 * Pack specified column. Sets the preferred width of the specified visible
	 * column so it is will be just wide enough to show the column head and the
	 * widest cell value in the column + {@code margin} each side.
	 * 
	 * Taken from http://www.exampledepot.com/egs/javax.swing.table/PackCol.html
	 * 
	 * NB: If a table has more than 100 rows, then only the width of the column
	 * header will be checked and not the widest cell value. Otherwise it could
	 * take too long.
	 * 
	 * @param table
	 *            table with column to pack
	 * @param vColIndex
	 *            column to pack
	 * @param margin
	 *            margin to leave at each side of the column (resulting in an
	 *            additional width of 2*margin pixels).
	 */
	public static void packColumn(JTable table, int vColIndex, int margin) {

		DefaultTableColumnModel colModel =
				(DefaultTableColumnModel) table.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp =
				renderer.getTableCellRendererComponent(table,
						col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		// because checking the width of each row can be time consuming,
		// only do so if less than 101 rows.
		boolean checkDataWidth = (table.getRowCount() < 101);

		if (checkDataWidth) {
			// Get maximum width from all column data
			for (int r = 0; r < table.getRowCount(); r++) {
				renderer = table.getCellRenderer(r, vColIndex);
				comp =
						renderer.getTableCellRendererComponent(table,
								table.getValueAt(r, vColIndex), false, false,
								r, vColIndex);
				width = Math.max(width, comp.getPreferredSize().width);
			}
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
	}
}

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
/*
 * Custom table model class to handle the lens work list
 */

public class WorkListTableModel extends AbstractTableModel {
	public String[] columnNames;
	public Vector data;
	
	public static final long serialVersionUID = 0;
	
	public WorkListTableModel(String[] columns, Vector rowData) {
		columnNames = columns;
		data = rowData;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {	
		return ((Vector) data.get(row)).get(col);
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		((Vector) data.get(row)).set(col, value);
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col < 9) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void addRow(Vector rowData) {
		data.add(rowData);
		fireTableDataChanged();
	}
	
	public void selectAll() {
		
		fireTableDataChanged();
	}
	
	public void clearAll() {
		int rows = getRowCount();
		if (rows == 0) {
			return;
		}
		
		data.clear();
		fireTableRowsDeleted(0, rows-1);
	}
}

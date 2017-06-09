package utils;

import java.util.HashMap;

public class TableInfo {
	private String tableName;
	private String tableComment;
	private HashMap<String, ColumnInfo> cols;

	class ColumnInfo {
		private String colName;
		private String colType;
		private String colComment;

		public String getColName() {
			return colName;
		}

		public void setColName(String colName) {
			this.colName = colName;
		}

		public String getColType() {
			return colType;
		}

		public void setColType(String colType) {
			this.colType = colType;
		}

		public String getColComment() {
			return colComment;
		}

		public void setColComment(String colComment) {
			this.colComment = colComment;
		}

	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public HashMap<String, ColumnInfo> getCols() {
		return cols;
	}

	public void setCols(HashMap<String, ColumnInfo> cols) {
		this.cols = cols;
	}

	public ColumnInfo getColumnInfo() {
		return new ColumnInfo();
	}
}

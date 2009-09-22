package dux.runner.staff.data;

import java.util.List;

import dux.staff.SqlTricker;

public abstract class CommandGenerator {
	
	protected List<String> fCommandList = null;
	
	public List<String> getDeleteCommands() {
		return SqlTricker.makeDeleteSqlList(SqlTricker.extractDeletedTable(fCommandList));
	}

	public List<String> getInsertCommands() {
		return fCommandList;
	}

}

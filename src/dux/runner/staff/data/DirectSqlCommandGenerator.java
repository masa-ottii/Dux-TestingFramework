package dux.runner.staff.data;

import dux.staff.SqlTricker;

public class DirectSqlCommandGenerator extends CommandGenerator {

	public DirectSqlCommandGenerator(String commandString) {
		this.fCommandList = SqlTricker.makeSqlList(commandString,SqlTricker.DELIMITER);
	}

}

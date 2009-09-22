package dux.runner.staff.data;

import dux.staff.DataTricker;

public class SqlFileCommandGenerator extends CommandGenerator {

	public SqlFileCommandGenerator(String commandFile) throws Exception{
		try {
			this.fCommandList = DataTricker.readResource(this,commandFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	protected SqlFileCommandGenerator() {
		// Dummy Constructor
		// For Test
	}

}

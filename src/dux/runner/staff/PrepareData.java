package dux.runner.staff;

import java.lang.reflect.Field;

import org.junit.runners.model.Statement;
import dux.annotate.InitialData;
import dux.runner.staff.data.CommandGenerator;
import dux.runner.staff.data.DirectSqlCommandGenerator;
import dux.Manipulator;
import dux.runner.staff.data.SqlFileCommandGenerator;
import dux.staff.DataTricker;
import dux.staff.JdbcManipulator;

public class PrepareData extends Statement {

	private Statement fNext = null;

	private InitialData fDataInfo = null;

	private Object fTestObject = null;

	private Manipulator fManipulator = null;

	private Field fManipulaterField = null;


	public PrepareData(Statement next, InitialData dataInfo, Object test, String configFileName, Field manipulaterField) {
		fNext = next;
		fDataInfo = dataInfo;
		fTestObject = test;
		fManipulator = new JdbcManipulator(configFileName);
		fManipulaterField = manipulaterField;
	}

	@Override
	public void evaluate() throws Throwable {

		fManipulator.connect();

		CommandGenerator generator = null;
		if(fDataInfo.direct()){
			generator = new DirectSqlCommandGenerator(fDataInfo.value());

		}else{
			generator = new SqlFileCommandGenerator(fDataInfo.value());
		}
		DataTricker.deleteAndInsertTestData(fManipulator,generator);

		if(fManipulaterField != null){
			fManipulaterField.set(fTestObject, fManipulator);
		}

		fNext.evaluate();

		fManipulator.close();
	}

}

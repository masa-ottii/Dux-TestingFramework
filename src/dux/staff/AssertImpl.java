package dux.staff;

import java.util.List;
import static org.junit.Assert.fail;

import dux.Manipulator;

public class AssertImpl {

	public static void assertHave(Manipulator manipulator,String message,int expected, String conditions,boolean direct) throws Exception {
		
		List<String> commandList = null;
		String selectSql = null;
		int actual = 0;
		
		if(conditions==null){
			failConditionsIsNull(message);
		}
		if(direct){
			commandList = SqlTricker.makeSqlList(conditions, SqlTricker.DELIMITER);
		}else{
			commandList = DataTricker.readResource(manipulator, conditions);
		}
		for(String sqlStatement : commandList){
			selectSql = SqlTricker.makeSelectSql(sqlStatement);
			if(selectSql!=null) actual += manipulator.count(selectSql);
		}
		if( expected == actual ){
			return;
		}else{
			failDontHave(message,expected,actual);
		}
	}
	
	protected static void failDontHave(String message, int expected,int actual) {
		if (message != null) {
			message +=  " ";
		}else{
			message = "";
		}
		fail(message + "expected :<" + expected + "> but was :<" + actual + ">");
	}
	
	protected static void failConditionsIsNull(String message) {
		if (message != null) {
			message +=  " ";
		}else{
			message = "";
		}
		fail(message + ": argument for expected data is null.");
	}

}

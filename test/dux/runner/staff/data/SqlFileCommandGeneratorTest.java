package dux.runner.staff.data;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class SqlFileCommandGeneratorTest {

	@Test
	public void testGetInsertCommands() throws Exception {
		SqlFileCommandGenerator gen = new SqlFileCommandGenerator("data_multitable.sql");
		List<String> commandList = gen.getInsertCommands();

		for(String list : commandList){
			System.out.println(list);
		}
		assertEquals(4,commandList.size());
	}
	
}

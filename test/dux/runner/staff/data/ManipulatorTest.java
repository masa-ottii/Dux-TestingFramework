
package dux.runner.staff.data;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import dux.Manipulator;
import dux.runner.staff.data.SqlFileCommandGenerator;
import dux.staff.DataTricker;
import dux.staff.JdbcManipulator;


public class ManipulatorTest {

	@Test
	public void testConnect() throws Exception {
		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
	}

	@Test
	public void testCount_TwoEmployeeData() throws Exception {

		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("test_data.sql"));
		int count = m.count("select count(*) from employee");
		assertEquals(2,count);
	}

	@Test
	public void testCount_ThreeEmployeeData() throws Exception {

		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("data_3datas.sql"));
		int count = m.count("select count(*) from employee");
		assertEquals(3,count);
	}
	
	@Test
	public void testCount_SQLwithError() throws Exception {

		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("test_data.sql"));
		int count = m.count("select name from employee");
		assertEquals(0,count);
	}

}

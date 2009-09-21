package dux.staff;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dux.Manipulator;
import dux.runner.staff.data.SqlFileCommandGenerator;

public class AssertImplTest {

	@Test
	public void testAssertHave() throws Exception {
		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("test_data.sql"));
		AssertImpl.assertHave(m, null, 2 , "test_data.sql", false);
		assertTrue(true);
	}

	@Test(expected=AssertionError.class)
	public void testAssertHave_whenTestFailed() throws Exception {
		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("test_data.sql"));
		AssertImpl.assertHave(m, null, 4, "test_data.sql", false);
		fail();
	}

	@Test
	public void testAssertHave_directSql() throws Exception {
		
		String directSql = "INSERT INTO employee(id,email,name) VALUES (10001,'yamada@email.com','山田太郎');"
									+ "INSERT INTO employee (id,email,name) VALUES (10002,'sato@email.com','佐藤次郎');";
		
		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("data_multilines.sql"));
		AssertImpl.assertHave(m, null, 2 , directSql, true);
		assertTrue(true);
	}

	@Test(expected=AssertionError.class)
	public void testAssertHave_directSql_Failed() throws Exception {
		
		String directSql = "INSERT INTO employee(id,email,name) VALUES (10001,'yamada@email.com','XXXXX');"
									+ "INSERT INTO employee (id,email,name) VALUES (10002,'sato@email.com','YYYYY');";
		
		Manipulator  m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m,new SqlFileCommandGenerator("data_multilines.sql"));
		AssertImpl.assertHave(m, null, 2 , directSql, true);
		fail();
	}
}

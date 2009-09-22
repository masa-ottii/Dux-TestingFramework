package dux.runner;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import dux.Manipulator;
import dux.annotate.DataManipulator;
import dux.annotate.InitialData;

@RunWith(Dux.class)
public class DuxDirectDataTest {

	@DataManipulator
	private Manipulator manipulator = null;

	private static final String DIRECT_SQL = "INSERT INTO employee (email,name) VALUES ('zzz@email.com','testZZ');";

	@Test
	@InitialData(value=DIRECT_SQL,direct=true)
	public void testTesteeDaoMethod() throws Exception {
		List<String> list = testeeDaoMethod();
		assertEquals("testZZ", list.get(0));
	}

	protected List<String> testeeDaoMethod() throws Exception {

		List<String> list = new ArrayList<String>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "SELECT name FROM employee ORDER BY id";

		try {
			Connection connection = manipulator.connect();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				list.add(resultSet.getString("name"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e) {
				throw e;
			}
		}
		return list;
	}

}

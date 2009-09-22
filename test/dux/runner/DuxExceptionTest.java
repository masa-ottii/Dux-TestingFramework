package dux.runner;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import dux.annotate.Configure;
import dux.annotate.DataManipulator;
import dux.annotate.InitialData;
import dux.Manipulator;

@RunWith(Dux.class)
@Configure("jdbc")
public class DuxExceptionTest {

	@DataManipulator
	Manipulator manipulator = null;

	@Test
	@InitialData("test_data_noThere.sql")
	public void testDummy1() {
		assertEquals(5, 5);
	}

	@Test
	@InitialData("test_data.sql")
	public void testTesteeDaoMethod() throws Exception {
		List<String> list = testeeDaoMethod();
		assertEquals("testXX", list.get(0));
		assertEquals("testYY", list.get(1));
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

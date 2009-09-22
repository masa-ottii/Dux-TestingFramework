package dux.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import dux.annotate.Configure;
import dux.annotate.DataManipulator;
import dux.annotate.InitialData;
import dux.Manipulator;

@RunWith(Dux.class)
@Configure("jdbc")
public class DuxTest {

	@DataManipulator
	Manipulator manipulator = null;

	@Test
	@InitialData("test_data.sql")
	public void testDummy1() {
		assertEquals(5, 5);
	}

	@Test
	@Ignore
	@InitialData("test_data.sql")
	public void testDummy2() {
		assertEquals(5, 4);
	}

	@Test
	public void testDummy3() {
		assertThat(1, is(1));
	}

	@Test
	@InitialData("test_data.sql")
	public void testSelectDaoMethod() throws Exception {
		List<String> list = (new TestTargetDao()).select(manipulator.connect());
		assertEquals("testXX", list.get(0));
		assertEquals("testYY", list.get(1));
	}

	@Test
	@InitialData("test_data.sql")
	public void testInsertDaoMethod() throws Exception {
		
		(new TestTargetDao()).insert(manipulator.connect());

		String expectedData = "INSERT INTO employee (email,name) VALUES ('suzuki@email.com','testZZ');";
		manipulator.assertHave(1, expectedData, true);
	}

	class TestTargetDao {

		List<String> select(Connection connection) throws Exception {
			List<String> list = new ArrayList<String>();
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			String sql = "SELECT name FROM employee ORDER BY id";
			try {
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

		void insert(Connection connection) throws Exception {
			PreparedStatement statement = null;
			String sql = "INSERT INTO employee (email,name) VALUES ('suzuki@email.com','testZZ')";
			try {
				statement = connection.prepareStatement(sql);
				statement.executeUpdate();
			} catch (Exception e) {
				throw e;
			} finally {
				try {
					statement.close();
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}
}

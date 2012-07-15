package dux.staff;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import dux.Manipulator;
import dux.runner.staff.data.SqlFileCommandGenerator;

public class DataTrickerTest {

	@Test
	public void testDeleteAndInsertTestData() throws Exception {
		Manipulator m = new JdbcManipulator("jdbc");
		m.connect();
		DataTricker.deleteAndInsertTestData(m, new SqlFileCommandGenerator("test_data.sql"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadResource_DontFindSqlFile() throws Exception {
		DataTricker.readResource(this, "data_XXXXX.sql");
	}

	@Test
	public void testReadAllScript() throws Exception {

		// following code is copy from readResource method.
		Class<?> c = this.getClass();
		URL url = c.getResource("/data_multilines.sql");
		InputStream is = url.openStream();
		// copied code is up to here. 

		String result = DataTricker.readAllScript(is);
		System.out.println(result);

		String expected =
			"INSERT INTO employee(id,email,name) VALUES (10001,'yamada@email.com','山田太郎');INSERT INTO employee (id,email,name) VALUES ( 10002, 'sato@email.com', '佐藤次郎');";
		assertEquals(expected, result);
	}
}

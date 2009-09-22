package dux.staff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import dux.staff.SqlTricker;


public class SqlTrickerTest {

	@Test
	public void testExtractDeletedTable(){

		ArrayList<String> testSqls = new ArrayList<String>();
		testSqls.add("INSERT INTO employee(id,email,name) VALUES (10001,'yamada@email.com','山田太郎');");
		testSqls.add("INSERT INTO employee (id,email,name) VALUES (10002,'sato@email.com','佐藤次郎');");
		testSqls.add("INSERT INTO item (id,code,name) VALUES (10001,'test1','日本史');");
		testSqls.add("insert into item (id,code,name) VALUES (10002,'test2','世界史');");

		List<String> deleteTables = SqlTricker.extractDeletedTable(testSqls);

		assertEquals(2,deleteTables.size());
		assertTrue("employee".equals(deleteTables.get(1)) || "employee".equals(deleteTables.get(0)));
		assertTrue("item".equals(deleteTables.get(1)) || "item".equals(deleteTables.get(0)));
	}

	@Test
	public void testMakeSQLCommandLists_CASE01(){


		String testScripts = "INSERT INTO employee(id,email,name) VALUES (1,'ABC','DEF');INSERT INTO employee(id,email,name) VALUES (1,'GHI','ZX;F');";
		List<String> commandList = SqlTricker.makeSqlList(testScripts, ';');

		for(String list : commandList){
			System.out.println(list);
		}
		assertEquals(2,commandList.size());

		assertEquals("INSERT INTO employee(id,email,name) VALUES (1,'ABC','DEF');",commandList.get(0));
		assertEquals("INSERT INTO employee(id,email,name) VALUES (1,'GHI','ZX;F');",commandList.get(1));

	}

	@Test
	public void testMakeSQLCommandLists_CASE02(){

		String testScripts = "INSERT INTO employee(id,email,name) VALUES (1,'ABC','DEF');HOGE HOGE";
		List<String> commandList = SqlTricker.makeSqlList(testScripts, ';');

		assertEquals(2,commandList.size());

		assertEquals("INSERT INTO employee(id,email,name) VALUES (1,'ABC','DEF');",commandList.get(0));
		assertEquals("HOGE HOGE",commandList.get(1));

	}

	@Test
	public void testMakeSelectSql_CASE01(){
		String sqlStatement = "INSERT INTO Store (store_name, Sales, Date) VALUES ('Los Angeles', 900, 'Jan-10-1999')";
		String sql = SqlTricker.makeSelectSql(sqlStatement);
		assertEquals(sql,"SELECT COUNT(*) FROM Store WHERE store_name='Los Angeles' and Sales=900 and Date='Jan-10-1999'");
	}

	@Test
	public void testMakeSelectSql_CASE02(){
		// MEMO : there is not space between Table name and Column names.
		String sqlStatement = "INSERT INTO Store(store_name) VALUES ('Los Angeles')";
		String sql = SqlTricker.makeSelectSql(sqlStatement);
		assertEquals(sql,"SELECT COUNT(*) FROM Store WHERE store_name='Los Angeles'");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMakeSelectSql_unmatchColumnNameAndValue(){
		String sqlStatement = "INSERT INTO Store (store_name) VALUES ('Los Angeles', 900, 'Jan-10-1999')";
		SqlTricker.makeSelectSql(sqlStatement);
	}

	@Test(expected=IllegalArgumentException.class)
	@Ignore
	public void testMakeSelectSql_withoutColumnName(){
		String sqlStatement = "INSERT INTO Store VALUES ('Los Angeles', 900, 'Jan-10-1999')";
		SqlTricker.makeSelectSql(sqlStatement);
	}

}

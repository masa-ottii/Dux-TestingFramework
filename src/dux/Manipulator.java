package dux;

import java.sql.Connection;

public interface Manipulator {

	public Connection connect() throws Exception;
	
	public void commit() throws Exception;
	
	public void rollback() throws Exception;

	public  void close() throws Exception;

	public  void insert(String sql) throws Exception;

	public  void delete(String sql) throws Exception;
	
	public int count(String sql) throws Exception;

	public void assertHave(int expected,String conditions) throws Exception;

	public void assertHave(int expected,String conditions,boolean direct) throws Exception;

	public void assertHave(String message,int expected,String conditions) throws Exception;

	public void assertHave(String message,int expected,String conditions,boolean direct) throws Exception;

}
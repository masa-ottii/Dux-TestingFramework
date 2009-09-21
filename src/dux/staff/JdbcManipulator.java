package dux.staff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dux.Manipulator;

public class JdbcManipulator implements Manipulator {

	private Connection fConnection = null;
	private String fConfigFileName = "jdbc";

	public JdbcManipulator(String configFileName){
		this.fConfigFileName  = configFileName;
	}

	@Override
	public Connection connect() throws Exception {

		ResourceBundle resources = ResourceBundle.getBundle(fConfigFileName);
		try {
			Class.forName(resources.getString("jdbc.driverClassName"));
			String url = resources.getString("jdbc.url");
			String dbUser = resources.getString("jdbc.username");
			String dbPassword = resources.getString("jdbc.password");

			this.fConnection = DriverManager.getConnection(url, dbUser,dbPassword);
			this.fConnection.setAutoCommit(false);

		} catch (Exception e) {
			throw e;
		}
		return fConnection;
	}
	
	@Override
	public void commit() throws Exception {
			this.fConnection.commit();
	}

	@Override
	public void rollback() throws Exception {
		this.fConnection.rollback();
	}

	@Override
	public void close() throws Exception {
		if( ! this.fConnection.isClosed()){
			this.fConnection.close();
		}
	}

	protected void executeJdbcUpdate(String sql) throws Exception {

		PreparedStatement statement = null;
		try {
			statement = this.fConnection.prepareStatement(sql);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if(statement != null){
					statement.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	@Override
	public void insert(String sql) throws Exception {
		executeJdbcUpdate(sql);
	}

	@Override
	public void delete(String sql) throws Exception {
		executeJdbcUpdate(sql);
	}

	@Override
	public int count(String sql) throws Exception {
		PreparedStatement statement = null;
		ResultSet resultset = null;
		int result = 0;
		try {
			statement = this.fConnection.prepareStatement(sql);
			resultset = statement.executeQuery();
			if(resultset.next()){
				try{
					result = resultset.getInt(1);
				}catch(SQLException e){
					result = 0;
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if(resultset!=null){
					resultset.close();
				}
				if(statement != null){
					statement.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return result;
	}

	@Override
	public void assertHave(int expected, String conditions) throws Exception {
		AssertImpl.assertHave(this,null,expected,conditions,false);
	}

	@Override
	public void assertHave(int expected, String conditions, boolean direct) throws Exception {
		AssertImpl.assertHave(this,null,expected,conditions,direct);		
	}


	@Override
	public void assertHave(String message,int expected, String conditions) throws Exception {
		AssertImpl.assertHave(this,message,expected,conditions,false);
	}

	@Override
	public void assertHave(String message,int expected, String conditions, boolean direct) throws Exception {
		AssertImpl.assertHave(this,message,expected,conditions,direct);		
	}

	
}

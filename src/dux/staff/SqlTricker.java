package dux.staff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SqlTricker {
	
	public static final char DELIMITER = ';';
	
	protected SqlTricker(){}

	public static List<String> extractDeletedTable(List<String> commandList ){

	    String regex = "INSERT[ ]+INTO[ ]+(.+?)[ (]+";
	    Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
	    ArrayList<String> tableNames = new ArrayList<String>();

	    // 正規表現を使用してINSERT文からテーブル名を抽出
		for(String command : commandList){
			    Matcher m = p.matcher(command);
			    m.reset();
			    while(m.find()){
			    	tableNames.add(m.group(1));
			     }
		}
		// テーブル名の重複を削除
		Set<String> set = new HashSet<String>(tableNames);
		ArrayList<String> uniqueTableNames = new ArrayList<String>(set);
		return uniqueTableNames;
	}


	public static List<String> makeDeleteSqlList(List<String> tableNameList) {

	    ArrayList<String> deleteCommandList = new ArrayList<String>();

		for(String tableName : tableNameList){
			deleteCommandList.add("DELETE FROM " + tableName);
		}
		return deleteCommandList;

	}

	public static List<String> makeSqlList(String scripts, char delim) {
		StringBuffer statement = new StringBuffer();
		ArrayList<String> commandList = new ArrayList<String>();
		boolean isInLiteral = false;

		char[] content = scripts.toCharArray();

		for (int i = 0; i < content.length; i++) {
			if (content[i] == '\'') {
				isInLiteral = isInLiteral ? false : true;
			}
			if (content[i] == delim && !isInLiteral) {
				if (statement.length() > 0) {
					statement.append(content[i]);
					commandList.add(statement.toString());
					statement = new StringBuffer();
				}
			}
			else {
				statement.append(content[i]);
			}
		}
		if (statement.length() > 0) {
			commandList.add(statement.toString());
		}
		return commandList;
	}


	public static String makeSelectSql(String sqlStatement) {

	    String validInsert   = "INSERT[ ]+INTO[ ]+(.+?)[ ]*\\((.*)\\)[ ]+VALUES[ ]+\\((.*)\\)";
	    String invalidInsert = "INSERT[ ]+INTO[ ]+([^\\(]?)[ ]+VALUES[ ]+\\((.*)\\)";
	    String selectSql = null;

	    // invalid INSERT statement check
		if(Pattern.compile(invalidInsert,Pattern.CASE_INSENSITIVE).matcher(sqlStatement).find()){
			throw new IllegalArgumentException("cannot omit the column names of the INSERT statement");
		}

	    Matcher m = Pattern.compile(validInsert,Pattern.CASE_INSENSITIVE).matcher(sqlStatement);
		m.reset();
		if(m.find()){

			String tableName = m.group(1);
			selectSql = "SELECT COUNT(*) FROM " + tableName + " WHERE ";

			String[] columnNames = m.group(2).split(",");
			String[] values = m.group(3).split(",");

			if(columnNames.length != values.length){
				throw new IllegalArgumentException("column names and values are not equal of the INSERT statement");
			}else{
				for(int i=0 ;  i < columnNames.length  ; i++ ){
					if(i != 0){
						selectSql += " and ";
					}
					selectSql += columnNames[i].trim() + "=" + values[i].trim();
				}
			}
		}

		return selectSql;
	}

}

package dux.staff;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import dux.Manipulator;
import dux.runner.staff.data.CommandGenerator;

public class DataTricker {

	protected DataTricker() {
	}

	public static void deleteAndInsertTestData(Manipulator manipulator, CommandGenerator generator) throws Exception {

		try {
			List<String> deleteSqlArray = generator.getDeleteCommands();
			List<String> insertSqlArray = generator.getInsertCommands();

			for (String deleteSql : deleteSqlArray) {
				System.out.println("EXECUTE : " + deleteSql);
				manipulator.delete(deleteSql);
			}
			for (String insertSql : insertSqlArray) {
				System.out.println("EXECUTE : " + insertSql);
				manipulator.insert(insertSql);
			}
			manipulator.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static List<String> readResource(Object srcObject, String commandFile) throws Exception {

		if (!commandFile.startsWith("/")) {
			commandFile = "/" + commandFile;
		}
		Class c = srcObject.getClass();
		URL url = c.getResource(commandFile);
		if (url == null) {
			throw new IllegalArgumentException("cannot find the sql resource file :" + commandFile);
		}
		InputStream is = url.openStream();
		String scripts = readAllScript(is);
		return SqlTricker.makeSqlList(scripts, SqlTricker.DELIMITER);
	}

	protected static String readAllScript(InputStream input) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuffer scripts = new StringBuffer();

		while (reader.ready()) {
			String line = reader.readLine();
			if (line.startsWith("-- ") || line.equals("")) {
				continue;
			}
			line = line.replace('\t', ' ');
			scripts.append(line);
		}
		reader.close();
		input.close();

		return scripts.toString();
	}
}

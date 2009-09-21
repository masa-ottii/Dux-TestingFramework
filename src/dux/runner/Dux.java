package dux.runner;

import java.lang.reflect.Field;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import dux.annotate.Configure;
import dux.annotate.DataManipulator;
import dux.annotate.InitialData;
import dux.runner.staff.PrepareData;

public class Dux extends BlockJUnit4ClassRunner {

	private String fConfigFileName;
	private Field fManipulaterField;

	public Dux(Class<?> klass) throws InitializationError {
		super(klass);
		fConfigFileName = getConfigFileName(klass);
		fManipulaterField = getManipulaterField(klass);
	}

	protected String getConfigFileName(Class<?> klass) {
		Configure annotation = klass.getAnnotation(Configure.class);
		if (annotation == null) {
			return "jdbc";
		}
		return annotation.value();
	}

	protected Field getManipulaterField(Class<?> klass) {
		Field[] fields = klass.getDeclaredFields();
		for (Field f : fields) {
			DataManipulator annotation = f.getAnnotation(DataManipulator.class);
			if (annotation != null) {
				if( ! f.isAccessible()){
					f.setAccessible(true);
				}
				return f;
			}
		}
		return null;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod fwMethod, Object test) {

		Statement testStatement = super.methodInvoker(fwMethod, test);
		InitialData annotation = fwMethod.getMethod().getAnnotation(InitialData.class);
		if (annotation != null) {
			return new PrepareData(testStatement, annotation, test,fConfigFileName,fManipulaterField);
		} else {
			return testStatement;
		}
	}

}

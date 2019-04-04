package onlyfortesting;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class TestFunction extends AbstractFunction {

	@Override
	public String getName() {
		return getNameStatic();
	}

	public String getNameStatic() {
		return "Boi";
	}
	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

		String[] arrayofPatients = (String[]) arg1.getValue(env);
		String patient = (String) arg2.getValue(env);
		boolean b = false;

		for (int i = 0; i < arrayofPatients.length; i++) {
			//System.out.println(arrayofPatients[i] + " -----------" + patient);
			if (arrayofPatients[i].equals(patient)) {
				b = true;
			}
		}

		return AviatorBoolean.valueOf(b);
	}

}

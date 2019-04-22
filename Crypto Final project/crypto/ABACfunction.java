package crypto;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class ABACfunction extends AbstractFunction {


	@Override
	public String getName() {
		return getNameStatic();
	}

	public String getNameStatic() {
		return "sameID";
	}
	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

		int doctor = (int) arg1.getValue(env);
		int[] patient = (int[]) arg2.getValue(env);
		boolean b = false;
		for (int i = 0; i < patient.length; i++) {
			if (patient[i] == doctor) {
				b = true;
			}
		}

		return AviatorBoolean.valueOf(b);
	}

}

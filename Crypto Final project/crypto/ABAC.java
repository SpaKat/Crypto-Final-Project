package crypto;

import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;

public class ABAC {

	private Enforcer enforcer;
	
	public ABAC() {
		Model model = new Model();
		model.addDef("r", "r", "doctor, patient");
        model.addDef("p", "p", "not,needed");
		model.addDef("e", "e", "some(where (p.eft == allow))");
		model.addDef("m", "m", "sameID(r.doctor.id, r.patient.doctorID)");
		
		ABACfunction abacFunction = new ABACfunction();
		
		enforcer = new Enforcer(model);
		enforcer.addFunction("sameID", abacFunction);
	}
	
	public boolean valid(Object doc, Object Pat) {
		return enforcer.enforce(doc,Pat);
	}
}

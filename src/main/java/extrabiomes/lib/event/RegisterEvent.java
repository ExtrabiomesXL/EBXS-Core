package extrabiomes.lib.event;

import extrabiomes.lib.IEBXSMod;

public class RegisterEvent extends EBXSEvent {
	public RegisterEvent(IEBXSMod mod) {
		super("register", mod);
	}
}

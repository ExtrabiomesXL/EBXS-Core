package extrabiomes.lib.event;

import extrabiomes.lib.IEBXSMod;

public class RegisterEvent extends EBXSEvent {
	public final String apiVersion;
	public RegisterEvent(IEBXSMod mod, String apiVersion) {
		super("register", mod);
		this.apiVersion = apiVersion;
	}
}

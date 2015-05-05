package extrabiomes.lib.event;

public class EBXSEvent {
	public final String type;
	public final Object data;
	public EBXSEvent(String type, Object data) {
		this.type = type;
		this.data = data;
	}
}

package extrabiomes.lib.event;

import org.apache.logging.log4j.Logger;

public interface IEBXSHandler {
	public Logger log();
	public void receive(EBXSEvent event);
}

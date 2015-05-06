package extrabiomes.lib.event;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import extrabiomes.lib.IEBXSMod;

public enum EBXSBus {
	INSTANCE;
	
	private EBXSBus() {
		eventQueue = Lists.newLinkedList();
	}
		
	private Logger logger;
	private IEBXSHandler handler;
	private List<EBXSEvent> eventQueue;
	
	// TODO: allow registration of multiple event handlers?
	
	public void init(IEBXSHandler handler) {
		this.handler = handler;
		this.logger = handler.log();
		
		logger.info("EventBus: Initialized with handler "+handler.getClass().getCanonicalName());
		
		// flush any queue we may have accumulated
		flushQueue();
	}
	
	public static boolean ready() {
		return INSTANCE.handler != null;
	}

	public static void raise(EBXSEvent event) {
		INSTANCE.onRaise(event);
	}
	
	private void onRaise(EBXSEvent event) {
		if( ready() ) {
			handleEvent(event);
		} else {
			queueEvent(event);
		}
	}
	
	private void flushQueue() {
		if( !eventQueue.isEmpty() ) {
			Iterator<EBXSEvent> it = eventQueue.iterator();
			while( it.hasNext() ) {
				handleEvent(it.next());
			}
			eventQueue.clear();
		}
	}
	
	private void handleEvent(EBXSEvent event) {
		handler.receive(event);
	}
	
	private void queueEvent(EBXSEvent event) {
		eventQueue.add(event);
	}
	
	//////////----------------------------------------------------- //////////
	
	public static final String QUERY_BIOME_ID = "biome_id";
	
	public static int queryInt(String query_type, Object param) {
		return INSTANCE.handler.queryInt(query_type, param);
	}
}

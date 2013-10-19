package org.gnucash.dataSync;

import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.meta.LocalService;

public class GnucashServiceManager extends DefaultServiceManager<GnucashDataServer> {

	private final CashData data;

	public GnucashServiceManager(final LocalService<GnucashDataServer> service, final CashData data) {
		super(service);
		this.data = data;
	}

	@Override
	protected GnucashDataServer createServiceInstance() throws Exception {

		System.out.println("Creating new instance");
		return new GnucashDataServer(data);
	}
}

package org.gnucash.dataSync;

import java.io.IOException;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.gnucash.dataSync.CashData.Flags;

public class Main implements Runnable {

	private final CashData data;

	public Main(final CashData data) {
		this.data = data;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final CashData data = new CashData();
		data.flags.add(Flags.ExportAccountsUseDirectFile);
		data.exportAccountDataFilePath = "/tmp/t3.xml";
		data.setData(args);
		final Thread serverThread = new Thread(new Main(data));
		serverThread.setDaemon(false);
		serverThread.start();
	}

	public void run() {
		try {

			final UpnpService upnpService = new UpnpServiceImpl();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			});

			// Add the bound local device to the registry
			upnpService.getRegistry().addDevice(createDevice());

		} catch (final Exception ex) {
			System.err.println("Exception occured: " + ex);
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}

	// DOC: CREATEDEVICE
	LocalDevice createDevice() throws ValidationException, LocalServiceBindingException, IOException {

		final DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier("Gnucash services"));

		final DeviceType type = new UDADeviceType("GnucashData", 1);

		final DeviceDetails details = new DeviceDetails("Gnucash device", new ManufacturerDetails("Gnucash GPL"), new ModelDetails("Gnucash",
				"Needs python impl of gnucash to run", "v2.4"));

		final LocalService<GnucashDataServer> switchPowerService = new AnnotationLocalServiceBinder().read(GnucashDataServer.class);

		switchPowerService.setManager(new GnucashServiceManager(switchPowerService, data));

		return new LocalDevice(identity, type, details, switchPowerService);

		/*
		 * Several services can be bound to the same device: return new LocalDevice( identity, type, details, icon, new LocalService[] {switchPowerService,
		 * myOtherService} );
		 */

	}
	// DOC: CREATEDEVICE
}

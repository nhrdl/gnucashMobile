package org.gnucash.dataSync;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.gnucash.dataSync.CashData.Flags;

@UpnpService(serviceId = @UpnpServiceId("GnucashServiceId"), serviceType = @UpnpServiceType(value = "GnucashServiceType", version = 1))
public class GnucashDataServer {

	private final CashData data;

	public GnucashDataServer(final CashData data) {
		this.data = data;
	}

	@UpnpStateVariable(defaultValue = "0")
	private final String filePath = "/tmp/gnucash.master.xml.gz";

	@UpnpStateVariable
	private String accountsData;

	@UpnpAction(out = @UpnpOutputArgument(name = "AccountsData"))
	public String getAccountsData() throws IOException, URISyntaxException {
		if (data.flags.contains(Flags.ExportAccountsUseDirectFile)) {
			try {
				final String retData = IOUtils.toString(new URI("file://" + data.exportAccountDataFilePath));
				return retData;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return "Hello world:" + filePath;
	}

	// @UpnpAction()
	// public void setAccountData(@UpnpInputArgument(name = "accountsData") final String accountData) {
	// this.accountsData = this.filePath = accountData;
	// }

}

package org.gnucash.dataSync;

import java.util.EnumSet;

public class CashData {

	enum Flags {
		ExportAccountsUsePython, ExportAccountsUseDirectFile
	}

	EnumSet<Flags> flags = EnumSet.of(Flags.ExportAccountsUseDirectFile);
	/**
	 * This is the python script that gets executed. This script should accept a output file name as argument. Output file will be returned to caller.
	 */
	public String exportAccountsScript;

	/**
	 * If you want to use data file directly, set this field.
	 */
	public String exportAccountDataFilePath;

	public void setData(final String[] args) {
		// TODO Parse the arguments and set the data

	}
}

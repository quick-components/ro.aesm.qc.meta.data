package ro.aesm.qc.meta.data.dm.processor.reader;

import java.util.List;
import java.util.Map;

import ro.aesm.qc.meta.data.dm.DmModel;

public class Handler4JsonStream {

	protected DmModel mmDs;
	protected DefaultDsReaderCustomizer customizer;
	protected Map<String, List<Map<String, Object>>> result;

	public Map<String, List<Map<String, Object>>> getResult() {
		return this.result;
	}

	public void setResult(Map<String, List<Map<String, Object>>> result) {
		this.result = result;
	}

	public DmModel getMmDs() {
		return mmDs;
	}

	public void setMmDs(DmModel mmDs) {
		this.mmDs = mmDs;
	}

	public DefaultDsReaderCustomizer getCustomizer() {
		return customizer;
	}

	public void setCustomizer(DefaultDsReaderCustomizer customizer) {
		this.customizer = customizer;
	}

}

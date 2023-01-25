package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.util.List;
import java.util.Map;

import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public class DsXmlReaderContext {

	private boolean inDataset;
	private boolean inRecord;
	private boolean inItem;

	private DsModel dsModel;
	private MDs_Item item;

	private List<Map<String, Object>> records;
	private Map<String, Object> record;

	private DsXmlReaderContext parentContext;

	public boolean isInDataset() {
		return inDataset;
	}

	public void setInDataset(boolean inDataset) {
		this.inDataset = inDataset;
	}

	public boolean isInRecord() {
		return inRecord;
	}

	public void setInRecord(boolean inRecord) {
		this.inRecord = inRecord;
	}

	public boolean isInItem() {
		return inItem;
	}

	public void setInItem(boolean inItem) {
		this.inItem = inItem;
	}

	public DsXmlReaderContext getParentContext() {
		return parentContext;
	}

	public void setParentContext(DsXmlReaderContext parentContext) {
		this.parentContext = parentContext;
	}

	public DsModel getDsModel() {
		return dsModel;
	}

	public void setDsModel(DsModel dsModel) {
		this.dsModel = dsModel;
	}

	public MDs_Item getItem() {
		return item;
	}

	public void setItem(MDs_Item item) {
		this.item = item;
	}

	public List<Map<String, Object>> getRecords() {
		return records;
	}

	public void setRecords(List<Map<String, Object>> records) {
		this.records = records;
	}

	public Map<String, Object> getRecord() {
		return record;
	}

	public void setRecord(Map<String, Object> record) {
		this.record = record;
		if (record != null) {
			this.records.add(record);
		}
	}

}

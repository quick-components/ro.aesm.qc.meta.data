package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.util.List;
import java.util.Map;

import ro.aesm.qc.api.base.IExecutionContext;

public abstract class AbstractDsCustomizer {

	protected IExecutionContext executionContext;

	public void onNewDataset(String listName, List<Map<String, Object>> records, Map<String, Object> parentRecord) {
	}

	public void onNewRecord(String listName, List<Map<String, Object>> records, Map<String, Object> record,
			Map<String, Object> parentRecord) {
	}

	public IExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(IExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

}

package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dataset.enums.DsItemValueType;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public class ValueProvider {
	protected IExecutionContext executionContext;
	/**
	 * Fields with value definitions
	 */
	protected Collection<MDs_Item> itemsWithValueDefinition;
	/**
	 * Fields with absolute path values
	 */
	protected Collection<MDs_Item> itemsWithAbsPathDefinition;
	protected Map<String, Object> absPathValuesMap;

	public void initNewRecord(List<Map<String, Object>> records, Map<String, Object> record,
			Map<String, Object> parentRecord) {
		if (itemsWithAbsPathDefinition != null) {
			for (MDs_Item item : this.itemsWithAbsPathDefinition) {
				record.put(item.getName(), this.absPathValuesMap.get(item.getFullPath()));
			}
		}
		if (itemsWithValueDefinition != null) {
			for (MDs_Item item : this.itemsWithValueDefinition) {
				this.initNewRecordItem(item, records, record, parentRecord);
			}
		}
	}

	protected void initNewRecordItem(MDs_Item item, List<Map<String, Object>> records, Map<String, Object> record,
			Map<String, Object> parentRecord) {

		// System.out.println("initNewRecordItem " + item.getValueType() + "." +
		// item.getValue());

		if (item.getValueType().equals(DsItemValueType.VAR)) {
			String var = item.getValue(); // .substring(4).trim();
			Object val = null;
			if (var.startsWith("uuid")) {
				val = this.createUuid(var);
			} else if (var.equals("rownum")) {
				val = "" + records.size();
			} else if (var.equals("current_date")) {
				val = LocalDate.now();
			} else if (var.equals("current_datetime")) {
				val = LocalDateTime.now();
			} else if (var.equals("exec_user")) {
				val = this.executionContext.getUser();
			} else if (var.equals("exec_id")) {
				val = this.executionContext.getId();
			}
			record.put(item.getName(), val);
		} else if (item.getValueType().equals(DsItemValueType.PARENT_ITEM)) {
			if (parentRecord != null) {
				record.put(item.getName(), parentRecord.get(item.getValue()));
			}
		} else if (item.getValueType().equals(DsItemValueType.STATIC)) {
			record.put(item.getName(), item.getValue());
		}
	}

	protected String createUuid(String var) {
		String val = (UUID.randomUUID()).toString();

		if (var.contains("-c")) {
			val = val.replace("-", "");
		}
		if (var.contains("-u")) {
			val = val.toUpperCase();
		}
		return val;
	}

	public IExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(IExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public Collection<MDs_Item> getItemsWithValueDefinition() {
		return itemsWithValueDefinition;
	}

	public void setItemsWithValueDefinition(Collection<MDs_Item> itemsWithValueDefinition) {
		this.itemsWithValueDefinition = itemsWithValueDefinition;
	}

	public Collection<MDs_Item> getItemsWithAbsPathDefinition() {
		return itemsWithAbsPathDefinition;
	}

	public void setItemsWithAbsPathDefinition(Collection<MDs_Item> itemsWithAbsPathDefinition) {
		this.itemsWithAbsPathDefinition = itemsWithAbsPathDefinition;
	}

	public Map<String, Object> getAbsPathValuesMap() {
		return absPathValuesMap;
	}

	public void setAbsPathValuesMap(Map<String, Object> absPathValuesMap) {
		this.absPathValuesMap = absPathValuesMap;
	}

}

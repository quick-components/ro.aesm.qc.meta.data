package ro.aesm.qc.meta.data.dm.processor.reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;

public class DefaultDsReaderCustomizer {

	protected DmModel mmDs;
	protected IExecutionContext executionContext;
	protected Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();

	public void onNewList(String listName, List<Map<String, Object>> recList, Map<String, Object> parentRec) {
	}

	public void onNewRecord(String listName, Map<String, Object> rec, Map<String, Object> parentRec) {
	}

	public void initNewRecordItem(MDm_Item item, String listName, List<Map<String, Object>> list,
			Map<String, Object> rec, Map<String, Object> parentRec) {
		if (item.getValue().startsWith("parent_field:")) {
			if (parentRec != null) {
				String parentFieldname = item.getValue().substring(13).trim();
				rec.put(item.getName(), parentRec.get(parentFieldname));
			}
		} else if (item.getValue().startsWith("static:")) {
			String val = item.getValue().substring(7).trim();
			rec.put(item.getName(), val);
		} else if (item.getValue().startsWith("var:")) {
			String var = item.getValue().substring(4).trim();
			Object val = null;
			if (var.startsWith("uuid")) {
				val = this.createUuid(var);
			} else if (var.equals("rownum")) {
				val = "" + list.size();
			} else if (var.equals("current_date")) {
				val = LocalDate.now();
			} else if (var.equals("current_datetime")) {
				val = LocalDateTime.now();
			} else if (var.equals("exec_user")) {
				val = this.executionContext.getUser();
			} else if (var.equals("exec_id")) {
				val = this.executionContext.getId();
			}
			rec.put(item.getName(), val);
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

	public DmModel getMmDs() {
		return mmDs;
	}

	public void setMmDs(DmModel mmDs) {
		this.mmDs = mmDs;
	}

	public IExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(IExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public Map<String, List<Map<String, Object>>> getResult() {
		return result;
	}

	public void setResult(Map<String, List<Map<String, Object>>> result) {
		this.result = result;
	}

}

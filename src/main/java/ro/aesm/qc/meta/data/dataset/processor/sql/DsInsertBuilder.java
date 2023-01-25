package ro.aesm.qc.meta.data.dataset.processor.sql;

import ro.aesm.qc.api.exception.QcExecutionException;
import ro.aesm.qc.meta.data.dm.model.MDm_Db;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DsInsertBuilder {

	public String buildSql(MDm_List mmList) {
		MDm_Db mmTargetDb = mmList.getTargetDb();
		if (mmTargetDb == null) {
			throw new QcExecutionException("No target database information provided for list `%s`", mmList.getName());
		}

		if (!mmTargetDb.getType().equals(MDm_Db.TYPE_TABLE)) {
			throw new QcExecutionException("Target type for dataset `%s` must be table for insert operations.",
					mmList.getName());
		}

		String[] parts = this.buildColumns(mmList);
		String sql = "insert into  " + mmTargetDb.getName() + "(" + parts[0] + ") " + "values ( " + parts[1] + " ) ";
		// sql = this.bindSessionParams(sql)
		return sql;
	}

	protected String[] buildColumns(MDm_List mmList) {
		StringBuilder cols = new StringBuilder();
		StringBuilder vals = new StringBuilder();
		for (MDm_Item item : mmList.getFieldItems()) {
			if (item.getType().equals(MDm_Item.TYPE_HELPER)) {
				continue;
			}
			String name = item.getName();
			if (cols.length() > 0) {
				cols.append(",");
				vals.append(",");
			}
			cols.append(name);
			if (item.getTarget() != null) {
				String targetFor = item.getTargetFor();
				if (targetFor.equals(MDm_Item.TARGET_FOR_INSERT) || targetFor.equals(MDm_Item.TARGET_FOR_SAVE)) {
					vals.append("(" + item.getTarget() + ")");
				}
			} else {
				vals.append(":" + name);
			}

		}
		return new String[] { cols.toString(), vals.toString() };
	}

}

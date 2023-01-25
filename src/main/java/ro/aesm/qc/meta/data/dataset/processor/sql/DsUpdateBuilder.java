package ro.aesm.qc.meta.data.dataset.processor.sql;

import ro.aesm.qc.api.exception.QcExecutionException;
import ro.aesm.qc.meta.data.dm.model.MDm_Db;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DsUpdateBuilder {

	public String buildSql(MDm_List mmList) {
		MDm_Db mmTargetDb = mmList.getTargetDb();
		if (mmTargetDb == null) {
			throw new QcExecutionException("No target database information provided for list `%s`", mmList.getName());
		}

		if (!mmTargetDb.getType().equals(MDm_Db.TYPE_TABLE)) {
			throw new QcExecutionException("Target type for list `%s` must be table for update operations.",
					mmList.getName());
		}

		String parts = this.buildColumns(mmList);
		String sql = "update " + mmTargetDb.getName() + " set " + parts + " where ";
		// sql = this.bindSessionParams(sql)
		return sql;
	}

	protected String buildColumns(MDm_List mmList) {
		StringBuilder cols = new StringBuilder();
		for (MDm_Item item : mmList.getFieldItems()) {
			String name = item.getName();
			if (cols.length() > 0) {
				cols.append(",");
			}
			cols.append(name + " = :" + name);
		}
		return cols.toString();
	}

}

package ro.aesm.qc.meta.data.dm.processor.sql;

import ro.aesm.qc.api.exception.QcExecutionException;
import ro.aesm.qc.meta.data.dm.model.MDm_Db;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DsQueryBuilder {

	public String buildSql(MDm_List dsModel) {
		MDm_Db mmSourceDb = dsModel.getSourceDb();
		if (mmSourceDb == null) {
			throw new QcExecutionException("No source database information provided for dataset `%s`",
					dsModel.getName());
		}
		String sql = "";
		String type = mmSourceDb.getType();
		if ("table".equals(type) || "view".equals(type)) {
			sql = "select " + this.builColumns(dsModel) + " from " + mmSourceDb.getName() + " " + mmSourceDb.getAlias()
					+ " " + this.buildWhere(dsModel) + " " + this.buildOrderBy(dsModel);
		}
		return sql;
	}

	protected String builColumns(MDm_List dsModel) {
		String alias = dsModel.getSourceDb().getAlias();
		StringBuilder cols = new StringBuilder();
		for (MDm_Item item : dsModel.getFieldItems()) {
			String name = item.getName();
			if (cols.length() > 0) {
				cols.append(",");
			}
			if (item.getSource() != null) {
				String source = item.getSource();
				if ("sql".equals(item.getSourceType())) {
					if (source.equals("")) {
						cols.append("'' " + item.getName());
					} else {
						cols.append("(" + source + ") " + item.getName());
					}
				}
			} else {
				cols.append(alias + "." + name);
			}
		}
		return cols.toString();
	}

	protected String buildWhere(MDm_List mmList) {

		String where = "";
		if (mmList.getSourceDb().getWhere() != null) {
			where = mmList.getSourceDb().getWhere();
		}

		if (!"".equals(where)) {
			where = " where " + where;
		}
		return where;
	}

	protected String buildOrderBy(MDm_List dsModel) {
		String defaultOrderBy = dsModel.getSourceDb().getOrderBy();
		String orderBy = "";
		if (defaultOrderBy != null && !"".equals(defaultOrderBy)) {
			orderBy += defaultOrderBy;
		}
		if (!"".equals(orderBy)) {
			orderBy = "order by " + orderBy;
		}
		return orderBy;
	}
}

package ro.aesm.qc.meta.data.dataset.processor.sql;

import ro.aesm.qc.api.exception.QcExecutionException;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.enums.DsDbType;
import ro.aesm.qc.meta.data.dataset.model.MDs_Db;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public class DsQueryBuilder {

	public String buildSql(DsModel dsModel) {
		MDs_Db mmSourceDb = dsModel.getSourceDb();
		if (mmSourceDb == null) {
			throw new QcExecutionException("No source database information provided for dataset `%s`",
					dsModel.getName());
		}
		String sql = "";
		DsDbType type = mmSourceDb.getType();
		if (DsDbType.TABLE.equals(type) || DsDbType.VIEW.equals(type)) {
			sql = "select " + this.builColumns(dsModel) + " from " + mmSourceDb.getName() + " " + mmSourceDb.getAlias()
					+ " " + this.buildWhere(dsModel) + " " + this.buildOrderBy(dsModel);
		}
		return sql;
	}

	protected String builColumns(DsModel dsModel) {
		String alias = dsModel.getSourceDb().getAlias();
		StringBuilder cols = new StringBuilder();
		for (MDs_Item item : dsModel.getFieldItems()) {
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

	protected String buildWhere(DsModel dsModel) {
		String where = "";
		if (dsModel.getSourceDb().getWhere() != null) {
			where = dsModel.getSourceDb().getWhere();
		}

		if (!"".equals(where)) {
			where = " where " + where;
		}
		return where;
	}

	protected String buildOrderBy(DsModel dsModel) {
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

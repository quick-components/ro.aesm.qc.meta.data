package ro.aesm.qc.meta.data.dataset.model;

import ro.aesm.qc.meta.data.dataset.enums.DsDbType;

public class MDs_Db {

//	public static final String TYPE_TABLE = "table";
//	public static final String TYPE_VIEW = "view";
//	public static final String TYPE_SQL = "sql";
//	public static final String TYPE_FACTORY = "factory";
	//

	public static final String DEFAULT_PU = "main";
	public static final String DEFAULT_PK = "id";

	//public static final String DEFAULT_TYPE = DsDbType.SQL.toString();
	//public static final String DEFAULT_ALIAS = "_mt";

	/**
	 * persistence unit name as configured in database connections
	 */
	private String pu;

	/**
	 * Comma separated primary key column names
	 */
	private String pk = DEFAULT_PK;
	/**
	 * auto(default, db autoincrement), uuid, seq:<seq_name> value from specified
	 * sequence, manual(save what comes),
	 */
	private String idMode;

	private DsDbType type;
	private String name;
	private String sql;
	private String where;
	private String orderBy;

	private String alias;
	private String factory;

	public String getPu() {
		return pu;
	}

	public void setPu(String pu) {
		this.pu = pu;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getIdMode() {
		return idMode;
	}

	public void setIdMode(String idMode) {
		this.idMode = idMode;
	}

	public DsDbType getType() {
		return type;
	}

	public void setType(DsDbType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = this.strip(name);
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = this.strip(sql);
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = this.strip(where);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = this.strip(orderBy);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = this.strip(alias);
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = this.strip(factory);
	}

	protected String strip(String v) {
		if (v != null) {
			return v.strip();
		} else {
			return v;
		}
	}
}

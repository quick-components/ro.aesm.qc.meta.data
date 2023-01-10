package ro.aesm.qc.meta.data.dm.model;

public class MDm_Db {

	public static final String TYPE_TABLE = "table";
	public static final String TYPE_VIEW = "view";
	public static final String TYPE_SQL = "sql";
	public static final String TYPE_FACTORY = "factory";

	public static final String DEFAULT_PU = "main";
	public static final String DEFAULT_PK = "id";
	public static final String DEFAULT_ID_MODE = "auto";
	public static final String DEFAULT_TYPE = "sql";
	public static final String DEFAULT_ALIAS = "_mt";

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

	private String type;
	private String name;
	private String sql;
	private String where;
	private String orderBy;

	private String alias;
	private String factory;

	public static String getTypeTable() {
		return TYPE_TABLE;
	}

	public static String getTypeView() {
		return TYPE_VIEW;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

}

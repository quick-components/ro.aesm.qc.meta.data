package ro.aesm.qc.meta.data.dm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDm_Item {
	public static final String TYPE_FIELD = "field";
	public static final String TYPE_HELPER = "helper";
	public static final String TYPE_PARAM = "param";
	public static final String TYPE_REF = "ref";

	// # data types
	public static final String BASE_DATATYPE_STRING = "string";
	public static final String BASE_DATATYPE_DATE = "date";
	public static final String BASE_DATATYPE_BOOLEAN = "boolean";
	public static final String BASE_DATATYPE_NUMBER = "number";
	public static final String BASE_DATATYPE_LOB = "lob";

	public static final String DATATYPE_STRING = "string";
	public static final String DATATYPE_TEXT = "text";
	public static final String DATATYPE_DATE = "date";
	public static final String DATATYPE_DATETIME = "datetime";
	public static final String DATATYPE_BOOLEAN = "boolean";
	public static final String DATATYPE_INTEGER = "integer";
	public static final String DATATYPE_DECIMAL = "decimal";
	public static final String DATATYPE_NUMBER = "number";
	public static final String DATATYPE_CLOB = "clob";
	public static final String DATATYPE_BLOB = "blob";

	public static final String SOURCE_TYPE_SQL = "sql";
	public static final String TARGET_TYPE_SQL = "sql";
	public static final String TARGET_FOR_INSERT = "insert";
	public static final String TARGET_FOR_UPDATE = "update";
	public static final String TARGET_FOR_SAVE = "save";

	public static final String DEFAULT_TYPE = TYPE_FIELD;
	public static final String DEFAULT_DATA_TYPE = DATATYPE_STRING;
	public static final String DEFAULT_SOURCE_TYPE = SOURCE_TYPE_SQL;
	public static final String DEFAULT_TARGET_TYPE = TARGET_TYPE_SQL;

	public static final boolean DEFAULT_REQUIRED = false;
	public static final String DEFAULT_LOV_TYPE = "sql";
	public static final String DEFAULT_INSERT_VALUE_TYPE = "sql";
	public static final String DEFAULT_UPDATE_VALUE_TYPE = "sql";
	public static final String DEFAULT_SAVE_VALUE_TYPE = "sql";

	private String name;
	// # field (default), param, ref:a referenced set
	private String type = DEFAULT_TYPE;
	private String ref;
	private List<MDs_RefParam> refParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> refParamsDict = new HashMap<String, MDs_RefParam>();
	private String dataType = DEFAULT_DATA_TYPE;
	private String source;
	private String sourceType = DEFAULT_SOURCE_TYPE;
	private String target;
	private String targetType = DEFAULT_SOURCE_TYPE;
	private String targetFor = "save";

	/**
	 * 
	 */
	private String path;
	/**
	 * Position of the field in a tabular data array(csv row). Can be derived from
	 * the path (in this case the field name in csv) or the path can have a numeric
	 * value representing this index.
	 */
	private int index = -1;
	private String value;

	public void addRefParam(MDs_RefParam refParam) {
		this.refParams.add(refParam);
		this.refParamsDict.put(refParam.getTarget(), refParam);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public List<MDs_RefParam> getRefParams() {
		return refParams;
	}

	public void setRefParams(List<MDs_RefParam> refParams) {
		this.refParams = refParams;
	}

	public Map<String, MDs_RefParam> getRefParamsDict() {
		return refParamsDict;
	}

	public void setRefParamsDict(Map<String, MDs_RefParam> refParamsDict) {
		this.refParamsDict = refParamsDict;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetFor() {
		return targetFor;
	}

	public void setTargetFor(String targetFor) {
		this.targetFor = targetFor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}

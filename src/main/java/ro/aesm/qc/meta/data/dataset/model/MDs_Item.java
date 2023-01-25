package ro.aesm.qc.meta.data.dataset.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.meta.data.dataset.enums.DsItemDataType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemValueType;

public class MDs_Item {

	public static final String SOURCE_TYPE_SQL = "sql";
	public static final String TARGET_TYPE_SQL = "sql";
	public static final String TARGET_FOR_INSERT = "insert";
	public static final String TARGET_FOR_UPDATE = "update";
	public static final String TARGET_FOR_SAVE = "save";

	public static final String DEFAULT_SOURCE_TYPE = SOURCE_TYPE_SQL;
	public static final String DEFAULT_TARGET_TYPE = TARGET_TYPE_SQL;

	public static final boolean DEFAULT_REQUIRED = false;
	public static final String DEFAULT_LOV_TYPE = "sql";
	public static final String DEFAULT_INSERT_VALUE_TYPE = "sql";
	public static final String DEFAULT_UPDATE_VALUE_TYPE = "sql";
	public static final String DEFAULT_SAVE_VALUE_TYPE = "sql";

	private String name;
	// # field (default), param, ref:a referenced set
	private DsItemType type;
	private String ref;
	private List<MDs_RefParam> refParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> refParamsDict = new HashMap<String, MDs_RefParam>();
	private DsItemDataType dataType;
	private String source;
	private String sourceType = DEFAULT_SOURCE_TYPE;
	private String target;
	private String targetType = DEFAULT_SOURCE_TYPE;
	private String targetFor = "save";

	
	
	/**
	 * 
	 */
	private String path;

	private String fullPath;

	private int fullPathTokenCount;

	/**
	 * Path is an attribute. Applies only for xml documents.
	 */
	private boolean pathAttr;

	private String pathAttrName;

	/**
	 * Path is an absolute xpath style expression
	 */
	private boolean pathAbsolute;

	/**
	 * Position of the field in a tabular data array(csv row). Can be derived from
	 * the path (in this case the field name in csv) or the path can have a numeric
	 * value representing this index.
	 */
	private int index = -1;
	/**
	 * parent_field, static, var
	 */
	private DsItemValueType valueType;
	private String value;

 
	
	
	public boolean isRefItem() {
		return DsItemType.REF.equals(this.type);
	}

	public boolean isFieldItem() {
		return DsItemType.FIELD.equals(this.type);
	}

	public boolean isParamItem() {
		return DsItemType.PARAM.equals(this.type);
	}

	public void addRefParam(MDs_RefParam refParam) {
		this.refParams.add(refParam);
		this.refParamsDict.put(refParam.getTarget(), refParam);
	}

	public boolean isPathAttr() {
		return pathAttr;
	}

	public boolean isPathAbsolute() {
		return pathAbsolute;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
		this.fullPathTokenCount = fullPath.split("\\.").length;
	}

	public int getFullPathTokenCount() {
		return fullPathTokenCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DsItemType getType() {
		return type;
	}

	public void setType(DsItemType type) {
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

	public DsItemDataType getDataType() {
		return dataType;
	}

	public void setDataType(DsItemDataType dataType) {
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
		if (this.path.contains("@")) {
			this.pathAttr = true;
			int idx = this.path.indexOf("@");
			this.pathAttrName = this.path.substring(idx + 1);
		}
		if (this.path.startsWith("//")) {
			this.pathAbsolute = true;
			this.fullPath = this.path;
		}
	}

	public DsItemValueType getValueType() {
		return valueType;
	}

	public void setValueType(DsItemValueType valueType) {
		this.valueType = valueType;
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

	public String getPathAttrName() {
		return pathAttrName;
	}

}

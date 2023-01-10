package ro.aesm.qc.meta.data.dm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDm_List {
	public static final String TYPE_MASTER = "master";
	public static final String TYPE_DETAIL = "detail";
	public static final String DEFAULT_TYPE = TYPE_MASTER;

	private String name;
	private String type;

	private String path;

	private String beforeSet;
	private List<MDs_RefParam> beforeSetParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> beforeSetParamsMap = new HashMap<String, MDs_RefParam>();
	private String afterSet;
	private List<MDs_RefParam> afterSetParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> afterSetParamsMap = new HashMap<String, MDs_RefParam>();

	private MDm_Db sourceDb;
	private MDm_Db targetDb;

	private List<MDm_Item> items = new ArrayList<MDm_Item>();
	private Map<String, MDm_Item> itemsMap = new HashMap<String, MDm_Item>();

	private List<MDm_Item> fieldItems = new ArrayList<MDm_Item>();
	private Map<String, MDm_Item> fieldItemsMap = new HashMap<String, MDm_Item>();

	private List<MDm_Item> refItems = new ArrayList<MDm_Item>();
	private Map<String, MDm_Item> refItemsMap = new HashMap<String, MDm_Item>();

	private List<MDm_Item> paramItems = new ArrayList<MDm_Item>();
	private Map<String, MDm_Item> paramItemsMap = new HashMap<String, MDm_Item>();

	public void addItem(MDm_Item item) {
		this.items.add(item);
		this.itemsMap.put(item.getName(), item);
		if (item.getType().equals("param")) {
			this.paramItems.add(item);
			this.paramItemsMap.put(item.getName(), item);
		} else if (item.getType().equals("field")) {
			this.fieldItems.add(item);
			this.fieldItemsMap.put(item.getName(), item);
		} else if (item.getType().equals("ref")) {
			this.refItems.add(item);
			this.refItemsMap.put(item.getName(), item);
		}
	}

	public boolean isDetailList() {
		return (this.type.equals(TYPE_DETAIL));
	}

	public void addBeforeSetParam(MDs_RefParam refParam) {
		this.beforeSetParams.add(refParam);
		this.beforeSetParamsMap.put(refParam.getTarget(), refParam);

	}

	public void addAfterSetParam(MDs_RefParam refParam) {
		this.afterSetParams.add(refParam);
		this.afterSetParamsMap.put(refParam.getTarget(), refParam);

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBeforeSet() {
		return beforeSet;
	}

	public void setBeforeSet(String beforeSet) {
		this.beforeSet = beforeSet;
	}

	public List<MDs_RefParam> getBeforeSetParams() {
		return beforeSetParams;
	}

	public void setBeforeSetParams(List<MDs_RefParam> beforeSetParams) {
		this.beforeSetParams = beforeSetParams;
	}

	public Map<String, MDs_RefParam> getBeforeSetParamsMap() {
		return beforeSetParamsMap;
	}

	public void setBeforeSetParamsMap(Map<String, MDs_RefParam> beforeSetParamsMap) {
		this.beforeSetParamsMap = beforeSetParamsMap;
	}

	public String getAfterSet() {
		return afterSet;
	}

	public void setAfterSet(String afterSet) {
		this.afterSet = afterSet;
	}

	public List<MDs_RefParam> getAfterSetParams() {
		return afterSetParams;
	}

	public void setAfterSetParams(List<MDs_RefParam> afterSetParams) {
		this.afterSetParams = afterSetParams;
	}

	public Map<String, MDs_RefParam> getAfterSetParamsMap() {
		return afterSetParamsMap;
	}

	public void setAfterSetParamsMap(Map<String, MDs_RefParam> afterSetParamsMap) {
		this.afterSetParamsMap = afterSetParamsMap;
	}

	public MDm_Db getSourceDb() {
		return sourceDb;
	}

	public void setSourceDb(MDm_Db sourceDb) {
		this.sourceDb = sourceDb;
	}

	public MDm_Db getTargetDb() {
		return targetDb;
	}

	public void setTargetDb(MDm_Db targetDb) {
		this.targetDb = targetDb;
	}

	public List<MDm_Item> getItems() {
		return items;
	}

	public void setItems(List<MDm_Item> items) {
		this.items = items;
	}

	public Map<String, MDm_Item> getItemsMap() {
		return itemsMap;
	}

	public void setItemsMap(Map<String, MDm_Item> itemsMap) {
		this.itemsMap = itemsMap;
	}

	public List<MDm_Item> getFieldItems() {
		return fieldItems;
	}

	public void setFieldItems(List<MDm_Item> fieldItems) {
		this.fieldItems = fieldItems;
	}

	public Map<String, MDm_Item> getFieldItemsMap() {
		return fieldItemsMap;
	}

	public void setFieldItemsMap(Map<String, MDm_Item> fieldItemsMap) {
		this.fieldItemsMap = fieldItemsMap;
	}

	public List<MDm_Item> getParamItems() {
		return paramItems;
	}

	public void setParamItems(List<MDm_Item> paramItems) {
		this.paramItems = paramItems;
	}

	public Map<String, MDm_Item> getParamItemsMap() {
		return paramItemsMap;
	}

	public void setParamItemsMap(Map<String, MDm_Item> paramItemsMap) {
		this.paramItemsMap = paramItemsMap;
	}

}

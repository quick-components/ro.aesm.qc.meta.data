package ro.aesm.qc.meta.data.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.base.AbstractMetaModelRoot;
import ro.aesm.qc.base.MetaComponentReference;
import ro.aesm.qc.meta.data.dataset.enums.DsItemType;
import ro.aesm.qc.meta.data.dataset.enums.DsType;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;
import ro.aesm.qc.meta.data.dataset.model.MDs_Db;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;
import ro.aesm.qc.meta.data.dataset.model.MDs_RefParam;

public class DsModel extends AbstractMetaModelRoot {
//	public static final String TYPE_MAIN = "main";
//	public static final String TYPE_DETAIL = "detail";
//	public static final String DEFAULT_TYPE = TYPE_MAIN;

	private DsType type;

	private String path;

	private String contextPath;

	private String listPath;
	private int listPathTokenCount;
	private String recordPath;
	private int recordPathTokenCount;

	private String beforeSet;
	private List<MDs_RefParam> beforeSetParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> beforeSetParamsMap = new HashMap<String, MDs_RefParam>();
	private String afterSet;
	private List<MDs_RefParam> afterSetParams = new ArrayList<MDs_RefParam>();
	private Map<String, MDs_RefParam> afterSetParamsMap = new HashMap<String, MDs_RefParam>();

	private MDs_Db sourceDb;
	private MDs_Db targetDb;

	private List<MDs_Item> items = new ArrayList<MDs_Item>();
	private Map<String, MDs_Item> itemsMap = new HashMap<String, MDs_Item>();

	private List<MDs_Item> fieldItems = new ArrayList<MDs_Item>();
	private Map<String, MDs_Item> fieldItemsMap = new HashMap<String, MDs_Item>();

	private List<MDs_Item> refItems = new ArrayList<MDs_Item>();
	private Map<String, MDs_Item> refItemsMap = new HashMap<String, MDs_Item>();

	private List<MDs_Item> paramItems = new ArrayList<MDs_Item>();
	private Map<String, MDs_Item> paramItemsMap = new HashMap<String, MDs_Item>();

	// private Map<String, MDs_Item> itemsPathMap = new HashMap<String, MDs_Item>();
	private Map<String, List<MDs_Item>> itemsPathMap2 = new HashMap<>();

	private List<MDs_Item> itemsWithValueDefinition;

	private List<MDs_Item> itemsWithAbsPathDefinition;
	private Map<String, Collection<MDs_Item>> itemsWithAbsPathDefinitionMap;

	private List<MDs_Customizer> customizers = new ArrayList<>();

	public DsModel(String name) {
		super(name);
	}

	@Override
	public void on$collectRefrences() {
		for (MDs_Item item : items) {
			if (item.isRefItem()) {
				MetaComponentReference r = new MetaComponentReference();
				r.setName(item.getName());
				r.setClassFqn(this.getClass().getName());
				this.addReference(r);
			}
		}
	}

	@Override
	public void on$ready(boolean propagate) {
		this.updatePaths();
		super.on$ready(propagate);
	}

	public void addItem(MDs_Item item) {
		this.items.add(item);
		this.itemsMap.put(item.getName(), item);
		if (item.getType().equals(DsItemType.PARAM)) {
			this.paramItems.add(item);
			this.paramItemsMap.put(item.getName(), item);
		} else if (item.getType().equals(DsItemType.FIELD)) {
			this.fieldItems.add(item);
			this.fieldItemsMap.put(item.getName(), item);
		} else if (item.getType().equals(DsItemType.REF)) {
			this.refItems.add(item);
			this.refItemsMap.put(item.getName(), item);
		}
		if (item.getValue() != null) {
			if (this.itemsWithValueDefinition == null) {
				this.itemsWithValueDefinition = new ArrayList<>();
			}
			this.itemsWithValueDefinition.add(item);
		}
//		if (item.isPathAbsolute()) {
//			if (this.itemsWithAbsPathDefinition == null) {
//				this.itemsWithAbsPathDefinition = new ArrayList<>();
//			}
//			this.itemsWithAbsPathDefinition.add(item);
//		}
	}

	public boolean isDetailDataset() {
		return (this.type.equals(DsType.DETAIL));
	}

	public boolean isMainDataset() {
		return (this.type.equals(DsType.MAIN));
	}

	public void addBeforeSetParam(MDs_RefParam refParam) {
		this.beforeSetParams.add(refParam);
		this.beforeSetParamsMap.put(refParam.getTarget(), refParam);

	}

	public void addAfterSetParam(MDs_RefParam refParam) {
		this.afterSetParams.add(refParam);
		this.afterSetParamsMap.put(refParam.getTarget(), refParam);

	}

	public DsType getType() {
		return type;
	}

	public void setType(DsType type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getListPath() {
		return listPath;
	}

	public String getRecordPath() {
		return recordPath;
	}

	protected void updatePaths() {
		String ctxPath = this.contextPath;
		if (ctxPath != null && !"".equals(ctxPath)) {
			ctxPath += ".";
		} else {
			ctxPath = "";
		}
		String _path = this.path;

		String lp = "";
		String rp = "";

		if (_path != null && !_path.equals("")) {
			int pathTokenCount = _path.split("\\.").length;
			if (pathTokenCount > 1) {
				int idx = _path.lastIndexOf(".");
				lp = ctxPath + _path.substring(0, idx);
				if (idx < _path.length() - 1) {
					rp = ctxPath + _path;
				} else {
					rp = lp;
				}
			} else {
				lp = this.removeLastDot(ctxPath);
				rp = ctxPath + _path;
			}
		}

		this.listPath = lp;
		this.recordPath = rp;

		System.out.println("listPath=" + listPath);
		System.out.println("recordPath=" + recordPath);

		if (this.listPath.endsWith(".")) {
			this.listPath = this.listPath.substring(0, this.listPath.length() - 1);
		}
		this.listPathTokenCount = this.listPath.split("\\.").length;
		this.recordPathTokenCount = this.recordPath.split("\\.").length;
		this.updateItemsPaths();
		// collect itemsWithAbsPathDefinition from Map
		if (itemsWithAbsPathDefinitionMap != null) {
			this.itemsWithAbsPathDefinition = new ArrayList<>();
			for (Collection<MDs_Item> items : this.itemsWithAbsPathDefinitionMap.values()) {
				this.itemsWithAbsPathDefinition.addAll(items);
			}
		}
	}

	protected String removeLastDot(String v) {
		if (v.lastIndexOf(".") == (v.length() - 1)) {
			return v.substring(0, v.length() - 1);
		}
		return v;
	}

	protected void updateItemsPaths() {
		this.itemsPathMap2.clear();
		String prefix = this.recordPath;
		if (prefix != null && !"".equals(prefix)) {
			prefix += ".";
		}
		for (MDs_Item item : this.items) {

			if (item.isPathAbsolute()) {
				String key = item.getPath();
				System.out.println("itempath: " + key);
				key = key.substring(2);
				if (item.isPathAttr()) {
					int idx = key.lastIndexOf(".");
					key = key.substring(0, idx);
				}
				if (itemsWithAbsPathDefinitionMap == null) {
					itemsWithAbsPathDefinitionMap = new HashMap<>();
				}
				Collection<MDs_Item> l = this.itemsWithAbsPathDefinitionMap.get(key);
				if (l == null) {
					l = new ArrayList<>();
					this.itemsWithAbsPathDefinitionMap.put(key, l);
				}
				this.itemsWithAbsPathDefinitionMap.get(key).add(item);
			} else {
				String key = prefix + item.getPath();
				if (key.endsWith(".")) {
					key = key.substring(0, key.length() - 1);
				}
				System.out.println("itempath: " + key);
				item.setFullPath(key);
				if (item.isPathAttr()) {
					int idx = key.lastIndexOf(".");
					key = key.substring(0, idx);
				}
				List<MDs_Item> l = this.itemsPathMap2.get(key);
				if (l == null) {
					l = new ArrayList<>();
					this.itemsPathMap2.put(key, l);
				}
				this.itemsPathMap2.get(key).add(item);

				if (item.isRefItem()) {
					String refName = item.getRef();
					DsModel m = (DsModel) this.referencesMap.get(refName).getInstance();
					m.setContextPath(key);

				}
			}
		}
	}

//	protected void updateItemsPaths0() {
//		this.itemsPathMap.clear();
//		String prefix = this.recordPath;
//		if (prefix != null && !"".equals(prefix)) {
//			prefix += ".";
//		}
//		for (MDs_Item item : this.items) {
//			if (!item.isPathAbsolute()) {
//				String key = prefix + item.getPath();
//				System.out.println("itempath: " + key);
//				item.setFullPath(key);
//				this.itemsPathMap.put(key, item);
//
//				if (item.isRefItem()) {
//					String refName = item.getRef();
//					DsModel m = (DsModel) this.referencesMap.get(refName).getInstance();
//					m.setContextPath(key);
//
//				}
//			}
//		}
//	}

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

	public MDs_Db getSourceDb() {
		return sourceDb;
	}

	public void setSourceDb(MDs_Db sourceDb) {
		this.sourceDb = sourceDb;
	}

	public MDs_Db getTargetDb() {
		return targetDb;
	}

	public void setTargetDb(MDs_Db targetDb) {
		this.targetDb = targetDb;
	}

	public List<MDs_Item> getItems() {
		return items;
	}

	public void setItems(List<MDs_Item> items) {
		this.items = items;
	}

	public Map<String, MDs_Item> getItemsMap() {
		return itemsMap;
	}

	public void setItemsMap(Map<String, MDs_Item> itemsMap) {
		this.itemsMap = itemsMap;
	}

	public List<MDs_Item> getFieldItems() {
		return fieldItems;
	}

	public void setFieldItems(List<MDs_Item> fieldItems) {
		this.fieldItems = fieldItems;
	}

	public Map<String, MDs_Item> getFieldItemsMap() {
		return fieldItemsMap;
	}

	public void setFieldItemsMap(Map<String, MDs_Item> fieldItemsMap) {
		this.fieldItemsMap = fieldItemsMap;
	}

	public List<MDs_Item> getParamItems() {
		return paramItems;
	}

	public void setParamItems(List<MDs_Item> paramItems) {
		this.paramItems = paramItems;
	}

	public Map<String, MDs_Item> getParamItemsMap() {
		return paramItemsMap;
	}

	public void setParamItemsMap(Map<String, MDs_Item> paramItemsMap) {
		this.paramItemsMap = paramItemsMap;
	}

	public int getListPathTokenCount() {
		return listPathTokenCount;
	}

	public int getRecordPathTokenCount() {
		return recordPathTokenCount;
	}

	public Map<String, List<MDs_Item>> getItemsPathMap2() {
		return itemsPathMap2;
	}

	public List<MDs_Item> getItemsWithValueDefinition() {
		return itemsWithValueDefinition;
	}

	public Map<String, Collection<MDs_Item>> getItemsWithAbsPathDefinitionMap() {
		return itemsWithAbsPathDefinitionMap;
	}

	public Collection<MDs_Item> getItemsWithAbsPathDefinition() {
		return itemsWithAbsPathDefinition;
	}

	public MDs_Customizer getCustomizer(String usedFor) {
		for (MDs_Customizer customizer : this.customizers) {
			if (customizer.getUsedFor().equals(usedFor)) {
				return customizer;
			}
		}
		return null;
	}

	public void addCustomizer(MDs_Customizer customizer) {
		this.customizers.add(customizer);
	}

}

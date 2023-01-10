package ro.aesm.qc.meta.data.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.base.AbstractMetaModel;
import ro.aesm.qc.base.util.StringUtils;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DmModel extends AbstractMetaModel {

	private List<MDm_Customizer> customizers = new ArrayList<MDm_Customizer>();

	private String path;

	private Map<String, PathInfo> pathsMap;

	private List<MDm_List> lists = new ArrayList<MDm_List>();
	private Map<String, MDm_List> listsMap = new HashMap<String, MDm_List>();

	public DmModel(String name) {
		super(name);
	}

	public void addList(MDm_List set) {
		this.lists.add(set);
		this.listsMap.put(set.getName(), set);
	}

	public Map<String, PathInfo> getPathsMap() {
		if (this.pathsMap == null) {
			this.pathsMap = new HashMap<String, PathInfo>();
			String dsPath = this.path;
			if (!StringUtils.isEmpty(dsPath)) {
				dsPath += ".";
			}
			for (MDm_List list : this.lists) {
				if (list.getType().equals(MDm_List.TYPE_MASTER)) {
					this.collectItemsPath(list, dsPath, null, null);
				}
			}
		}
		return pathsMap;
	}

	private void collectItemsPath(MDm_List list, String dsPath, String contextPath, MDm_List parentList) {

		String listPath = ""; // dsPath + list.getPath();
		if (contextPath != null) {
			listPath = contextPath + list.getPath();
		} else {
			listPath = dsPath + list.getPath();
		}

		this.pathsMap.put(listPath, new PathInfo(list, parentList, listPath, list.getName(), null));
		System.out.println("path: " + listPath);
		if (!StringUtils.isEmpty(list.getPath())) {
			listPath += ".";
		}
		for (MDm_Item item : list.getItems()) {
			String itemPath = item.getPath();
			if (itemPath.startsWith("//")) {
				itemPath = itemPath.substring(2);
				this.pathsMap.put(itemPath,
						new PathInfo(list, parentList, itemPath, list.getName(), item.getName(), true));
				System.out.println("path: " + itemPath);
			} else {
				if (item.getType().equals(MDm_Item.TYPE_REF)) {
					itemPath = listPath + item.getPath() + ".";
					MDm_List detailList = this.listsMap.get(item.getRef());
					this.collectItemsPath(detailList, dsPath, itemPath, list);
				} else {
					itemPath = listPath + item.getPath();
					this.pathsMap.put(itemPath,
							new PathInfo(list, parentList, itemPath, list.getName(), item.getName()));
					System.out.println("path: " + itemPath);
				}
			}

		}

	}

	public MDm_Customizer getCustomizer(String usedFor) {
		for (MDm_Customizer customizer : this.customizers) {
			if (customizer.getUsedFor().equals(usedFor)) {
				return customizer;
			}
		}
		return null;
	}

	public void addCustomizer(MDm_Customizer customizer) {
		this.customizers.add(customizer);
	}

	public List<MDm_Customizer> getCustomizers() {
		return customizers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MDm_List> getLists() {
		return lists;
	}

	public Map<String, MDm_List> getListsMap() {
		return listsMap;
	}

	public class PathInfo {

		private MDm_List list;
		private MDm_List parentList;
		private String path;
		private String listName;
		private String itemName;
		private boolean absolutePath;

		public PathInfo(MDm_List list, MDm_List parentList, String path, String listName, String itemName) {
			super();
			this.list = list;
			this.parentList = parentList;
			this.path = path;
			this.listName = listName;
			this.itemName = itemName;
		}

		public PathInfo(MDm_List list, MDm_List parentList, String path, String listName, String itemName,
				boolean absolutePath) {
			super();
			this.list = list;
			this.parentList = parentList;
			this.path = path;
			this.listName = listName;
			this.itemName = itemName;
			this.absolutePath = absolutePath;
		}

		public MDm_List getList() {
			return list;
		}

		public void setList(MDm_List list) {
			this.list = list;
		}

		public MDm_List getParentList() {
			return parentList;
		}

		public void setParentList(MDm_List parentList) {
			this.parentList = parentList;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getListName() {
			return listName;
		}

		public void setListName(String listName) {
			this.listName = listName;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public boolean isAbsolutePath() {
			return absolutePath;
		}

		public void setAbsolutePath(boolean absolutePath) {
			this.absolutePath = absolutePath;
		}

	}
}

package ro.aesm.qc.meta.data.datapack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.base.AbstractMetaModelRoot;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;

public class DpModel extends AbstractMetaModelRoot {

	private String path;

	private List<DsModel> datasets = new ArrayList<>();
	private Map<String, DsModel> datasetsMap = new HashMap<>();

	private List<MDs_Customizer> customizers = new ArrayList<>();

	public DpModel(String name) {
		super(name);
	}

	public void addDataset(DsModel dsModel) {
		this.datasets.add(dsModel);
		this.datasetsMap.put(dsModel.getName(), dsModel);
	}

	@Override
	public void on$ready(boolean propagate) {
		for (DsModel dsModel : this.getDatasets()) {
			if (dsModel.isMainDataset()) {
				dsModel.on$ready(true);
			}
		}
		super.on$ready(propagate);
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

	public List<MDs_Customizer> getCustomizers() {
		return customizers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<DsModel> getDatasets() {
		return datasets;
	}

	public Map<String, DsModel> getDatasetsMap() {
		return datasetsMap;
	}

//
//	public class PathInfo {
//
//		private MDm_List list;
//		private MDm_List parentList;
//		private String path;
//		private String listName;
//		private String itemName;
//		private boolean absolutePath;
//
//		public PathInfo(MDm_List list, MDm_List parentList, String path, String listName, String itemName) {
//			super();
//			this.list = list;
//			this.parentList = parentList;
//			this.path = path;
//			this.listName = listName;
//			this.itemName = itemName;
//		}
//
//		public PathInfo(MDm_List list, MDm_List parentList, String path, String listName, String itemName,
//				boolean absolutePath) {
//			super();
//			this.list = list;
//			this.parentList = parentList;
//			this.path = path;
//			this.listName = listName;
//			this.itemName = itemName;
//			this.absolutePath = absolutePath;
//		}
//
//		public MDm_List getList() {
//			return list;
//		}
//
//		public void setList(MDm_List list) {
//			this.list = list;
//		}
//
//		public MDm_List getParentList() {
//			return parentList;
//		}
//
//		public void setParentList(MDm_List parentList) {
//			this.parentList = parentList;
//		}
//
//		public String getPath() {
//			return path;
//		}
//
//		public void setPath(String path) {
//			this.path = path;
//		}
//
//		public String getListName() {
//			return listName;
//		}
//
//		public void setListName(String listName) {
//			this.listName = listName;
//		}
//
//		public String getItemName() {
//			return itemName;
//		}
//
//		public void setItemName(String itemName) {
//			this.itemName = itemName;
//		}
//
//		public boolean isAbsolutePath() {
//			return absolutePath;
//		}
//
//		public void setAbsolutePath(boolean absolutePath) {
//			this.absolutePath = absolutePath;
//		}
//
//	}
}

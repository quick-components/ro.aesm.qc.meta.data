package ro.aesm.qc.meta.data.dm.processor.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.DmModel.PathInfo;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;

public class Handler4XmlStream extends DefaultHandler {

	protected StringBuilder elementValue = new StringBuilder();
	protected Map<String, String> pathValues = new HashMap<String, String>();
	protected String currentPath = "";

	protected DmModel mmDs;
	protected DefaultDsReaderCustomizer customizer;
	protected Map<String, List<Map<String, Object>>> result;

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		elementValue.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String lName, String qName, Attributes attrs) throws SAXException {

		this.elementValue.setLength(0);
		this.updateCurrentPath(qName);

		PathInfo pathInfo = this.getCurrentPathInfo();

		if (pathInfo != null && pathInfo.getItemName() == null) {
			String listName = pathInfo.getListName();
			if (!this.result.containsKey(listName)) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				this.result.put(pathInfo.getListName(), list);
				this.customizer.onNewList(listName, list, this.getParentRecord(pathInfo));
			}

			Map<String, Object> rec = new HashMap<String, Object>();
			List<Map<String, Object>> recList = this.result.get(listName);
			recList.add(rec);

			// initialize record
			Map<String, Object> parentRec = this.getParentRecord(pathInfo);
			for (MDm_Item item : this.mmDs.getListsMap().get(listName).getItems()) {
				// fields with absolute path values
				if (item.getPath().startsWith("//")) {
					rec.put(item.getName(), this.pathValues.get(item.getPath().substring(2)));
				}
				// with value definitions
				if (item.getValue() != null) {
					this.customizer.initNewRecordItem(item, listName, recList, rec, parentRec);
				}
			}
		}

		for (int i = 0; i < attrs.getLength(); i++) {
			// for each declared attribute check if there is an item reading its value
			String attrName = attrs.getLocalName(i);
			String attrValue = attrs.getValue(i);
			this.updateCurrentPath("@" + attrName);
			this.updateItemValue(attrValue);
			this.updateCurrentPath();
		}
	}

	@Override
	public void endElement(String uri, String lName, String qName) throws SAXException {
		PathInfo pathInfo = this.getCurrentPathInfo();
		if (pathInfo != null) {
			if (pathInfo.getItemName() != null) {
				this.updateItemValue(this.elementValue.toString());
			} else {
				this.updateCurrentPath("");
				this.updateItemValue(this.elementValue.toString());
				this.updateCurrentPath();
				String listName = pathInfo.getListName();
				this.customizer.onNewRecord(listName, this.getCurrentRecord(listName), this.getParentRecord(pathInfo));

			}
		}
		// this.elementValue.setLength(0);
		this.updateCurrentPath();
	}

	protected void updateItemValue(String value) {
		PathInfo pathInfo = this.getCurrentPathInfo();
		if (pathInfo != null && pathInfo.getItemName() != null) {
			if (pathInfo.isAbsolutePath()) {
				this.pathValues.put(pathInfo.getPath(), value);
			} else {
				List<Map<String, Object>> l = this.result.get(pathInfo.getListName());
				Map<String, Object> rec = l.get(l.size() - 1);
				rec.put(pathInfo.getItemName(), value);
			}
		}
	}

	/**
	 * Return the record which is currently processed.
	 * 
	 * @param listName
	 * @return
	 */
	protected Map<String, Object> getCurrentRecord(String listName) {
		List<Map<String, Object>> listResult = this.result.get(listName);
		return listResult.get(listResult.size() - 1);
	}

	/**
	 * Return the parent record. It is available only when a list of type `detail`
	 * is being processed.
	 * 
	 * @param pathInfo
	 * @return
	 */
	protected Map<String, Object> getParentRecord(PathInfo pathInfo) {
		Map<String, Object> parentRec = null;
		if (pathInfo.getList().isDetailList()) {
			String parentListName = pathInfo.getParentList().getName();
			List<Map<String, Object>> parentListResults = this.result.get(parentListName);
			if (parentListResults.size() > 0) {
				parentRec = parentListResults.get(parentListResults.size() - 1);
			}
		}
		return parentRec;
	}

	protected PathInfo getCurrentPathInfo() {
		return this.mmDs.getPathsMap().get(this.currentPath);
	}

	protected String updateCurrentPath() {
		return this.updateCurrentPath(null);
	}

	protected String updateCurrentPath(String newTag) {
		if (newTag != null) {
			if (currentPath.length() > 0) {
				this.currentPath += ".";
			}
			this.currentPath += newTag;
		} else {
			int idx = this.currentPath.lastIndexOf('.');
			if (idx > 0) {
				this.currentPath = this.currentPath.substring(0, idx);
			} else {
				this.currentPath = "";
			}
		}
		return this.currentPath;
	}

	public Map<String, List<Map<String, Object>>> getResult() {
		return this.result;
	}

	public void setResult(Map<String, List<Map<String, Object>>> result) {
		this.result = result;
	}

	public DmModel getMmDs() {
		return mmDs;
	}

	public void setMmDs(DmModel mmDs) {
		this.mmDs = mmDs;
	}

	public DefaultDsReaderCustomizer getCustomizer() {
		return customizer;
	}

	public void setCustomizer(DefaultDsReaderCustomizer customizer) {
		this.customizer = customizer;
	}

}

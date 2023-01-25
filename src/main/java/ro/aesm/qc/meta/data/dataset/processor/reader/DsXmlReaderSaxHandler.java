package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public class DsXmlReaderSaxHandler extends DefaultHandler {

	protected StringBuilder elementValue = new StringBuilder();
	protected DsXmlReaderContext ctx = new DsXmlReaderContext();
	protected PathPointer currentPath = new PathPointer("");
	protected Map<String, DsModel> dsModelsMap;
	protected Map<String, List<Map<String, Object>>> result;
	protected Map<String, DsModel> dsPaths = new HashMap<>();
	protected ValueProvider valueProvider;
	protected Map<String, Collection<MDs_Item>> absPathItemsMap;
	protected int absPathItemsCount;
	protected Map<String, Object> absPathValuesMap;
	private boolean collectAbsPathValues;
	protected IExecutionContext executionContext;
	protected AbstractDsCustomizer customizer;
	protected String itemName;

	public DsXmlReaderSaxHandler(Map<String, DsModel> dsModelsMap, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext, AbstractDsCustomizer customizer, ValueProvider valueProvider) {
		this.dsModelsMap = dsModelsMap;
		this.result = result;
		for (DsModel dsModel : dsModelsMap.values()) {
			dsPaths.put(dsModel.getListPath(), dsModel);
			if (dsModel.getItemsWithAbsPathDefinitionMap() != null) {
				if (this.absPathItemsMap == null) {
					this.absPathItemsMap = new HashMap<>();
					this.collectAbsPathValues = true;
				}
				this.absPathItemsMap.putAll(dsModel.getItemsWithAbsPathDefinitionMap());
				for (Collection<MDs_Item> l : this.absPathItemsMap.values()) {
					this.absPathItemsCount += l.size();
				}
				// System.out.println("Total absPathItemsCount:" +this.absPathItemsCount);
			}
		}
		this.executionContext = executionContext;
		this.absPathValuesMap = new HashMap<>();
		this.valueProvider = valueProvider;
		this.valueProvider.setAbsPathValuesMap(this.absPathValuesMap);
		this.valueProvider.setExecutionContext(this.executionContext);
		this.customizer = customizer;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.elementValue.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String lName, String qName, Attributes attrs) throws SAXException {
		this.elementValue.setLength(0);
		// System.out.println("startElement: " + qName);
		this.currentPath.addToken(qName, attrs);

		if (this.collectAbsPathValues) {
			this.collectAbsPathValue(attrs);
		}

		if (this.ctx.isInRecord()) {
			if (this.ctx.isInItem()) {
				// this should not happen, something wrong
			} else {
				// check if new item match
				this.checkItemStart();
			}
		} else {
			if (this.ctx.isInDataset()) {
				this.checkRecordStart();
			} else {
				this.checkDatasetStart();
			}
		}
	}

	protected void collectAbsPathValue(Attributes attrs) {

		Collection<MDs_Item> items = this.absPathItemsMap.get(currentPath.getPath());
		if (items != null) {
			//// System.out.println("collectAbsPathValue: " + currentPath.getPath());
			for (MDs_Item item : items) {

				if (item.isPathAttr()) {
					// it is an attribute path
					if (attrs != null) {
						// System.out.println("collectAbsPathValue: " + item.getFullPath());
						this.absPathValuesMap.put(item.getFullPath(), attrs.getValue(item.getPathAttrName()));
					}
				} else {
					// it is an field path
					if (attrs == null) {
						// System.out.println("collectAbsPathValue: " + item.getFullPath());
						this.absPathValuesMap.put(item.getFullPath(), this.elementValue.toString());
					}
				}
			}
			// if all values have been collected stop the collection process
			if (this.absPathValuesMap.size() == this.absPathItemsCount) {
				this.collectAbsPathValues = false;
			}
		}
	}

	protected void checkDatasetStart() {
		if (!this.ctx.isInDataset()) {
			DsModel dsModel = this.dsPaths.get(currentPath.getPath());
			if (dsModel != null) {
				this.doDatasetStart(dsModel);
			}
		}
	}

	protected void doDatasetStart(DsModel dsModel) {
		this.ctx.setInDataset(true);
		// this.ctx.setDsPath(this.currentPath.getPath(), this.currentPath.length());
		this.ctx.setDsModel(dsModel);
		List<Map<String, Object>> records = this.result.get(dsModel.getName());
		if (records == null) {
			records = new ArrayList<Map<String, Object>>();
			this.result.put(dsModel.getName(), records);
		}
		this.ctx.setRecords(records);
		this.valueProvider.setItemsWithValueDefinition(dsModel.getItemsWithValueDefinition());
		this.valueProvider.setItemsWithAbsPathDefinition(dsModel.getItemsWithAbsPathDefinition());
		if (this.customizer != null) {
			if (this.ctx.getParentContext() == null) {
				this.customizer.onNewDataset(dsModel.getName(), records, null);
			} else {
				this.customizer.onNewDataset(dsModel.getName(), records, this.ctx.getParentContext().getRecord());
			}
		}
		// System.out.println("inDataset = true");
		this.checkRecordStart();
	}

	protected void checkRecordStart() {
		if (!this.ctx.isInRecord()) {
			if (this.ctx.getDsModel().getRecordPath().equals(this.currentPath.getPath())) {
				this.doRecordStart();
			}
		}
	}

	protected void doRecordStart() {
		this.ctx.setInRecord(true);
		this.ctx.setRecord(new HashMap<String, Object>());
		// System.out.println("inRecord = true");
		if (this.ctx.getParentContext() != null) {
			this.valueProvider.initNewRecord(this.ctx.getRecords(), this.ctx.getRecord(),
					this.ctx.getParentContext().getRecord());
		} else {
			this.valueProvider.initNewRecord(this.ctx.getRecords(), this.ctx.getRecord(), null);
		}
		this.checkItemStart();
	}

	protected void updateValue(Object value, MDs_Item item) {
		// System.out.println("set value " + item.getName() + "=" + value);
		this.ctx.getRecord().put(item.getName(), value);
	}

	protected void checkItemStart() {
		String cp = this.currentPath.getPath();
		// MDs_Item item = this.ctx.getDsModel().getItemsPathMap().get(cp);
		List<MDs_Item> list = this.ctx.getDsModel().getItemsPathMap2().get(cp);
		if (list != null) {
			MDs_Item item = null;
			for (MDs_Item _item : list) {
				// System.out.println("matched item: " + _item.getFullPath());
				if (_item.isPathAttr()) {
					Object val = this.currentPath.getAttrs().getValue(_item.getPathAttrName());
					if (_item.isPathAbsolute()) {
						// collect attribute value into absPathValuesMap
						this.absPathValuesMap.put(_item.getFullPath(), val);
					} else {
						// set record value
						this.updateValue(val, _item);
					}
				} else {
					item = _item;
				}
			}
			if (item != null) {
				this.doItemStart(item);
			}
		}
	}

	protected void doItemStart(MDs_Item item) {
		this.ctx.setInItem(true);
		this.ctx.setItem(item);
		// System.out.println("inItem = true");
		if (item.isRefItem()) {
			// create a new context as it is a nested data-set
			// DsModel crtDsModel = this.ctx.getDsModel();
			// DsModel childDsModel = (DsModel)
			// crtDsModel.getReferencesMap().get(item.getRef()).getInstance();
			DsXmlReaderContext newCtx = new DsXmlReaderContext();
			newCtx.setParentContext(this.ctx);
			this.ctx = newCtx;
			// System.out.println("this.ctx = " + this.ctx);
			this.checkDatasetStart();
			// newCtx.setDsModel(childDsModel);
		}
	}

	@Override
	public void endElement(String uri, String lName, String qName) throws SAXException {
		// System.out.println("endElement: " + qName);
		// this.updateCurrentPath(null);
		if (this.collectAbsPathValues) {
			this.collectAbsPathValue(null);
		}

		this.currentPath.removeLastToken();
		if (this.ctx.isInRecord()) {
			if (this.ctx.isInItem()) {
				this.checkItemEnd();
			} else {
				this.checkRecordEnd();
			}
		} else {
			this.checkDatasetEnd();
		}
	}

	protected void checkItemEnd() {
		if (this.ctx.isInItem()) {
			if (this.currentPath.tokenCount() < this.ctx.getItem().getFullPathTokenCount()) {
				this.doItemEnd();
			}
		}
	}

	protected void doItemEnd() {
		MDs_Item item = this.ctx.getItem();
		if (!item.isPathAttr() && !item.isRefItem()) {
			Object val = this.elementValue.toString();
			if (item.isPathAbsolute()) {
				// collect attribute value into absPathValuesMap
				this.absPathValuesMap.put(item.getFullPath(), val);
			} else {
				// save field value
				this.updateValue(this.elementValue.toString(), item);
			}
		}
		this.ctx.setInItem(false);
		this.ctx.setItem(null);
		// System.out.println("value: " + this.elementValue.toString());
		// System.out.println("inItem (" + item.getName() + ") = false");
		this.checkRecordEnd();
	}

	protected void checkRecordEnd() {
		if (this.ctx.isInRecord()) {
			if (this.currentPath.tokenCount() < this.ctx.getDsModel().getRecordPathTokenCount()) {
				this.doRecordEnd();
			}
		}
	}

	protected void doRecordEnd() {
		if (this.customizer != null) {
			DsModel dsModel = this.ctx.getDsModel();
			if (this.ctx.getParentContext() == null) {
				this.customizer.onNewRecord(dsModel.getName(), this.ctx.getRecords(), this.ctx.getRecord(), null);
			} else {
				this.customizer.onNewRecord(dsModel.getName(), this.ctx.getRecords(), this.ctx.getRecord(),
						this.ctx.getParentContext().getRecord());
			}
		}
		this.ctx.setInRecord(false);
		this.ctx.setRecord(null);
		// System.out.println("inRecord = false");
		this.checkDatasetEnd();
	}

	protected void checkDatasetEnd() {
		if (this.ctx.isInDataset()) {
			if (this.currentPath.tokenCount() < this.ctx.getDsModel().getListPathTokenCount()) {
				this.doDatasetEnd();
			}
		}
	}

	protected void doDatasetEnd() {
		this.ctx.setInDataset(false);
		this.ctx.setDsModel(null);
		// System.out.println("inDataset = false");
		if (this.ctx.getParentContext() != null) {
			this.ctx = this.ctx.getParentContext();
			this.valueProvider.setItemsWithValueDefinition(this.ctx.getDsModel().getItemsWithValueDefinition());
			this.valueProvider.setItemsWithAbsPathDefinition(this.ctx.getDsModel().getItemsWithAbsPathDefinition());
			// System.out.println("this.ctx = " + this.ctx);
			this.checkItemEnd();
		}
	}

}

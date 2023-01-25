package ro.aesm.qc.meta.data.dataset;

import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Node;

import ro.aesm.qc.api.meta.component.IMetaComponentModel;
import ro.aesm.qc.api.meta.component.IMetaComponentParser;
import ro.aesm.qc.base.AbstractMetaParser;
import ro.aesm.qc.meta.data.ConstantsData;
import ro.aesm.qc.meta.data.dataset.enums.DsDbType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemDataType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemValueType;
import ro.aesm.qc.meta.data.dataset.enums.DsType;
import ro.aesm.qc.meta.data.dataset.model.MDs_Db;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;
import ro.aesm.qc.meta.data.dataset.model.MDs_RefParam;

@Component(service = IMetaComponentParser.class)
public class DsParser extends AbstractMetaParser {

	public DsParser() {
		super();
		this.getNamespaceMap().put(this.getNsAlias(), this.getNs());
		this.getNamespaceMap().put("", "*");
	}

	public String qualifiedTag(String tag) {
		return this.getNsAlias() + ":" + tag;
	}

	public String getNsAlias() {
		return ConstantsData.XMLNS_ALIAS;
	}

	public String getNs() {
		return ConstantsData.XMLNS;
	}

	@Override
	public String getTag() {
		return ConstantsData.DATASET_TAG;
	}

	@Override
	public IMetaComponentModel parse(Node node, boolean nested) {

		String type = this.getAttr(node, ConstantsData.DS_TYPE, ConstantsData.DS_TYPE_DEFAULT);
		String name = this.getAttr(node, ConstantsData.DS_NAME);
		String path = this.getAttr(node, ConstantsData.DS_PATH);

		DsModel dsModel = new DsModel(name);
		dsModel.setType(DsType.valueOf(type.toUpperCase()));
		dsModel.setPath(path);
		Node beforeSetNode = this.getNode(node, "before_set");
		if (beforeSetNode != null) {
			dsModel.setBeforeSet(this.getAttr(beforeSetNode, "ref"));
			for (Node refParamNode : this.getNodes(beforeSetNode, "refparam")) {
				MDs_RefParam _mm = this.parseRefParam(refParamNode);
				dsModel.addBeforeSetParam(_mm);
			}
		}

		Node afterSetNode = this.getNode(node, "after_set");
		if (afterSetNode != null) {
			dsModel.setAfterSet(this.getAttr(afterSetNode, "ref"));
			for (Node refParamNode : this.getNodes(afterSetNode, "refparam")) {
				MDs_RefParam _mm = this.parseRefParam(refParamNode);
				dsModel.addAfterSetParam(_mm);
			}
		}

		Node dbNode = this.getNode(node, ConstantsData.DS_SOURCE);
		if (dbNode != null) {
			dsModel.setSourceDb(this.parseDb(dbNode));
		}
		dbNode = this.getNode(node, ConstantsData.DS_TARGET);
		if (dbNode != null) {
			dsModel.setTargetDb(this.parseDb(dbNode));
		}

		Node itemsNode = this.getNode(node, ConstantsData.DS_ITEMS);
		if (itemsNode != null) {
			// defaults = {}
			for (Node itemNode : this.getNodes(itemsNode, ConstantsData.DS_ITEM)) {
				MDs_Item _mm = this.parseItem(itemNode);
				dsModel.addItem(_mm);
			}
		}
		dsModel.on$collectRefrences();
		if (!nested) {
			dsModel.on$ready(true);
		}
		return dsModel;
	}

	protected MDs_Db parseDb(Node node) {
		MDs_Db mm = new MDs_Db();
		mm.setPu(this.getAttr(node, "db", MDs_Db.DEFAULT_PU));
		mm.setPk(this.getAttr(node, "pk", MDs_Db.DEFAULT_PK));
		mm.setIdMode(this.getAttr(node, ConstantsData.DS_DB_IDMODE, ConstantsData.DS_DB_IDMODE_DEFAULT));
		// mm.setIdSequence ( this.getAttr(node, "sequence", ""));
		// mm.pk = mm.pk.replace(" ", "")
		String type = this.getAttr(node, ConstantsData.DS_DB_TYPE, ConstantsData.DS_DB_TYPE_DEFAULT).strip()
				.toUpperCase();
		mm.setType(DsDbType.valueOf(type));

		mm.setName(this.getAttr(node, ConstantsData.DS_DB_NAME));
		mm.setAlias(this.getAttr(node, ConstantsData.DS_DB_ALIAS, ConstantsData.DS_DB_ALIAS_DEFAULT));

		mm.setWhere(this.getValue(node, this.qualifiedTag(ConstantsData.DS_DB_WHERE)));
		mm.setOrderBy(this.getValue(node, this.qualifiedTag(ConstantsData.DS_DB_ORDERBY)));
		mm.setSql(this.getValue(node, this.qualifiedTag(ConstantsData.DS_DB_SQL)));
		mm.setFactory(this.getValue(node, this.qualifiedTag(ConstantsData.DS_DB_FACTORY)));

		return mm;
	}

	protected MDs_Item parseItem(Node itemNode) {
		Node node = itemNode;
		String tmp;
		MDs_Item mm = new MDs_Item();

		tmp = this.getAttr(node, ConstantsData.DS_ITEM_TYPE, ConstantsData.DS_ITEM_TYPE_DEFAULT);
		mm.setType(DsItemType.valueOf(tmp.toUpperCase()));
		tmp = this.getAttr(node, ConstantsData.DS_ITEM_DATATYPE, ConstantsData.DS_ITEM_DATATYPE_DEFAULT);
		mm.setDataType(DsItemDataType.valueOf(tmp.toUpperCase()));

		mm.setName(this.getAttr(node, ConstantsData.DS_ITEM_NAME));
		mm.setPath(this.getAttr(node, ConstantsData.DS_ITEM_PATH, mm.getName()));
		mm.setRef(this.getAttr(node, ConstantsData.DS_ITEM_REF));

		mm.setValue(this.getAttr(node, ConstantsData.DS_ITEM_VALUE));
		if (mm.getValue() != null) {
			tmp = this.getAttr(node, ConstantsData.DS_ITEM_VALUETYPE, ConstantsData.DS_ITEM_VALUETYPE_DEFAULT);
			mm.setValueType(DsItemValueType.valueOf(tmp.toUpperCase()));
		}

		for (Node refParamNode : this.getNodes(node, ConstantsData.DS_ITEM_REFPARAM)) {
			MDs_RefParam _mm = this.parseRefParam(refParamNode);
			mm.addRefParam(_mm);
		}

		Node sourceNode = this.getNode(node, ConstantsData.DS_ITEM_SOURCE);
		if (sourceNode != null) {
			mm.setSourceType(this.getAttr(sourceNode, "type", MDs_Item.DEFAULT_SOURCE_TYPE));
			mm.setSource(this.getValue(sourceNode));
		}
		Node targetNode = this.getNode(node, ConstantsData.DS_ITEM_TARGET);
		if (targetNode != null) {
			mm.setTargetType(this.getAttr(targetNode, "type", MDs_Item.DEFAULT_TARGET_TYPE));
			mm.setTarget(this.getValue(targetNode));
		}

		return mm;
	}

	protected MDs_RefParam parseRefParam(Node refParamNode) {
		Node node = refParamNode;
		MDs_RefParam mm = new MDs_RefParam();
		mm.setTarget(this.getAttr(node, "target"));
		mm.setItemValue(this.getAttr(node, "item_value", null));
		mm.setValue(this.getAttr(node, "value"));
		mm.setOperation(this.getAttr(node, "op", "="));
		// mm.setFn ( this.getAttr(node, "fn"));
		// mm.setExpression ( this.getValue(node));
		return mm;

	}

}

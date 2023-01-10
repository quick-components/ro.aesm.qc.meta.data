package ro.aesm.qc.meta.data.dm;

import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Node;

import ro.aesm.qc.api.meta.component.IMetaComponentModel;
import ro.aesm.qc.api.meta.component.IMetaComponentParser;
import ro.aesm.qc.base.AbstractMetaParser;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;
import ro.aesm.qc.meta.data.dm.model.MDm_Db;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;
import ro.aesm.qc.meta.data.dm.model.MDs_RefParam;

@Component(service = IMetaComponentParser.class)
public class DmParser extends AbstractMetaParser {

	public DmParser() {
		super();
		this.getNamespaceMap().put(this.getNsAlias(), this.getNs());
		this.getNamespaceMap().put("", "*");
	}

	public String qualifiedTag(String tag) {
		return this.getNsAlias() + ":" + tag;
	}

	public String getNsAlias() {
		return ConstantsDm.XMLNS_ALIAS;
	}

	public String getNs() {
		return ConstantsDm.XMLNS;
	}

	@Override
	public String getTag() {
		return ConstantsDm.TAG;
	}

	@Override
	public IMetaComponentModel parse(Node node) {
		String name = this.getAttr(node, "name").trim();
		DmModel mm = new DmModel(name);

		mm.setPath(this.getAttr(node, "path", name));
		for (Node listNode : this.getNodes(node, "list")) {
			MDm_List _mm = this.parseList(listNode);
			mm.addList(_mm);
		}
		for (Node customizerNode : this.getNodes(node, "customizer")) {
			MDm_Customizer _mm = new MDm_Customizer();
			_mm.setUsedFor(this.getAttr(customizerNode, "for"));
			_mm.setClassFqn(this.getAttr(customizerNode, "class"));
			mm.addCustomizer(_mm);
		}
		return mm;
	}

	protected MDm_List parseList(Node node) {
		String name = this.getAttr(node, "name");
		String type = this.getAttr(node, "type", MDm_List.TYPE_MASTER);
		String path = this.getAttr(node, "path", null);

		MDm_List mm = new MDm_List();
		mm.setName(name);
		mm.setType(type);
		mm.setPath(path);
		Node beforeSetNode = this.getNode(node, "before_set");
		if (beforeSetNode != null) {
			mm.setBeforeSet(this.getAttr(beforeSetNode, "ref"));
			for (Node refParamNode : this.getNodes(beforeSetNode, "refparam")) {
				MDs_RefParam _mm = this.parseRefParam(refParamNode);
				mm.addBeforeSetParam(_mm);
			}

		}

		Node afterSetNode = this.getNode(node, "after_set");
		if (afterSetNode != null) {
			mm.setAfterSet(this.getAttr(afterSetNode, "ref"));
			for (Node refParamNode : this.getNodes(afterSetNode, "refparam")) {
				MDs_RefParam _mm = this.parseRefParam(refParamNode);
				mm.addAfterSetParam(_mm);
			}
		}

		Node dbNode = this.getNode(node, "source");
		if (dbNode != null) {
			mm.setSourceDb(this.parseDb(dbNode));
		}
		dbNode = this.getNode(node, "target");
		if (dbNode != null) {
			mm.setTargetDb(this.parseDb(dbNode));
		}

		Node itemsNode = this.getNode(node, "items");
		if (itemsNode != null) {
			// defaults = {}
			for (Node itemNode : this.getNodes(itemsNode, "item")) {
				MDm_Item _mm = this.parseItem(itemNode);
				mm.addItem(_mm);
			}
		}
		return mm;
	}

	protected MDm_Db parseDb(Node node) {
		MDm_Db mm = new MDm_Db();
		mm.setPu(this.getAttr(node, "db", MDm_Db.DEFAULT_PU));
		mm.setPk(this.getAttr(node, "pk", MDm_Db.DEFAULT_PK));
		mm.setIdMode(this.getAttr(node, "id_mode", MDm_Db.DEFAULT_ID_MODE));
		// mm.setIdSequence ( this.getAttr(node, "sequence", ""));
		// mm.pk = mm.pk.replace(" ", "")

		mm.setType(this.getAttr(node, "type", MDm_Db.DEFAULT_TYPE).strip());
		mm.setName(this.getAttr(node, "name"));
		mm.setAlias(this.getAttr(node, "alias", MDm_Db.DEFAULT_ALIAS).strip());

		//mm.setTable(this.getValue(node, this.qualifiedTag("table"), "").strip());
		mm.setWhere(this.getValue(node, this.qualifiedTag("where"), "").strip());
		mm.setOrderBy(this.getValue(node, this.qualifiedTag("order_by"), "").strip());
		mm.setSql(this.getValue(node, this.qualifiedTag("sql"), "").strip());
		mm.setFactory(this.getValue(node, this.qualifiedTag("factory"), "").strip());

		return mm;
	}

	protected MDm_Item parseItem(Node itemNode) {
		Node node = itemNode;
		MDm_Item mm = new MDm_Item();

		mm.setName(this.getAttr(node, "name"));
		mm.setType(this.getAttr(node, "type", MDm_Item.DEFAULT_TYPE));
		mm.setRef(this.getAttr(node, "ref", null));
		mm.setDataType(this.getAttr(node, "data_type", MDm_Item.DEFAULT_DATA_TYPE));

		mm.setPath(this.getAttr(node, "path", mm.getName()));
		mm.setValue(this.getAttr(node, "value", null));

		for (Node refParamNode : this.getNodes(node, "refparam")) {
			MDs_RefParam _mm = this.parseRefParam(refParamNode);
			mm.addRefParam(_mm);
		}

		Node sourceNode = this.getNode(node, "source");
		if (sourceNode != null) {
			mm.setSourceType(this.getAttr(sourceNode, "type", MDm_Item.DEFAULT_SOURCE_TYPE));
			mm.setSource(this.getValue(sourceNode));
		}
		Node targetNode = this.getNode(node, "target");
		if (targetNode != null) {
			mm.setTargetType(this.getAttr(targetNode, "type", MDm_Item.DEFAULT_TARGET_TYPE));
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

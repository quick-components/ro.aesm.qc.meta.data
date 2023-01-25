package ro.aesm.qc.meta.data.datapack;

import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Node;

import ro.aesm.qc.api.meta.component.IMetaComponentModel;
import ro.aesm.qc.api.meta.component.IMetaComponentParser;
import ro.aesm.qc.base.AbstractMetaParser;
import ro.aesm.qc.base.MetaComponentReference;
import ro.aesm.qc.meta.data.ConstantsData;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.DsParser;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;

@Component(service = IMetaComponentParser.class)
public class DpParser extends AbstractMetaParser {

	public DpParser() {
		super();
		this.getNamespaceMap().put(this.getNsAlias(), this.getNs());
		this.getNamespaceMap().put("", "*");
	}

	public String getNsAlias() {
		return ConstantsData.XMLNS_ALIAS;
	}

	public String getNs() {
		return ConstantsData.XMLNS;
	}

	@Override
	public String getTag() {
		return ConstantsData.DATAPACK_TAG;
	}

	@Override
	public IMetaComponentModel parse(Node node, boolean nested) {
		String name = this.getAttr(node, "name").trim();
		DpModel dpModel = new DpModel(name);

		dpModel.setPath(this.getAttr(node, "path", name));

		DsParser dsParser = new DsParser();

		// parse
		for (Node listNode : this.getNodes(node, "dataset")) {
			DsModel dsModel = (DsModel) dsParser.parse(listNode, true);
			if (dsModel.isMainDataset()) {
				dsModel.setContextPath(dpModel.getPath());
			}
			dpModel.addDataset(dsModel);
		}

		// link
		for (DsModel dsModel : dpModel.getDatasets()) {
			for (MetaComponentReference ref : dsModel.getReferences()) {
				DsModel refDsModel = dpModel.getDatasetsMap().get(ref.getName());
				ref.setInstance(refDsModel);
			}
		}

		// customizers
		for (Node customizerNode : this.getNodes(node, "customizer")) {
			MDs_Customizer custModel = new MDs_Customizer();
			custModel.setUsedFor(this.getAttr(customizerNode, "for"));
			custModel.setClassFqn(this.getAttr(customizerNode, "class"));
			dpModel.addCustomizer(custModel);
		}

		dpModel.on$collectRefrences();
		if (!nested) {
			dpModel.on$ready(true);
		}

		return dpModel;
	}

}

package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;

public class DsXmlReader extends AbstractDsReader {

	public void read(DsModel dsModel, InputStream inputStream, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		ValueProvider valueProvider = this.createValueProvider(dsModel, executionContext);
		AbstractDsCustomizer customizer = this.createDsCustomizer(dsModel, executionContext, MDs_Customizer.XML_READER);

		Map<String, DsModel> dsModelsMap = new HashMap<>();
		dsModelsMap.put(dsModel.getName(), dsModel);

		DsXmlReaderSaxHandler hd = new DsXmlReaderSaxHandler(dsModelsMap, result, executionContext, customizer,
				valueProvider);
		saxParser.parse(inputStream, hd);
	}
}

package ro.aesm.qc.meta.data.dm.processor.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;

public class DmXmlReader extends AbstractDsReader {

	public Map<String, List<Map<String, Object>>> read(DmModel metaModel, InputStream inputStream,
			IExecutionContext executionContext) throws Exception {
		DmModel _dsModel = metaModel;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		Handler4XmlStream hd = new Handler4XmlStream();
		MDm_Customizer mmCustomizer = _dsModel.getCustomizer(MDm_Customizer.XML_READER);
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
		DefaultDsReaderCustomizer customizer = null;
		if (mmCustomizer != null) {
			customizer = (DefaultDsReaderCustomizer) Class.forName(mmCustomizer.getClassFqn()).getDeclaredConstructor()
					.newInstance();
		} else {
			customizer = new DefaultDsReaderCustomizer();
		}
		customizer.setMmDs(_dsModel);
		customizer.setResult(result);
		customizer.setExecutionContext(executionContext);

		hd.setMmDs(_dsModel);
		hd.setResult(result);
		hd.setCustomizer(customizer);

		saxParser.parse(inputStream, hd);

		return result;
	}

}

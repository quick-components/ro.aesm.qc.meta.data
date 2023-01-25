package ro.aesm.qc.meta.data.datapack.processor.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.datapack.DpModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;
import ro.aesm.qc.meta.data.dataset.processor.reader.AbstractDsCustomizer;
import ro.aesm.qc.meta.data.dataset.processor.reader.DsXmlReaderSaxHandler;
import ro.aesm.qc.meta.data.dataset.processor.reader.ValueProvider;

public class DpXmlReader extends AbstractDpReader {

	@Override
	public void read(DpModel dpModel, InputStream inputStream, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext) throws Exception {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		ValueProvider valueProvider = this.createValueProvider(dpModel, executionContext);
		AbstractDsCustomizer customizer = this.createDsCustomizer(dpModel, executionContext, MDs_Customizer.XML_READER);

		DsXmlReaderSaxHandler hd = new DsXmlReaderSaxHandler(dpModel.getDatasetsMap(), result, executionContext,
				customizer, valueProvider);
		saxParser.parse(inputStream, hd);

	}

}

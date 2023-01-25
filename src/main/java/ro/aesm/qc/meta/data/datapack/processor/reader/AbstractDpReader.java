package ro.aesm.qc.meta.data.datapack.processor.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.datapack.DpModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;
import ro.aesm.qc.meta.data.dataset.processor.reader.AbstractDsCustomizer;
import ro.aesm.qc.meta.data.dataset.processor.reader.ValueProvider;

public abstract class AbstractDpReader {

	public abstract void read(DpModel dpModel, InputStream inputStream, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext) throws Exception;

	protected ValueProvider createValueProvider(DpModel dpModel, IExecutionContext executionContext) {
		ValueProvider valueProvider = new ValueProvider();
		valueProvider.setExecutionContext(executionContext);
		return valueProvider;
	}

	protected AbstractDsCustomizer createDsCustomizer(DpModel dpModel, IExecutionContext executionContext,
			String customizerType) throws Exception {
		AbstractDsCustomizer customizer = null;
		MDs_Customizer customizerModel = dpModel.getCustomizer(customizerType);
		if (customizerModel != null) {
			customizer = (AbstractDsCustomizer) Class.forName(customizerModel.getClassFqn()).getDeclaredConstructor()
					.newInstance();
			customizer.setExecutionContext(executionContext);
		}
		return customizer;
	}

}

package ro.aesm.qc.meta.data.dataset.processor.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;

public abstract class AbstractDsReader {

	public abstract void read(DsModel dsModel, InputStream inputStream, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext) throws Exception;

	protected ValueProvider createValueProvider(DsModel dsModel, IExecutionContext executionContext) {
		ValueProvider valueProvider = new ValueProvider();
		valueProvider.setExecutionContext(executionContext);
		valueProvider.setItemsWithValueDefinition(dsModel.getItemsWithValueDefinition());
		valueProvider.setItemsWithAbsPathDefinition(dsModel.getItemsWithAbsPathDefinition());
		return valueProvider;
	}

	protected AbstractDsCustomizer createDsCustomizer(DsModel dsModel, IExecutionContext executionContext,
			String customizerType) throws Exception {
		AbstractDsCustomizer customizer = null;
		MDs_Customizer customizerModel = dsModel.getCustomizer(customizerType);
		if (customizerModel != null) {
			customizer = (AbstractDsCustomizer) Class.forName(customizerModel.getClassFqn()).getDeclaredConstructor()
					.newInstance();
			customizer.setExecutionContext(executionContext);
		}
		return customizer;
	}

}

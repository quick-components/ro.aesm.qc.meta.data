package ro.aesm.qc.meta.data.dm.processor.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dm.DmModel;

public abstract class AbstractDsReader {

	public abstract Map<String, List<Map<String, Object>>> read(DmModel metaModel, InputStream inputStream,
			IExecutionContext executionContext) throws Exception;
}

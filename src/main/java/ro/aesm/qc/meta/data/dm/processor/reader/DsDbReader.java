package ro.aesm.qc.meta.data.dm.processor.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.base.db.Db;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;
import ro.aesm.qc.meta.data.dm.model.MDm_List;
import ro.aesm.qc.meta.data.dm.processor.sql.DsQueryBuilder;

public class DsDbReader {

	public Map<String, List<Map<String, Object>>> read(DmModel metaModel, DataSource dataSource,
			IExecutionContext executionContext) throws Exception {

		DmModel _dsModel = metaModel;
		 
		MDm_Customizer mmCustomizer = _dsModel.getCustomizer(MDm_Customizer.DB_READER);
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

		return result;
	}
	
	
	protected void read(DmModel metaModel, String dataSetName, Map<String, List<Map<String, Object>>> result ) {
		DsQueryBuilder qb = new DsQueryBuilder();
		MDm_List ds = metaModel.getListsMap().get(dataSetName);
		String sql = qb.buildSql(ds);
		List<String> params =  Db.translateSql(sql);
		String translatedSql  = params.get(0);
		
	}

	
	
	
	
	
	
	
	
	
	
}

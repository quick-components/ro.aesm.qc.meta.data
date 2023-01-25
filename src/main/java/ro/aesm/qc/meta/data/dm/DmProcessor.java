package ro.aesm.qc.meta.data.dm;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.aesm.qc.api.Constants;
import ro.aesm.qc.api.base.IDbcp;
import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.base.AbstractMetaProcessor;
import ro.aesm.qc.base.db.Db;
import ro.aesm.qc.meta.data.dm.model.MDm_List;
import ro.aesm.qc.meta.data.dm.processor.reader.AbstractDsReader;
import ro.aesm.qc.meta.data.dm.processor.reader.DsCsvReader;
import ro.aesm.qc.meta.data.dm.processor.reader.DmXmlReader;
import ro.aesm.qc.meta.data.dm.processor.sql.DsInsertBuilder;

public class DmProcessor extends AbstractMetaProcessor {

	protected IDbcp dbcp;

	public Map<String, List<Map<String, Object>>> parseDataStream(DmModel dsModel, InputStream dataInputStream,
			String format, IExecutionContext executionContext) throws Exception {
		AbstractDsReader dsReader = null;
//		if (format.equals(Constants.JSON)) {
//			dsReader = new DsJsonReader();
//		} else
		if (format.equals(Constants.XML)) {
			dsReader = new DmXmlReader();
		} else if (format.equals(Constants.CSV)) {
			dsReader = new DsCsvReader();
		} else {
			throw new Exception(format + " is not a supported format to parse dataset streams.");
		}
		return dsReader.read(dsModel, dataInputStream, executionContext);
	}

	public void save(DmModel dsModel, Map<String, List<Map<String, Object>>> data) throws Exception {
		Map<String, Connection> dbConnectionsMap = new HashMap<String, Connection>();
		try {
			for (MDm_List mmList : ((DmModel) dsModel).getLists()) {
				List<Map<String, Object>> listData = data.get(mmList.getName());
				String pu = mmList.getTargetDb().getPu();
				if (!dbConnectionsMap.containsKey(pu)) {
					dbConnectionsMap.put(pu, this.dbcp.getConnection(pu));
				}
				this.insert(mmList, listData, dbConnectionsMap.get(pu));
			}
			// TODO transaction management rules
			for (Connection conn : dbConnectionsMap.values()) {
				conn.commit();
			}
		} finally {
			for (Connection conn : dbConnectionsMap.values()) {
				conn.close();
			}
		}
	}

	public void insert(MDm_List mmList, List<Map<String, Object>> data, Connection conn) throws Exception {
		DsInsertBuilder ib = new DsInsertBuilder();
		String sqli = ib.buildSql(mmList);
		System.out.println(sqli);
		List<String> iprm = Db.translateSql(sqli);
		System.out.println(iprm.get(0));

		try (PreparedStatement stmt = conn.prepareStatement(iprm.get(0))) {
			// execute query
			for (Map<String, Object> rec : data) {
				for (int k = 1; k < iprm.size(); k++) {
					stmt.setObject(k, rec.get(iprm.get(k)));
				}
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}

	public IDbcp getDbcp() {
		return dbcp;
	}

	public void setDbcp(IDbcp dbcp) {
		this.dbcp = dbcp;
	}

}

package ro.aesm.qc.meta.data.dm.processor.reader;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.base.db.Db;
import ro.aesm.qc.base.db.Dbcp;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;
import ro.aesm.qc.meta.data.dm.processor.sql.DsQueryBuilder;
import ro.aesm.qc.meta.data.dm.processor.writer.DmXmlWriter;

public class DmDbReader {

	public Map<String, List<Map<String, Object>>> read(DmModel dmModel, IExecutionContext executionContext)
			throws Exception {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DmXmlWriter writer = new DmXmlWriter(os);
		DmModel _dsModel = dmModel;

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

		for (MDm_List dsModel : dmModel.getLists()) {
			if (!dsModel.isDetailList()) {
				this.read(dmModel, dsModel, executionContext, writer);
			}
		}
		writer.close();
		System.out.println(os.toString());
		return result;
	}

	protected void read(DmModel metaModel, MDm_List dsModel, IExecutionContext executionContext, DmXmlWriter writer)
			throws Exception {
		DsQueryBuilder qb = new DsQueryBuilder();
		// MDm_List ds = metaModel.getListsMap().get(dataSetName);
		String sql = qb.buildSql(dsModel);
		List<String> params = Db.translateSql(sql);
		String translatedSql = params.get(0);

		Dbcp dbcp = new Dbcp();
		// Connection conn = dbcp.getConnection("main");

		DataSource dataSource = dbcp.getDataSource("main");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {

			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			rset = stmt.executeQuery(translatedSql);

			int numcols = rset.getMetaData().getColumnCount();
			MDm_List prevDs = null;
			while (rset.next()) {
				String[] recordTags = writer.onRecordStart(dsModel); //onRecordStart
				MDm_Item prevItem = null;

				for (MDm_Item item : dsModel.getItems()) {
					if (prevItem != null) {
						writer.onItemEnd(prevItem, item);
					}
					//System.out.println(item.getPath());
					writer.onItemStart(item, prevItem);
					writer.onItemValue(item, rset.getString(item.getName()));
//					String[] itemTags = writer.beginItem(item);
//					writer.value(item, rset);
//					writer.endItem(itemTags);
					prevItem = item;
				}
				writer.onItemEnd(prevItem, null);
				writer.onRecordEnd(recordTags);  //onRecordEnd
//				for (int i = 1; i <= numcols; i++) {
//					System.out.print("\t" + rset.getString(i));
//				}
//				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

	}

}

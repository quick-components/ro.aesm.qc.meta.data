package ro.aesm.qc.meta.data.dm.processor.reader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

import ro.aesm.qc.api.base.IExecutionContext;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Customizer;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DsCsvReader extends AbstractDsReader {

	public Map<String, List<Map<String, Object>>> read(DmModel metaModel, InputStream inputStream,
			IExecutionContext executionContext) throws Exception {
		DmModel _dsModel = metaModel;
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
		MDm_Customizer mmCustomizer = _dsModel.getCustomizer(MDm_Customizer.XML_READER);

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

		MDm_List mmList = _dsModel.getLists().get(0);
		String listName = mmList.getName();
		List<Map<String, Object>> recList = new ArrayList<Map<String, Object>>();
		result.put(mmList.getName(), recList);

		customizer.onNewList(mmList.getName(), recList, null);

		// try (Reader reader = Files.newBufferedReader(path)) {
		try (Reader reader = new InputStreamReader(inputStream)) {
			try (CSVReader csvReader = new CSVReader(reader)) {
				String[] line = csvReader.readNext();
				List<String> csvFieldNames = Arrays.asList(line);
				// prepare item index values
				if (line != null) {
					for (MDm_Item item : mmList.getItems()) {
						String path = item.getPath();
						if (path != null) {
							try {
								item.setIndex(Integer.parseInt(path));
							} catch (Exception e) {
								item.setIndex(csvFieldNames.indexOf(path));
							}
						} else {
							if (item.getValue() == null) {
								item.setIndex(csvFieldNames.indexOf(item.getName()));
							}
						}
					}
				}
				while ((line = csvReader.readNext()) != null) {
					Map<String, Object> rec = new HashMap<String, Object>();
					recList.add(rec);
					for (MDm_Item item : mmList.getItems()) {

						// with value definitions
						if (item.getValue() != null) {
							customizer.initNewRecordItem(item, listName, recList, rec, null);
						} else {
							if (item.getIndex() >= 0) {
								rec.put(item.getName(), line[item.getIndex()]);
							}
						}
					}
					customizer.onNewRecord(listName, rec, null);
				}
			}
		}
		return result;
	}
}

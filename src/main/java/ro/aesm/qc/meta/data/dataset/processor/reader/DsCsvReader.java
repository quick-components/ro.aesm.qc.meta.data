package ro.aesm.qc.meta.data.dataset.processor.reader;

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
import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Customizer;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public class DsCsvReader extends AbstractDsReader {

	public void read(DsModel dsModel, InputStream inputStream, Map<String, List<Map<String, Object>>> result,
			IExecutionContext executionContext) throws Exception {

		ValueProvider valueProvider = this.createValueProvider(dsModel, executionContext);
		AbstractDsCustomizer customizer = this.createDsCustomizer(dsModel, executionContext, MDs_Customizer.CSV_READER);

		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		result.put(dsModel.getName(), records);

		if (customizer != null) {
			customizer.onNewDataset(dsModel.getName(), records, null);
		}

		// try (Reader reader = Files.newBufferedReader(path)) {
		try (Reader reader = new InputStreamReader(inputStream)) {
			try (CSVReader csvReader = new CSVReader(reader)) {
				String[] line = csvReader.readNext();
				List<String> csvFieldNames = Arrays.asList(line);
				// prepare item index values
				if (line != null) {
					for (MDs_Item item : dsModel.getItems()) {
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
					records.add(rec);
					for (MDs_Item item : dsModel.getItems()) {

						// with value definitions
						if (item.getValue() != null) {
							// customizer.initNewRecordItem(item, listName, records, rec, null);
							valueProvider.initNewRecord(records, rec, null);
						} else {
							if (item.getIndex() >= 0) {
								rec.put(item.getName(), line[item.getIndex()]);
							}
						}
					}
					if (customizer != null) {
						customizer.onNewRecord(dsModel.getName(), records, rec, null);
					}
				}
			}
		}

	}
}

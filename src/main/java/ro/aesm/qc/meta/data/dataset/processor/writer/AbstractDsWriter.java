package ro.aesm.qc.meta.data.dataset.processor.writer;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import ro.aesm.qc.meta.data.dataset.DsModel;
import ro.aesm.qc.meta.data.dataset.model.MDs_Item;

public abstract class AbstractDsWriter {

	protected BufferedOutputStream buffer;

	protected String format;

	public AbstractDsWriter(OutputStream outputStream) {
		this.buffer = new BufferedOutputStream(outputStream);
	}

	public abstract String[] onRecordStart(DsModel dsModel) throws Exception;

	public abstract void onRecordEnd(String[] tags) throws Exception;

	public abstract void onItemStart(MDs_Item crtItem, MDs_Item prevItem) throws Exception;

	public abstract void onItemEnd(MDs_Item crtItem, MDs_Item nextItem) throws Exception;

	public void onItemValue(MDs_Item item, String value) throws Exception {
		this.write(value);
	}

	protected void write(String s) throws Exception {
		if (s != null) {
			this.buffer.write(s.getBytes());
		}
	}

	protected void writeNL() throws Exception {
		this.buffer.write("\n".getBytes());
	}

	public void close() throws Exception {
		buffer.flush();
		buffer.close();
	}
}

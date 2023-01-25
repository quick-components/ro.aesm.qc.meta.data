package ro.aesm.qc.meta.data.dm.processor.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import ro.aesm.qc.base.util.QcArrayUtils;
import ro.aesm.qc.base.util.StringUtils;
import ro.aesm.qc.meta.data.dm.DmModel;
import ro.aesm.qc.meta.data.dm.model.MDm_Item;
import ro.aesm.qc.meta.data.dm.model.MDm_List;

public class DmXmlWriter {

//	protected List<String> stack;

	private boolean format = true;
	private int depth = 0;
	private boolean lastDirectiveOpen = false;

	protected BufferedOutputStream buffer;
	// protected BufferedWriter writer;

	public DmXmlWriter(OutputStream outputStream) {
		this.buffer = new BufferedOutputStream(outputStream);
	}

	// --------------------- start element -------------------------------

	public void onDocumentStart(DmModel dmModel) throws Exception {
		String path = dmModel.getPath();
		if (!StringUtils.isEmpty(path)) {
			String[] parts = path.split("\\.");
			this.openTags(parts);
		}
	}
	 
	public String[] onRecordStart(MDm_List ds) throws Exception {
		this.closeDirective();
		String path = ds.getPath();
		if (!StringUtils.isEmpty(path)) {
			String[] parts = path.split("\\.");
			int cnt = 0;
			int len = parts.length;
			for (String part : parts) {
				cnt++;
				if (cnt == len) {
					this.openTag(part, false);
				} else {
					this.openTag(part);
				}
			}
			return parts;
		}
		return null;
	}

	public void onItemStart(MDm_Item crtItem, MDm_Item prevItem) throws Exception {
		if (crtItem == null) {
			return;
		}
		//System.out.println("onItemStart: " + crtItem.getPath());
		String[] crtPathParts = crtItem.getPath().split("\\.");
		int crtLen = crtPathParts.length;
		// close RecordStart directive
		if (this.lastDirectiveOpen) {
			if (prevItem == null) {
				// if it is first item
				if (crtLen > 1 || !crtPathParts[0].startsWith("@")) {
					this.closeDirective();
				}
			} else {
				if (!crtItem.isPathAttr()) {
					this.closeDirective();
				} else {
					// previous is also attribute
					// close if length or tags path is different
					String[] prevPathParts = prevItem.getPath().split("\\.");
					int prevLen = prevPathParts.length;
					if (crtLen != prevLen) {
						this.closeDirective();
					} else {
						for (int i = 0; i <= crtLen - 2; i++) {
							if (!crtPathParts[i].equals(prevPathParts[i])) {
								this.closeDirective();
								break;
							}
						}
					}

				}
			}
		}

		if (crtLen == 1) {
			this.openTags(crtPathParts);
			return;
		}

		if (prevItem == null) { // first
			this.openTags(crtPathParts);
		} else {
			String[] prevPathParts = prevItem.getPath().split("\\.");
			// int prevLen = prevPathParts.length;
			this.openTags(this.collectTagsToOpen(crtPathParts, prevPathParts));

		}
	}

	protected String[] collectTagsToOpen(String[] crt, String[] prev) {
		int len = Math.min(crt.length, prev.length);
		int from = len - 1;
		for (int i = 0; i < len; i++) {
			if (!crt[i].equals(prev[i])) {
				from = i;
				break;
			}
		}
		return QcArrayUtils.copyRange(crt, from, crt.length - 1);
		// throw new IllegalArgumentException("Duplicate paths found " +
		// String.join(".", crt));
	}

	protected void openTags(String[] tags) throws Exception {
		if (tags != null) {
			int len = tags.length;
			String lastTag = tags[len - 1];
			int beforeLastIndex = -1;
			if (lastTag.startsWith("@")) {
				beforeLastIndex = len - 2;
			}
			for (int i = 0; i < len; i++) {
				if (i == beforeLastIndex) {
					this.openTag(tags[i], false);
				} else {
					this.openTag(tags[i]);
				}
			}
		}
	}

	protected void openTag(String tag) throws Exception {
		this.openTag(tag, true);
	}

	protected void openTag(String tag, boolean closeDirective) throws Exception {
		//System.out.println("_openTag: " + tag);
		if (tag.startsWith("@")) {
			this.write(" " + tag.substring(1) + "=\"");
		} else {
			this.depth++;
			this.write("<" + tag);
			this.lastDirectiveOpen = true;
			if (closeDirective) {
				this.write(">");
				this.lastDirectiveOpen = false;
			}

		}
	}

	// --------------------- end element -------------------------------

	public void onDocumentEnd(DmModel dmModel) throws Exception {
		String path = dmModel.getPath();
		if (!StringUtils.isEmpty(path)) {
			String[] parts = path.split("\\.");
			QcArrayUtils.reverse(parts);
			this.closeTags(parts);
		}
	}
	
	public void onRecordEnd(String[] tags) throws Exception {
		for (int i = tags.length - 1; i >= 0; i--) {
			this.closeTag(tags[i]);
		}
	}

	public void onItemEnd(MDm_Item crtItem, MDm_Item nextItem) throws Exception {
		if (crtItem == null) {
			return;
		}
		//System.out.println("onItemEnd: " + crtItem.getPath());
		String[] crtPathParts = crtItem.getPath().split("\\.");
		int crtLen = crtPathParts.length;

		if (crtLen == 1) {
			this.closeTags(crtPathParts);
			if (nextItem == null) {
				this.closeDirective();
			}
			return;
		}

		if (nextItem != null) {
			String[] nextPathParts = nextItem.getPath().split("\\.");
			int nextLen = nextPathParts.length;

			if (crtLen > nextLen) {
				this.closeTags(QcArrayUtils.copyRange(crtPathParts, crtLen - 1, nextLen));
			}
			this.closeTags(this.collectTagsToClose(crtPathParts, nextPathParts, nextLen - 1));
		} else { // last
			if (crtItem.isPathAttr()) {
				this.closeTag(crtPathParts[crtPathParts.length - 1]);
				this.closeDirective();
				this.closeTags(QcArrayUtils.copyRange(crtPathParts, crtPathParts.length - 2, 0));
			} else {
				this.closeTags(QcArrayUtils.copyRange(crtPathParts, crtPathParts.length - 1, 0));
			}
		}
	}

	protected String[] collectTagsToClose(String[] crt, String[] next, int to) {
		for (int i = 0; i <= to; i++) {
			if (!crt[i].equals(next[i])) {
				return QcArrayUtils.copyRange(crt, to, i);
			}
		}
		throw new IllegalArgumentException("Duplicate paths found " + String.join(".", crt));
	}

	protected void closeTags(String[] tags) throws Exception {
		if (tags != null) {
			for (String tag : tags) {
				this.closeTag(tag);
			}
		}
	}

	protected void closeTag(String tag) throws Exception {
		//System.out.tem.out.println("_closeTag: " + tag);
		if (tag.startsWith("@")) {
			this.write("\"");
		} else {
			this.depth++;
			this.write("</" + tag + ">");
			if (this.format) {
				this.writeNL();
			}
		}
	}

	// --------------------- element value -------------------------------

	public void onItemValue(MDm_Item item, String value) throws Exception {
		this.write(value );
	}

	protected void closeDirective() throws Exception {
		if (this.lastDirectiveOpen) {
			this.write(">");
			this.lastDirectiveOpen = false;
		}
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
	// ===============================================
	// ===============================================
	// ===============================================

//	public String[] beginRecord(MDm_List ds) throws Exception {
//		this.closeDirective();
//		String path = ds.getPath();
//		if (!StringUtils.isEmpty(path)) {
//			String[] parts = path.split("\\.");
//			int cnt = 0;
//			int len = parts.length;
//			for (String part : parts) {
//				cnt++;
//				if (cnt == len) {
//					this.openTag(part, false);
//				} else {
//					this.openTag(part);
//				}
//			}
//			return parts;
//		}
//		return null;
//	}
//
//	public void endRecord(String[] tags) throws Exception {
//		for (int i = tags.length - 1; i >= 0; i--) {
//			this.closeTag(tags[i]);
//		}
//	}

//	public String[] beginItem(MDm_Item item) throws Exception {
//
//		String path = item.getPath();
//		String[] parts = path.split("\\.");
//		if (parts.length == 1 && parts[0].startsWith("@")) {
//			this.write(" " + parts[0].substring(1) + "=\"");
//			return new String[] { "\"" };
//		} else {
//			if (this.lastDirectiveOpen) {
//				this.write(">");
//				this.lastDirectiveOpen = false;
//			}
//
//			for (String part : parts) {
//				this.openTag(part);
//			}
//		}
//		return parts;
//	}

//	public void value(MDm_Item item, ResultSet rs) throws Exception {
//		this.write(rs.getString(item.getName()));
//	}

//	public void endItem(String[] tags) throws Exception {
//		for (int i = tags.length - 1; i >= 0; i--) {
//			this.closeTag(tags[i]);
//		}
//	}

//	protected void openTag(String tag) throws Exception {
//		this.openTag(tag, true);
//	}
//
//	protected void openTag(String tag, boolean closeDirective) throws Exception {
//		// stack.add(tag);
//		this.level++;
//		this.write("<" + tag);
//
//		this.lastDirectiveOpen = true;
//		if (closeDirective) {
//			this.write(">");
//			this.lastDirectiveOpen = false;
//		}
//	}
//
//	protected void closeTag(String tag) throws Exception {
//		if (!StringUtils.isEmpty(tag)) {
//			if (this.lastDirectiveOpen) {
//				// close an attribute
//				this.write(tag);
//			} else {
//				this.write("</" + tag + ">");
//				this.level--;
//			}
//
//		}
//	}

}

package ro.aesm.qc.meta.data.dataset.processor.reader;

import org.xml.sax.Attributes;

public class PathPointer {

	private Attributes attrs;
	private String path;
	private int tokenCount;

	public PathPointer(String path) {
		if (path == null || path.equals("")) {
			this.path = "";
		} else {
			this.path = path;
			String[] t = this.path.split("\\.");
			tokenCount = t.length;
		}
	}

	public void addToken(String token, Attributes attrs) {
		if (token == null) {
			return;
		}
		if (path.length() > 0) {
			this.path += "." + token;
		} else {
			this.path = token;
		}
		this.tokenCount++;
		// this.tokens.add(token);
		this.attrs = attrs;
		// System.out.println("PathPointer: "+this.path);
	}

	public void removeLastToken() {
		int idx = this.path.lastIndexOf('.');
		if (idx > 0) {
			this.path = this.path.substring(0, idx);
		} else {
			this.path = "";
		}
		tokenCount--;
		// this.tokens.remove(this.tokens.size() - 1);
		this.attrs = null;
		// System.out.println("PathPointer: "+this.path);
	}

	public int tokenCount() {
		return this.tokenCount;
	}

	public String getPath() {
		return path;
	}

	public Attributes getAttrs() {
		return attrs;
	}

}

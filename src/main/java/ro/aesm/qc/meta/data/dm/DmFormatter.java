package ro.aesm.qc.meta.data.dm;

 

public class DmFormatter {

	public String format(DmModel model, String resourceNs, String componentNs) {
		StringBuilder sb = new StringBuilder();
		String _resourceNs = (resourceNs == null) ? "" : resourceNs + ":";
		this.begin(sb, _resourceNs, "dataset");
		DmModel _model = (DmModel) model;
//		for () {
//			
//		}
		this.end(sb, _resourceNs, "dataset");
		return null;
	}

	protected void begin(StringBuilder sb, String resourceNs, String tag) {

		sb.append("<" + resourceNs + tag);
	}

	protected void end(StringBuilder sb, String resourceNs, String tag) {

		sb.append(resourceNs + tag + ">");
	}
}

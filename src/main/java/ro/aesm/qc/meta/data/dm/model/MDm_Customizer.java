package ro.aesm.qc.meta.data.dm.model;

public class MDm_Customizer {

	public static final String XML_READER = "xml_reader";
	public static final String DB_READER = "db_reader";

	private String usedFor;
	private String classFqn;

	public String getUsedFor() {
		return usedFor;
	}

	public void setUsedFor(String usedFor) {
		this.usedFor = usedFor;
	}

	public String getClassFqn() {
		return classFqn;
	}

	public void setClassFqn(String classFqn) {
		this.classFqn = classFqn;
	}

}

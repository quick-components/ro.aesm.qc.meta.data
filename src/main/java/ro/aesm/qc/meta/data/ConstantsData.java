package ro.aesm.qc.meta.data;

import ro.aesm.qc.meta.data.dataset.enums.DsDbType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemDataType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemType;
import ro.aesm.qc.meta.data.dataset.enums.DsItemValueType;
import ro.aesm.qc.meta.data.dataset.enums.DsType;

public class ConstantsData {
	public static final String XMLNS_ALIAS = "d";
	public static final String XMLNS = "https://qc.aesm.ro/meta/data";

	public static final String DATASET_TAG = "dataset";
	public static final String DATAPACK_TAG = "datapack";

	// ================= dataset ======================

	public static final String DS_SOURCE = "source";
	public static final String DS_TARGET = "target";
	public static final String DS_ITEMS = "items";
	public static final String DS_ITEM = "item";
	public static final String DS_TYPE = "type";
	public static final String DS_NAME = "name";
	public static final String DS_PATH = "path";

	public static final String DS_TYPE_DEFAULT = DsType.MAIN.toString();

	// db source or target
	public static final String DS_DB_TYPE = "type";
	public static final String DS_DB_NAME = "name";
	public static final String DS_DB_ALIAS = "alias";
	public static final String DS_DB_WHERE = "where";
	public static final String DS_DB_ORDERBY = "order_by";
	public static final String DS_DB_SQL = "sql";
	public static final String DS_DB_FACTORY = "factory";
	public static final String DS_DB_IDMODE = "id_mode";

	public static final String DS_DB_TYPE_DEFAULT = DsDbType.TABLE.toString();
	public static final String DS_DB_ALIAS_DEFAULT = "_mt";
	public static final String DS_DB_IDMODE_DEFAULT = "auto";

	// item
	public static final String DS_ITEM_TYPE = "type";
	public static final String DS_ITEM_NAME = "name";
	public static final String DS_ITEM_DATATYPE = "data_type";
	public static final String DS_ITEM_PATH = "path";
	public static final String DS_ITEM_VALUE = "value";
	public static final String DS_ITEM_VALUETYPE = "valueType";
	public static final String DS_ITEM_REF = "ref";
	public static final String DS_ITEM_REFPARAM = "refparam";
	public static final String DS_ITEM_SOURCE = "source";
	public static final String DS_ITEM_TARGET = "target";

	public static final String DS_ITEM_TYPE_DEFAULT = DsItemType.FIELD.toString();
	public static final String DS_ITEM_DATATYPE_DEFAULT = DsItemDataType.STRING.toString();
	public static final String DS_ITEM_VALUETYPE_DEFAULT = DsItemValueType.VAR.toString();

}

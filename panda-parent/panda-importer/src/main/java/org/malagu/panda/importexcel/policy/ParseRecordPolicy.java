package org.malagu.panda.importexcel.policy;


/**
 *@author Kevin.yang
 *@since 2015年8月22日
 */
public interface ParseRecordPolicy {
	
	final String BEAN_ID = "importer.parseRecordPolicy";
	
	void apply(Context context) throws ClassNotFoundException;
	
}

package org.malagu.panda.importexcel.parser;

import org.malagu.panda.importexcel.policy.Context;

/**
 *@author Kevin.yang
 *@since 2015年8月30日
 */
public interface CellPostParser {
	
	final String DEFAULT = "importer.defaultCellPostParser";
	
	String getName();
	
	void parse(Context context);
}

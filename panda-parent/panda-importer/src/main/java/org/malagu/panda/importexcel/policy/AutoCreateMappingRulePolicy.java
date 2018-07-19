package org.malagu.panda.importexcel.policy;

import org.malagu.panda.importexcel.model.ImporterSolution;

/**
 *@author Kevin.yang
 *@since 2015年9月3日
 */
public interface AutoCreateMappingRulePolicy {
	void apply(ImporterSolution importerSolution);
}

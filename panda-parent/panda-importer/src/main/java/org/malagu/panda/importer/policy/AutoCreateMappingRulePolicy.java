package org.malagu.panda.importer.policy;

import org.malagu.panda.importer.model.ImporterSolution;

/**
 *@author Kevin.yang
 *@since 2015年9月3日
 */
public interface AutoCreateMappingRulePolicy {
	void apply(ImporterSolution importerSolution);
}

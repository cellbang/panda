package org.malagu.panda.notice.ui.strategy;

import java.util.List;

import org.malagu.panda.notice.domain.Notice;
import org.malagu.panda.notice.strategy.MemberProcessor;
import org.malagu.panda.notice.strategy.ReceiverIterator;
import org.malagu.panda.notice.ui.Constants;
import org.malagu.panda.notice.ui.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月13日
 */
@Order
@Component
public class ReceiverIteratorImpl implements ReceiverIterator {

	@Autowired
	private GroupService groupService;

	@Override
	public void each(Notice notice, MemberProcessor memberProcessor) {
		List<String> memberIds = groupService.getMemberIds(notice.getGroupId());
		for (String memberId : memberIds) {
			memberProcessor.process(memberId);
		}
	}

	@Override
	public boolean support(Notice notice) {
		return Constants.MESSAGE_TYPE.equals(notice.getType());
	}
	
	

}

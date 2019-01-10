package org.malagu.panda.notice.ui.strategy;

import java.util.List;

import org.malagu.panda.notice.domain.Notice;
import org.malagu.panda.notice.strategy.Pusher;
import org.malagu.panda.notice.strategy.ReceiveStrategy;
import org.malagu.panda.notice.strategy.ReceiverIterator;
import org.malagu.panda.notice.strategy.SocketSource;
import org.malagu.panda.notice.ui.Constants;
import org.malagu.panda.notice.ui.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.socket.Socket;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月13日
 */
@Order
@Component
public class MessageReceiveSrategy implements ReceiveStrategy {
		
	@Autowired
	private SocketSource<Socket> socketSource;
	
	@Autowired
	private Pusher<Socket> pusher;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private List<ReceiverIterator> receiverIterators;

	@Override
	public void apply(Notice notice) {
		
		for (ReceiverIterator receiverIterator : receiverIterators) {
			if (receiverIterator.support(notice)) {
				receiverIterator.each(notice, memberId -> {
					Socket socket = socketSource.getSocket(memberId);
					if (socket != null) {
						pusher.push(socket, notice);
					}
				});
			}
		}
		noticeService.addNotice(notice);
	}

	@Override
	public boolean support(Notice notice) {
		if (Constants.MESSAGE_TYPE.equals(notice.getType()) && !notice.isAll()) {
			return true;
		}
		return false;
	}

}

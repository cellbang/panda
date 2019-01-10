package org.malagu.panda.notice.ui.strategy;

import java.util.List;

import org.malagu.panda.notice.domain.Notice;
import org.malagu.panda.notice.strategy.Pusher;
import org.malagu.panda.notice.strategy.SocketRegister;
import org.malagu.panda.notice.strategy.SocketSource;
import org.malagu.panda.notice.ui.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.socket.Socket;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月13日
 */
@Component
public class SocketRegisterImpl implements SocketRegister<Socket> {
	
	@Autowired
	private SocketSource<Socket> socketSource;
	
	@Autowired
	private Pusher<Socket> pusher;
	
	@Autowired
	private NoticeService noticeService;

	@Override
	public void register(String key, Socket socket) {
		socketSource.register(key, socket);
		List<Notice> notices = noticeService.getNotices(key);
		for (Notice notice : notices) {
			pusher.push(socket, notice);
		}
	}

}

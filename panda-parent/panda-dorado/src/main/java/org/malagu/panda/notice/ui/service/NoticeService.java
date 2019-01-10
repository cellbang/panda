package org.malagu.panda.notice.ui.service;

import java.util.List;

import org.malagu.panda.notice.domain.Notice;
import org.malagu.panda.security.orm.User;

import com.bstek.dorado.data.provider.Page;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月11日
 */
public interface NoticeService {
	
	void getNotices(Page<Notice> page, String memberId);

	Long getNoticeCount(String memberId);

	void addNotice(Notice notice);

	List<Notice> getNotices(String memberId);
	
	void markRead(String groupId, String memberId);

	List<Notice> getNotices(String groupId, String memberId);

	void loadUnselectedUsers(Page<User> page, String groupId, String memberId, String usernameOrNickname);

	void loadUsers(Page<User> page, String memberId, String usernameOrNickname);

	List<Notice> loadLastNotices(String groupId, String memberId);

}

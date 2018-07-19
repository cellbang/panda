package org.malagu.panda.notice.strategy;

import org.malagu.panda.notice.domain.Notice;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月13日
 */
public interface Pusher<T> {

	void push(T socket, Notice notice);
}

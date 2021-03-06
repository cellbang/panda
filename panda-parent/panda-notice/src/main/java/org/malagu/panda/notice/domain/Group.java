package org.malagu.panda.notice.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年9月11日
 */
@Entity
@Table(name = "PANDA_GROUP")
public class Group {
	
	@Id
	@Column(name = "ID_", length = 64)
	private String id;
	
	@Column(name = "NAME_", length = 255)
	private String name;
	
	@Column(name = "ICON_", length = 255)
	private String icon;
	
	@Column(name = "CREATOR_", length = 64)
	private String creator;
	
	@Column(name = "CREATE_TIME_")
	private Date createTime;
	
	@Column(name = "ALL_")
	private boolean all;
	
	@Column(name = "TEMPORARY_")
	private boolean temporary;
	
	@Column(name = "SYSTEM_")
	private boolean system;
	
	@Column(name = "PRIVATE_LETTER_")
	private boolean privateLetter;
	
	@Column(name = "MEMBER_COUNT_")
	private int memberCount;
	
	@Column(name = "LAST_NOTICE_ID_", length = 64)
	private String lastNoticeId;
	
	@Column(name = "TEMPLATE_ID_", length = 64)
	private String templateId;
	
	@Column(name = "LAST_NOTICE_SEND_TIME_")
	private Date lastNoticeSendTime;
	
	@Column(name = "URL_", length = 512)
	private String url;
	
	@Column(name = "DESCRIPTION_", length = 512)
	private String description;
	
	@Transient
	private GroupMember current;
	
	@Transient
	private GroupMember other;
	
	@Transient
	private List<GroupMember> members;
	
	@Transient
	private List<Template> functions;
	
	@Transient
	private Notice lastNotice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}

	public GroupMember getCurrent() {
		return current;
	}

	public void setCurrent(GroupMember current) {
		this.current = current;
	}

	

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public String getLastNoticeId() {
		return lastNoticeId;
	}

	public void setLastNoticeId(String lastNoticeId) {
		this.lastNoticeId = lastNoticeId;
	}

	public Notice getLastNotice() {
		return lastNotice;
	}

	public void setLastNotice(Notice lastNotice) {
		this.lastNotice = lastNotice;
	}

	public GroupMember getOther() {
		return other;
	}

	public void setOther(GroupMember other) {
		this.other = other;
	}

	public boolean isPrivateLetter() {
		return privateLetter;
	}

	public void setPrivateLetter(boolean privateLetter) {
		this.privateLetter = privateLetter;
	}

	public Date getLastNoticeSendTime() {
		return lastNoticeSendTime;
	}

	public void setLastNoticeSendTime(Date lastNoticeSendTime) {
		this.lastNoticeSendTime = lastNoticeSendTime;
	}

	public List<GroupMember> getMembers() {
		return members;
	}

	public void setMembers(List<GroupMember> members) {
		this.members = members;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Template> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Template> functions) {
		this.functions = functions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	
	
	

}

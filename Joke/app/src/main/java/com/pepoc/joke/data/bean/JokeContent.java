package com.pepoc.joke.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class JokeContent implements Parcelable {

	private String jokeId;
	private String content;
	private String createTime;
	private String userId;
	private String userNickName;
	private String userAvatar;
	private String islike;
	private String iscollect;
	private String likeCount;
	private String collectCount;
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getJokeId() {
		return jokeId;
	}
	public void setJokeId(String jokeId) {
		this.jokeId = jokeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getIslike() {
		return islike;
	}
	public void setIslike(String islike) {
		this.islike = islike;
	}
	public String getIscollect() {
		return iscollect;
	}
	public void setIscollect(String iscollect) {
		this.iscollect = iscollect;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getCollectCount() {
		return collectCount;
	}
	public void setCollectCount(String collectCount) {
		this.collectCount = collectCount;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jokeId);
		dest.writeString(content);
		dest.writeString(createTime);
		dest.writeString(userId);
		dest.writeString(userNickName);
		dest.writeString(userAvatar);
		dest.writeString(islike);
		dest.writeString(iscollect);
		dest.writeString(likeCount);
		dest.writeString(collectCount);
		dest.writeString(imageUrl);
	}
	
	public static final Parcelable.Creator<JokeContent> CREATOR = new Creator<JokeContent>() {

		@Override
		public JokeContent createFromParcel(Parcel source) {
			JokeContent jokeContent = new JokeContent();
			jokeContent.jokeId = source.readString();
			jokeContent.content = source.readString();
			jokeContent.createTime = source.readString();
			jokeContent.userId = source.readString();
			jokeContent.userNickName = source.readString();
			jokeContent.userAvatar = source.readString();
			jokeContent.islike = source.readString();
			jokeContent.iscollect = source.readString();
			jokeContent.likeCount = source.readString();
			jokeContent.collectCount = source.readString();
			jokeContent.imageUrl = source.readString();
			return jokeContent;
		}

		@Override
		public JokeContent[] newArray(int size) {
			return new JokeContent[size];
		}
	};
	
}

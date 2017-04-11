package com.ebay.park.service.item.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * 
 * @author marcos.lambolay
 */
public class UploadPhotosRequest extends UserItemRequest {

	private MultipartFile photo1;
	private MultipartFile photo2;
	private MultipartFile photo3;
	private MultipartFile photo4;

	//@formatter:off
	private UploadPhotosRequest(
			String token
			, Long itemId
			, MultipartFile photo1
			, MultipartFile photo2
			, MultipartFile photo3
			, MultipartFile photo4) {
	//@formatter:on
		super(token);
		setItemId(itemId);
		this.photo1 = photo1;
		this.photo2 = photo2;
		this.photo3 = photo3;
		this.photo4 = photo4;
	}

	public MultipartFile getPhoto1() {
		return photo1;
	}

	public MultipartFile getPhoto2() {
		return photo2;
	}

	public MultipartFile getPhoto3() {
		return photo3;
	}

	public MultipartFile getPhoto4() {
		return photo4;
	}

	public void setPhoto1(MultipartFile photo1) {
		this.photo1 = photo1;
	}

	public void setPhoto2(MultipartFile photo2) {
		this.photo2 = photo2;
	}

	public void setPhoto3(MultipartFile photo3) {
		this.photo3 = photo3;
	}

	public void setPhoto4(MultipartFile photo4) {
		this.photo4 = photo4;
	}

	public Iterator<MultipartFile> iterator() {
		final Queue<MultipartFile> urls = new ArrayDeque<MultipartFile>();

		//@formatter:off
		if (photo4 != null) urls.add(photo4);
		if (photo3 != null) urls.add(photo3);
		if (photo2 != null) urls.add(photo2);
		if (photo1 != null) urls.add(photo1);
		
		return new Iterator<MultipartFile>(){
			@Override
			public boolean hasNext() { return !urls.isEmpty(); }

			@Override
			public MultipartFile next() { return urls.poll(); }

			@Override
			public void remove() { urls.remove(); }
		};
		//@formatter:on
	}

	public static class Builder {
		private String token;
		private Long itemId;
		private MultipartFile photo1;
		private MultipartFile photo2;
		private MultipartFile photo3;
		private MultipartFile photo4;
		
		public Builder token(String token) {
			this.token = token;
			return this;
		}

		public Builder itemId(Long itemId) {
			this.itemId = itemId;
			return this;
		}

		public Builder photo1(MultipartFile photo1) {
			this.photo1 = photo1;
			return this;
		}

		public Builder photo2(MultipartFile photo2) {
			this.photo2 = photo2;
			return this;
		}

		public Builder photo3(MultipartFile photo3) {
			this.photo3 = photo3;
			return this;
		}

		public Builder photo4(MultipartFile photo4) {
			this.photo4 = photo4;
			return this;
		}

		public UploadPhotosRequest build() {
			//@formatter:off
			return new UploadPhotosRequest(token
					, itemId
					, photo1
					, photo2
					, photo3
					, photo4);
			//@formatter:on
		}
	}

}

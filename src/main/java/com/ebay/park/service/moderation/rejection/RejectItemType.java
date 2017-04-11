package com.ebay.park.service.moderation.rejection;

/**
 * Enum to select the proper class (given an id) that manages the rejection.
 * @author Julieta Salvadó
 *
 */
public enum RejectItemType {
    DO_NOT_SEND() {
        @Override
        public Class<?> getExecutorClass(){
            return RejectDoNotSendItemExecutor.class;
        }
    },
	DUPLICATED() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectDuplicatedItemExecutor.class;
		}
	},
	INTERNET_PICTURES() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectPicturesItemExecutor.class;
		}
	},
	SERVICES() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectServicesItemExecutor.class;
		}
	},
	MAKEUP() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectMakeupItemExecutor.class;
		}
	},
	ANIMALS() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectAnimalsItemExecutor.class;
		}
	},
	COMMISSION() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectCommissionItemExecutor.class;
		}
	},
	STYLE() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectStyleItemExecutor.class;
		}
	},
	PRICE() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectPriceItemExecutor.class;
		}
	},
	FORBIDDEN() {
		@Override
		public Class<?> getExecutorClass(){
			return RejectForbiddenItemExecutor.class;
		}
	}
	;
	
	public static RejectItemType fromId(Integer reasonId) {
		if (reasonId != null && reasonId < values().length) {
			return values()[reasonId];
		}

		return null;
	}
	
	public static int getSize() {
	    return values().length;
	}

	public abstract Class<?> getExecutorClass();
}

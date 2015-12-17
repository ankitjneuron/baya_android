package com.baya.ApiRequest;

public interface RequestCompleteListener<T> {

	public void onTaskComplete(String tag, T response);
}

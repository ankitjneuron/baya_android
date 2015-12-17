package com.baya.WebServices;

public enum URL {
	
	BASEURL("http://baya.whatall.com/api/"),
  //BASEURL("http://192.168.0.157:2171/api/"),
	SIGN_UP(BASEURL.getUrl() + "users/signup"),
	LOGIN(BASEURL.getUrl() + "users/login"),
	IMAGEURL("http://baya.whatall.com/uploads/profile"),
	Facebooklogin(BASEURL.getUrl() + "users/facebook-login"),
	FORGOTPASSWORD(BASEURL.getUrl() + "users/forgot-password"),
	LOGOUT(BASEURL.getUrl() + "users/logout"),
	Change_Passwords(BASEURL.getUrl() + "users/change-password"),
	GETCATEGORY(BASEURL.getUrl() + "public/getcategory"),
    PROFILE(BASEURL.getUrl() +  "users/me?"),
	PROFILEUPDATE(BASEURL.getUrl() + "users/updateprofile?"),
	GET_BUISNESS(BASEURL.getUrl()+"users/get-all-business-listing/"),
	BOOKAPPOINTMENT(BASEURL.getUrl()+"users/appointment?"),
	UPCOMINGAPPOINTMENT(BASEURL.getUrl()+"users/get-appointments-by-date?"),
	ALLUPCOMMINGAPPOINTMENT(BASEURL.getUrl()+"users/get-upcoming-appointments?"),
	GETAPPOINTMENTDETAIL(BASEURL.getUrl()+"users/get-appointment-detail-by-id"),
	STATE(BASEURL.getUrl()+"users/states"),
	ADDLISTING(BASEURL.getUrl()+"users/save-listing?"),
	APPOINTMENTHISTORY(BASEURL.getUrl()+"users/get-appointments-history?"),
	BUSINESSDETAIL(BASEURL.getUrl()+"users/get-business-detail-by-id/"),
	CANCELAPPOINTMENT(BASEURL.getUrl()+"users/change-appointment-status/"),
	FAVORITEBUSINESS(BASEURL.getUrl()+"users/get-favourites-appointment/"),
	TWOAPPOINTMENT(BASEURL.getUrl()+"users/get-two-upcoming-appointments?"),
	UPCOMINGCOUNT(BASEURL.getUrl()+"users/get-appointment-count?");


	private String callingURL;

	URL(String url) {

		callingURL = url;
	}

	public String getUrl() {

		return callingURL;
	}

}

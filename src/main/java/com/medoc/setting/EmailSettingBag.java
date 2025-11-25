package com.medoc.setting;

import java.util.List;

import com.medoc.entity.Setting;
import com.medoc.entity.SettingBag;


public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public String getHost() {
		return super.getValue("MAIL_HOST");
	}

	public int getPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}
	
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}
	
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
	
	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}
	
	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}	
	
	public String getOrderConfirmationContentClient() {
		return super.getValue("ORDER_CONFIRMATION_CONTENT");
	}
	
	public String getOrderConfirmationSubjectClient() {
		return super.getValue("ORDER_CONFIRMATION_SUBJECT");
	}
	
	public String getOrderConfirmationContentPharma() {
		return super.getValue("ORDER_RECEPTION_CONTENT_PHARMA");
	}
	
	public String getOrderConfirmationSubjectPharma() {
		return super.getValue("ORDER_RECEPTION_SUBJECT_PHARMA");
	}
}

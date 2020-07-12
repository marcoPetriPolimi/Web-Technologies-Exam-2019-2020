"use strict";

/********************************
 * 								*
 * 								*
 * RIA PROJECT MAIN JAVASCRIPT	*
 * 								*
 * 								*
 ********************************/

/**
 * This is the base class used by the website script to store user preferences such as language, which default is eng-US, following the international standard ISO 639 alpha 3 for languages and ISO 3166 alpha 2.
 */
class UserPreferences {
	constructor(language = "eng",country = "US") {
		this.language = language;
		this.country = country;
	}

	getLanguage() {
		return this.language;
	}
	getCountry() {
		return this.country;
	}

	setLanguage(language) {
		this.language = language;
	}
	setCountry(country) {
		this.country = country;
	}
}

/**
 * This is the class used to store information about the page the user is seeing and its relative views
 */
class InformationHolder {
	constructor(languagePack,userInfo = null,userAccounts = null,accountInfo = null,ingoingTransfers = null,outgoingTransfers = null,errorType = null) {
		this.languagePack = languagePack;
		this.userInfo = userInfo;
		this.userAccounts = userAccounts;
		this.accountInfo = accountInfo;
		this.ingoingTransfers = ingoingTransfers;
		this.outgoingTransfers = outgoingTransfers;
		this.errorType = errorType;
	}

	getLang() {
		return this.languagePack;
	}
	getUserInfo() {
		return this.userInfo;
	}
	getUserAccounts() {
		return this.userAccounts;
	}
	getAccountInfo() {
		return this.accountInfo;
	}
	getIngoingTransfers() {
		return this.ingoingTransfers;
	}
	getOutgoingTransfers() {
		return this.outgoingTransfers;
	}
	getErrorType() {
		return this.errorType;
	}
}

/**
 * These function makes an asynchronous call to the url given as parameter with a default value for the method type. The user preferred language is sent to the server to allows it to return the correct language pack with the json format.
 * @param method The form method to use.
 * @param url The url where the request must be sent.
 * @param responseFunction The callback function to call when the server replies to the call.
 * @param formValues Eventually form values to send with the request.
 * @param userPreferences A UserPreferences object or its subclass with UserPreferences information.
 */
function ajaxCall(method = "GET", url, responseFunction, formValues, userPreferences = new UserPreferences()) {
	// needed variable to open a request, visible because of the closure
	var XMLObject = new XMLHttpRequest();

	if (url !== null) {
		XMLObject.onreadystatechange = function() {
			responseFunction(XMLObject);
		};

		XMLObject.open(method,url);
		XMLObject.setRequestHeader("Accept-Language",userPreferences.getLanguage()+"-"+userPreferences.getCountry());
		if (responseFunction !== null) {
			XMLObject.send(new FormData(formValues))
		} else {
			XMLObject.send()
		}
	}
}
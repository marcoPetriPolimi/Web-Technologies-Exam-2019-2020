"use strict";

/********************************
 * 								*
 * 								*
 * RIA PROJECT MAIN JAVASCRIPT	*
 * 								*
 * 								*
 ********************************/

/**
 * This is the class used to store information about the page the user is seeing and its relative views.
 */
class InformationHolder {
	constructor(languagePack,userInfo = null,userAccounts = null,accountInfo = null,ingoingTransfers = null,outgoingTransfers = null,errorType = null, addressBook = null, ingoingPages = 1, outgoingPages = 1) {
		this.languagePack = languagePack;
		this.userInfo = userInfo;
		this.userAccounts = userAccounts;
		this.accountInfo = accountInfo;
		this.ingoingTransfers = ingoingTransfers;
		this.outgoingTransfers = outgoingTransfers;
		this.errorType = errorType;
		this.addressBook = addressBook;
		this.ingoingPages = ingoingPages;
		this.outgoingPages = outgoingPages;
		this.recipientAccount = null;
		this.inPage = 1;
		this.outPage = 1;
	}

	/********************
	 * 					*
	 *		SETTERS		*
	 * 					*
	 ********************/
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
	getAddressBook() {
		return this.addressBook;
	}
	getIngoingPages() {
		return this.ingoingPages;
	}
	getOutgoingPages() {
		return this.outgoingPages;
	}
	getInPage() {
		return this.inPage;
	}
	getOutPage() {
		return this.outPage;
	}
	getRecipientAccount() {
		return this.recipientAccount;
	}

	/********************
	 * 					*
	 *		GETTERS		*
	 * 					*
	 ********************/
	setLang(lang) {
		this.languagePack = lang;
	}
	setUserInfo(userInfo) {
		this.userInfo = userInfo;
	}
	setUserAccounts(userAccounts) {
		this.userAccounts = userAccounts;
	}
	setAccountInfo(accountInfo) {
		this.accountInfo = accountInfo;
	}
	setIngoingTransfers(ingoingTransfers) {
		this.ingoingTransfers = ingoingTransfers;
	}
	setOutgoingTransfers(outgoingTransfers) {
		this.outgoingTransfers = outgoingTransfers;
	}
	setErrorType(errorType) {
		this.errorType = errorType;
	}
	setAddressBook(addressBook) {
		this.addressBook = addressBook;
	}
	setIngoingPages(ingoingPages) {
		this.ingoingPages = ingoingPages;
	}
	setOutgoingPages(outgoingPages) {
		this.outgoingPages = outgoingPages;
	}
	setInPage(inPage) {
		this.inPage = inPage;
	}
	setOutPage(outPage) {
		this.outPage = outPage;
	}
	setRecipientAccount(recipientAccount) {
		this.recipientAccount = recipientAccount;
	}
}

class Couple {
	constructor(one = null,two = null) {
		this.one = one;
		this.two = two;
	}

	getOne() {
		return this.one;
	}
	getTwo() {
		return this.two;
	}
	setOne(one) {
		this.one = one;
	}
	setTwo(two) {
		this.two = two;
	}
}

/**
 * This function makes an asynchronous call to the url given as parameter with a default value for the method type. The user preferred language is sent to the server to allows it to return the correct language pack with the json format.
 * @param method The form method to use.
 * @param url The url where the request must be sent.
 * @param responseFunction The callback function to call when the server replies to the call.
 * @param formValues Eventually form values to send with the request.
 */
function ajaxCall(method = "GET", url, responseFunction, formValues) {
	// needed variable to open a request, visible because of the closure
	var XMLObject = new XMLHttpRequest();

	if (url !== null) {
		XMLObject.onreadystatechange = function() {
			responseFunction(XMLObject);
		};

		XMLObject.open(method,url);
		if (responseFunction !== null) {
			XMLObject.send(new FormData(formValues))
		} else {
			XMLObject.send()
		}
	}
}
"use strict";

/********************************
 * 								*
 * 								*
 *  LOGIN RIA PROJECT SCRIPT	*
 * 								*
 * 								*
 ********************************/

// anonymous class declaration and call to create a script without global variables
(function() {
	/********************************
	 * 								*
	 * 								*
	 * JAVASCRIPT OBJECT DEFINITION	*
	 * 								*
	 * 								*
	 ********************************/
	class PageManager {
		constructor() {}

		start() {
			// instantiates all listener for page objects
			document.getElementById("showHomepage").addEventListener("click",requestHomepage);
			document.getElementById("logoutButton").addEventListener("click",logoutClick);
			document.getElementById("popupErrorClose").addEventListener("click",closeErrorPopup);
			document.querySelectorAll("#homepageAccounts table.moneyTable tbody tr td a").forEach(function(elem) {
				elem.addEventListener("click",accountClick);
			});
			document.querySelectorAll("#accountStatePageNumbersIngoing a").forEach(function(elem) {
				elem.addEventListener("click",ingoingTransferPageClick);
			});
			document.querySelectorAll("#accountStatePageNumbersOutgoing a").forEach(function(elem) {
				elem.addEventListener("click",outgoingTransferPageClick);
			});
			document.querySelector("#accountStateForm form input[type=button]").addEventListener("click",orderTransferClick);
			document.getElementById("popupAddressBookAdd").addEventListener("click",addToAddressBook);
			document.getElementById("popupAddressBookNotAdd").addEventListener("click",notAddToAddressBook);
			document.querySelectorAll("#accountStateForm form input")[0].addEventListener("input",checkContactPresence);
			document.querySelectorAll("#accountStateForm form input")[1].addEventListener("input",checkContactPresence);
			document.querySelectorAll("#accountStateForm form input")[1].addEventListener("input",accountCodeCheck);
			document.querySelectorAll("#accountStateForm form input")[2].addEventListener("input",amountCheck);

			// auto clicks the homepage loader
			document.getElementById("showHomepage").dispatchEvent(new Event("click"));
		}
		loadHomepage(infoHolder) {
			var userInfo, accountsTable, accountsTableBody;

			document.querySelectorAll("#homepageInfoBoxCode td")[1].textContent = infoHolder.getUserInfo()["code"];
			document.querySelectorAll("#homepageInfoBoxName td")[1].textContent = infoHolder.getUserInfo()["name"];
			accountsTable = document.querySelector("#homepageAccounts table.moneyTable");
			accountsTableBody = document.querySelector("#homepageAccounts table.moneyTable tbody");
			accountsTableBody.innerHTML = "";
			for (let i = 0; i < infoHolder.getUserAccounts().length; i++) {
				let row = document.createElement("tr");
				let accountCode = document.createElement("td");
				let accountCodeLink = document.createElement("a");
				let accountBalance = document.createElement("td");
				accountCodeLink.textContent = infoHolder.getUserAccounts()[i]["code"];
				accountCodeLink.href = "#";
				accountBalance.textContent = infoHolder.getUserAccounts()[i]["balance"];

				accountsTableBody.append(row);
				row.append(accountCode);
				accountCode.append(accountCodeLink);
				row.append(accountBalance);
			}

			document.querySelectorAll("#homepageAccounts table.moneyTable tbody tr td a").forEach(function(elem) {
				elem.addEventListener("click",accountClick);
			});
			document.getElementById("homepageInfoBox").style.display = "block";
			document.getElementById("homepageAccounts").style.display = "block";
			document.getElementById("showHomepage").style.display = "none";
			document.getElementById("accountStateInfoBox").style.display = "none";
			document.getElementById("accountStateTransfers").style.display = "none";
			document.getElementById("accountStateForm").style.display = "none";
			document.querySelector("#accountStateForm form").reset();
			this.unShowCompletionTips();
		}
		loadAccount(infoHolder) {
			document.getElementById("infoTableAccount").textContent = infoHolder.getAccountInfo()["code"];
			document.getElementById("infoTableAmount").textContent = infoHolder.getAccountInfo()["balance"];
			document.getElementById("accountStateFormHiddenCode").setAttribute("value",infoHolder.getAccountInfo()["code"]);
			this.loadIngoingTransfers(infoHolder);
			this.loadOutgoingTransfers(infoHolder);

			document.getElementById("homepageInfoBox").style.display = "none";
			document.getElementById("homepageAccounts").style.display = "none";
			document.getElementById("showHomepage").style.display = "block";
			document.getElementById("accountStateInfoBox").style.display = "block";
			document.getElementById("accountStateTransfers").style.display = "block";
			document.getElementById("accountStateForm").style.display = "block";
		}
		loadIngoingTransfers(infoHolder) {
			var ingoingTransfersTable, ingoingTransfersTableBody, ingoingPages;

			ingoingPages = document.querySelector("#accountStateTransfers div.ingoingTransfers div.pageNumbers");
			ingoingTransfersTable = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable");
			ingoingTransfersTableBody = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable tbody");
			ingoingTransfersTableBody.innerHTML = "";
			ingoingPages.innerHTML = "";

			// loads and writes each transfer present in the user view
			for (let i = 0; i < infoHolder.getIngoingTransfers().length; i++) {
				let row = document.createElement("tr");
				let sender = document.createElement("td");
				let senderAccount = document.createElement("td");
				let amount = document.createElement("td");
				let date = document.createElement("td");
				let reason = document.createElement("td");
				sender.textContent = infoHolder.getIngoingTransfers()[i]["outgoing"]["owner"]["code"];
				senderAccount.textContent = infoHolder.getIngoingTransfers()[i]["outgoing"]["code"];
				amount.textContent = infoHolder.getIngoingTransfers()[i]["amount"];
				date.textContent = infoHolder.getIngoingTransfers()[i]["datetime"];
				reason.textContent = infoHolder.getIngoingTransfers()[i]["reason"];
				reason.className = "reason";

				ingoingTransfersTableBody.append(row);
				row.append(sender);
				row.append(senderAccount);
				row.append(amount);
				row.append(date);
				row.append(reason);
			}

			// updates the number of pages on the bottom of the transfer table
			let startPage, endPage;
			startPage = infoHolder.getInPage() >= 2 ? infoHolder.getInPage()-1 : 1;
			endPage = infoHolder.getInPage() < infoHolder.getIngoingPages() ? infoHolder.getInPage()+1 : infoHolder.getInPage();
			if (infoHolder.getInPage() > 2) {
				let first = document.createElement("a");
				first.href = "#";
				first.textContent = "1 ";
				ingoingPages.append(first);
			}
			if (infoHolder.getInPage() > 3) {
				let points = document.createElement("span");
				points.textContent = "... ";
				ingoingPages.append(points);
			}
			for (let i = startPage; i <= endPage; i++) {
				let page = document.createElement("a");
				page.href = "#";
				page.textContent = i+" ";
				ingoingPages.append(page);
			}
			if (infoHolder.getInPage()+1 < infoHolder.getIngoingPages()-1) {
				let points = document.createElement("span");
				points.textContent = "... ";
				ingoingPages.append(points);
			}
			if (infoHolder.getInPage()+1 < infoHolder.getIngoingPages()) {
				let last = document.createElement("a");
				last.href = "#";
				last.textContent = infoHolder.getIngoingPages();
				ingoingPages.append(last);
			}

			document.querySelectorAll("#accountStatePageNumbersIngoing a").forEach(function(elem) {
				elem.addEventListener("click",ingoingTransferPageClick);
			});
		}
		loadOutgoingTransfers(infoHolder) {
			var outgoingTransfersTable, outgoingTransfersTableBody, outgoingPages;

			outgoingPages = document.querySelector("#accountStateTransfers div.outgoingTransfers div.pageNumbers");
			outgoingTransfersTable = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable");
			outgoingTransfersTableBody = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable tbody");
			outgoingTransfersTableBody.innerHTML = "";
			outgoingPages.innerHTML = "";

			// loads and writes each transfer present in the user view
			for (let i = 0; i < infoHolder.getOutgoingTransfers().length; i++) {
				let row = document.createElement("tr");
				let sender = document.createElement("td");
				let senderAccount = document.createElement("td");
				let amount = document.createElement("td");
				let date = document.createElement("td");
				let reason = document.createElement("td");
				sender.textContent = infoHolder.getOutgoingTransfers()[i]["ingoing"]["owner"]["code"];
				senderAccount.textContent = infoHolder.getOutgoingTransfers()[i]["ingoing"]["code"];
				amount.textContent = infoHolder.getOutgoingTransfers()[i]["amount"];
				date.textContent = infoHolder.getOutgoingTransfers()[i]["datetime"];
				reason.textContent = infoHolder.getOutgoingTransfers()[i]["reason"];
				reason.className = "reason";

				outgoingTransfersTableBody.append(row);
				row.append(sender);
				row.append(senderAccount);
				row.append(amount);
				row.append(date);
				row.append(reason);
			}

			// updates the number of pages on the bottom of the transfer table
			let startPage, endPage;
			startPage = infoHolder.getOutPage() >= 2 ? infoHolder.getOutPage()-1 : 1;
			endPage = infoHolder.getOutPage() < infoHolder.getOutgoingPages() ? infoHolder.getOutPage()+1 : infoHolder.getOutPage();
			if (infoHolder.getOutPage() > 2) {
				let first = document.createElement("a");
				first.href = "#";
				first.textContent = "1 ";
				outgoingPages.append(first);
			}
			if (infoHolder.getOutPage() > 3) {
				let points = document.createElement("span");
				points.textContent = "... ";
				outgoingPages.append(points);
			}
			for (let i = startPage; i <= endPage; i++) {
				let page = document.createElement("a");
				page.href = "#";
				page.textContent = i+" ";
				outgoingPages.append(page);
			}
			if (infoHolder.getOutPage()+1 < infoHolder.getOutgoingPages()-1) {
				let points = document.createElement("span");
				points.textContent = "... ";
				outgoingPages.append(points);
			}
			if (infoHolder.getOutPage()+1 < infoHolder.getOutgoingPages()) {
				let last = document.createElement("a");
				last.href = "#";
				last.textContent = infoHolder.getOutgoingPages();
				outgoingPages.append(last);
			}

			document.querySelectorAll("#accountStatePageNumbersOutgoing a").forEach(function(elem) {
				elem.addEventListener("click",outgoingTransferPageClick);
			});
		}
		orderRefresh(infoHolder) {
			document.getElementById("infoTableAmount").textContent = infoHolder.getAccountInfo()["balance"];
			this.loadIngoingTransfers(infoHolder);
			this.loadOutgoingTransfers(infoHolder);
		}
		showError(infoHolder, error) {
			document.querySelector("#popupError h2").className = "";
			document.querySelector("#popupError h2").textContent = infoHolder.getLang()["transferResultFail"];
			let errorText = document.getElementById("popupErrorText");
			let ol = document.createElement("ol");
			let li = document.createElement("li");

			li.textContent = error;
			errorText.innerHTML = "";
			errorText.textContent = infoHolder.getLang()["transferResultFailText"];
			errorText.append(ol);
			ol.append(li);

			document.getElementById("greyBackground").className = "display";
			document.getElementById("popupError").className = "display";
			document.getElementById("popupErrorClose").textContent = infoHolder.getLang()["popupCloseErrorPopup"];
		}
		showSuccess(infoHolder) {
			document.querySelector("#popupError h2").className = "";
			document.getElementById("successTable").className = "moneyTable";
			document.querySelector("#popupError h2").textContent = infoHolder.getLang()["transferResultSuccess"];
			let successText = document.getElementById("popupErrorText");
			let table = document.getElementById("successTable");
			let thead = document.createElement("thead");
			let tbody = document.createElement("tbody");
			let row1 = document.createElement("tr");
			let row2 = document.createElement("tr");
			let row3 = document.createElement("tr");
			let thOwner = document.createElement("th");
			let thAccount = document.createElement("th");
			let thBalance = document.createElement("th");
			let tdOwner1 = document.createElement("td");
			let tdAccount1 = document.createElement("td");
			let tdBalance1 = document.createElement("td");
			let tdOwner2 = document.createElement("td");
			let tdAccount2 = document.createElement("td");
			let tdBalance2 = document.createElement("td");

			table.innerHTML = "";
			table.append(thead);
			table.append(tbody);
			thead.append(row1);
			row1.append(thOwner);
			row1.append(thAccount);
			row1.append(thBalance);
			tbody.append(row2);
			tbody.append(row3);
			row2.append(tdOwner1);
			row2.append(tdAccount1);
			row2.append(tdBalance1);
			row3.append(tdOwner2);
			row3.append(tdAccount2);
			row3.append(tdBalance2);

			successText.innerHTML = "";
			successText.textContent = infoHolder.getLang()["transferResultSuccessText"];
			thOwner.textContent = infoHolder.getLang()["transferResultSuccessOwner"];
			thAccount.textContent = infoHolder.getLang()["transferResultSuccessAccount"];
			thBalance.textContent = infoHolder.getLang()["transferResultSuccessBalance"];
			tdOwner1.textContent = infoHolder.getAccountInfo()["owner"]["code"];
			tdAccount1.textContent = infoHolder.getAccountInfo()["code"];
			tdBalance1.textContent = infoHolder.getAccountInfo()["balance"];
			tdOwner2.textContent = infoHolder.getRecipientAccount()["owner"]["code"];
			tdAccount2.textContent = infoHolder.getRecipientAccount()["code"];
			tdBalance2.textContent = infoHolder.getRecipientAccount()["balance"];

			document.getElementById("greyBackground").className = "display";
			document.getElementById("popupError").className = "display";
			document.getElementById("popupErrorClose").textContent = infoHolder.getLang()["popupCloseSuccessPopup"];
		}
		showAddressBookRequest(infoHolder) {
			// evaluate if the user can be added to user's address book
			let found = false;
			for (let i = 0; i < infoHolder.getAddressBook().recipientList.length && !found; i++) {
				if (infoHolder.getAddressBook().recipientList[i]["userId"] === infoHolder.getRecipientAccount()["owner"]["code"] && infoHolder.getAddressBook().recipientList[i]["accountId"] === infoHolder.getRecipientAccount()["code"]) {
					found = true;
				}
			}
			// the user isn't inside the address book, now it's time to ask to the user if he wants to add it to it
			if (!found) {
				document.getElementById("greyBackgroundAddressBook").className = "display";
				document.getElementById("popupAddressBook").className = "display";
				document.getElementById("popupAddressBookText").textContent = infoHolder.getLang().popupAddressBookQuestion+" ("+infoHolder.getRecipientAccount()["owner"]["code"]+", "+infoHolder.getRecipientAccount()["code"]+")";
				document.getElementById("popupAddressBookAdd").textContent = infoHolder.getLang().popupAddressBookAdd;
				document.getElementById("popupAddressBookNotAdd").textContent = infoHolder.getLang().popupAddressBookNotAdd;
			}
		}
		showCompletionTips(infoHolder,tipsArray) {
			var select;

			document.getElementById("autoCompletionTip").className = "";
			document.getElementById("autoCompletionTip").textContent = infoHolder.getLang()["completionTipText"];
			select = document.getElementById("completionContacts");
			select.className = "";
			select.innerHTML = "";

			// adds the default option
			let defaultOption = document.createElement("option");
			defaultOption.value = "none";
			defaultOption.textContent = infoHolder.getLang()["completionOptionDefault"];
			select.append(defaultOption);

			// adds every prediction
			for (let i = 0; i < tipsArray.length; i++) {
				let option = document.createElement("option");
				option.value = tipsArray[i].getOne()+"-"+tipsArray[i].getTwo();
				option.textContent = tipsArray[i].getOne()+", "+tipsArray[i].getTwo();
				select.append(option);
			}

			select.addEventListener("input",applyCompletion);
		}
		unShowCompletionTips() {
			document.getElementById("autoCompletionTip").className = "hidden";
			document.getElementById("completionContacts").className = "hidden";
			document.getElementById("completionContacts").innerHTML = "";
		}
		showErrorPopup(text,closeText) {
			document.getElementById("greyBackground").className = "display";
			document.getElementById("popupError").className = "display";
			document.getElementById("popupErrorText").textContent = text;
			document.getElementById("popupErrorClose").textContent = closeText;
		}
	}

	/********************************
	 * 								*
	 * 								*
	 *		 SCRIPT MAIN BODY		*
	 * 								*
	 * 								*
	 ********************************/
	var informationHolder;
	var pageManager = new PageManager();

	window.onload = function () {
		if (sessionStorage.getItem("logged") == null) {
			document.location.href = "/index";
		} else {
			pageManager.start();
		}
	}

	/********************************
	 * 								*
	 * 								*
	 *   AUTO COMPLETION FUNCTION	*
	 * 								*
	 * 								*
	 ********************************/
	function checkContactPresence(e) {
		var recipient, recipientAccount, foundPossibilities = [];

		recipient = document.querySelectorAll("#accountStateForm form input")[0].value;
		recipientAccount = document.querySelectorAll("#accountStateForm form input")[1].value;

		for (let i = 0; i < informationHolder.getAddressBook()["recipientList"].length; i++) {
			if (informationHolder.getAddressBook()["recipientList"][i]["userId"].toString().startsWith(recipient)
				&& informationHolder.getAddressBook()["recipientList"][i]["accountId"].toString().startsWith(recipientAccount)
				&& informationHolder.getAddressBook()["recipientList"][i]["accountId"] !== informationHolder.getAccountInfo()["code"]
				&& recipient !== "") {
				foundPossibilities.push(new Couple(informationHolder.getAddressBook()["recipientList"][i]["userId"],informationHolder.getAddressBook()["recipientList"][i]["accountId"]));
			}
		}

		if (foundPossibilities.length > 0) {
			pageManager.showCompletionTips(informationHolder, foundPossibilities);
		} else {
			pageManager.unShowCompletionTips();
		}
	}
	function applyCompletion(e) {
		var target = e.target, splitValues;

		if (target.value !== "none") {
			splitValues = target.value.split("-");
			document.querySelectorAll("#accountStateForm form input")[0].value = splitValues[0];
			document.querySelectorAll("#accountStateForm form input")[1].value = splitValues[1];
		}
	}

	/********************************
	 * 								*
	 * 								*
	 *   SERVER RESPONSE GETTERS	*
	 * 								*
	 * 								*
	 ********************************/
	function requestHomepageResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder = new InformationHolder(objectReceived.keys);
					informationHolder.setUserInfo(objectReceived.userInfo);
					informationHolder.setUserAccounts(objectReceived.accounts);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					pageManager.loadHomepage(informationHolder);
					break;

				case 401:
					pageManager.showErrorPopup(informationHolder.getLang().error401,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}
	function logoutResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			sessionStorage.removeItem("logged");
			document.location.href = "/index";
		}
	}
	function accountClickResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setAccountInfo(objectReceived.accountInfo);
					informationHolder.setIngoingTransfers(objectReceived.ingoingTransfers);
					informationHolder.setOutgoingTransfers(objectReceived.outgoingTransfers);
					informationHolder.setIngoingPages(objectReceived.ingoingPages);
					informationHolder.setOutgoingPages(objectReceived.outgoingPages);
					informationHolder.setAddressBook(objectReceived.addressBook);
					informationHolder.setInPage(1);
					informationHolder.setOutPage(1);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					pageManager.loadAccount(informationHolder);
					break;

				case 400:
					pageManager.showErrorPopup(informationHolder.getLang().error400,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 401:
					pageManager.showErrorPopup(informationHolder.getLang().error401,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}
	function ingoingTransferPageResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setIngoingTransfers(objectReceived.transfers);
					informationHolder.setIngoingPages(objectReceived.transferPages);
					informationHolder.setInPage(objectReceived.transferPage);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					pageManager.loadIngoingTransfers(informationHolder);
					break;

				case 401:
					pageManager.showErrorPopup(informationHolder.getLang().error401,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}
	function outgoingTransferPageResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setOutgoingTransfers(objectReceived.transfers);
					informationHolder.setOutgoingPages(objectReceived.transferPages);
					informationHolder.setOutPage(objectReceived.transferPage);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					pageManager.loadOutgoingTransfers(informationHolder);
					break;

				case 401:
					pageManager.showErrorPopup(informationHolder.getLang().error401,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}
	function orderTransferResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setAccountInfo(objectReceived.account);
					informationHolder.setRecipientAccount(objectReceived.recipientAccount);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);

					// evaluate if the user
					let urlToAsk = "/getTransfers?accountCode="+informationHolder.getAccountInfo()["code"]+"&type=out&out="+informationHolder.getOutPage();
					ajaxCall("GET",urlToAsk,outgoingTransferPageResponse);

					pageManager.unShowCompletionTips();
					pageManager.showAddressBookRequest(informationHolder);
					pageManager.orderRefresh(informationHolder);
					pageManager.showSuccess(informationHolder);
					document.querySelector("#accountStateForm form").reset();
					break;

				case 400:
					informationHolder.setLang(objectReceived.keys);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					pageManager.showError(informationHolder,objectReceived.error);
					break;

				case 401:
					pageManager.showErrorPopup(informationHolder.getLang().error401,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}
	function addToAddressBookResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setAddressBook(objectReceived.addressBook);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					break;

				case 400:
					pageManager.showErrorPopup(informationHolder.getLang().error400,informationHolder.getLang().popupCloseErrorPopup);
					break;

				case 500:
					pageManager.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
					break;
			}
		}
	}

	/********************************
	 * 								*
	 * 								*
	 *  	 EVENT RESPONDERS		*
	 * 								*
	 * 								*
	 ********************************/
	function addToAddressBook() {
		document.getElementById("greyBackgroundAddressBook").className = "hidden";
		document.getElementById("popupAddressBook").className = "hidden";

		var addressToRequest = "/addToAddressBook?user="+informationHolder.getRecipientAccount()["owner"]["code"]+"&account="+informationHolder.getRecipientAccount()["code"];
		ajaxCall("GET",addressToRequest,addToAddressBookResponse);
	}
	function notAddToAddressBook() {
		document.getElementById("greyBackgroundAddressBook").className = "hidden";
		document.getElementById("popupAddressBook").className = "hidden";
	}
	function closeErrorPopup() {
		document.querySelector("#popupError h2").className = "hidden";
		document.getElementById("successTable").className = "moneyTable hidden";
		document.getElementById("greyBackground").className = "hidden";
		document.getElementById("popupError").className = "hidden";
	}
	function requestHomepage() {
		ajaxCall("GET","/getUserInfo",requestHomepageResponse);
	}
	function logoutClick() {
		ajaxCall("GET","/logout",logoutResponse);
	}
	function accountClick(e) {
		var accountNumber = e.target;
		var addressToRequest = "/accountState?accountCode="+accountNumber.textContent+"&out=1&in=1";
		ajaxCall("GET",addressToRequest,accountClickResponse);
	}
	function ingoingTransferPageClick(e) {
		var pageNumber = e.target.textContent;
		var addressToRequest = "/getTransfers?accountCode="+informationHolder.getAccountInfo()["code"]+"&type=in&in="+pageNumber;
		ajaxCall("GET",addressToRequest,ingoingTransferPageResponse);
	}
	function outgoingTransferPageClick(e) {
		var pageNumber = e.target.textContent;
		var addressToRequest = "/getTransfers?accountCode="+informationHolder.getAccountInfo()["code"]+"&type=out&out="+pageNumber;
		ajaxCall("GET",addressToRequest,outgoingTransferPageResponse);
	}
	function orderTransferClick(e) {
		var enclosingForm = e.target.closest("form");

		if (enclosingForm.checkValidity()) {
			if (amountCheck() && accountCodeCheck()) {
				ajaxCall("POST", "/orderTransfer", orderTransferResponse, enclosingForm);
			}
		} else {
			enclosingForm.reportValidity();
		}
	}
	function accountCodeCheck(e) {
		var accountCode;
		accountCode = document.querySelectorAll("#accountStateForm form input")[1];

		if (parseInt(accountCode.value) === informationHolder.getAccountInfo()["code"]) {
			accountCode.setCustomValidity(informationHolder.getLang()["errorOrderAutoTransfer"]);
			return false;
		} else {
			accountCode.setCustomValidity("");
			return true;
		}
	}
	function amountCheck(e) {
		var amount;
		amount = document.querySelectorAll("#accountStateForm form input")[2];

		if (parseInt(amount.value) > informationHolder.getAccountInfo()["balance"]) {
			amount.setCustomValidity(informationHolder.getLang()["errorOrderAmount"]);
			return false;
		} else {
			amount.setCustomValidity("");
			return true;
		}
	}
})();
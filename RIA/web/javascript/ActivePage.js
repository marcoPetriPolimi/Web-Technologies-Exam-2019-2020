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
				elem.addEventListener("click",ingoingTransferPageClick);
			});
			document.querySelector("#accountStateForm form input[type=button]").addEventListener("click",orderTransferClick);

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

			ingoingTransfersTable = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable");
			ingoingTransfersTableBody = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable tbody");
			ingoingTransfersTableBody.innerHTML = "";
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
		}
		loadOutgoingTransfers(infoHolder) {
			var outgoingTransfersTable, outgoingTransfersTableBody, outgoingPages;

			outgoingTransfersTable = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable");
			outgoingTransfersTableBody = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable tbody");
			outgoingTransfersTableBody.innerHTML = "";
			for (let i = 0; i < infoHolder.getOutgoingTransfers().length; i++) {
				let row = document.createElement("tr");
				let sender = document.createElement("td");
				let senderAccount = document.createElement("td");
				let amount = document.createElement("td");
				let date = document.createElement("td");
				let reason = document.createElement("td");
				sender.textContent = infoHolder.getOutgoingTransfers()[i]["outgoing"]["owner"]["code"];
				senderAccount.textContent = infoHolder.getOutgoingTransfers()[i]["outgoing"]["code"];
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
		}
		orderRefresh(infoHolder) {

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
		}
		showSuccess(infoHolder) {
			document.querySelector("#popupError h2").className = "";
			document.querySelector("#popupError h2").textContent = infoHolder.getLang()["transferResultSuccess"];
			let successText = document.getElementById("popupErrorText");
			let table = document.createElement("table");
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
			successText.append(table);

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
					pageManager.loadHomepage(informationHolder);
					break;

				case 401:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error401;
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error500;
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
					pageManager.loadAccount(informationHolder);
					break;

				case 400:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error400;
					break;

				case 401:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error401;
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error500;
					break;
			}
		}
	}
	function ingoingTransferPageResponse(resp) {

	}
	function outgoingTransferPageResponse(resp) {

	}
	function orderTransferResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var objectReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder.setLang(objectReceived.keys);
					informationHolder.setAccountInfo(objectReceived.account);
					informationHolder.setRecipientAccount(objectReceived.recipientAccount);

					let urlToAsk = "/getTransfers?type=out&out="+informationHolder.getOutPage();
					ajaxCall("GET",urlToAsk,outgoingTransferPageResponse);

					pageManager.orderRefresh(informationHolder);
					pageManager.showSuccess(informationHolder);
					break;

				case 400:
					informationHolder.setLang(objectReceived.keys);
					pageManager.showError(informationHolder,objectReceived.error);
					break;

				case 401:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error401;
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").innerHTML = "";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error500;
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
	function closeErrorPopup() {
		document.querySelector("#popupError h2").className = "hidden";
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
		
	}
	function outgoingTransferPageClick(e) {

	}
	function orderTransferClick(e) {
		var enclosingForm = e.target.closest("form");

		if (enclosingForm.checkValidity()) {
			// TODO: check also values forbidden which aren't checked by html
			ajaxCall("POST","/orderTransfer",orderTransferResponse,enclosingForm);
			enclosingForm.reset();
		} else {
			enclosingForm.reportValidity();
		}
	}
})();
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
			var ingoingTransfersTable, outgoingTransfersTable, ingoingTransfersTableBody, outgoingTransfersTableBody, ingoingPages, outgoingPages;

			document.getElementById("infoTableAccount").textContent = infoHolder.getAccountInfo()["code"];
			document.getElementById("infoTableAmount").textContent = infoHolder.getAccountInfo()["balance"];
			ingoingTransfersTable = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable");
			ingoingTransfersTableBody = document.querySelector("#accountStateTransfers div.ingoingTransfers table.moneyTable tbody");
			outgoingTransfersTable = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable");
			outgoingTransfersTableBody = document.querySelector("#accountStateTransfers div.outgoingTransfers table.moneyTable tbody");
			ingoingTransfersTableBody.innerHTML = "";
			outgoingTransfersTableBody.innerHTML = "";
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

			document.getElementById("homepageInfoBox").style.display = "none";
			document.getElementById("homepageAccounts").style.display = "none";
			document.getElementById("showHomepage").style.display = "block";
			document.getElementById("accountStateInfoBox").style.display = "block";
			document.getElementById("accountStateTransfers").style.display = "block";
			document.getElementById("accountStateForm").style.display = "block";
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

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
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
					informationHolder.setInPage(1);
					informationHolder.setOutPage(1);
					pageManager.loadAccount(informationHolder);
					break;

				case 400:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error400;
					break;

				case 401:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error401;
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
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

	}

	/********************************
	 * 								*
	 * 								*
	 *  	 EVENT RESPONDERS		*
	 * 								*
	 * 								*
	 ********************************/
	function closeErrorPopup() {
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

	}
})();
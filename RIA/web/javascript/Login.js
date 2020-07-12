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
	var informationHolder;

	/********************************
	 * 								*
	 * 								*
	 *   SERVER RESPONSE GETTERS	*
	 * 								*
	 * 								*
	 ********************************/
	function loginResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var jsonReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					sessionStorage.setItem("logged","yes");
					window.location.href = "/activePage";
					break;

				case 400:
					informationHolder = new InformationHolder(jsonReceived.keys);
					document.getElementById("LoginError").className = "error";
					document.getElementById("LoginError").textContent = informationHolder.getLang().errorLogin;
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
					document.getElementById("popupErrorText").textContent = informationHolder.getLang().error500;
					break;
			}
		}
	}
	function registrationResponse(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var jsonReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder = new InformationHolder(jsonReceived.keys);
					document.getElementById("RegistrationError").className = "success";
					document.getElementById("RegistrationError").textContent = informationHolder.getLang().loginRegistrationSuccess;
					break;

				case 400:
					informationHolder = new InformationHolder(jsonReceived.keys,null,null,null,null,null,jsonReceived.error);
					document.getElementById("RegistrationError").className = "error";
					document.getElementById("RegistrationError").textContent = informationHolder.getErrorType();
					break;

				case 500:
					document.getElementById("greyBackground").className = "display";
					document.getElementById("popupError").className = "display";
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
	function loginClick(e) {
		var enclosingForm = e.target.closest("form");

		if (enclosingForm.checkValidity()) {
			ajaxCall("POST","/login",loginResponse,enclosingForm);
		} else {
			enclosingForm.reportValidity();
		}
	}
	function registrationClick(e) {
		var enclosingForm = e.target.closest("form");

		if (enclosingForm.checkValidity()) {
			// TODO: check also values forbidden which aren't checked by html
			ajaxCall("POST","/registration",registrationResponse,enclosingForm);
		} else {
			enclosingForm.reportValidity();
		}
	}
	function closeErrorPopup() {
		document.getElementById("greyBackground").className = "hidden";
		document.getElementById("popupError").className = "hidden";
	}

	document.getElementById("LoginSubmit").addEventListener("click",loginClick);
	document.getElementById("RegistrationSubmit").addEventListener("click",registrationClick);
	document.getElementById("popupErrorClose").addEventListener("click",closeErrorPopup);
})();
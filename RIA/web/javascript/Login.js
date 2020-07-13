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
	var informationHolder = new InformationHolder();

	window.onload = function () {
		document.getElementById("LoginSubmit").addEventListener("click",loginClick);
		document.getElementById("RegistrationSubmit").addEventListener("click",registrationClick);
		document.getElementById("popupErrorClose").addEventListener("click",closeErrorPopup);
		document.querySelectorAll("#registrationForm input")[1].addEventListener("input",emailCorrectPattern);
		document.querySelectorAll("#registrationForm input")[2].addEventListener("input",passwordEquality);
		document.querySelectorAll("#registrationForm input")[3].addEventListener("input",passwordEquality);
		ajaxCall("GET","/language",setLanguage);
	}

	function setLanguage(resp) {
		if (resp.readyState == XMLHttpRequest.DONE) {
			var jsonReceived = JSON.parse(resp.responseText);

			switch (resp.status) {
				case 200:
					informationHolder = new InformationHolder(jsonReceived.keys);
					sessionStorage.setItem("language",informationHolder.getLang()["language"]);
					sessionStorage.setItem("country",informationHolder.getLang()["country"]);
					break;
			}
		}
	}
	function showErrorPopup(text,closeText) {
		document.getElementById("greyBackground").className = "display";
		document.getElementById("popupError").className = "display";
		document.getElementById("popupErrorText").textContent = text;
		document.getElementById("popupErrorClose").textContent = closeText;
	}

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
					showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
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
					showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
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
		document.getElementById("greyBackground").className = "hidden";
		document.getElementById("popupError").className = "hidden";
	}
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
			if (passwordEquality() && emailCorrectPattern()) {
				ajaxCall("POST", "/registration", registrationResponse, enclosingForm);
			}
		} else {
			enclosingForm.reportValidity();
		}
	}
	function passwordEquality(e) {
		var password, repeatedPassword;
		password = document.querySelectorAll("#registrationForm input")[2];
		repeatedPassword = document.querySelectorAll("#registrationForm input")[3];

		if (repeatedPassword.value !== "") {
			if (password.value !== repeatedPassword.value) {
				repeatedPassword.setCustomValidity(informationHolder.getLang()["errorRegistrationPassword"]);
				return false;
			} else {
				repeatedPassword.setCustomValidity("");
				return true;
			}
		}
	}
	function emailCorrectPattern(e) {
		var emailField, pattern;
		emailField = document.querySelectorAll("#registrationForm input")[1];

		pattern = new RegExp(".*[@].*[\\.].*");
		if (!pattern.test(emailField.value)) {
			emailField.setCustomValidity(informationHolder.getLang()["errorRegistrationEmail"]);
			return false;
		} else {
			emailField.setCustomValidity("");
			return true;
		}
	}
})();
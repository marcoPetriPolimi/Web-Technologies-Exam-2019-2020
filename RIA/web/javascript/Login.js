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
	 *   COMPONENTS DEFINITION		*
	 * 								*
	 * 								*
	 ********************************/
	function RegistrationComponent(popupComponent) {
		var self = this;
		this.popupHandler = popupComponent;

		/********************************
		 * 								*
		 *   SERVER RESPONSE GETTERS	*
		 * 								*
		 ********************************/
		this.registrationResponse = function(resp) {
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
						this.popupHandler.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
						break;
				}
			}
		}

		/********************************
		 * 								*
		 *  	 EVENT RESPONDERS		*
		 * 								*
		 ********************************/
		this.registrationClick = function(e) {
			var enclosingForm = e.target.closest("form");

			if (enclosingForm.checkValidity()) {
				if (self.passwordEquality() && self.emailCorrectPattern()) {
					ajaxCall("POST", "/registration", self.registrationResponse, enclosingForm);
				}
			} else {
				enclosingForm.reportValidity();
			}
		}
		this.passwordEquality = function(e) {
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
		this.emailCorrectPattern = function(e) {
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
	}
	function LoginComponent(popupComponent) {
		var self = this;
		this.popupHandler = popupComponent;

		/********************************
		 * 								*
		 *   SERVER RESPONSE GETTERS	*
		 * 								*
		 ********************************/
		this.loginResponse = function(resp) {
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
						this.popupHandler.showErrorPopup(informationHolder.getLang().error500,informationHolder.getLang().popupCloseErrorPopup);
						break;
				}
			}
		}

		/********************************
		 * 								*
		 *  	 EVENT RESPONDERS		*
		 * 								*
		 ********************************/
		this.loginClick = function(e) {
			var enclosingForm = e.target.closest("form");

			if (enclosingForm.checkValidity()) {
				ajaxCall("POST","/login",self.loginResponse,enclosingForm);
			} else {
				enclosingForm.reportValidity();
			}
		}
	}
	function PopupComponent() {
		this.showErrorPopup = function(text,closeText) {
			document.getElementById("greyBackground").className = "display";
			document.getElementById("popupError").className = "display";
			document.getElementById("popupErrorText").textContent = text;
			document.getElementById("popupErrorClose").textContent = closeText;
		}
		this.closeErrorPopup = function() {
			document.getElementById("greyBackground").className = "hidden";
			document.getElementById("popupError").className = "hidden";
		}
	}

	/********************************
	 * 								*
	 * 								*
	 *   		SCRIPT BODY			*
	 * 								*
	 * 								*
	 ********************************/
	var registrationComponent, loginComponent, popupComponent;
	var informationHolder = new InformationHolder();

	window.onload = function () {
		popupComponent = new PopupComponent();
		registrationComponent = new RegistrationComponent(popupComponent);
		loginComponent = new LoginComponent(popupComponent);

		document.getElementById("LoginSubmit").addEventListener("click",loginComponent.loginClick);
		document.getElementById("RegistrationSubmit").addEventListener("click",registrationComponent.registrationClick);
		document.getElementById("popupErrorClose").addEventListener("click",popupComponent.closeErrorPopup);
		document.querySelectorAll("#registrationForm input")[1].addEventListener("input",registrationComponent.emailCorrectPattern);
		document.querySelectorAll("#registrationForm input")[2].addEventListener("input",registrationComponent.passwordEquality);
		document.querySelectorAll("#registrationForm input")[3].addEventListener("input",registrationComponent.passwordEquality);
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
})();
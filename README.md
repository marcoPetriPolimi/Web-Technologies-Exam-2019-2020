# Money Transfer - Tecnologie informatiche per il web Project
This repository is the repository for the Web Technologies (Tecnologie informatiche per il web) exam's project. The delivery for the exam was to create an application in which people can open accounts, log in and make transfers. The application is asked to be safe to HTTP request manipulation and other types of page manipulation activities resulting in inconsistent state for the application. Users and accounts are assumed to exists before the creation of the application, which means that I manually inserted them into the database using MySQL workbench.

## Screenshots
The application starts with a login page in which the user must insert its own id (I supposed that the id is the equivalent of bank id) and the password. If the user inserts wrong information, a message will be printed. This is the login page:

![login page image](https://github.com/marcopetri98/MoneyTransfer_Java_TIW/blob/master/images/login.png)

In case the user inserts wrong login information, a red text will be printed. Similar behaviour will be used also for other parts of the application in which the user can send a form.

![login error](https://github.com/marcopetri98/MoneyTransfer_Java_TIW/blob/master/images/error.png)

Once the user has logged in, he/she can see the list of all his/her accounts and the account's balance. The user can also click on the accounts' id to access the account and make or check transfers.

![page with the information of the accounts of the user](https://github.com/marcopetri98/MoneyTransfer_Java_TIW/blob/master/images/account%20page.png)

When the user access one of its accounts, he/she can see a list of the ingoing and outgoing transfers for that account. Moreover, on the right it is also possible to make transfers to other accounts.

![page with account's transfers](https://github.com/marcopetri98/MoneyTransfer_Java_TIW/blob/master/images/account%20detail.png)

## Technologies
The technologies used to develop this project are: Java, Thymeleaf, HTML, CSS, Javascript and SQL. No framework has been used to develop this project. There are two different versions of the project: pure HTML and RIA. The former does not contain or contain a little javascript in the page, almost every operation is done by means of sending HTTP requests to the server. The latter is realized using AJAX such that only the new information is loaded (e.g., when the user makes a transfer, the thick client receives only the informations of the content and javascript will update the interface of the user).

In both cases, the application has been provided with internationalization. It means that the application verifies the language of the browser of the client and accordingly generates the page with the right language (only italian or english).

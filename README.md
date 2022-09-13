# Description

This project illustrates Spring security (authorization, XSS, CSP...) and Spring OpenID Connect integration with external SSO providers like Auth0.

The developed application is a simplified document management system.
There are 3 types of users:
- System administrator
- Document administrator
- Client

There are also 2 applications:
- System administrator application - application for system administrators.
- Client application - application for all users.

**System administrator application** allows system administrators to:
- Update user accounts - delete an account, deactivate or activate the account, set user role and permissions.
- Set root directory for clients and document administrators.
- Set IP address or domain from which a client can access the application.

**Document application** can be accessed by all users. It allows:
- *Clients* to download their files, upload new ones, change existing files and upload a new version and delete their files. 
- *Document administrators* to create and delete directories within their root, move files from one directory to another and perform all actions that the "Client" role can.
- *System administrators* to view the files of all users, download files and see history of actions performed on a file. 

Users perform login via an external SSO provider.
Users authenticated in one application can access the other one only if they are authorized. 

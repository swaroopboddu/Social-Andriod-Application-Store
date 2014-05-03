Web Application ReadMe file
-------------------------------------
"Entire web application has been built on top of CakePHP Framework"
You can download the latest stable framework here - https://github.com/cakephp/cakephp/zipball/2.4.9

Contribution
-------------------------------------
Entire web application has been developed from scratch by us.
No part of the source code is given by mentor or anyother source.

Pre-Configurations (Check)
---------------------------
Please make sure you have the following as specified in httpd.conf file to ensure framework url rewriting works well.
<Directory />
    Options FollowSymLinks
    AllowOverride All
#    Order deny,allow
#    Deny from all
</Directory>

Make sure you are loading mod_rewrite correctly. You should see something like:
LoadModule rewrite_module libexec/apache2/mod_rewrite.so (This should be uncommented)

Folder Structure - Outline
---------------------------------------
Application follows the MVC architecture pattern and entire application resides inside 'app' folder.
'lib' folder holds the library files of the application and shouldn't be changed in any way unless you are upgrading the version of CakePHP you are using.

Deployment Instructions:
---------------------------------------
Entire Application resides in the 'app' folder - webapp/app/
1) Copy the webapp folder into your webroot.
2) Create the database using schema file uploaded along with the code.
3) Change the database connection setting in webapp/app/Config/database.php file accordingly.
	Eg: 'database' => 'social_app_store' TO 'database' => 'YOUR_DATABASE_NAME'
	Change the rest of the settings accordingly.

Folder Structure
----------------------------------------
webapp/app/Model --> All the Models of the application are inside the 'Model' folder
webapp/app/Controller --> All the Controllers of the application are inside the 'Controller' folder	
webapp/app/View --> All the Views (html files) of the application are inside the 'View' folder
webapp/app/webroot --> Webroot for the project
	'css' - Holds all the css files
	'img' - Holds all the images and icons of the application
	'js' - Holds all the javascript files of the application.
	'uploads' - Holds all the user uploads E.g., 'Developer applications, images etc'



Contact
----------------------------------------
Please send an email to 'avallab1@asu.edu' for any Deployment issues.
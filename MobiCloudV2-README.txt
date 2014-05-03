Android  Project Read Me File
-------------------------------
Instructions to run:
----------------------
1. Make sure that the web-application is running at http://androidgeekvm.vlab.asu.edu/webapp/ 
2. Load the APK file into the android mobile and install it.
3. Then register as a new user and login with that user. 
4. You should be in the application home screen.

Instructions to run build and run the project
-----------------------------------------------
1. Download the android development kit from http://developer.android.com/sdk/index.html
2. Update SDK and install Google play services. 
2. After that download the source code into the workspace.
3. Once the source code is downloaded, import it into the eclipse.
4. Now clean the project and build it in eclipse.
5. Now export the project and provide a public key if asked while exporting.

Contributions:
----------------
Project is coded starting from the scratch. No part of source code is from another source or provided by mentor.

Code details:(Code folder MobiCloudV2)
-------------
1. It contains the structure of the basic android project.
2. Android manifest file has all the Android activities used.
3. edu.asu.mobicloud package contains all the activities.
4. edu.asu.mobicloud.retrofit package contains all the rest client code.
5. edu.asu.mobicloud.gcm contains the logic for push notifications.
6. edu.asu.mobicloud.dataproviders contains data providers.

Design Patterns used:
---------------------
1. To update the UI if data gets updated from remote server, we used observer and observable pattern. All the data providers are observable.
2. Also used call backs for remote rest requests to avoid running rest client thread in main or UI thread. As not doing so may cause application not responding errors. 


Libraries used:
---------------
1. Retrofit Rest Client API is used to design the rest client.
2. GSON API to connect for object to JSON and JSON to Object conversions 
3. Google play service library to use Google Cloud Messaging service. 

Please send an email to 'sboddu1@asu.edu' for queries.

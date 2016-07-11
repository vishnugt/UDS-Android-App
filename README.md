#Android Supervisor App UDS:


This app is built on SDK Version 23, which supports Android phones with SDK version from 15 upto the latest, since Android is usually backward compaitable.  
The app is version Controlled at https://github.com/vishnugt/UDS-Android-App.
	
#Configuration:

        minSdkVersion 15
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"


#Build Instructions:

  If you want to build the existing code, we recommend you to download Android Studio (preferably 2.0), and download the latest SDK.  Once you are ready with the setup, clone or download the git repository from https://github.com/vishnugt/UDS-Android-App.  Import the code in Android Studio and compile and run the application.  


#Permissions Application need:

ACCESS_WIFI_STATE

ACCESS_NETWORK_STATE

INTERNET



#List of Java files:

Activity_Client.java 

BirlaFeedbackActivity.java

ClientSelection_Activity.java

DataObject.java

DataObject2.java

Delivery_or_Visit.java

Material_Activity.java

MonthYearPicker.java

MyRecyclerViewAdapter.java

RecyclerViewforFeedback.java

RecyclerViewforMaterials.java

TimeInTimeOut.java

Feedback_activity.java

Login_activity.java


#Activities:


Login_activity.java

	Login activity is the first activity when we open the app, it contains two edittexts to get the username and password from the user.  After that the username and password are authenticated using the login API.

Activity_Client.java 

	This activity is linked with activity_client.xml.  This page shows us two options - Birla and Shriram.  From this activity user will be able to move to ClientSelectionActivity.

BirlaFeedbackActivity.java

	This activity shows all the questions that are fetched from xtime.  Depending on the number of questions, dynamically we create the number of edittexts and spinner.  After the user filled all the choices, they can click the submit button and a request will be created.  If it shows a toast saying “Unsuccessful”, it means there is some internal error.


ClientSelection_Activity.java

	After choosing either Birla or Shriram, the user has to choose the location of the client.  This activity lists all the client’s location.  From this activity user will be moved to Delivery_or_Visit.java.

Delivery_or_Visit.java

	This activity gives the user two options, either to choose from Materials Delivery or Supervisor Visit.  This activity also displays the user a list of previous request list.  From this activity, user will be taken to either Feedback activity or Material Delivery activity.

Material_Activity.java

	This activity lists all the materials name after fetching it from xtime.  This list is also dynamically populated.  After filling the details, the user can press submit and the request will be created.

MonthYearPicker.java

	This java is support class to feature the month and year picker while we choose from delivery or visit.  Server time is fetched from xtime and using that, we populate the month list with previous, current and next month.

MyRecyclerViewAdapter.java
RecyclerViewforFeedback.java
RecyclerViewforMaterials.java

	These 3 java classes are Recycler View Adapter used throught the app to dynamically populate the lists.  These Adapter are used to create card views with materials and feedbacks.

TimeInTimeOut.java

	This activity is used to get the Time In and Time Out from the supervisor after selecting delivery or visit.

Feedback_activity.java

	This activity is used to get the feedback for the client.  After the user filled his options he can submit the response by clicking submit button.

 
#Layout XML Files: 

Activity_birla_feedback.xml  

Activity_card_view.xml 

Activity_client.xml

Activity_feedback_activity.xml

Activity_listview_activity.xml

Activity_time_in_time_out.xml

Layout_delivery_supervisor.xml

Login_layout.xml

Material_cardview.xml


#Primitive XML Layout Views:

Card_view_row.xml

Month_year_picker_view.xml

Feedback_cardview.xml

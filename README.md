Initial Setup
=============
1. Create your Remote Access endpoint in your Salesforce Company
   - http://www.salesforce.com/us/developer/docs/chatterapi/Content/quickstart_oauth.htm
   - the callback URL has to be _https://login.salesforce.com/services/oauth2/success_
2. (optional) rebuild your own example app
    gradle fatJar
  
Running the example
===================

1. copy _forcedotcom-credentials.properties.example_ to _forcedotcom-credentials.properties_ and update the file with your specific oauth credentials
2. run the application
   java -jar build/libs/dreamforce-*.jar --credentials=forcedotcom-credentials.properties


Commands in the example
=======================
The example is an interactive command line app. You can get a list of all available commands with

    ?list

You should start with:

    oauth-flow-start

This will open your browser and you have to allow the Remote Access endpoint to access your Salesforce account. You will be redirected to fairly white page from salesforce. Now copy the url and run the next command in the dreamforce-12 prompt:

    oauth-flow-finish 'THE-COPIED-URL'

Please put the url in ' to be safe for any command characters.

After you did this your oauth connection is safed and you can see it with

    oauth-list-connections [true]

Now you can run the chatter examples

    chatter-get-groups
    chatter-post-to-group GROUP_ID 'The text to post'

Implementation remarks
======================
The OAuth setup for spring-social is found in the package com.conceptboard.social.provider.forcedotcom Split between api and connect. The sub-package api is the acutal implementation of Chatter API Calls and the sub-package connect contains all OAuth Flow related classes.

The package com.conceptboard.social.oauth2useragentflow is modelled after the spring-social packages to implement the minimum required for UserAgentFlow.

You will see that for Salesforce the ConnectionRepository has to be adapted. Because with the salesforce API you need to store additional data for every user. This is the instanceUrl of the user. This is not originally a part of spring-social and you can find an implementation in com.conceptboard.social.simpleconnectionrepository together with com.conceptboard.social.ConnectionData


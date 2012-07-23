Spring Social PayPal Quickstart With OpenId Connect (Spring 3.0.x)
==============================================

This sample app shows how use [Spring Social PayPal] to login with a PayPal user and obtain identity information.

    git clone git@github.com/abhijith-prabhakar/spring-social-paypal-openidconnect.git
    git clone git@github.com/abhijith-prabhakar/spring-social-security-paypal-openidconnect-quickstart.git	
    cd spring-social-paypal
    mvn clean install
    cd ../spring-social-security-paypal-openidconnect-quickstart/
    mvn tomcat:run

Then browse: `http://localhost:8080/spring-social-security-paypal-openidconnect-quickstart/signin`

Some of the interesting lessons learnt while developing this application:

Final goal was to integrate Spring social and Spring security with PayPal OpenId Connect protocol

First task was to have a separate module for spring-social-paypal with openid connect instead of OAuth2.
This can be found at 
https://github.com/abhijith-prabhakar/spring-social-paypal-openidconnect.git

Please note that this branch(socialsignin-filter) employs another way to authorize user into application.  For the first approach please look at ReadMe file of master branch of this repo.

This approach is based heavily on spring-social-security github project.
https://github.com/socialsignin/spring-social-security.git

Important: You could just use spring-social-security project as dependency and can eliminate lot of classes and duplicated code.  I could not do that because spring-social-security loads a UserNamePasswordAuthenticationToken by default and ignores userdetails.  Our application need userdetails(much more than username and password)  than what is available in this token.  I could not find an easy way to override certain methods as most of the beans were autowired using qualifiers.  The best way I found is to take out spring-social-security dependency from our project and essentially copy the stuff which is needed by our quickstart project.   Also, note that there is an optional remember me service which you can use.

Please follow below steps to configure it in you app
1. You do not need connectionRepository and paypal bean definitions in your SocialConfig.  You will create connection in filter instead of configuration.
2. Define a Filter which extends spring security AbstractAuthenticationProcessingFilter and override attemptAuthentication() method.  Remember this filter gets invoked to the uri which you configure it to usually in its constructor.  In this example, we are using "/authenticate" as the uri. Make sure you have postSignInUrl property of ProviderSignInController set to "/authenticate"
3.  Configure Your SigInAdapter to either store user details in session or in cookie.  In this example we are storing it in session.
4. Define custom UserAuthoritiesService and depending on user who is logging in, you can grant authorities here.
5. Configure your spring-security.xml file, and grant access to according to your application needs.  For example you can have a look at /WEB-INF/spring/spring-security-config.xml.

Note: Most of the stuff of managing connections in userrepository were copied from spring-social-security and I did not change it.  You can always give custom implementation for these instead of the one included in this project.

Hope this helps!!

---------------------------------------------------------------------

This app was based on [Spring Social Security Demo].
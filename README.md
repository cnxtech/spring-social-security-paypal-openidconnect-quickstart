Spring Social PayPal Quickstart (Spring 3.0.x)
==============================================

This sample app shows how use [Spring Social PayPal] to login with a PayPal user and obtain identity information.

    git clone git@github.com:abhijith-prabhakar/spring-social-paypal-openidconnect.git
    git clone git@github.com:abhijith-prabhakar/spring-social-security-paypal-openidconnect-quickstart.git
    cd spring-social-paypal-openidconnect
    mvn clean install
    cd ../spring-social-security-paypal-openidconnect-quickstart/
    mvn tomcat:run

Then browse: `http://localhost:8080/spring-social-paypal-quickstart-30x/signin`

To check if security is working, you can directly try to access `http://localhost:8080/spring-social-paypal-quickstart-30x/home` and you should be routed back to login page which is /signin

Learn how to use Spring Social PayPal with OpenId Connect in your application:

Some of the interesting lessons learnt while developing this application:

Final goal was to integrate Spring social and Spring security with PayPal OpenId Connect protocol

First task was to have a separate module for spring-social-paypal with openid connect instead of OAuth2. This can be found at https://github.com/abhijith-prabhakar/spring-social-paypal-openidconnect.git

Please note that this branch(master) employs one of the 2 ways to authorize user into application. For the second approach please look at ReadMe file of (socialsignin-filter) branch of this repo.  This is a simpler approach compared to the other one.

This approach is based on tutorial find at: `http://harmonicdevelopment.tumblr.com/post/13613051804/adding-spring-social-to-a-spring-mvc-and-spring`

1. Override signin method of SignInAdapter, to create a security context.  In this example SecurityUtil class takes care of this.
2. Authorizations and security is specific to your application and define them to your application needs.
for ex: /WEB-INF/spring/spring-security-config.xml



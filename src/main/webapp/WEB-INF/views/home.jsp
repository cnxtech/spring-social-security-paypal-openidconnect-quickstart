<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Home</title>
        <script>
            if(window.opener) {
                window.opener.location.replace(document.URL);
                window.close();
            }
        </script>
	</head>
	<body>
	<ul>
		<li><a href="<c:url value="/signout" />">Sign Out</a></li>
	</ul>
	<h3>Your PayPal Profile</h3>
	<ul>
        <li>User ID: <c:out value="${profile.userId}"/></li>
        <li>Full Name (including middle name): <c:out value="${profile.name}"/></li>
        <li>First Name: <c:out value="${profile.givenName}"/></li>
        <li>Last Name: <c:out value="${profile.familyName}"/></li>
        <li>Locale: <c:out value="${profile.locale}"/></li>
        <li>Verified Status: <c:out value="${profile.verified}"/></li>
        <li>Email: <c:out value="${profile.email}"/></li>
        <li>
            Address
            <ul>
                <li>Street 1: <c:out value="${profile.address.streetAddress}"/></li>
                <li>City: <c:out value="${profile.address.locality}"/></li>
                <li>State: <c:out value="${profile.address.region}"/></li>
                <li>Country: <c:out value="${profile.address.country}"/></li>
                <li>Zip: <c:out value="${profile.address.postalCode}"/></li>
            </ul>
        </li>        
	</ul>	
	</body>
</html>
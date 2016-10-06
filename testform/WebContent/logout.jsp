<%
  session.invalidate();

  Cookie cookies[] = request.getCookies();
  Cookie sessionCookie = null;

  if (cookies != null) {
    for (final Cookie cooky : cookies) {
      if (cooky.getName().equals("JSESSIONID")) {
        sessionCookie = cooky;
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath(request.getContextPath());
        response.addCookie(sessionCookie);
      }
    }
  }
%>

<!DOCTYPE HTML>
<html>
<head>
  <meta http-equiv="refresh" content="1; URL=."/>
</head>
<body>
<div id="page"></div>
</body>
</html>

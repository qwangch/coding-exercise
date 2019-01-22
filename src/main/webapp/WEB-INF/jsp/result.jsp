<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

</head>
<body>

	<div class="container">
        <h2 class="result-title">Potential Duplicates: ${message}</h2>
         <table class="table table-striped">
             <tbody>
                   <c:forEach items="${duplicates}" var="temp">
                     <tr>
                         <td>${temp}</td>

                     </tr>
                    </c:forEach>
             </tbody>
         </table>
	</div>

	<div class="container">
        <h2 class="result-title">None Duplicates: ${message}</h2>
            <table class="table table-striped">
                <tbody>
                    <c:forEach items="${uniques}" var="temp">
                       <tr>
                        <td>${temp}</td>
                        </tr>
                     </c:forEach>
                </tbody>
            </table>
    </div>


</body>

</html>

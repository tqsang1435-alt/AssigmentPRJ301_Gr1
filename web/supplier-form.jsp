<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <html>

        <head>
            <title>Supplier Form</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="admin-sidebar.jsp" />
                <div class="admin-main">
                    <div class="admin-header">
                        <h2>Supplier Management</h2>
                    </div>
                    <div class="admin-content">
                        <div class="container-fluid" style="max-width: 600px; margin: 0 auto;">
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title">
                                        <c:choose>
                                            <c:when test="${supplier != null}">Update Supplier</c:when>
                                            <c:otherwise>Add Supplier</c:otherwise>
                                        </c:choose>
                                    </h3>
                                </div>
                                <div class="card-body">
                                    <form action="supplier" method="post">
                                        <c:if test="${not empty error}">
                                            <div class="alert alert-danger">${error}</div>
                                        </c:if>

                                        <c:choose>
                                            <c:when test='${supplier != null}'><input type="hidden" name="action"
                                                    value="update" /></c:when>
                                            <c:otherwise><input type="hidden" name="action" value="add" /></c:otherwise>
                                        </c:choose>

                                        <c:if test="${supplier != null}">
                                            <input type="hidden" name="id" value="${supplier.id}" />
                                        </c:if>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Supplier Name</label>
                                            <input type="text" name="name" class="form-control" value="${supplier.name}"
                                                required>
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Contact Name</label>
                                            <input type="text" name="contact" class="form-control"
                                                value="${supplier.contactName}">
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Phone</label>
                                            <input type="text" name="phone" class="form-control"
                                                value="${supplier.phone}">
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Email</label>
                                            <input type="email" name="email" class="form-control"
                                                value="${supplier.email}">
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Address</label>
                                            <input type="text" name="address" class="form-control"
                                                value="${supplier.address}">
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Logo URL</label>
                                            <input type="text" name="logo" class="form-control"
                                                value="${supplier.logo}">
                                        </div>

                                        <div class="form-group mb-3">
                                            <label class="form-label">Status</label>
                                            <select name="status" class="form-control">
                                                <option value="true" ${supplier==null || supplier.status ? 'selected'
                                                    : '' }>Active</option>
                                                <option value="false" ${supplier !=null && !supplier.status ? 'selected'
                                                    : '' }>Inactive</option>
                                            </select>
                                        </div>

                                        <div class="mt-4 d-flex justify-content-between">
                                            <a href="supplier" class="btn btn-secondary">Back</a>
                                            <button type="submit" class="btn btn-success">
                                                <c:choose>
                                                    <c:when test="${supplier != null}">Update</c:when>
                                                    <c:otherwise>Add</c:otherwise>
                                                </c:choose>
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>
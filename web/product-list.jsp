<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@page import="java.util.*, vn.edu.phoneshop.model.Product" %>

        <html>

        <head>
            <title>Product Management</title>
        </head>

        <body>

            <h2>Product List</h2>

            <!-- Form thêm sản phẩm -->
            <form action="products" method="post">
                Name: <input type="text" name="name" required>
                Price: <input type="number" step="0.01" name="price" required>
                Quantity: <input type="number" name="quantity" required>
                Description: <input type="text" name="description">
                Image URL: <input type="text" name="image">
                <button type="submit">Add</button>
            </form>

            <br>

            <table border="1">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Description</th>
                    <th>Image</th>
                    <th>Action</th>
                </tr>

                <% List<Product> list = (List<Product>) request.getAttribute("list");

                        if (list != null && !list.isEmpty()) {
                        for(Product p : list){
                        %>
                        <tr>
                            <td>
                                <%= p.getProductId() %>
                            </td>
                            <td>
                                <%= p.getProductName() %>
                            </td>
                            <td>
                                <%= p.getPrice() %>
                            </td>
                            <td>
                                <%= p.getQuantity() %>
                            </td>
                            <td>
                                <%= p.getDescription() %>
                            </td>
                            <td>
                                <img src="<%= (p.getImage() != null && !p.getImage().trim().isEmpty()) ? p.getImage() : request.getContextPath() + "
                                    /assets/img/default-phone.png" %>" width="60" height="60"
                                onerror="this.onerror=null;this.src='<%= request.getContextPath() %>
                                    /assets/img/default-phone.png';"/>
                            </td>
                            <td>
                                <a href="products?action=delete&id=<%= p.getProductId() %>">
                                    Delete
                                </a>
                            </td>
                        </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="7" style="text-align:center;">
                                    No products found!
                                </td>
                            </tr>
                            <% } %>

            </table>

        </body>

        </html>
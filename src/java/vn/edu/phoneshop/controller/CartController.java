package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Cart;
import vn.edu.phoneshop.model.CartItem;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "CartController", urlPatterns = {
    "/view-cart",
    "/cart",
    "/add-to-cart",
    "/add-cart",
    "/remove-from-cart",
    "/update-cart",
    "/clear-cart",
    "/toggle-select"
})
public class CartController extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/view-cart":
            case "/cart":
                viewCart(request, response);
                break;
            case "/add-to-cart":
                addToCart(request, response);
                break;
            case "/add-cart":
                addCart(request, response);
                break;
            case "/remove-from-cart":
                removeFromCart(request, response);
                break;
            case "/update-cart":
                updateCart(request, response);
                break;
            case "/clear-cart":
                clearCart(request, response);
                break;
            case "/toggle-select":
                toggleSelect(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/view-cart");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object cartObj = session.getAttribute("cart");
        Cart cart = null;
        if (cartObj != null && cartObj instanceof Cart) {
            cart = (Cart) cartObj;
            System.out.println("Cart found in session. Total items: " + cart.getTotalQuantity());
        } else {
            System.out.println("Cart NOT found in session (or invalid type). Creating new cart.");
            cart = new Cart();
            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", 0);
        }

        // Cập nhật % giảm giá theo hạng thành viên nếu đã đăng nhập
        User account = (User) session.getAttribute("ACC");
        if (account != null) {
            double discount = CheckoutControl.getDiscountPercent(account.getCustomerType());
            cart.setDiscountPercent(discount);
        } else {
            cart.setDiscountPercent(0);
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            if (productIDStr == null) {
                productIDStr = request.getParameter("pid");
            }
            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            System.out.println("Input PID: " + productIDStr + ", Quantity: " + quantityStr);
            int productID = Integer.parseInt(productIDStr);
            int quantity = 1;
            if (quantityStr != null && !quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
            }
            if (quantity < 1) {
                quantity = 1;
            }
            
            Product product = productDAO.getProductByID(productID);
            if (product != null) {
                System.out.println("Product Found: " + product.getProductName());
                HttpSession session = request.getSession();
                Object cartObj = session.getAttribute("cart");
                Cart cart = null;
                if (cartObj != null && cartObj instanceof Cart) {
                    cart = (Cart) cartObj;
                } else {
                    cart = new Cart();
                }
                
                // Kiểm tra tồn kho
                CartItem existing = cart.getItemByProductID(productID);
                int currentQty = (existing != null) ? existing.getQuantity() : 0;
                if (currentQty + quantity > product.getStockQuantity()) {
                    session.setAttribute("errorMessage", "Sản phẩm " + product.getProductName() + " không đủ hàng. Chỉ còn " + product.getStockQuantity() + " sản phẩm.");
                    response.sendRedirect(request.getContextPath() + "/view-cart");
                    return;
                }
                
                cart.addProduct(product, quantity);
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.getTotalQuantity());
                System.out.println("Added to cart. Current Total Quantity: " + cart.getTotalQuantity());
            } else {
                System.out.println("ERROR: Product is NULL for ID = " + productID);
            }

            String returnURL = request.getParameter("returnURL");
            if (returnURL == null || returnURL.isEmpty()) {
                String referer = request.getHeader("Referer");
                returnURL = (referer != null && !referer.isEmpty()) ? referer
                        : request.getContextPath() + "/product-list";
            }
            response.sendRedirect(returnURL);
            System.out.println("--- DEBUG: AddToCartControl END ---");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: NumberFormatException " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    private void addCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productIdStr = request.getParameter("id");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("ACC");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if ("buynow".equals(action)) {
            double totalAmount = 0;

            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Product p = productDAO.getProductByID(id);
                if (p != null) {
                    totalAmount = p.getPrice();
                }
            } else {
                Object totalMoneyObj = session.getAttribute("totalMoney");
                if (totalMoneyObj != null) {
                    totalAmount = (Double) totalMoneyObj;
                }
            }
            int orderId = orderDAO.createOrder(user.getUserID(), totalAmount, 1);
            session.setAttribute("currentOrderId", orderId);

            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Product p = productDAO.getProductByID(id);
                orderDAO.insertOrderDetail(orderId, id, 1, p.getPrice());

                request.setAttribute("quickProduct", p);
                request.setAttribute("payMode", "single");
            } else {
                Map<Integer, Integer> cartItems = (Map<Integer, Integer>) session.getAttribute("cart_map"); // Fallback for add-cart style, but this is conflicting with Cart model above
                if (cartItems != null) { // Note: original AddCartController used Map cart, while AddToCart used model.Cart!
                    for (Integer pId : cartItems.keySet()) {
                        Product p = productDAO.getProductByID(pId);
                        orderDAO.insertOrderDetail(orderId, pId, cartItems.get(pId), p.getPrice());
                    }
                }
                request.setAttribute("payMode", "cart");
            }
            request.getRequestDispatcher("quick-pay.jsp").forward(request, response);
            return;
        }
        try {
            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart_map"); // Separating map session
                if (cart == null) {
                    cart = new HashMap<>();
                }

                if ("sub".equals(action)) {
                    if (cart.containsKey(id)) {
                        int newQty = cart.get(id) - 1;
                        if (newQty <= 0) {
                            cart.remove(id);
                        } else {
                            cart.put(id, newQty);
                        }
                    }
                } else if ("remove".equals(action)) {
                    cart.remove(id);
                } else {
                    cart.put(id, cart.getOrDefault(id, 0) + 1);
                }
                updateCartSessionMap(session, cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Điều hướng quay lại
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("cart")) {
            response.sendRedirect("cart.jsp");
        } else {
            response.sendRedirect("home");
        }
    }

    private void updateCartSessionMap(HttpSession session, Map<Integer, Integer> cart) {
        List<Map<String, Object>> cartItems = new ArrayList<>();
        int totalCount = 0;
        double totalMoney = 0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product p = productDAO.getProductByID(entry.getKey());
            if (p != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("product", p);
                item.put("quantity", entry.getValue());
                cartItems.add(item);
                totalCount += entry.getValue();
                totalMoney += p.getPrice() * entry.getValue();
            }
        }
        session.setAttribute("cart_map", cart); // Renamed session attribute avoid conflict
        session.setAttribute("cartItems", cartItems);
        session.setAttribute("mapCartCount", totalCount);
        session.setAttribute("totalMoney", totalMoney);
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productIDStr = request.getParameter("productID");

            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            int productID = Integer.parseInt(productIDStr);

            HttpSession session = request.getSession();
            Object cartObj = session.getAttribute("cart");
            Cart cart = null;
            if (cartObj != null && cartObj instanceof Cart) {
                cart = (Cart) cartObj;
            }

            if (cart != null) {
                cart.removeProduct(productID);

                User account = (User) session.getAttribute("ACC");
                if (account != null) {
                    cart.setDiscountPercent(CheckoutControl.getDiscountPercent(account.getCustomerType()));
                }

                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.getTotalQuantity());
            }

            response.sendRedirect(request.getContextPath() + "/view-cart");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view-cart");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");

            if (productIDStr == null || productIDStr.isEmpty() || quantityStr == null || quantityStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);

            if (quantity < 1) {
                response.sendRedirect(request.getContextPath() + "/remove-from-cart?productID=" + productID);
                return;
            }

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                Product p = productDAO.getProductByID(productID);
                if (p != null) {
                    if (quantity > p.getStockQuantity()) {
                        session.setAttribute("errorMessage", "Sản phẩm " + p.getProductName() + " không đủ hàng. Chỉ còn " + p.getStockQuantity() + " sản phẩm.");
                        response.sendRedirect(request.getContextPath() + "/view-cart");
                        return;
                    }
                }

                cart.updateQuantity(productID, quantity);
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.getTotalQuantity());
            }

            response.sendRedirect(request.getContextPath() + "/view-cart");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view-cart");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = new Cart();
        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/view-cart");
    }

    private void toggleSelect(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productIDStr = request.getParameter("productID");

            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            int productID = Integer.parseInt(productIDStr);

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                CartItem item = cart.getItemByProductID(productID);
                if (item != null) {
                    item.setSelected(!item.isSelected()); // toggle
                }
            }

            response.sendRedirect(request.getContextPath() + "/view-cart");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view-cart");
        }
    }
}

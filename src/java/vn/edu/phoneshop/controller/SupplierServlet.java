package vn.edu.phoneshop.controller;

import vn.edu.phoneshop.dao.SupplierDAO;
import vn.edu.phoneshop.model.Supplier;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/supplier")
public class SupplierServlet extends HttpServlet {

    private SupplierDAO dao = new SupplierDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            // flash message support (store in session from POST)
            HttpSession session = request.getSession();
            String flash = (String) session.getAttribute("message");
            if (flash != null) {
                request.setAttribute("message", flash);
                session.removeAttribute("message");
            }
            switch (action) {

                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Supplier supplier = dao.getById(id);
                    request.setAttribute("supplier", supplier);
                    request.getRequestDispatcher("supplier-form.jsp")
                           .forward(request, response);
                    break;

                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    dao.updateStatus(deleteId, false); // soft-disable
                    response.sendRedirect("supplier");
                    break;

                case "toggle":
                    int toggleId = Integer.parseInt(request.getParameter("id"));
                    dao.toggleStatus(toggleId);
                    response.sendRedirect("supplier");
                    break;

                case "remove":
                    int removeId = Integer.parseInt(request.getParameter("id"));
                    dao.delete(removeId); // hard delete
                    response.sendRedirect("supplier");
                    break;

                case "search":
                    String keyword = request.getParameter("keyword");
                    List<Supplier> searchList = dao.searchByName(keyword);
                    request.setAttribute("suppliers", searchList);
                    request.getRequestDispatcher("supplier-list.jsp")
                           .forward(request, response);
                    break;

                default: // LIST
                    List<Supplier> list = dao.getAll();
                    request.setAttribute("suppliers", list);
                    request.getRequestDispatcher("supplier-list.jsp")
                           .forward(request, response);
                    break;
            }

        } catch (Exception e) {
            throw new RuntimeException(e); // Không nuốt lỗi nữa
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            String name = request.getParameter("name");
            String contact = request.getParameter("contact");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String logo = request.getParameter("logo");

            // basic server-side validation
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Supplier name is required.");
                // preserve form values
                Supplier sErr = null;
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idStr);
                        sErr = new Supplier(id, name, contact, phone, email, address, logo, true);
                    } catch (NumberFormatException ex) {
                        sErr = new Supplier(name, contact, phone, email, address, logo, true);
                    }
                } else {
                    sErr = new Supplier(name, contact, phone, email, address, logo, true);
                }
                request.setAttribute("supplier", sErr);
                request.getRequestDispatcher("supplier-form.jsp").forward(request, response);
                return;
            }

            if ("add".equals(action)) {

                Supplier s = new Supplier(name, contact, phone, email, address, logo, true);
                dao.insert(s);
                request.getSession().setAttribute("message", "Supplier added successfully.");

            } else if ("update".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                Supplier s = new Supplier(id, name, contact, phone, email, address, logo, true);
                dao.update(s);
                request.getSession().setAttribute("message", "Supplier updated successfully.");
            }

            response.sendRedirect("supplier");

        } catch (Exception e) {
            // show error page with message
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("supplier-list.jsp").forward(request, response);
        }
    }
}
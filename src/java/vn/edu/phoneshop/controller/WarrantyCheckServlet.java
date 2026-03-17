package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.IMEIDAO;
import vn.edu.phoneshop.model.IMEI;

@WebServlet(name = "WarrantyCheckServlet", urlPatterns = { "/warranty-check" })
public class WarrantyCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Điều hướng tới trang tra cứu bảo hành
        request.getRequestDispatcher("warranty.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imeiNumber = request.getParameter("imei");
        IMEIDAO imeiDAO = new IMEIDAO();
        IMEI imei = imeiDAO.findBySerial(imeiNumber);

        if (imei != null) {
            request.setAttribute("imei", imei);
        } else {
            request.setAttribute("error", "Không tìm thấy thông tin bảo hành cho số IMEI này.");
        }

        request.getRequestDispatcher("warranty.jsp").forward(request, response);
    }
}
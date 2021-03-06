/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Logica.Controladora;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
@WebServlet(name = "SvReserva", urlPatterns = {"/SvReserva"})
public class SvReserva extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SvReserva</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SvReserva at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String fechaIngreso = request.getParameter("fechaIngreso");
            String fechaEgreso = request.getParameter("fechaEgreso");
            String idHabitacion = request.getParameter("idHabitacion");
            String piso = request.getParameter("piso");

            String idEmpleado = request.getParameter("idEmpleado");

            Controladora control = new Controladora();

            boolean disponibilidad = control.verificarFecha(fechaIngreso, fechaEgreso, idHabitacion, piso);

            if (disponibilidad == true) {

                HttpSession misession = request.getSession(true);

                LocalDate fechaHoy = LocalDate.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String fechaReservaH = fechaHoy.format(formatter);

                System.out.println("FechaReservaH : " + fechaReservaH);

                String nya = request.getParameter("nya");
                String fechaNac = request.getParameter("fechaNac");
                String dni = request.getParameter("dni");
                String tipoHabitacion = request.getParameter("tipo");
                String direccion = request.getParameter("direccion");
                String profesion = request.getParameter("profesion");
                String tematica = request.getParameter("tematica");
                String numeroHabitacion = request.getParameter("idHabitacion");

                request.getSession().setAttribute("nya", nya);
                request.getSession().setAttribute("fechaNac", fechaNac);
                request.getSession().setAttribute("dni", dni);
                request.getSession().setAttribute("tipoHabitacion", tipoHabitacion);
                request.getSession().setAttribute("direccion", direccion);
                request.getSession().setAttribute("tematica", tematica);
                request.getSession().setAttribute("numeroHabitacion", numeroHabitacion);
                request.getSession().setAttribute("piso", piso);

                control.crearReserva(numeroHabitacion, fechaIngreso, fechaEgreso, tipoHabitacion, piso, dni, fechaReservaH, idEmpleado);
                control.guardarHuesped(dni, nya, fechaNac, direccion, profesion);
                double precio = control.calcularPrecio(fechaIngreso, fechaEgreso, tipoHabitacion);
                control.guardarHabitacion(idHabitacion, piso, tematica, tipoHabitacion, precio);

                misession.setAttribute("fechaIngreso", fechaIngreso);
                misession.setAttribute("fechaEgreso", fechaEgreso);
                misession.setAttribute("tipoHabitacion", tipoHabitacion);

                response.sendRedirect("menu.jsp");

            } else {
                HttpSession misession = request.getSession();
                misession.setAttribute("idEmpleado", idEmpleado);

                response.sendRedirect("FechaOcupada.jsp");

                System.out.println("CHAU");
            }
        } catch (ParseException ex) {
            Logger.getLogger(SvReserva.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("index.jsp");
        } catch (Exception ex) {
            Logger.getLogger(SvReserva.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

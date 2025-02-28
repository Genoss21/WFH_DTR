package dev.tgsi.attendance_registration_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Error 500
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(HttpServletRequest request, Exception ex) {
        logger.error("Unexpected error occurred", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "An unexpected error occurred");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("status", 500);
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    // Error 403
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        logger.warn("Access denied for user attempting to access: {}", request.getRequestURI(), ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "Access Denied");
        modelAndView.addObject("message", "You do not have permission to access this page.");
        modelAndView.addObject("status", 403);
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    // Error 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFoundException(HttpServletRequest request, Exception ex) {
        logger.warn("Resource not found: {}", request.getRequestURI(), ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "Page Not Found");
        modelAndView.addObject("message", "The page you are looking for does not exist.");
        modelAndView.addObject("status", 404);
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }
}

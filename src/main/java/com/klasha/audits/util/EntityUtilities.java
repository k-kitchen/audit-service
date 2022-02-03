package com.klasha.audits.util;


import com.klasha.audits.enums.Action;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.klasha.audits.enums.Action.*;


public class EntityUtilities {

    public static String getTableName(Object object) {
        String tableName = "";
        try {
            Field field = object.getClass().getDeclaredField("tableName");
            tableName = (String) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public static ArrayList<String> getRequestRemoteAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        ArrayList<String> response = new ArrayList<>();
        response.add(request.getSession().getId());
        response.add(request.getRemoteHost());
        response.add(request.getRequestURL().toString());
        response.add(request.getRequestedSessionId());
        return response;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return request;
    }


    public static String getFieldValue(Object object, String fieldName) {
        String fieldValue = "";
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            fieldValue = (String) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static boolean getBFieldValue(Object object, String fieldName) {
        boolean fieldValue = false;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            fieldValue = (boolean) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static String createActionText(Object object, Action action) {
        String userAction = "";
        if (action == INSERTED) {
            userAction = "Inserted " + getFieldValue(object, "entity");
        } else if (action == UPDATED) {
            userAction = "Updated " + getFieldValue(object, "entity");
        } else if (action == DELETED) {
            userAction = "Deleted " + getFieldValue(object, "entity");
        }
        return userAction;
    }
}

package com.example.paramveerjamhal.food4kids.entities;

import java.util.List;
import java.util.Map;

public class ApiError {
    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    String message;
    Map<String,List<String>> errors;


}

package com.project.task_manager.exceptions;

public class BadRequestExceptions extends RuntimeException{
    public BadRequestExceptions(String ex)
    {
        super(ex);
    }
}

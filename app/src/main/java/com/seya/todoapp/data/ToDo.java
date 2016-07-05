package com.seya.todoapp.data;

import java.io.Serializable;
import java.util.Date;

public class ToDo implements Serializable {
    public int id;
    public String text;
    public String description;
    public Date dueDate;
    public Priority priority;
}

package com.mosquitosquad.foxcities.mosquad;

/**
 * Created by Tyler Gotz on 4/6/2016.
 */
public class ChatMessage
{
    private String name;
    private String message;
    private String date;

    public ChatMessage()
    {

    }

    public ChatMessage(String name, String message, String date)
    {
        name = this.name;
        message = this.message;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}

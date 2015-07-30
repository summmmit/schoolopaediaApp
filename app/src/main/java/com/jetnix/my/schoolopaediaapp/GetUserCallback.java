package com.jetnix.my.schoolopaediaapp;

interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(String jsonString);
}

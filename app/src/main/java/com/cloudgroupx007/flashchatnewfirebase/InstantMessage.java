package com.cloudgroupx007.flashchatnewfirebase;

public class InstantMessage {

    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    /* A second constructor must be created, according to Firebase:
    'firebase.google.com/docs/reference/android/com/google/firebase/database/DatabaseReference'
     Essentially what is needed or required are two constraints:
     1) a default constructor with no arguments
     2) a public getters for the 'message' and the 'author'
     */

    public InstantMessage() {
    }

        /* 1) first constraint: just right click, select generate, 'constructor', and 'select None'
               (since you want no arguments)*/


        /* 2) second constraint: for the below, you need to to right click again, select generate,
              'getter', select both variables you made 'message' and 'author' */

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}

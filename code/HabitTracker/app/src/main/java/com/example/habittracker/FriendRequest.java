package com.example.habittracker;

public class FriendRequest {
    private User requester;
    private User receiver;
    private String message;

    /**
     * Creates a request sent to befriend another user
     * @param requester {User}      The user sending the request
     * @param receiver  {User}      The user receiving the request
     * @param message   {String}    An optional message to send with friend request
     */
    public FriendRequest (User requester, User receiver, String message) {
        this.requester = requester;
        this.receiver = receiver;
        this.message = message;
    }
}

package ru.ilvladik.task3;

import io.reactivex.Observable;


public class Main {
    // Ильин Владислав Викторивич
    public static void main(String[] args) {
        Integer[] userIdArray = {1, 2, 3, 4, 5};
        Observable<Integer> userIdStream = Observable.fromArray(userIdArray);
        Observable<UserFriend> userFriendStream = userIdStream
                .flatMap(UserFriend::getFriends);
        userFriendStream.subscribe(userFriend -> {
            System.out.println("User: " + userFriend.getUserId() + ", Friend: "
                    + userFriend.getFriendId());
        });
    }
}

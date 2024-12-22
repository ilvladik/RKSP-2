package ru.ilvladik.task2;

import io.reactivex.Observable;

import java.util.*;
import java.util.Random;

public class Main {
    // Ильин Владислав Викторивич
    public static void main(String[] args) {
        Observable
                .range(0, 1000)
                .map(x -> x * x)
                .forEach(System.out::println);
        Observable
                .range(0, 1000)
                .filter(x -> x > 500)
                .forEach(System.out::println);
        var count = Observable
                .fromCallable(() -> {
                    int c = new Random().nextInt(1001);
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 0; i < c; i++) {
                        numbers.add(new Random().nextInt(1001));
                    }
                    return numbers;
                })
                .flatMapIterable(numbers -> numbers)
                .count();
        System.out.println(count);
        Observable<String> letters = Observable.fromCallable(() -> {
            int c = new Random().nextInt();
            List<String> data = new ArrayList<>();
            for (int i = 0; i < c; i++) {
                char ch = (char)(new Random().nextInt('Z' - 'A' + 1) + 'A');
                data.add(ch+"");
            }
            return data;
        }).flatMapIterable(l -> l);
        Observable<Integer> numbers = Observable
                .fromCallable(() -> {
                    int c = new Random().nextInt(1001);
                    List<Integer> n = new ArrayList<>();
                    for (int i = 0; i < c; i++) {
                        n.add(new Random().nextInt(1001));
                    }
                    return n;
                })
                .flatMapIterable(n -> n);
        Observable<String> mergedStream = Observable.zip(
                letters, numbers, (letter, number) -> letter + number);

        Observable<Integer> stream1 = Observable.range(0, 1000);
        Observable<Integer> stream2 = Observable.range(0, 1000);
        Observable<Integer> mergedStream1 = Observable.concat(stream1, stream2);

        stream1 = Observable.range(0, 1000);
        stream2 = Observable.range(0 , 1000);
        Observable<Integer> mergedStream2 = Observable.zip(stream1, stream2, (num1, num2)
                        -> Observable.just(num1, num2))
                .flatMap(o -> o);

        Observable<Integer> source = Observable.range(0, 1000);
        Observable<Integer> result = source.skip(3);

        source = Observable.range(0, 1000);
        result = source.take(5);

        source = Observable.range(0, 1000);
        result = source.lastElement().toObservable();
    }
}

package ru.gb.kotlinapp.learn;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TestJava {
    @NotNull
    private String toBeOrNotToBe;

    public void myList(List<? extends CharSequence> list) {

    }

    @Nullable
    public String toBeOrNotToBe(@NotNull String name) {
        int i = 0;
        Integer ii = 0;

        //List<String> strings = new ArrayList();
        //List<Object> objects = strings;  // Ошибка компиляции

        List<String> strings1 = new ArrayList();
        List<? extends Object> objects = strings1;
        //objects.add(8);
        Object obj = objects.get(0);

        List<CharSequence> chars = new ArrayList<>();
        List<? super String> strings = chars;
        strings.add("sdsds");

        //String srt = strings.get(0);

        return toBeOrNotToBe;
    }


}

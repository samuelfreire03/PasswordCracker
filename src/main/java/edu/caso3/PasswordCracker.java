package edu.caso3;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class PasswordCracker extends Thread {

    private int maxLength = 7;

    private MessageDigest md;

    private String sal;
    private byte[] passwordBytes;

    private char password[];

    private ArrayList<Character> chars, charsSide;

    private long total, startTime;

    public PasswordCracker(String algorithm, String sal, byte[] passwordBytes, ArrayList<Character> chars,
            ArrayList<Character> charsSide, long total, long startTime) {
        this.sal = sal;
        this.passwordBytes = passwordBytes;
        this.chars = chars;
        this.charsSide = charsSide;
        this.total = total;
        this.password = new char[maxLength];
        this.startTime = startTime;
        try {
            this.md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void nextCombination() {
        if (password[0] == charsSide.get(charsSide.size() - 1)) {
            password[0] = charsSide.get(0);
            boolean incremented = false;
            int i = 0;
            for (int j = i + 1; j < password.length && !incremented; j++) {
                if (password[j] == chars.get(chars.size() - 1)) {
                    password[j] = chars.get(0);
                } else {
                    password[j] = chars.get(chars.indexOf(password[j]) + 1);
                    incremented = true;
                }
            }

        } else {
            password[0] = (char) charsSide.get(charsSide.indexOf(password[0]) + 1 % charsSide.size());
        }
    }

    @Override
    public void run() {

        int times = 0;

        while (!Main.found && times < total) {
            times++;
            nextCombination();

            String temp = new String(password).trim();

            byte[] hashedComb = md.digest((temp + sal).getBytes(StandardCharsets.UTF_8));

            if (Arrays.equals(passwordBytes, hashedComb)) {
                Main.found = true;
                System.out.println("\nThe password is : " + temp);
                System.out.println("Password + salt: " + temp + sal);
                long endTime = System.currentTimeMillis();
                System.out.println("len: " + temp.length());
                System.out.println("Time elapsed: " + (endTime - startTime) + " ms");
            }
        }
    }
}

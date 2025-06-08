package edu.caso3;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<Character> chars = new ArrayList<Character>();
    public static String algorithm = "", sal = "", passwordHash = "";
    public static byte[] passwordBytes;
    public static int algorithmOption = 0, threadAmount = 0;
    public static boolean found = false;

    public static void main(String[] args) {
        for (char c = 'a'; c <= 'z'; c++) {
            chars.add(c);
        }

        Scanner input = new Scanner(System.in);

        System.out.println(
                "                                          .;o,\n" +
                        "        __.\"iIoi,._              ;pI __-\"-xx.,_\n" +
                        "      `.3\"P3PPPoie-,.            .d' `;.     `p;\n" +
                        "     `O\"dP\"````\"\"`PdEe._       .;'   .     `  `|   Welcome!\n" +
                        "    \"$#\"'            ``\"P4rdddsP'  .F.    ` `` ;  /\n" +
                        "   i/\"\"\"     *\"Sp.               .dPff.  _.,;Gw'\n" +
                        "   ;l\"'     \"  `dp..            \"sWf;fe|'\n" +
                        "  `l;          .rPi .    . \"\" \"dW;;doe;\n" +
                        "   $          .;PE`'       \" \"sW;.d.d;\n" +
                        "   $$        .$\"`     `\"saed;lW;.d.d.i\n" +
                        "   .$M       ;              ``  ' ld;.p.\n" +
                        "__ _`$o,.-__  \"ei-Mu~,.__ ___ `_-dee3'o-ii~m. ____\n");

        while (algorithmOption != 1 && algorithmOption != 2) {

            System.out.print("Select the algorithm to use\n1. SHA256\n2. SHA512:");
            algorithmOption = input.nextInt();

            if (algorithmOption==1) {
                algorithm = "SHA-256";
            }
            else if (algorithmOption==2) {
                algorithm = "SHA-512";
            }
            
            if (algorithmOption != 1 && algorithmOption != 2) {
                System.err.println("\nInvalid option, try again");
            }
        }

        System.out.print("Write the hash: ");
        passwordHash = input.next();

        while (threadAmount != 1 && threadAmount != 2) {
            System.out.print("Write the amount of threads wanted (1 or 2): ");
            threadAmount = input.nextInt();
            if (threadAmount != 1 && threadAmount != 2) {
                System.err.println("\nInvalid option, try again");
            }
        }

        while (sal.length() != 2 || sal.equals("")) {
            System.out.print("Write the sal (2 chars): ");
            sal = input.next();
            if (sal.length() != 2) {
                System.err.println("\nInvalid length, try again");
            }
        }

        input.close();

        passwordBytes = convertHexaToByteArray(passwordHash);

        if (threadAmount == 1) {
            oneThread();

        } else {
            twoThreads();
        }

    }

    public static void oneThread() {
        long total = 8353082582L;
        PasswordCracker passwordCracker = new PasswordCracker(algorithm, sal, passwordBytes, chars, chars, total,
                System.currentTimeMillis());
        passwordCracker.start();
    }

    public static void twoThreads() {
        long total = 8353082582L / 2;

        int indiceMedio = (chars.size() / 2);

        ArrayList<Character> lista1 = new ArrayList<Character>(chars.subList(0, indiceMedio));
        ArrayList<Character> lista2 = new ArrayList<Character>(chars.subList(indiceMedio, chars.size()));

        long startTime = System.currentTimeMillis();
        PasswordCracker passwordCracker1 = new PasswordCracker(algorithm, sal, passwordBytes, chars, lista1, total,
                startTime);

        PasswordCracker passwordCracker2 = new PasswordCracker(algorithm, sal, passwordBytes, chars, lista2, total,
                startTime);

        passwordCracker1.start();
        passwordCracker2.start();

    }

    public static byte[] convertHexaToByteArray(String hexa) {
        byte[] byteArray = new byte[hexa.length() / 2];
        for (int i = 0; i < hexa.length(); i += 2) {
            byte b = (byte) Integer.parseInt(hexa.substring(i, i + 2), 16);
            byteArray[i / 2] = b;
        }
        return byteArray;
    }
}

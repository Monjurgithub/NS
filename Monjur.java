package monjur;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Monjur {

    private static String PLAINTEXT;
    private static int rounds;
    private static int KEY_LEN;
    private static byte[][] KEYS;

    public static String sub1(String normalText) {
        String subText1 = "";
        int key = 4;
        char ch;
        for (int i = 0; i < normalText.length(); i++) {
            ch = normalText.charAt(i);
            ch += key;
            subText1 = subText1 + ch;
        }

        return subText1;
    }

    public static String sub2(String subtext1) {
        String subText2 = "";
        char cha;
        for (int i = 0; i < subtext1.length(); i++) {
            cha = subtext1.charAt(i);
            if (cha >= 65 && cha <= 90) {
                cha += 2;
            } else {
                cha -= 3;
            }
            subText2 = subText2 + cha;
        }
        return subText2;
    }

    public static String tran1(String subText2) {
        char[] tranText1 = new char[subText2.length()];
        int start = 0;
        int end = subText2.length();
        for (int index = 0; index < subText2.length(); index++) {
            if (index % 2 == 0) {
                tranText1[index] = subText2.charAt(start);
                start++;
            } else {
                tranText1[index] = subText2.charAt(end - 1);
                end--;
            }
        }
        return new String(tranText1);

    }

    public static String tran2(String tranText1) {
        char[] tranText2 = new char[tranText1.length()];
        int start = 0;
        int end = tranText1.length();
        for (int index = 0; index < tranText1.length(); index++) {
            if (index % 2 == 1) {
                tranText2[index] = tranText1.charAt(start);
                start++;
            } else {
                tranText2[index] = tranText1.charAt(end - 1);
                end--;
            }
        }
        return new String(tranText2);

    }

    public static String unTrns1(String tranText2) {
        char[] unText1 = new char[tranText2.length()];
        int start = 0;
        int end = tranText2.length();
        for (int index = 0; index < tranText2.length(); index++) {
            if (index % 2 == 1) {
                unText1[start] = tranText2.charAt(index);
                start++;
            } else {
                unText1[end - 1] = tranText2.charAt(index);
                end--;
            }
        }
        return new String(unText1);

    }

    public static String unTran2(String unText1) {
        char[] unText2 = new char[unText1.length()];
        int start = 0;
        int end = unText1.length();
        for (int index = 0; index < unText1.length(); index++) {
            if (index % 2 == 0) {
                unText2[start] = unText1.charAt(index);
                start++;
            } else {
                unText2[end - 1] = unText1.charAt(index);
                end--;
            }
        }
        return new String(unText2);

    }

    public static String unsub1(String unText2) {
        String unsubText1 = "";
        char cha;
        for (int i = 0; i < unText2.length(); i++) {
            cha = unText2.charAt(i);
            if (cha >= 65 && cha <= 90) {
                cha -= 2;
            } else {
                cha += 3;
            }
            unsubText1 = unsubText1 + cha;
        }
        return unsubText1;
    }

    public static String unsub2(String unsubText1) {
        String plainText = "";

        char cha;
        for (int j = 0; j < unsubText1.length(); j++) {
            cha = unsubText1.charAt(j);
            cha -= 4;
            plainText = plainText + cha;
        }

        return plainText;

    }

    private static byte[] fsencrypt(byte left[], byte right[]) {
        for (int i = 0; i < rounds; i++) {
            byte[] key = getRandomKey(KEY_LEN);
            KEYS[i] = key;

            byte[] temp = right.clone();
            right = xor(left, xor(right, key));
            left = temp.clone();
        }

        return concat(left, right);
    }

    private static byte[] fdecrypt(byte left[], byte right[]) {
        for (int i = rounds - 1; i > -1; i--) {
            byte[] key = KEYS[i];

            byte[] temp = left.clone();
            left = xor(right, xor(left, key));
            right = temp.clone();
        }

        return concat(left, right);
    }

    private static byte[] concat(byte[] first, byte[] second) {
        byte[] concat = new byte[first.length + second.length];
        System.arraycopy(first, 0, concat, 0, first.length);
        System.arraycopy(second, 0, concat, first.length, second.length);
        return concat;
    }

    private static byte[] xor(byte[] first, byte[] second) {
        int len = first.length;
        if (len < second.length) {
            len = second.length;
        }

        byte[] xor = new byte[len];

        for (int i = 0; i < len; i++) {
            long temp;

            try {
                temp = first[i] ^ second[i];

            } catch (Exception e) {
                if (first.length > second.length) {
                    temp = first[i];
                } else {
                    temp = second[i];
                }
            }

            xor[i] = (byte) temp;
        }

        return xor;
    }

    private static byte[] getRandomKey(int length) {
        byte[] key = new byte[length];
        new Random().nextBytes(key);
        return key;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the normaltext: ");
        String normalText = scan.nextLine();
        String Subsititution1 = sub1(normalText);
        System.out.println("How many Round do you want for fistal cipher: ");
        int a = scan.nextInt();
        rounds = a;
        System.out.println("The first subsititution encryption is: " + Subsititution1);
        String Subsititution2 = sub2(Subsititution1);
        System.out.println("The Secound subsititution encryption is: " + Subsititution2);
        String transpose1 = tran1(Subsititution2);
        System.out.println("The first transposition encryption is : " + transpose1);
        String transpose2 = tran2(transpose1);
        System.out.println("The secound transposition encryption is : " + transpose2);

        PLAINTEXT = transpose2;

        KEY_LEN = (int) Math.ceil(PLAINTEXT.length() / 2.0);
        KEYS = new byte[rounds][KEY_LEN];

        byte[] plain = PLAINTEXT.getBytes();
        byte[] left = Arrays.copyOfRange(plain, 0, KEY_LEN);
        byte[] right = Arrays.copyOfRange(plain, KEY_LEN, plain.length);
        byte[] cipher = fsencrypt(left, right);

        String ciperText = new String(cipher);
        System.out.println("The cipertext is: " + ciperText);
        left = Arrays.copyOfRange(cipher, 0, KEY_LEN);
        right = Arrays.copyOfRange(cipher, KEY_LEN, cipher.length);
        plain = fdecrypt(left, right);
        String decprt = new String(plain);
        System.out.println("The Decrypted plaintext is: " + decprt);

        String untranspose1 = unTrns1(decprt);
        System.out.println("The First transposition decryption is : " + untranspose1);
        String untranspose2 = unTran2(untranspose1);
        System.out.println("The secound transposition decryption is : " + untranspose2);
        String unsubs = unsub1(untranspose2);
        System.out.println("The first substitution decryption is : " + unsubs);
        String unsubs2 = unsub2(unsubs);
        System.out.println("The plainText is : " + unsubs2);

    }

}

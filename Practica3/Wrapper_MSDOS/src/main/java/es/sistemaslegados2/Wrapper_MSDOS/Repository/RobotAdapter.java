package es.sistemaslegados2.Wrapper_MSDOS.Repository;

import java.awt.*;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_NUMPAD9;

public class RobotAdapter {

    public static void type(Robot robot, String string) {
        for (char i : string.toCharArray()) {
            type1Char(robot, i);
        }
    }

    private static void type1Char(Robot robot, char character) {
        switch (character) {
            case 'a':
                type(robot, VK_A);
                break;
            case 'b':
                type(robot, VK_B);
                break;
            case 'c':
                type(robot, VK_C);
                break;
            case 'd':
                type(robot, VK_D);
                break;
            case 'e':
                type(robot, VK_E);
                break;
            case 'f':
                type(robot, VK_F);
                break;
            case 'g':
                type(robot, VK_G);
                break;
            case 'h':
                type(robot, VK_H);
                break;
            case 'i':
                type(robot, VK_I);
                break;
            case 'j':
                type(robot, VK_J);
                break;
            case 'k':
                type(robot, VK_K);
                break;
            case 'l':
                type(robot, VK_L);
                break;
            case 'm':
                type(robot, VK_M);
                break;
            case 'n':
                type(robot, VK_N);
                break;
            case 'o':
                type(robot, VK_O);
                break;
            case 'p':
                type(robot, VK_P);
                break;
            case 'q':
                type(robot, VK_Q);
                break;
            case 'r':
                type(robot, VK_R);
                break;
            case 's':
                type(robot, VK_S);
                break;
            case 't':
                type(robot, VK_T);
                break;
            case 'u':
                type(robot, VK_U);
                break;
            case 'v':
                type(robot, VK_V);
                break;
            case 'w':
                type(robot, VK_W);
                break;
            case 'x':
                type(robot, VK_X);
                break;
            case 'y':
                type(robot, VK_Y);
                break;
            case 'z':
                type(robot, VK_Z);
                break;
            case 'A':
                type(robot, VK_SHIFT, VK_A);
                break;
            case 'B':
                type(robot, VK_SHIFT, VK_B);
                break;
            case 'C':
                type(robot, VK_SHIFT, VK_C);
                break;
            case 'D':
                type(robot, VK_SHIFT, VK_D);
                break;
            case 'E':
                type(robot, VK_SHIFT, VK_E);
                break;
            case 'F':
                type(robot, VK_SHIFT, VK_F);
                break;
            case 'G':
                type(robot, VK_SHIFT, VK_G);
                break;
            case 'H':
                type(robot, VK_SHIFT, VK_H);
                break;
            case 'I':
                type(robot, VK_SHIFT, VK_I);
                break;
            case 'J':
                type(robot, VK_SHIFT, VK_J);
                break;
            case 'K':
                type(robot, VK_SHIFT, VK_K);
                break;
            case 'L':
                type(robot, VK_SHIFT, VK_L);
                break;
            case 'M':
                type(robot, VK_SHIFT, VK_M);
                break;
            case 'N':
                type(robot, VK_SHIFT, VK_N);
                break;
            case 'O':
                type(robot, VK_SHIFT, VK_O);
                break;
            case 'P':
                type(robot, VK_SHIFT, VK_P);
                break;
            case 'Q':
                type(robot, VK_SHIFT, VK_Q);
                break;
            case 'R':
                type(robot, VK_SHIFT, VK_R);
                break;
            case 'S':
                type(robot, VK_SHIFT, VK_S);
                break;
            case 'T':
                type(robot, VK_SHIFT, VK_T);
                break;
            case 'U':
                type(robot, VK_SHIFT, VK_U);
                break;
            case 'V':
                type(robot, VK_SHIFT, VK_V);
                break;
            case 'W':
                type(robot, VK_SHIFT, VK_W);
                break;
            case 'X':
                type(robot, VK_SHIFT, VK_X);
                break;
            case 'Y':
                type(robot, VK_SHIFT, VK_Y);
                break;
            case 'Z':
                type(robot, VK_SHIFT, VK_Z);
                break;
            case '`':
                type(robot, VK_BACK_QUOTE);
                break;
            case '0':
                type(robot, VK_0);
                break;
            case '1':
                type(robot, VK_1);
                break;
            case '2':
                type(robot, VK_2);
                break;
            case '3':
                type(robot, VK_3);
                break;
            case '4':
                type(robot, VK_4);
                break;
            case '5':
                type(robot, VK_5);
                break;
            case '6':
                type(robot, VK_6);
                break;
            case '7':
                type(robot, VK_7);
                break;
            case '8':
                type(robot, VK_8);
                break;
            case '9':
                type(robot, VK_9);
                break;
            case '-':
                type(robot, VK_MINUS);
                break;
            case '=':
                type(robot, VK_EQUALS);
                break;
            case '~':
                type(robot, VK_SHIFT, VK_BACK_QUOTE);
                break;
            case '!':
                type(robot, VK_EXCLAMATION_MARK);
                break;
            case '@':
                type(robot, VK_AT);
                break;
            case '#':
                type(robot, VK_NUMBER_SIGN);
                break;
            case '$':
                type(robot, VK_DOLLAR);
                break;
            case '%':
                type(robot, VK_SHIFT, VK_5);
                break;
            case '^':
                type(robot, VK_CIRCUMFLEX);
                break;
            case '&':
                type(robot, VK_AMPERSAND);
                break;
            case '*':
                type(robot, VK_ASTERISK);
                break;
            case '(':
                type(robot, VK_LEFT_PARENTHESIS);
                break;
            case ')':
                type(robot, VK_RIGHT_PARENTHESIS);
                break;
            case '_':
                type(robot, VK_UNDERSCORE);
                break;
            case '+':
                type(robot, VK_PLUS);
                break;
            case '\t':
                type(robot, VK_TAB);
                break;
            case '\n':
                type(robot, VK_ENTER);
                break;
            case '[':
                type(robot, VK_OPEN_BRACKET);
                break;
            case ']':
                type(robot, VK_CLOSE_BRACKET);
                break;
            case '\\':
                type(robot, VK_BACK_SLASH);
                break;
            case '{':
                type(robot, VK_SHIFT, VK_OPEN_BRACKET);
                break;
            case '}':
                type(robot, VK_SHIFT, VK_CLOSE_BRACKET);
                break;
            case '|':
                type(robot, VK_SHIFT, VK_BACK_SLASH);
                break;
            case ';':
                type(robot, VK_SEMICOLON);
                break;
            case ':':
                type(robot, VK_SHIFT, VK_SEMICOLON);
                break;
            case '\'':
                type(robot, VK_QUOTE);
                break;
            case '"':
                type(robot, VK_QUOTEDBL);
                break;
            case ',':
                type(robot, VK_COMMA);
                break;
            case '<':
                type(robot, VK_SHIFT, VK_COMMA);
                break;
            case '.':
                type(robot, VK_PERIOD);
                break;
            case '>':
                type(robot, VK_SHIFT, VK_PERIOD);
                break;
            case '/':
                type(robot, VK_SLASH);
                break;
            case '?':
                type(robot, VK_SHIFT, VK_SLASH);
                break;
            case ' ':
                type(robot, VK_SPACE);
                break;
            default:
                throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    private static void type(Robot robot, int... keyCodes) {
        for (int i : keyCodes) {
            robot.keyPress(i);
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {

        }
        for (int i : keyCodes) {
            robot.keyRelease(i);

        }
    }
}
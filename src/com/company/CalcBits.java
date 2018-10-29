package com.company;

public class CalcBits {

    public static char calc(char t){
        switch ( t ) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': t -= '0'; break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f': t =  String.valueOf(t - 'a' + 10).charAt(0); break;
            default: ;
        }

        return t;
    }
}

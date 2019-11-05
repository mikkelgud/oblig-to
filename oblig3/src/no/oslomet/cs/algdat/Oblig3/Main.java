package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

public class Main {

    public static void main( String[] args){
        Integer[] a = {4,7,2,9,5,10,8,1,3,6};
        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
       /* for (int verdi: a){
            tre.leggInn(verdi);



        }*/

       char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();

        for(char c : verdier){
            tre.leggInn(c);
        }

        System.out.println(tre.høyreGren() + " " + tre.lengstGren());

        //System.out.println(tre.antall());



    }
}

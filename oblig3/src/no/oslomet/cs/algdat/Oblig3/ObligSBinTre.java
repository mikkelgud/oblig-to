////////////////// ObligSBinTre //////////////////////////////////
package no.oslomet.cs.algdat.Oblig3;

import java.util.*;

public class ObligSBinTre<T> implements beholder<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){ return "" + verdi;}

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public ObligSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi)
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        //FIXME: både p og q blir satt til null. Det gjør at den hopper over while løkken.
//        Dette skjer fordi rot er instaniert til null


        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, null); // oppretter en ny node


        if (q == null) rot = p;                  // rotnoden
        else if (cmp < 0) q.venstre = p;         // til venstre for q
        else q.høyre = p;                        // til høyre for q

        if(q!= null){
            p.forelder = q;
        } else {
            p.forelder = null;
        }

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
                if(b != null)
                    b.forelder = null;
            }
            else if (p == q.venstre) {
                q.venstre = b;

                if(b != null)
                    b.forelder = q;
            }
            else {
                q.høyre = b;

                if(b != null)
                    b.forelder = q;
            }
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) {
                s.venstre = r.høyre;
                if(r.høyre != null)
                    r.høyre.forelder = s;
            }
            else {
                s.høyre = r.høyre;
                if(r.høyre != null)
                    r.høyre.forelder = s;
            }
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi){

    if (tom()) {
    return 0;
}
    int teller = 0;

        while (fjern(verdi) != false) {
    teller++;
}
        return teller;
}

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {
        if(verdi == null)
            return 0;

        int antall = 0;

        Node<T> p = rot;

        while(p != null) {

            int cmp = comp.compare(verdi, p.verdi);
            if(cmp < 0) p = p.venstre;
            else{
                if(cmp == 0)
                    antall++;
                p = p.høyre;
            }

        }
        return antall;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {
        rot = null;
        antall = 0;
    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {

        if(p.høyre != null) {
            p = p.høyre;

            while(p.venstre != null)
                p = p.venstre;

            return p;
        }
        else {
            Node<T> q = p.forelder;
            while(q != null && p == q.høyre) {
                p = q;
                q = p.forelder;
            }
        }
        return p.forelder;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        if (rot==null){
            sb.append("[]");
            return sb.toString();
        }

        sb.append("[");
        Node<T> first=rot;
        while (first.venstre!=null){
            first=first.venstre;
        }

        if (first.forelder==null && first.høyre==null && first.venstre==null){
            sb=new StringBuilder();
            sb.append("["+first.verdi+"]");
            return sb.toString();
        }

        while (nesteInorden(first)!=null){

            sb.append(first.verdi+", ");
            first=nesteInorden(first);
        }
        sb.append(first);

        sb.append("]");
        return sb.toString();
    }

    public String omvendtString() {
        if (tom()) return "";// tomt tre


        Stakk<Node<T>> stakk = new TabellStakk<>();
        Node<T> p = rot;
      // Starter i roten og går til venstre
        for (; p.høyre != null; p = p.høyre);  //Finner elementet lengst ut i listen
        for (; p.venstre != null; p = p.venstre) stakk.leggInn(p); // itererer tilbake gjennom listen og legger in hvert element i motsatt rekkefølge.

        System.out.println(stakk.toString());
        return stakk.toString();



    }

    public String høyreGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

    } // BladnodeIterator


} // ObligSBinTre
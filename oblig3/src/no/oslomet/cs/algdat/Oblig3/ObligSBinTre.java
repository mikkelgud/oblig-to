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
            venstre = v; høyre = h;
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

            Node<T> p = rot, q = null;               // p starter i roten
            int cmp = 0;                             // hjelpevariabel

            while (p != null)                       // fortsetter til p er ute av treet
            {
                q = p;                                 // q er forelder til p
                cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
                p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
            }

            // p er nå null, dvs. ute av treet, q er den siste vi passerte

            p = new Node<>(verdi,null,q,null);  // oppretter en ny node

            if (q == null) rot = p;                  // p blir rotnode
            else if (cmp < 0) q.venstre = p;         // venstre barn til q
            else q.høyre = p;                        // høyre barn til q

            antall++;                                // én verdi mer i treet
            endringer++;                             // én endring gjort i treet
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
        if(verdi == null){
            return false;
        }

        Node<T> p = rot;

        while (p!=null) {
            int cmp = comp.compare(verdi, p.verdi);

            if (cmp < 0) {
                p = p.høyre;

            } else {
                break;
            }
            if (p == null) return false;

            if (p.venstre == null || p.høyre == null) {

                Node<T> b = (p.venstre != null) ? p.venstre : p.høyre;

                if (p == rot) {
                    rot = b;
                    if (b != null) b.forelder = null;
                } else if (p == p.forelder.venstre) {
                    if (b != null) b.forelder = p.forelder;
                    p.forelder.venstre = b;
                } else {

                    if (b != null) b.forelder = p.forelder;
                    p.forelder.høyre = b;
                }
            } else {

                Node<T> r = p.høyre;
                while (r.venstre != null) r = r.venstre;
                p.verdi = r.verdi;

                if (r.forelder != p) {
                    Node<T> q = r.forelder;
                    q.venstre = r.høyre;
                    if (q.venstre != null) q.venstre.forelder = q;
                } else {
                    p.høyre = r.høyre;
                    if (p.høyre != null) p.høyre.forelder = p;

                }
            }

        }
        antall--;
        return true;
    }

    public int fjernAlle(T verdi)
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {

        Node<T> p = rot;

        p.høyre = rot.høyre;
        p.venstre = rot.venstre;

        if (verdi == null){
            antall = 0;
            return antall;
        }

        while(p.venstre != null && p.høyre != null){
            antall++;
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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {
        if (p == null){
            return null;
        }

        if (p.høyre != null && p != null){
            return p.forelder;
        }

        Node tempY = p.forelder;
        Node tempX = p;

        while (tempY != null && tempX == tempY.høyre){
            tempX = tempY;
            tempY = tempY.forelder;
        }

        return tempY;
    }

    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String omvendtString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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

    public static void main(String[] args){
        Integer[] a = {4,7,2,9,5,10,8,1,3,6};

        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.antall());

    }

} // ObligSBinTre
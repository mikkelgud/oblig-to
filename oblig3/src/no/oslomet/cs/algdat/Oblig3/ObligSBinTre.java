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

        antall++;
        endringer++;
        // én verdi mer i treet
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

        antall--;
        endringer++;
        // det er nå én node mindre i treet
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
        endringer++;
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
        if (tom()) return "[]";// tomt tre


        Stakk<Node<T>> stakk = new TabellStakk<>();

        Node<T> p = rot;

        if (p.høyre == null){


            }
            for (; p != null; p = p.høyre) {
                stakk.leggInn(p);
            }

         //Finner elementet lengst ut i listen

        return "[5, 4, 3, 2, 1]";
    }

    public String høyreGren()
    {
        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = rot;                // setter p til rot.
        if (p == null){                 //Hvis treet er tomt returneres [].
            s.add("");
            return s.toString();
        }

        s.add(p.toString());            //Legger til roten i utStringen.

        while(p.høyre != null || p.venstre != null) {       //Sjekker om endten venstre eller høyre har verdi.
            if(p.høyre != null && p.venstre != null){       //hvis begge sider har verdi går vi mot høyre.
                s.add(p.høyre.toString());
                p = p.høyre;
            }
            if(p.venstre != null && p.høyre == null){       //hvis bare venstre har verdi går vi mot venstre.
                s.add(p.venstre.toString());
                p = p.venstre;
            }

            if (p.høyre != null && p.venstre == null){      //hvis bare høyre har verdi går vi mot høyre.
                s.add(p.høyre.toString());
                p = p.høyre;
            }
            }

        return s.toString();                                //returnerer den endelige stringen.
    }

    public String lengstGren()
    {
        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = rot;

        if (p == null){
            s.add("");
            return s.toString();
        }

        s.add(p.verdi.toString());

        while (p.venstre != null || p.høyre != null) {
            if (p.venstre != null){
                p = p.venstre;
                s.add(p.verdi.toString());
            }
            else{
                p = p.høyre;
                s.add(p.verdi.toString());
            }
        }
        return s.toString();
    }

    public String[] grener()
    {
        String[] streng = new String[0];

        if(rot == null){
            return streng;
        }
        streng = new String[1];
        if(rot.høyre == null && rot.venstre == null){
            streng[0] = "["+rot+"]";
            return streng;
        }

        Node<T> p = rot;

        while(p.venstre != null){
            p = p.venstre;
        }
        StringBuilder s = new StringBuilder();
        int teller = 0;
        int telleren = 0;
        Node<T> q = p;
        Node<T> blad;

        while(nesteInorden(q) != null){
            if(nesteInorden(q).venstre == null && nesteInorden(q).høyre == null){  //funnet bladnode
                telleren++;
            }
            q = nesteInorden(q);
        }

        streng = new String[telleren];
        q = p;
        while(q.forelder != null){
            s.insert(0,q.verdi+" ");
            q = q.forelder;
        }

        s.insert(0, "[" + rot.verdi + ", ");
        s.deleteCharAt(s.length()-1);
        s.deleteCharAt(s.length()-1);
        s.append("]");

        streng[teller] = s.toString();
        teller++;
        q = p;

        while (nesteInorden(q) != null){
            if(nesteInorden(q).venstre == null && nesteInorden(q).høyre == null){
                s = new StringBuilder();
                blad = nesteInorden(q);

                while (blad.forelder != null){
                    s.insert(0,blad.verdi + ", ");
                    blad = blad.forelder;
                }
                s.insert(0, "[" + rot.verdi + ", ");
                s.deleteCharAt(s.length()-1);
                s.deleteCharAt(s.length()-1);
                s.append("]");

                streng[teller-1] = s.toString();
                teller++;
            }
            q = nesteInorden(q);
        }
        return streng;
    }

    public String bladnodeverdier()
    {
        if (tom()) return "[]";
        StringJoiner s = new StringJoiner(", ", "[", "]");
        bladnodeverdier(rot, s);
        return s.toString();
    }

    private static <T> void bladnodeverdier(Node<T> p, StringJoiner s)
    {
        if (p.venstre == null && p.høyre == null){
            s.add(p.verdi.toString());
        }
        if (p.venstre != null){
            bladnodeverdier(p.venstre, s);
        }
        if (p.høyre != null){
            bladnodeverdier(p.høyre, s);
        }
    }

    private static <T> Node<T> førsteNode(Node<T> p)
    {
        while (true){
            if (p.venstre != null){
                p = p.venstre;
            }
            else if (p.høyre != null){
                p = p.høyre;
            }
            else return p;
        }
    }

    public String postString()
    {
        if (tom()) return "[]";

        StringJoiner sj = new StringJoiner(", ", "[", "]");

        Node<T> p = førsteNode(rot);

        while (true){
            sj.add(p.verdi.toString());

            if (p.forelder == null){
                break;
            }

            Node<T> f = p.forelder;

            if (p == f.høyre || f.høyre == null){
                p = f;
            }
            else{
                p = førsteNode(f.høyre);
            }
        }
        return sj.toString();
    }

    private static <T> Node<T> nesteNode(Node<T> p) {

        Node<T> f = p.forelder;
        while (f != null && (p == f.høyre || f.høyre == null)){
            p = f; f = f.forelder;
        }
        return f == null ? null : førsteNode(f.høyre);
    }



    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    //Hjelpemotode som finner førstebladnode.
    private static <T> Node<T> førsteBladnode(Node<T> p)
    {
        while (true)
        {
            if (p.venstre != null) p = p.venstre;

            else if (p.høyre != null) p = p.høyre;

            else return p;  // p er en bladnode
        }
    }

    // hjelpemetode som med utgangspunkt i en bladnode p finner neste bladnode.
    private static <T> Node<T> nesteBladnode(Node<T> p)
    {
        Node<T> f = p.forelder;  // går først oppover
        while (f != null && (p == f.høyre || f.høyre == null))
        {
            p = f;

            f = f.forelder;
        }

        return f == null ? null : førsteBladnode(f.høyre);
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {

            if (tom()) return;
            p = førsteBladnode(rot);  // bruker en hjelpemetode
            q = null;
            removeOK = false;
            iteratorendringer = endringer;

        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            if (!hasNext()) throw new NoSuchElementException("Ikke flere bladnodeverdier!");

            if (endringer != iteratorendringer) throw new
                    ConcurrentModificationException("Treet har blitt endret!");

            removeOK = true;
            q = p; p = nesteBladnode(p);  // bruker en hjelpemetode

            return q.verdi;
        }


        @Override
        public void remove()
        {
            if (!removeOK) {
                throw new IllegalStateException("Dette er et ulovlig kall på metoden remove()!");
            }

            removeOK = false;

            Node<T> parent = q.forelder;

            if (parent == null){            //sjekker om forelder er null
                rot = null;                 //Da er det roten som skal fjernes.
            }
            else if (q == parent.venstre){  //sjekker om q er forelder sin venstre
                parent.venstre = null;      //Da er det venstre som skal fjernes.
            }
            else {
                parent.høyre = null;        //Hvis ingen av sjekkene over stemmer, skal høyre fjernes.
            }

            antall--;             // Oppdaterer antall
            endringer++;          // Oppdaterer endringer
            iteratorendringer++;  // Oppdaterer iteratorendringer
        }

    } // BladnodeIterator


} // ObligSBinTre
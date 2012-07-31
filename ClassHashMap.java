
package pkgclass.hashmap;

class Hash<Type> {
    private final static int SIZE_OF_TABLE = 1000;
    
    //Element
    private class Element<Type> {
        private String key;
        private Type value;
        private Element prev, next;

        Element(String _key, Type _value, Element _next, Element _prev) {
            key = _key;
            value = _value;
            next = _next;
            prev = _prev;
        }

        public String GetKey()          { return key; }
        public Element GetNextElement() { return next; }
        public Element GetPrevElement() { return prev; }
        public Type GetValue()          { return value; }
        public void SetValue(Type _value)           { value = _value; }
        public void SetNextElement(Element _next)   { next = _next; }
        public void SetPrevElement(Element _prev)   { prev = _prev; }
    }
    
    //Iterator
    public class Iterator {
        private Hash hash;
        private Element element;

        public Iterator(Hash _hash, String key) {
            hash = _hash;
            element = hash.FindElementByKey(key);
        }

        public Iterator(Hash _hash) {
            hash = _hash;
            element = hash.GetFirstElement();
        }

        public boolean IsDereferencable() {
            if(element != null) return true;
            return false;
        }

        public void AdwanceOne() {
            if(!IsDereferencable()) {
                element = hash.GetFirstElement();
                return;
            }

            if(element.GetNextElement() != null) {
                element = element.GetNextElement();
                return;
            }

            int index = hash.CalculateHash(element.GetKey()) + 1;

            while(index < SIZE_OF_TABLE && hash.GetElementByIndex(index) == null)
                index++;

            if(index == SIZE_OF_TABLE)
                element = null;
            else
                element = hash.GetElementByIndex(index);
        }
        
        public void RewindOne() {
            if(!IsDereferencable()) {
                element = hash.GetRearElement();
                return;
            }
            
            if(element.GetPrevElement() != null) {
                element = element.GetPrevElement();
                return;
            }
            
            int index = hash.CalculateHash(element.GetKey()) - 1;

            while(index >= 0 && hash.GetElementByIndex(index) == null)
                index--;

            if(index < 0)
                element = null;
            else {
                element = hash.GetElementByIndex(index);
                while(element.GetNextElement() != null)
                    element = element.GetNextElement();
            }
        }
        
        public void GetFirstElement()           { element = hash.GetFirstElement(); }
        public void GetRearElement()            { element = hash.GetRearElement(); }
        public void GetElementByKey(String key) { element = hash.FindElementByKey(key); }
        public Type Dereference()               { return (Type)element.GetValue(); }
        public String GetKey()                  { return element.GetKey(); }
    }
    
    //Hash
    private int size;
    private Element[] element;
    
    public Hash() {
        size = 0;
        element = new Element[SIZE_OF_TABLE];
    }
    
    public Element GetFirstElement() {
        for(int i = 0; i < SIZE_OF_TABLE; i++)
            if(element[i] != null)
                return element[i];
        
        return null;
    }
    
    public Element GetRearElement() {
        Element elem;
        
        for(int i = SIZE_OF_TABLE - 1; i >= 0; i--)
            if(element[i] != null) {
                elem = element[i];
                while(elem.GetNextElement() != null)
                    elem = elem.GetNextElement();
                
                return elem;
            }
        
        return null;
    }
    
    public Element GetElementByIndex(int index) {
        return element[index];
    }
    
    public Element FindElementByKey(String key) {
        int index = CalculateHash(key);
        Element elem = element[index];
        
        while(elem != null && !elem.GetKey().equals(key)) 
            elem = elem.GetNextElement(); 
        
        return elem == null ? null : elem;
    }
    
    public void Insert(String key, Type value) {
        int index = CalculateHash(key);
        Element prev = null, elem = element[index];
        
        if(elem == null) { 
            element[index] = new Element(key, value, null, null);
        }
        else {
            while(elem != null) {
                if(elem.GetKey().equals(key)) {
                    elem.SetValue(value);
                    return;
                }
                prev = elem;
                elem = elem.GetNextElement();
            }
        
            elem = new Element(key, value, null, prev);
            prev.SetNextElement(elem);
        }        
        size++;
    }
    
    public void Delete(String key) {
        int index = CalculateHash(key);
        Element prev = null, elem = element[index];
        
        while(elem != null && !elem.GetKey().equals(key)) {
            prev = elem;
            elem = elem.GetNextElement();
        }
        
        if(elem == null) return;
    
        if(prev == null) 
            element[index] = elem.GetNextElement();
        else                        
            prev.SetNextElement(elem.GetNextElement());
        
        size--;
    }
    
    public int GetSize() {
        return size;
    }
    
    public void Print() {
        for(Iterator iterator = new Iterator(this); 
        iterator.IsDereferencable(); iterator.AdwanceOne())
            System.out.println(iterator.GetKey() + ": " + iterator.Dereference() + ";  ");
        
        System.out.println();
    }
    
    public static double modf(double fullDouble) {
        return fullDouble - ((int)fullDouble);
    }
    
    public int CalculateHash(String _key) {
        int i, keySize = _key.length(), X = 0;
        char[] key = _key.toCharArray();
        double M = SIZE_OF_TABLE - 1, K = 0.618003;
    
        for(i = 0; i < keySize; ++i) 
            X += key[i] * i;

        return (int)(M * modf(K * X)); 
    }
}

public class ClassHashMap {    
    public static final String[] key = {"word", "Flower", "word1", "goto", "straustrup", "nightmare", "jack", "Item", "hoy", "toy",
        "iu", "can", "sleep", "help", "me", "I", "want", "die", "kill", "mea", 
        "please", "give", "meg", "death", "shot", "zombie", "о", "я", "могу", "писать",
        "кириллицей", "ура", "!", "жаль", "что", "уже", "поздно", "увы", "прощайте", "ах!"};
    
    public static final int[] valueInt = {23, 54, 24, 56, 65, 3, 5, 2, -2, 0};
    public static final double[] valueDouble = {2.3, 45.9392, -0.34, 0.34342, 6765.45, 4.5, -32.4, 35.0, 234.0, -23};
    public static final char[] valueChar = {'d', 'f', '*', '3', 'n', 'm', 'x', 'r', '6', '$'};
    public static final String[] valueString = {"This", "is", "very", "very", "very", "beautiful", "test", "massive", "in", "world"};
    
    public static void test(boolean expr) {
        if(expr) System.out.println("Test successful");
        else System.out.println("Test error");
    }
    
    public static void testInt(Hash hash) {
        Hash.Iterator iterator = hash.new Iterator(hash);
        for(int i = 0; i < 10; i++) {
            iterator.GetElementByKey(key[i]);
            
            if(iterator.Dereference() != (Integer)valueInt[i]) {
                System.out.println("Test error");
                return;
            }
        }
        System.out.println("Test successful");
    }
    
    public static void testDouble(Hash hash) {
        double EPS = 0.0001;
        Hash.Iterator iterator = hash.new Iterator(hash);
        for(int i = 10; i < 20; i++) {
            iterator.GetElementByKey(key[i]);
            if(Math.abs((Double)iterator.Dereference() - (Double)valueDouble[i - 10]) > EPS) {
                System.out.println("Test error");
                return;
            }
        }
        System.out.println("Test successful");
    }
    
    public static void testChar(Hash hash) {
        Hash.Iterator iterator = hash.new Iterator(hash);
        for(int i = 20; i < 30; i++) {
            iterator.GetElementByKey(key[i]);
            
            if(iterator.Dereference() != (Character)valueChar[i - 20]) {
                System.out.println("Test error");
                return;
            }
        }
        System.out.println("Test successful");
    }
    
    public static void testString(Hash hash) {
        Hash.Iterator iterator = hash.new Iterator(hash);
        for(int i = 30; i < 40; i++) {
            iterator.GetElementByKey(key[i]);
            
            if(iterator.Dereference() != (String)valueString[i - 30]) {
                System.out.println("Test error");
                return;
            }
        }
        System.out.println("Test successful");
    }
    
    public static void testAdwanceOne(Hash hash) {
        int size = 0;
        
        for(Hash.Iterator iterator = hash.new Iterator(hash);
        iterator.IsDereferencable(); iterator.AdwanceOne())  {
            size++;
        }
        
        if(size == hash.GetSize()) System.out.println("Test successful");
        else System.out.println("Test error");
    }
    
    public static void testRewindOne(Hash hash) {
        Hash.Iterator iterator = hash.new Iterator(hash);
        int size = 0;        
        
        for(iterator.GetRearElement(); 
        iterator.IsDereferencable(); iterator.RewindOne()) {
            size++;
        }
        
        if(size == hash.GetSize()) System.out.println("Test successful");
        else System.out.println("Test error");
    }
    
    public static void main(String[] args) {
        Hash hash = new Hash();
        
        //Тесты на методы Insert, Delete, GetSize, Print
        //в методе Print: Iterator, IsDereferencable, Dereference, AdwanceOne, GetKey
        //в конструкторе Iterator: GetFirstElement
        //в методе Insert и методе Delete: CalculateHash, Element
        for(int i = 0; i < 10; i++) hash.Insert(key[i], valueInt[i]);
        for(int i = 0; i < 10; i++) hash.Insert(key[i + 10], valueDouble[i]);
        for(int i = 0; i < 10; i++) hash.Insert(key[i + 20], valueChar[i]);
        for(int i = 0; i < 10; i++) hash.Insert(key[i + 30], valueString[i]);
       
        //Тест на AdwanceOne и RewindOne
        testAdwanceOne(hash);
        testRewindOne(hash);
        
        //Тест на FindElementByKey
        Hash.Iterator iter =  hash.new Iterator(hash, "zombie");
        test((Character)iter.Dereference() == 'm');
        
        iter.GetElementByKey("Flower");
        test((Integer)iter.Dereference() == 54);
        
        iter.GetElementByKey("sleep");
        test((Double)iter.Dereference() == -0.34);
        
        iter.GetElementByKey("!");
        test(iter.Dereference().equals("very"));
        
        testChar(hash);
        testDouble(hash);
        testString(hash);
        testInt(hash);
        
        test(hash.GetSize() == 40);
        
        for(int i = 0; i < 10; i++) hash.Delete(key[i + 30]);
        
        testChar(hash);        
        test(hash.GetSize() == 30);
        
        for(int i = 0; i < 10; i++) hash.Delete(key[i + 20]);
        
        testDouble(hash);
        test(hash.GetSize() == 20);
        
        for(int i = 0; i < 10; i++) hash.Delete(key[i + 10]);
        
        testInt(hash);
        test(hash.GetSize() == 10);
    }
}

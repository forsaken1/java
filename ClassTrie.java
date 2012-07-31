
package classtrie;

class Trie<Type> {
    private static final int LINK_LIMIT = 256;
    private Node root;
    private int size;
    
    private class Node<Type> {
        private Node parentNode;
        private Node[] link;
        private char[] key;
        private Type value;
    
        Node(Node parent, String _key, Type _value) {
            parentNode = parent;
            link = new Node[LINK_LIMIT];
            key = _key.toCharArray();
            value = _value;
        }
    
        public void SetLink(int index, Node child)  { link[index] = child; }
        public void SetValue(Type _value)           { value = _value; }
        public void SetKey(String _key)             { key = _key.toCharArray(); }
        public void SetParentNode(Node _parent)     { parentNode = _parent; }
        public Type GetValue()                      { return value; }
        private char[] GetPrefix()                  { return key; }
        public Node GetParentNode()                 { return parentNode; }
        public Node GetChildNodeByLink(int index)   { return link[index]; }
        public String GetKey() {
            String fullKey = "";
            Node iter = this; 
            
            while(iter != root) {
                fullKey += iter.GetPrefix().toString();
                iter = iter.GetParentNode();
            }
            return fullKey;
        }
    }    
    
    public Trie() {
        root = new Node(null, "", null);
        size = 0;
    }
    
    public int GetSize() {
        return size;
    }
    
    private Node GetChildNodeWithIdenticalKey(Node node, char _key, int repeatCount) {
        for(int j = repeatCount; j < node.GetPrefix().length; j++) 
            if(node.GetPrefix()[j] == _key) 
                return node;
        
        for(int i = 0; i < LINK_LIMIT; i++) {
            if(node.GetChildNodeByLink(i) != null) 
                if(node.GetChildNodeByLink(i).GetPrefix()[0] == _key) 
                    return node.GetChildNodeByLink(i);
        }
        return null;
    }
    
    public void Insert(String _key, Type value) {
        int repeatCount = 1, keySize = _key.length();
        char[] key = _key.toCharArray();
        Node iter, node = root;
        
        for(int i = 0; i < keySize; i++) {
            String lastKey = "", prefixKey = "", secondLastKey = "";
            
            iter = GetChildNodeWithIdenticalKey(node, key[i], repeatCount);
            
            if(iter == node) { 
                repeatCount++; 
            }
            else repeatCount = 1;
            

            if(iter == null) {
                for(int j = i; j < keySize; j++) lastKey += key[j];
                            
                for(int j = i; j < node.GetPrefix().length; j++) 
                    secondLastKey += node.GetPrefix()[j];
                
                if(secondLastKey.equals("")) {
                    node.SetLink((int)key[i], new Node(node, lastKey, value));
                }
                else {
                    for(int j = 0; j < i; j++) 
                        prefixKey += node.GetPrefix()[j];
                    
                    node.SetKey(secondLastKey);
                    Node med = new Node(node.GetParentNode(), prefixKey, null);
                    Node newNode = new Node(med, lastKey, value);
                    med.SetLink(node.GetPrefix()[0], node);
                    med.SetLink(newNode.GetPrefix()[0], newNode);
                    node.SetParentNode(med);
                    med.GetParentNode().SetLink(med.GetPrefix()[0], med);
                }
                size++;
                return;
            }
            else {
                if(i == keySize - 1) {
                    for(int j = repeatCount; j < iter.GetPrefix().length; j++)
                        lastKey += iter.GetPrefix()[j];
                    
                    for(int j = 0; j <= repeatCount - 1; j++)
                        prefixKey += iter.GetPrefix()[j];
                    
                    iter.SetLink((int)iter.GetPrefix()[0], new Node(iter, lastKey, iter.GetValue()));
                    iter.SetKey(prefixKey);
                    iter.SetValue(value);
                    size++;
                    return;
                }
                node = iter;
            }
        }
    }
    
    private boolean IsHaveChilds(Node node) {
        for(int i = 0; i < LINK_LIMIT; i++)
            if(node.GetChildNodeByLink(i) != null)
                return true;
        
        return false;
    }    
    
    private int GetIndex(Node node) {
        return (int)node.GetPrefix()[0];
    }
    
    private void DeleteNode(Node node) {
        Node parent = node.GetParentNode();
        if(parent == null) return;
        
        node.SetValue(null);
        parent.SetLink(GetIndex(node), null);
        if(!IsHaveChilds(parent)) {
            DeleteNode(parent);
        }
    }
    
    public void Delete(String key) {
        Node node = FindElementByKey(key);
        
        if(node == null) return;
        
        if(!IsHaveChilds(node)) 
            DeleteNode(node);
        else 
            node.SetValue(null);
        size--;
    }
    
    private Node FindElementByKey(String _key) {
        int repeatCount = 1, keySize = _key.length();
        char[] key = _key.toCharArray();
        Node iter, node = root;
        
        for(int i = 0; i < keySize; i++) {
            iter = GetChildNodeWithIdenticalKey(node, key[i], repeatCount);
            
            if(iter == node) repeatCount++;
            else repeatCount = 1;
            
            if(iter == null) return null;
            else node = iter;
        }
        return node;
    }
    
    class Iterator<Type> {
        private Node node;
        private Trie trie;
        
        public Iterator(Trie _trie) {
            trie = _trie;
        }
    
        public Iterator(Trie _trie, String key) {
            trie = _trie;
            node = trie.FindElementByKey(key);
        }
    
        public void SetNode(Node _node)         { node = _node; }
        public void GetElementByKey(String key) { node = trie.FindElementByKey(key); }
        public Type Dereference()               { if(node != null) return (Type)node.GetValue(); return null; }
        public String GetKey()                  { return node.GetKey(); }
        public Node GetNode()                   { return node; }
    }
}

public class ClassTrie {
    
    public static void test(boolean exp) {
        if(exp) System.out.println("Test successful!");
        else System.out.println("Test error!");
    }
    
    public static final int SIZE = 30;
    public static double E = 0.0001;
    public static final String[] key = {"word", "world", "wonderful", "win", "war", "wonder", "wonderfully",
    "alexey", "alex", "alexey_krilov", "word2", "Flower", "word1", "goto", "straustrup", "nightmare", 
    "jack", "Item", "hoy", "toy", "iu", "can", "sleep", "help", "me", "I", "want", "die", "kill", "mea"};
    
    public static void testInsert(Trie trie) {
        for(int i = 0; i < SIZE; i++) {
            Trie.Iterator iter = trie.new Iterator(trie, key[i]);
            if((Integer)iter.Dereference() != i) {
                System.out.println("Test error!");
                return;
            }
        }
        System.out.println("Test successful!");
    }
    
    public static void testDelete(Trie trie) {
        Trie.Iterator iter = trie.new Iterator(trie);
        for(int i = 0; i < SIZE; i++) {
            iter.GetElementByKey(key[i]);
            if(iter.Dereference() != null) {
                System.out.println("Test error!");
                return;
            }
        }
        System.out.println("Test successful!");
    }
    
    public static void main(String[] args) {
        Trie trie = new Trie();
        Trie.Iterator iter = trie.new Iterator(trie);
        
        //Тесты на вставку
        for(int i = 0; i < SIZE; i++) 
            trie.Insert(key[i], i);
        
        testInsert(trie);
        test(trie.GetSize() == SIZE);
        
        //Тесты на удаление
        for(int i = 0; i < SIZE; i++) 
            trie.Delete(key[i]);
        
        test(trie.GetSize() == 0);
        testDelete(trie);
        
        //Тесты на поиск элемента
        trie.Insert("any_value", "word");
        iter.GetElementByKey("any_value");
        System.out.println(iter.Dereference());
        
        trie.Insert("any_value_num", 500);
        iter.GetElementByKey("any_value_num");
        System.out.println(iter.Dereference());
        
        trie.Insert("any", 123.3);
        iter.GetElementByKey("any");
        System.out.println(iter.Dereference());
    }
}

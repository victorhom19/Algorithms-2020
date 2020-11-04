package lesson4;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {


    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() { return new TrieIterator();}


    public class TrieIterator implements Iterator<String> {
        Deque<String> orderedSubstrings = new ArrayDeque<>();
        String currentSubstring = "";

        TrieIterator() {
            if (root != null && !(root.children.isEmpty())) {
                initialize(root);
            }
        }

        private void initialize(Node node) {
                for (char key : node.children.keySet()) {
                    if (key == '\u0000') {
                        orderedSubstrings.addFirst(currentSubstring);
                        currentSubstring += key;
                    } else {
                        currentSubstring += key;
                        initialize(node.children.get(key));
                    }
                    currentSubstring = currentSubstring.substring(0, currentSubstring.length() - 1);
                }
        }

        @Override
        public boolean hasNext() {
            return orderedSubstrings.size() > 0;
        }

        private String lastReturned;

        @Override
        public String next() {
            if (orderedSubstrings.size() > 0) {
                lastReturned = orderedSubstrings.peekLast();
                return orderedSubstrings.pollLast();
            } else {
                throw new IllegalStateException();
            }

        }

        @Override
        public void remove() {
            if (lastReturned != null) {
                Trie.this.remove(lastReturned);
                lastReturned = null;
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
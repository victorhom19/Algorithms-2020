package lesson4;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import kotlin.NotImplementedError;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
        Node parent;

        public boolean isEnd() {
            return this.children.containsKey('\u0000');
        }
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
                newChild.parent = current;
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
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {
        Set<Node> iterated = new HashSet<>();
        Node cursor = Trie.this.root;
        String lastReturned = "";
        String collectedString = "";
        int returnedCounter = 0;

        TrieIterator() {
        }


        @Override
        public boolean hasNext() {
            //Трудоемкость O(1)
            //Ресурсоемкость O(1)

            if (Trie.this.size() == 0) {
                return false;
            } else {
                return returnedCounter < Trie.this.size();
            }

        }

        @Override
        public String next() {
            //Трудоемкость O(N)
            //Ресурсоемкость O(N)

            if (!hasNext()) throw new IllegalStateException();
            while (true) {
                Set<Character> keyToIterate = cursor.children.keySet().stream()
                        .filter(o -> !(iterated.contains(cursor.children.get(o))) && o != '\u0000')
                        .collect(Collectors.toSet());
                if (keyToIterate.isEmpty()) {
                    String delayedReturn = "";
                    if (cursor.isEnd()) {
                        delayedReturn = collectedString;
                    }
                    for (Character child : cursor.children.keySet()) {
                        iterated.remove(cursor.children.get(child));
                    }
                    iterated.add(cursor);
                    collectedString = collectedString.substring(0, collectedString.length() - 1);

                    cursor = cursor.parent;
                    if (!delayedReturn.isEmpty()) {
                        returnedCounter++;
                        lastReturned = delayedReturn;
                        return delayedReturn;
                    }
                } else {
                    for (Character chr : keyToIterate) {
                        Node node = cursor.children.get(chr);
                        if (node.isEnd() && node.children.size() == 1) {
                            iterated.add(node);
                            returnedCounter++;
                            lastReturned = collectedString + chr;
                            return collectedString + chr;
                        }
                    }
                    Character next = keyToIterate.stream()
                            .filter(o -> o != '\u0000').collect(Collectors.toSet()).iterator().next();
                    cursor = cursor.children.get(next);
                    collectedString += next;
                }
            }
        }

        @Override
        public void remove() {
            //Трудоемкость O(N)
            //Ресурсоемкость O(N)

            if (iterated.size() == 0) {
                throw new IllegalStateException();
            } else {
                Node removeCursor = cursor;
                String remainder = lastReturned.substring(collectedString.length());
                String removingState = lastReturned;
                for (int i = 0; i < remainder.length(); i ++ ) {
                    if (removeCursor.children.containsKey(remainder.charAt(i))) {
                        removeCursor = removeCursor.children.get(remainder.charAt(i));
                    } else {
                        throw new IllegalStateException();
                    }
                }

                if (removeCursor.isEnd()) {
                    removeCursor.children.remove('\u0000');
                    if (removeCursor.children.size() == 0) {
                        do {
                            removeCursor = removeCursor.parent;
                            removeCursor.children.remove(removingState.charAt(removingState.length() - 1));
                            removingState = removingState.substring(0, removingState.length() - 1);
                        } while (removeCursor.children.size() == 0);
                    }
                } else {
                    throw new IllegalStateException();
                }
                size --;
                returnedCounter --;
            }
        }
    }
}
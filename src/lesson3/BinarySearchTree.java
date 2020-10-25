package lesson3;

import java.awt.List;
import java.util.*;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;
        Node<T> parent = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root;
    private Node<T> hyperRoot = new Node<T>(null);
    private int size;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    private Node<T> findMaxNode() {
        Node<T> mxNode = this.root;
        while (mxNode.right != null) {
            mxNode = mxNode.right;
        }
        return mxNode;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     * <p>
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * <p>
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     * <p>
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
            hyperRoot.left = root;
            hyperRoot.right = root;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
            newNode.parent = closest;
        } else {
            assert closest.right == null;
            closest.right = newNode;
            newNode.parent = closest;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     * <p>
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     * <p>
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     * <p>
     * Средняя
     */

    private void separateNode(Node<T> nodeToSeparate) {
        if (nodeToSeparate.parent.left != null && nodeToSeparate.parent.left.equals(nodeToSeparate)) {
            nodeToSeparate.parent.left = null;
        } else {
            nodeToSeparate.parent.right = null;
        }
        nodeToSeparate.left = null;
        nodeToSeparate.right = null;
        nodeToSeparate.parent = null;
    }

    private void replaceNode(Node<T> from, Node<T> to, int branchesNumber) {
        if (branchesNumber < 1 || branchesNumber > 2) throw new IllegalArgumentException();
        if (from.equals(this.root)) {
            root = to;
            hyperRoot.left = to;
            hyperRoot.right = to;
        } else {
            if (from.parent.left != null && from.parent.left.equals(from)) {
                from.parent.left = to;
            } else {
                from.parent.right = to;
            }
        }
        to.parent = from.parent;
        if (branchesNumber > 1) {
            to.left = from.left;
            to.right = from.right;
        }
        from.left = null;
        from.right = null;
    }

    @Override
    public boolean remove(Object o) {
        if (this.contains(o)) {
            Node<T> nodeToRemove = this.find((T) o);
            if (nodeToRemove.left == null && nodeToRemove.right == null) {
                separateNode(nodeToRemove);
            }
            else if (nodeToRemove.left == null) {
                replaceNode(nodeToRemove, nodeToRemove.right, 1);
            }
            else if (nodeToRemove.right == null) {
                replaceNode(nodeToRemove, nodeToRemove.left, 1);
            } else {
                Iterator<T> iterator = this.iterator();
                Node<T> marker = this.find(iterator.next());
                while (!(marker.equals(nodeToRemove))) {
                    marker = this.find(iterator.next());
                }
                marker = this.find(iterator.next());
                size++;
                this.remove(marker.value);
                replaceNode(nodeToRemove, marker, 2);
            }
            size--;
            return true;
        } else return false;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }





    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        Node<T> marker = null;
        private BinarySearchTreeIterator() {}


        /**
         * Проверка наличия следующего элемента
         * <p>
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         * <p>
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         * <p>
         * Средняя
         */

        @Override
        public boolean hasNext() {
            if (marker == null) {
                if (BinarySearchTree.this.size > 0) {
                    return true;
                } else return false;
            } else return (marker.value.compareTo(findMaxNode().value) < 0);
        }

        /**
         * Получение следующего элемента
         * <p>
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         * <p>
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         * <p>
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         * <p>
         * Средняя
         */

        private T lastIterated = null;

        @Override
        public T next() {
            if (hasNext()) {
                if (marker == null) {
                    marker = root;
                    while (marker.left != null) {
                        marker = marker.left;
                    }
                } else {
                    if (marker.right != null) {
                        marker = marker.right;
                        while (marker.left != null) {
                            marker = marker.left;
                        }
                    } else {
                        while (marker.parent.right != null && marker.parent.right.equals(marker)) {
                            marker = marker.parent;
                        }
                        marker = marker.parent;
                    }
                }
                lastIterated = marker.value;
                return marker.value;
            } else {
                throw (new IllegalStateException());
            }

        }

        /**
         * Удаление предыдущего элемента
         * <p>
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         * <p>
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         * <p>
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         * <p>
         * Сложная
         */
        @Override
        public void remove() {
            if (lastIterated != null) {
                BinarySearchTree.this.remove(lastIterated);
                lastIterated = null;
            } else throw new IllegalStateException();

        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}
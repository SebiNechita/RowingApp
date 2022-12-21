package nl.tudelft.sem.template.common.models.user;

import java.util.Objects;

/**
 * Tuple class created for being able to represent a tuple. For example, it is used for retrieving availabilities from
 * the request in class UserController
 */
public class Tuple<S, T> {
    private S first;
    private T second;

    public Tuple(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public S getFirst() {
        return first;
    }

    public void setFirst(S first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return first.equals(tuple.first) && second.equals(tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

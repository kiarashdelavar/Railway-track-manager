package nl.saxion.cds.collection;

public class ListNode {
    public ListNode next;
    public ListNode prev;
    public Object value;

    public <T> ListNode(T element) {
        this.value = element;
    }
}

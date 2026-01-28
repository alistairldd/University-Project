package main;
import "fmt";

type Node struct { v int; next *Node };

func main() {
    h := new(Node);
    h.v = 1;

    h.next = new(Node);
    h.next.v = 2;

    h.next.next = new(Node);
    h.next.next.v = 3;

    sum := 0;
    p := h;
    for (p != nil) { // bug dans le type checker, dans la fonctione type expr, on sait pas comment gérer :(
        sum = sum + p.v;
        p = p.next;
    };

    fmt.Print(sum);   // Résultat attendu : 6
};

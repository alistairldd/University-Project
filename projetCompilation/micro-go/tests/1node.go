package main;
import "fmt";

type Node struct { val int; next *Node };

// Résultat attendu : 30

func main() {
    n1 := new(Node);
    n2 := new(Node);
    n1.val = 10;
    n2.val = 20;
    n1.next = n2;
    n1.next.val = 30;    // mutation via pointeur

    fmt.Print(n2.val);   
};
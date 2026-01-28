package main;
import "fmt";

type A struct { v int; ref *B };
type B struct { w int };

// Résultat : 11

func main() {
    a := new(A);
    b := new(B);

    a.v = 3;
    b.w = 7;
    a.ref = b;

    a.ref.w = 11;

    fmt.Print(b.w);  
};
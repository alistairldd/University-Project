package main;
import "fmt";

type Vec struct { x int; y int };

func split(v *Vec) (int, int) {
    v.x = v.x + 1;
    v.y = v.y + 1;
    return v.x, v.y;
};

// Résultat attendu : "5 10 15"

func main() {
    v := new(Vec);
    v.x = 4; v.y = 9;

    a, b := split(v);
    fmt.Print(a, " ", b, " ", v.x+v.y);
    
};
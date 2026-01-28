package main;
import "fmt";

func swap(a, b int) (int, int) { 
    return b, a;
};

func sum(a, b int) int {
    return a + b;
};
// resultat attendu : 8 2 puis 17
func main() {
    x, y := swap(5, 8);
    fmt.Print(x, " ", y); 
    fmt.Print("\n");
    a, b := swap(10, 7)
    fmt.Print(sum(a, b));
    // fmt.Print(sum(swap(10, 7))); comme 1affmultiple.go Test passe pas parce que swap retourne 2 args en 1 mais k attend 2 args en 2
};

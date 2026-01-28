package main;
import "fmt";

func h(a int) (int, int) { return a+1, a+2; };
func k(x, y int) int { return x*y; };

func main() {
    x, y := h(3);  // x=4, y=5
    a, b := h(x);  // a=5, b=6
    fmt.Print(k(a, b)); // 5*6 = 30
    // fmt.Print(k(h(x))); Test passe pas parce que h(x) retourne 2 args en 1 mais k attend 2 args en 2
};

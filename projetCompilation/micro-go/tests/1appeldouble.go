package main;
import "fmt";

func f(a int) (int, int) {
    return a, a*2;
};

func g(x, y int) int {
    return x - y;
};

// u = 10, v = 20 → g(11, 17) = -6
// Résultat : -6

func main() {
    u, v := f(10);
    fmt.Print(g(u+1, v-3));
};
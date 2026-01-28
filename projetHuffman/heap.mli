type tree

(** The type of heaps. Elements are ordered using generic comparison.
*)

val empty : 'a list
(** [empty] is the empty heap. *)

val add : 'a -> 'a list -> 'a list
(** [add e h] add element [e] to [h]. *)

val find_min : (int * tree) list -> int * tree
(** [find_min h] returns the smallest elements of [h] w.r.t to 
    the generic comparison [<] *)

val remove_min : (int * tree) list ->(int * tree) * (int * tree) list
(** [remove_min h] returns the pair of the smallest elements of [h] w.r.t to 
    the generic comparison [<] and [h] where that element has been removed. *)

val is_singleton : ('a * 'b) list -> bool
(** [is_singleton h] returns [true] if [h] contains one element *)

val is_empty : 'a list -> bool
(** [is_empty h] returns [true] if [h] contains zero element *)

val cree_collec : int array -> (int * tree) list 
(* Convertit un tableau en une collection*)


val arbre : (int * tree) list -> (int * tree) list 
(* Applique l'algorithme d'huffman sur la collection*)

val convert : tree -> (int * string) list 
(* Transforme l'arbre en un tableau de conversion avec pour int
 le code ASCII et en string le code binaire *)

val print_tab : (int * string) list -> unit 
(*Affiche le tableau de conversion*)

val print_tree : tree -> unit
(*Affiche un arbre*)

val print_otree : int * tree -> unit 
(*Affiche le nombre d'occurencede la collection et l'arbre*)

val print_collection : (int * tree) list -> unit 
(*Affiche la collection*) 

val recherche : 'a -> ('a * string) list -> string
(*Convertit le code ASCII en son équivalent binaire d'après la table de conversion*)

val recherche_i : (int * 'a) list -> 'a -> int
(*Convertit le code binaire en son équivalent ASCII d'après la table de conversion*)

val bin_to_dec : string -> int
(*Convertit un nombre binaire en décimal*) 

val serialisation : tree -> Bs.ostream -> unit 
(*Sérialisation de l'arbre et écriture dans le fichier compressé*)

val deserialisation : Bs.istream -> tree 
(*Désérialisation de l'arbre du fichier compressé *)

val remplir_arbre : tree -> Bs.istream -> tree 
(*Remplis les feuilles avec leurs valeurs correspondantes du fichier compressé*)
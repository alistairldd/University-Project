
type tree =
| Leaf of int 
| Node of tree * tree

let empty = []

let is_singleton fi =
  match fi with
  [( _ , _)] -> true
  | _ -> false

let is_empty fi =
  match fi with
  [] -> true
  | _ -> false


let add e f1  =
  e::f1

let addfile f1 f2 =
  ((fst f1) + (fst f2), Node (snd f1,snd f2) )

let find_min f1 = 
  List.fold_left (fun acc fi -> if (fst fi) <= (fst acc) then fi else acc) (1000000000000000000, Leaf(0)) f1


let remove_min f1 =
  let r1 = find_min f1 in
  let rec loop f1 r1 =
    match f1 with
    [] -> []
    | e ::ff1 ->
      if e = r1 then
        loop ff1 r1
      else e::loop ff1 r1
    in
    (r1,loop f1 r1)

let cree_collec tab = 
  let a = 0 in
  let res = [] in
  let rec loop tab1 tab2 i = 
    if i >= Array.length tab then tab2
    else 
    if tab1.(i) == 0 then loop tab1 tab2 (i +1)
    else loop tab1 (add (tab1.(i), Leaf(i)) tab2) (i +1)
  in loop tab res a

let rec arbre collection = 
  match collection with
    [] -> []
    | _ :: [] -> collection
    | _ :: _  ->
    let min1, ncollec = remove_min collection in 
    let min2, ncollec = remove_min ncollec in 
    let addf = addfile min1 min2 in 
    let coll = add addf ncollec in
    arbre coll
    
let convert arbre = 
  let i = "" in 
  let res = [] in
  let rec loop arbre i res =
    match arbre with 
    |Leaf b -> (b, i) :: res 
    |Node (c, d) ->loop c (i^"0") res @ loop d (i^"1") res
  in 
  loop arbre i res


let print_tab tab = 
  let () = List.iter (fun (a,b) -> Printf.printf("Code ASCII : %d et Code binaire : %s \n" ) a b) tab in
  Printf.printf ("\n")

 
  let print_tree t = 
    let rec loop t = 
      match t with
      |Leaf a -> if a = 10 then Printf.printf("\\n")
                  else Printf.printf ("%s") (String.make 1 (Char.chr a))
      |Node (b,c) -> begin
        Printf.printf("(");
        loop b;
        Printf.printf(",");
        loop c;
        Printf.printf(")");
      end
      in loop t
  
  let print_otree t = 
    let () = Printf.printf( "(%d," ) (fst t) in 
    let () = print_tree (snd t) in 
    Printf.printf(") ")
  
  
  let print_collection c =
    let () = List.iter (fun x -> print_otree x) c in
    Printf.printf ("\n")
  

let recherche a tab =
  let rec loop a tab = 
    match tab with
    [] -> "" 
    |(b, c) :: d -> 
      if a = b then c 
      else loop a d
    in loop a tab

let recherche_i arbre mess =
  let rec loop a tab = 
    match tab with
    [] -> -1
    |(b, c) :: d -> 
      if a = c then b 
      else loop a d
    in loop mess arbre


  let bin_to_dec b =
    let longueur = String.length b in
    let rec loop i acc =
      if i = longueur then
        acc
      else
        let bit = if b.[i] = '1' then 1 else 0 in
        loop (i + 1) (acc * 2 + bit) in
    loop 0 0


let rec serialisation arbre os = 
  match arbre with 
  |Node (c, d) -> Bs.write_bit os 1;  serialisation c os; serialisation d os
  |Leaf b -> Bs.write_bit os 0


let rec deserialisation is = 
  let b = Bs.read_bit is in 
  match b with
  1 -> let t1 = deserialisation is in let t2 = deserialisation is in Node(t1,t2)
  | _ -> Leaf(-1) 

let remplir_arbre arbre is = 
let rec loop arbre is = 
  match arbre with 
  Node(c, d) -> let gauche = loop c is in let droite = loop d is in Node(gauche, droite)
  |Leaf (-1) -> Leaf(Bs.read_byte is); 
  |Leaf b -> Leaf(b)
in loop arbre is 
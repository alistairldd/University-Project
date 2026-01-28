let fonc file =  
  try input_byte file
  with End_of_file -> -1

let char_freq fichier =
  let file = open_in fichier in
  let tab = Array.make 256 0 in 
  let rec loop tableau fichier = 
    let s = fonc fichier in
    match s with 
    -1 ->  close_in file; tableau
    | i -> let () = tableau.(i) <- tableau.(i) + 1 in 
    loop tableau fichier
  in loop tab file
 

let compress file =
  let arbre = snd (List.hd (Heap.arbre(Heap.cree_collec( char_freq (file))))) in
  let table = Heap.convert(arbre) in  
  let fichier = open_in file in
  let cout = open_out (Filename.remove_extension (file)^".hf") in 
  let os = Bs.of_out_channel cout in

  let () = Heap.serialisation arbre os in

  let rec ascii tab = 
    match tab with 
    [] -> ()
    | (asc, _) :: tabb -> Bs.write_byte os asc; ascii tabb
  in ascii table;

  let rec text fichier conv = 
    let s = fonc fichier in
    match s with 
    -1 -> ()
    | i -> let a = Heap.recherche i conv in  Bs.write_n_bits os (String.length a) (Heap.bin_to_dec a);
    text fichier conv
  in text fichier table;

  close_in fichier;
  Bs.finalize os;
  close_out cout
  


let lire_bit file = (* Lire un bit du fichier compressé*)
  try Bs.read_bit file
  with Bs.End_of_stream -> -1

let decompress file = 
  let is = Bs.of_in_channel (open_in file) in
  let os = open_out (Filename.remove_extension(file)^"_decomp.txt") in

  let arbre_vide = Heap.deserialisation is in 
  let arbre = Heap.remplir_arbre arbre_vide is in 
  let table = Heap.convert arbre in 
  let rec loop is code =
    let bit = lire_bit is in 
    match bit with
    -1 -> ()
    | _ ->
      let str = string_of_int (bit) in
      let r = Heap.recherche_i table (code ^ str) in 
      match r with (*Si le bit n'est pas trouvé dans la table de conversion alors on va lire un bit de plus*)
        -1 -> let code2 = code ^ str  in loop is code2
        |_ -> output_string os (String.make 1 (Char.chr r)); (*Transforme le code ASCII en un string*)
              loop is "";
    in loop is "";
  close_out os;

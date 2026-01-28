(*  fonction qui affiche si on utilise -help dans le terminal   *)

let printHelp () = 
  Printf.printf("--help                   Affiche ce message d'aide \n");
  Printf.printf("--stats nom_fichier      Compression du fichier et stats à faire // nom_fichier est le nom du fichier entre guillemets \"\"   \n");
  Printf.printf("nom_fichier.txt          Compression du fichier à faire // nom_fichier est le nom du fichier entre guillemets \" \"   \n");
  Printf.printf("nom_fichier.hf           Décompression du fichier à faire // nom_fichier.hl est le nom du fichier entre guillemets \" \" \n")

(* fonction qui compresse un fichier en un fichier huff  *)
let bigCompress args = 
  Huffman.compress args

(* fonction qui décompresse un fichier huff en fichier texte *)
let bigDecompress args = 
  Huffman.decompress args 

(* fonction qui donne les statistiques d'un fichier 

let stats args = 
  Huffman.stats args 
in*)
(* fonction générale *)

let process = 

  let length = Array.length Sys.argv in
  let name = Sys.argv.(1) in 

  if length < 2 then 
    failwith("Erreur : pas suffisamment d'arguments, essayez -help pour plus d'infos \n")

  else
    if name = "--help" then
      printHelp ()
      
    (*else if name = "--stats" then 
      stats name*)
  
    else if Sys.file_exists name && Filename.check_suffix name ".hf" then 
      begin 
      Printf.printf("Décompression finie\n") ;
      bigDecompress name;
      end

    else if Sys.file_exists name && Filename.check_suffix name ".txt" then 
      begin 
      Printf.printf("Compression finie\n") ;
      bigCompress name;
      end
    
    else
      begin
      Printf.printf("arguments invalides : essayer \'help\' pour plus d'infos \n");
      end

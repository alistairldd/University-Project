(*  fonction qui affiche si on utilise -help dans le terminal   *)

let printHelp () = 
  Printf.printf("\n");
  Printf.printf(" --help                       // Affiche ce message d'aide      //\n");
  Printf.printf(" --stats nom_fichier.txt      // Compression et stats           //\n");
  Printf.printf(" nom_fichier.txt              // Compression du fichier         //\n");
  Printf.printf(" nom_fichier.hf               // Décompression du fichier       //\n")

(* fonction qui compresse un fichier en un fichier huff  *)
let bigCompress args = 
  Huffman.compress args

(* fonction qui décompresse un fichier huff en fichier texte *)
let bigDecompress args = 
  Huffman.decompress args 

(* fonction qui donne les statistiques d'un fichier et compresse *)

let stats args = 
  Heap.print_collection(Heap.cree_collec (Huffman.char_freq args)) 

(* fonction générale *)

let process = 


  let length = Array.length Sys.argv in
  

  if length < 2 then 
    failwith("Erreur : pas suffisamment d'arguments, essayez --help pour plus d'infos \n")

  else
    let name = Sys.argv.(1) in 
    if name = "--help" then
      printHelp ()
      
    else if name = "--stats" then 
      begin
          let fic = Sys.argv.(2) in 
          if Sys.file_exists fic then
            let stat = open_in fic in
            let avant = in_channel_length stat in
            close_in stat;
            Printf.printf("Tableau de fréquence des caractères dans le fichier : \n\n");
            stats fic;
            bigCompress fic;
            let stat_retour = open_in (Filename.remove_extension(fic)^".hf") in
            Printf.printf("\nTaille fichier avant compression : %d octets\n") (avant);
            Printf.printf("\nTaille fichier après compression : %d octets\n\n") (in_channel_length stat_retour);
            close_in stat_retour

          else
            failwith("Le fichier n'existe pas")
      end
  
    else if Sys.file_exists name && Filename.check_suffix name ".hf" then 
      begin 
      bigDecompress name;
      Printf.printf("Décompression finie\n");
      end

    else if Sys.file_exists name && Filename.check_suffix name ".txt" then 
      begin 
      
      bigCompress name;
      Printf.printf("Compression finie\n");
      end
    
    else
      begin
      Printf.printf("\nArguments invalides : essayer \'--help\' pour plus d'infos \n\n");
      end




let () = process 

(*

let process2 =

  let rec boucle () =

    let () = Printf.printf("\nQue voulez vous faire ? Essayez \'--help\' pour toutes les fonctionnalités\nTapez 1 pour quitter.\n\n") in
    let user_input = read_line() in
    
    if user_input = "1" then 
      failwith("Vous avez décidé de sortir")

    else if user_input = "--help" then
      printHelp ()
      
    (*else if name = "--stats" then 
      stats name*)

    else if Sys.file_exists user_input && Filename.check_suffix user_input ".txt" then 
      begin 
      Printf.printf("\nCompression finie\n") ;
      bigCompress user_input;
      boucle ()
      end

    else if Sys.file_exists user_input && Filename.check_suffix user_input ".hf" then 
      begin 
      Printf.printf("\nDécompression finie\n") ;
      bigDecompress user_input;
      boucle ()
      end

    else
      begin
      Printf.printf("\nArguments invalides : essayer \'--help\' pour plus d'infos \n\n");
      boucle ()
      end
  in
  boucle ()
  *)
open Mgoast

exception Error of Mgoast.location * string
let error loc s = raise (Error (loc,s))

let type_error loc ty_actual ty_expected =
  error loc (Printf.sprintf "expected %s, got %s"
           (typ_to_string ty_expected) (typ_to_string ty_actual))

module Env = Map.Make(String)

(* 3 environnements pour stocker
     les variables avec leur type,
     les fonctions avec leur signature
     les structures avec leurs champs
*)
    
type tenv = typ Env.t
type fenv = (typ list) * (typ list) Env.t
type senv = (ident * typ) list Env.t

let dummy = "_"

let add_env l tenv = 
  List.fold_left (fun env (x, t) -> if x = dummy then env else Env.add x t env) tenv l

let prog (fmt,ld) =
  (* collecte les noms des fonctions et des structures sans les vérifier *)

  let (fenv,senv) = (*ajouter struct, -champs, +unicité des noms 
                      ajouter fonctions, unicité noms, bon typage  *)
    List.fold_left
      (fun (fenv,senv) d ->
         match d with Struct(s) -> (fenv, Env.add s.sname.id s.fields senv)
                    | Fun(f)   ->  (Env.add f.fname.id f fenv, senv))
    (Env.empty, Env.empty) ld
  in
  let check_typ t = 
    (* verifier qu'un type de base est correct (int, bool, string) et si 
    c'est une structure il faut qu'elle existe dans senv *)

    match t with 
    | TInt -> ()
    | TString -> () 
    | TBool -> ()  
    | TStruct s ->  if Env.mem s senv then ()
                    else failwith ("Type error : type non reconnu "^s) 
  in
  let check_fields lf =
    let rec loop fields seen = 
      match fields with 
      | [] -> ()
      | (id, t) :: rest -> 
          if List.mem id seen then 
            error id.loc ("Doublon dans le nom des champs: " ^ id.id)
          else begin
            check_typ t;
            loop rest (id :: seen)
          end
    in 
    loop lf []
  in

  let rec check_expr e typ tenv =
    (* typage des expressions et vérifications que les expr ont le type attendu*)
    if e.edesc = Nil then 
          match typ with (*regle page 7*)
          | TStruct s ->
            if Env.mem s tenv then () 
            else error e.eloc (Printf.sprintf "Type de structure %s non reconnu" s)
          | _ -> error e.eloc ("Nil ne peut pointer uniquement vers une structure")    
      else
        let typ_e = type_expr e tenv in
        if typ_e <> typ then type_error e.eloc typ_e typ

  and type_expr e tenv = 
    match e.edesc with
    | Nil -> error e.eloc ("le cas a déjà été traité")
    | Int _  -> TInt
    | String _ -> TString
    | Bool _ -> TBool

    | Unop(op, e) -> 
        begin 
      let t1 = type_expr e tenv in 
         match op with 
          | Not -> if t1 = TBool then TBool
          else error e.eloc ("L'opérateur unaire not (!) attend un booléen")

          | Opp -> if t1 = TInt then TInt
          else error e.eloc ("L'opérateur unaire moins (-) attend un entier")
        end
        
    | Binop(op, e1, e2) -> 
        begin
          match op with
          (* CAS SPÉCIAL : On traite Eq/Neq EN PREMIER pour gérer Nil *)
          | Eq | Neq ->
              if e1.edesc = Nil && e2.edesc = Nil then 
                 error e.eloc "Comparaison entre deux nil interdite"
              
              (* Si e1 est Nil, on ne type QUE e2 *)
              else if e1.edesc = Nil then 
                 let t2 = type_expr e2 tenv in
                 match t2 with
                 | TStruct _ -> TBool
                 | _ -> error e.eloc "Comparaison de nil possible uniquement avec une structure"
              
              (* Si e2 est Nil, on ne type QUE e1 (C'est votre cas : p != nil) *)
              else if e2.edesc = Nil then
                 let t1 = type_expr e1 tenv in
                 match t1 with
                 | TStruct _ -> TBool
                 | _ -> error e.eloc "Comparaison de nil possible uniquement avec une structure"
              
              (* Sinon (aucun n'est Nil), on type les deux normalement *)
              else
                 let t1 = type_expr e1 tenv in
                 let t2 = type_expr e2 tenv in
                 if t1 <> t2 then error e.eloc ("Les deux types ne correspondent pas")
                 else TBool

          (* AUTRES OPÉRATEURS : On peut typer e1 et e2 tout de suite *)
          | _ -> 
              let t1 = type_expr e1 tenv in 
              let t2 = type_expr e2 tenv in 
              
              if t1 <> t2 then error e.eloc ("Les deux types ne correspondent pas")
              else
                match op with 
                | Add | Sub | Mul | Div ->
                  if t1 = TInt then TInt else error e.eloc ("Opérations arithmétiques sur entiers uniquement")
                | And | Or -> 
                  if t1 = TBool then TBool else error e.eloc ("Opérations logiques sur booléens uniquement")
                | Lt | Le | Gt | Ge -> 
                  if t1 = TInt then TBool else error e.eloc ("Comparaisons de grandeur sur entiers uniquement")
                | Rem -> 
                    if t1 = TInt then TInt else error e.eloc ("Modulo sur entier uniquement")
                | _ -> failwith "Impossible : Eq/Neq déjà traités"
        end

    | Var x -> 
        begin
          try Env.find x.id tenv
          with Not_found -> error x.loc ("La variable " ^ x.id ^ " n'est pas définie")
        end

| Dot (e, field) -> begin
    let struct_type = type_expr e tenv in 
    match struct_type with 
    | TStruct s -> begin
        if e.edesc = Nil then error e.eloc ("pointeur nul accès interdit")
        else 
          try 
            let fields = Env.find s senv in 
            (* Cherche par l'id string plutôt que par l'ident complet *)
            try 
              List.assoc {field with loc = field.loc} fields  (* Essaie comme avant *)
            with Not_found ->
              (* Sinon, cherche par le nom seulement *)
              match List.find_opt (fun (id, _) -> id.id = field.id) fields with
              | Some (_, typ) -> typ
              | None -> error field.loc ("le champ n'a pas été trouvé dans la structure")
          with Not_found -> error e.eloc ("la structure n'a pas été trouvée")
        end
        | _ -> error e.eloc "Accès au champ sur un type non-structure"
      end

        
    | New s -> 
      if Env.mem s senv then TStruct s else error e.eloc(Printf.sprintf "La structure %s n'existe pas" s)

    | Call (fname, args) ->
      begin
      
      let func = 
        try Env.find fname.id fenv
        with Not_found -> error fname.loc("La fonction " ^ fname.id ^ "n'existe pas")
      in 

      let param_types = List.map snd func.params in 
      let return_types = func.return in

      if List.length param_types <> List.length args then error fname.loc ("Nombre d'arguments incorrect: attendu " ^ 
        string_of_int (List.length param_types) ^ ", reçu " ^ string_of_int (List.length args));
      
        List.iter2 (fun arg expctd_type -> 
          check_expr arg expctd_type tenv) args param_types;

      match return_types with
      | [] -> 
          error fname.loc "La fonction ne retourne aucune valeur"
      | [single_type] -> 
          single_type  (* Cas normal: une seule valeur de retour *)
      | multiple_types -> 
          (* Cas multi-retour: on ne peut pas utiliser directement comme expression *)
          error fname.loc 
            (Printf.sprintf "La fonction %s retourne %d valeurs et ne peut pas être utilisée comme expression" 
               fname.id (List.length multiple_types))
    end


    | Print args ->
    let rec process_args args tenv =
        List.iter (fun arg ->
          match arg.edesc with
          | Call(fname, call_args) ->
              (* Cas spécial pour les appels de fonctions dans Print *)
              let func = 
                try Env.find fname.id fenv
                with Not_found -> error fname.loc ("Fonction " ^ fname.id ^ " non trouvée")
              in
              let param_types = List.map snd func.params in
              if List.length param_types <> List.length call_args then
                error fname.loc "Nombre d'arguments incorrect pour la fonction";
              
              List.iter2 (fun arg expected_type -> 
                check_expr arg expected_type tenv
              ) call_args param_types;
              
              (* On accepte les fonctions multi-retour dans Print *)
              ignore func.return  (* On ignore les types de retour car Print accepte tout *)
          | _ ->
              (* Cas normal: on type l'expression *)
              ignore (type_expr arg tenv)
        ) args
      in
      process_args args tenv;
        TInt  (* fmt.Print retourne le nombre d'octets écrits *)



  in
  let rec valeur_gauche e tenv =
  match e.edesc with
  | Var _ -> true
  | Dot (e1, _) -> valeur_gauche e1 tenv
  | _ -> false in 

  let rec check_instr i ret tenv = 
    match i.idesc with
    | Expr e -> 
        ignore (type_expr e tenv);
        tenv

    | Inc e | Dec e ->
        let t = type_expr e tenv in
        if t <> TInt then error i.iloc "++/-- possible qu'entre des entiers"
        else if not (valeur_gauche e tenv) then
          error i.iloc "++/-- peut être appliqué qu'à des variables ou fields";
        tenv

    | Set (g, d) -> 
        if List.length g <> List.length d then
          error i.iloc "nombre de variable différent du nombre de valeur";

        (* Valeurs gauche *)
        List.iter (fun l -> 
          if not (valeur_gauche l tenv) then
            error i.iloc "Le côté gauche de l'affectation doit contenir des variables ou des fields"
        ) g;

        (* Compatibilité des types *)
        List.iter2 (fun l r -> 
          let lt = type_expr l tenv in
          let rt = type_expr r tenv in
          if lt <> rt then
            type_error i.iloc rt lt
        ) g d;
        tenv

    | If (cond, thenn, elsee) ->
        let cond_type = type_expr cond tenv in
        if cond_type <> TBool then
          error cond.eloc "if condition doit être un bool";
        ignore (check_seq thenn ret tenv);
        ignore (check_seq elsee ret tenv);
        tenv

    | For (cond, body) ->
        let cond_type = type_expr cond tenv in
        if cond_type <> TBool then
          error cond.eloc "for condition doit être un bool";
        ignore (check_seq body ret tenv);
        tenv

    | Block seq ->
        let _ = check_seq seq ret tenv in
        tenv

   | Vars (ids, typ_opt, init_seq) ->

      let is_dummy id = (id.id = "_") in

      (* erreur si plusieurs initialisations *)
      let expr_opt =
        match init_seq with
        | [] -> None
        | [{ idesc = Expr e; _ }] -> Some e
        | [{ iloc = l; _  }] ->
            error l "initialisation invalide : attendu une expression"
        | instr :: _ ->
            error instr.iloc "initialisation invalide : trop d’instructions"
      in

      (* vérifier doublons, hors "_" *)
      let names =
        ids |> List.filter (fun i -> not (is_dummy i)) |> List.map (fun i -> i.id)
      in
      let rec check_dup = function
        | [] -> ()
        | x::xs ->
            if List.mem x xs then
              error (List.find (fun id -> id.id = x) ids).loc ("variable déjà déclarée : "^x)
            else check_dup xs
      in
      check_dup names;

      let n_ids = List.length ids in

      (* Ajoute un type unique à toutes les variables *)
      let add_uniform tenv t =
        List.fold_left (fun env id ->
          if is_dummy id then env else Env.add id.id t env
        ) tenv ids
      in

      (* Ajoute une liste de types à chaque variable *)
      let add_all tenv types =
        List.fold_left2 (fun env id t ->
          if is_dummy id then env else Env.add id.id t env
        ) tenv ids types
      in

      begin match typ_opt, expr_opt with

      (* Cas 1 : ni type ni initialisation → interdit *)
      | None, None ->
          error (List.hd ids).loc "il faut un type ou une initialisation"

      (* Cas 2 : type explicite sans initialisation *)
      | Some t, None ->
          check_typ t;
          add_uniform tenv t

      (* Cas 3 : type explicite + initialisation *)
      | Some t, Some e ->
          check_typ t;
          begin match e.edesc with

          | Call (fname, _) ->
              (* fenv : ident -> func_def *)
              let fdef =
                try Env.find fname.id fenv
                with Not_found ->
                  error fname.loc ("fonction inconnue : "^fname.id)
              in
              let ret_ts = fdef.return in

              if List.length ret_ts <> n_ids then
                error e.eloc "nombre de valeurs retournées incompatible";

              (* Tous les retours doivent être du type déclaré t *)
              List.iter (fun rt -> if rt <> t then type_error e.eloc rt t) ret_ts;
              add_uniform tenv t

          | _ ->
              if n_ids <> 1 then
                error e.eloc "initialisation multiple requiert un appel de fonction";

              let te = type_expr e tenv in
              if te <> t then type_error e.eloc te t;
              add_uniform tenv t
          end

      (* Cas 4 : pas de type explicite → inférence *)
      | None, Some e ->
          begin match e.edesc with

          | Call (fname, _) ->
              let fdef =
                try Env.find fname.id fenv
                with Not_found ->
                  error fname.loc ("fonction inconnue : "^fname.id)
              in
              let ret_ts = fdef.return in

              if List.length ret_ts <> n_ids then
                error e.eloc "nombre de valeurs retournées incompatible";

              add_all tenv ret_ts

          | _ ->
              if n_ids <> 1 then
                error e.eloc "initialisation multiple requiert un appel de fonction";

              if e.edesc = Nil then
                error e.eloc "impossible d'inférer le type depuis nil";

              let t = type_expr e tenv in
              add_uniform tenv t
          end
      end


    | Return exprs -> 
        match ret with 
        | [] -> 
            if exprs <> [] then 
              error i.iloc "fonction sans retour ne doit pas retourner de valeurs"; tenv
        | expected_types -> 
            if List.length exprs <> List.length expected_types then 
              error i.iloc (Printf.sprintf "pas le bon nombre de valeurs de retour: attendu %d, reçu %d" 
                          (List.length expected_types) (List.length exprs));
            List.iter2 (fun e expected_type ->
              check_expr e expected_type tenv
            ) exprs expected_types;
        tenv
        

  and check_seq s ret tenv =
  List.fold_left (fun env i -> check_instr i ret env) tenv s 
in 
  
  let check_function f = 

    let { fname = nom; params = param; return = retur; body = body } = f in

    if not (Env.mem nom.id fenv) then (*verif fonction dans l'env*)
      error nom.loc (Printf.sprintf "fonction %s non déclarée" nom.id);
    check_fields param;

    List.iter (fun t -> check_typ t) retur; (*verif les types sont ok*)
    

    let var_env = (*on crée notre env local*)
      List.fold_left (fun env (id, typ) ->
        Env.add id.id typ env
        ) Env.empty param 
      in 

    ignore(check_seq body retur var_env);

(* Renvoie true si l'instruction mène forcément à un return *)
let rec instr_always_returns instr =
  match instr.idesc with
  | Return _ -> true
  | Block seq -> seq_always_returns seq
  | If (_, thenn, elsee) ->
      seq_always_returns thenn && seq_always_returns elsee
  | _ -> false

(* Renvoie true si une séquence mène forcément à un return *)
and seq_always_returns seq =
  match seq with
  | [] -> false
  | _ ->
      (* seule la dernière instruction compte *)
      instr_always_returns (List.hd (List.rev seq))
in

(* vérification finale : seulement si la fonction a un return *)
if retur <> [] && not (seq_always_returns body) then
  error nom.loc "La fonction ne termine pas toujours par un return";

    (* collecte récursive des variables utilisées dans une expression *)
    let rec vars_in_expr acc e =
      let acc =
        match e.edesc with
        | Var x -> if x.id = dummy then acc else x.id :: acc
        | Dot (e1, _) -> vars_in_expr acc e1
        | Unop (_, e1) -> vars_in_expr acc e1
        | Binop (_, e1, e2) -> vars_in_expr (vars_in_expr acc e1) e2
        | Call (_, args) -> List.fold_left vars_in_expr acc args
        | Print args -> List.fold_left vars_in_expr acc args
        | New _ | Int _ | String _ | Bool _ | Nil -> acc
      in
      acc
    in

    (* collecte récursive des variables utilisées dans une instruction / séquence *)
    let rec collect_used_vars seq used =
      match seq with
      | [] -> used
      | instr :: rest ->
        let used_here =
          match instr.idesc with
          | Expr e -> vars_in_expr used e
          | Inc e | Dec e -> vars_in_expr used e
          | Set (lhs, rhs) ->
              let used_after_rhs = List.fold_left vars_in_expr used rhs in
              List.fold_left vars_in_expr used_after_rhs lhs
          | If (cond, thenn, elsee) ->
              let u1 = vars_in_expr used cond in
              let u2 = collect_used_vars thenn u1 in
              collect_used_vars elsee u2
          | For (cond, body2) ->
              let u1 = vars_in_expr used cond in
              collect_used_vars body2 u1
          | Block seq2 -> collect_used_vars seq2 used
          | Vars (_ids, _typ_opt, init_seq) ->
              (* initialisations éventuelles dans la déclaration *)
              collect_used_vars init_seq used
          | Return exprs ->
              List.fold_left vars_in_expr used exprs
        in
        collect_used_vars rest used_here
    in

    let used_vars = collect_used_vars body [] in
    List.iter (fun (param_id, _) ->
      if not (List.mem param_id.id used_vars) then
        error param_id.loc (Printf.sprintf "Paramètre %s déclaré mais non utilisé" param_id.id)
    ) param;

    

  in Env.iter (fun _ lf -> check_fields lf) senv;
     Env.iter (fun _ fd -> check_function fd) fenv;
     ld 

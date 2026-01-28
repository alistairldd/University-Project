{

  open Lexing
  open Mgoparser

  exception Error of string

  (* Stocker le dernier token *)
  let last_token = ref None

  (* Mettre à jour le dernier token *)
  let up_last_token token = 
    last_token := Some token

  (* Vérifier si on doit insérer un point-virgule *)
  let should_insert_pv () =
    match !last_token with
    | Some (IDENT _) | Some (INT _) | Some (STRING _)
    | Some (TRUE _) | Some (FALSE _) | Some (NIL)
    | Some (RETURN) | Some (INC) | Some (DEC) 
    | Some (RPAR) | Some (END)-> true
    | _ -> false

  let keyword_or_ident =
  let h = Hashtbl.create 17 in
  List.iter (fun (s, k) -> Hashtbl.add h s k)
    [ "else",       ELSE;
      "false",      FALSE(false);
      "true",       TRUE(true);
      "for",        FOR;
      "func",       FUNC;
      "if",         IF;
      "import",     IMPORT;
      "nil",        NIL;
      "package",    PACKAGE;
      "return",     RETURN;
      "struct",     STRUCT;
      "type",       TYPE; 
      "var",        VAR;

    ] ;
  fun s ->
    try  Hashtbl.find h s
    with Not_found -> IDENT(s)
        
}

let digit = ['0'-'9']
let number = digit+
let alpha = ['a'-'z' 'A'-'Z' '_']
let ident = alpha (alpha | digit)*
let fmt = "fmt" 


let hexa = ['0'-'9' 'a'-'f' 'A'-'F']
let entier = digit+ | (("0x" | "0X") hexa+)
let car =  [' ' - '!' '#'-'[' ']' - '~'] | '\\' | '\"' | '\n' | '\t'
let chaine  = '\"' car* '\"'
  
rule token = parse
  | ['\n'] {
      new_line lexbuf;
      (* on vérifie si on doit insérer un point-virgule *)
      if should_insert_pv () then (
        up_last_token (SEMI);
        SEMI
      ) else
        token lexbuf
    }
  | [' ' '\t' '\r']+  { token lexbuf }

  | "/*"              { comment lexbuf; token lexbuf }

  | "//"              { comment_line lexbuf; token lexbuf }

  | '"' fmt '"'       { let t = STRING("fmt") in up_last_token t; t }

  | '"' [^'"']* '"'     { STRING(lexeme lexbuf) }

  | number as n  { 
      let t = try INT(Int64.of_string n) with _ -> raise (Error "literal constant too large") in
      up_last_token t; t }
      
  | ident as id  { 
      let t = keyword_or_ident id in
      up_last_token t; t }

  | ";"  { let t = SEMI in up_last_token t; t }
  | ","  { let t = COMMA in up_last_token t; t }
  | "("  { let t = LPAR in up_last_token t; t }
  | ")"  { let t = RPAR in up_last_token t; t }
  | "{"  { let t = BEGIN in up_last_token t; t }
  | "}"  { let t = END in up_last_token t; t }
  | "*"  { let t = STAR in up_last_token t; t }
  | "+"  { let t = ADD in up_last_token t; t }
  | "-"  { let t = SUB in up_last_token t; t }
  | "||" { let t = OR in up_last_token t; t }
  | "&&" { let t = AND in up_last_token t; t }
  | ":=" { let t = COLONEQ in up_last_token t; t }
  | "="  { let t = SET in up_last_token t; t}
  | "==" { let t = EQ in up_last_token t; t }
  | "!=" { let t = NEQ in up_last_token t; t }
  | ">"  { let t = GT in up_last_token t; t }
  | "<"  { let t = LT in up_last_token t; t }
  | ">=" { let t = GE in up_last_token t; t }
  | "<=" { let t = LE in up_last_token t; t }
  | "/"  { let t = DIV in up_last_token t; t }
  | "%"  { let t = REM in up_last_token t; t }
  | "."  { let t = DOT in up_last_token t; t }
  | "!"  { let t = NOT in up_last_token t; t }
  | "++" { let t = INC in up_last_token t; t }
  | "--" { let t = DEC in up_last_token t; t }


  | _    { raise (Error ("unknown character : " ^ lexeme lexbuf)) }
  | eof  { 
        if should_insert_pv () then (
          (* Point virgule automatique en fin de fichier (correction du bug) *)
          up_last_token SEMI;
          SEMI
        ) else (
          EOF 
        )
      }

and comment = parse
  | '\n' { new_line lexbuf; comment lexbuf }
  | "*/" { () }
  | _    { comment lexbuf }
  | eof  { raise (Error "unterminated comment") }

and comment_line = parse
  | '\n' { new_line lexbuf}
  | eof  { () }
  | _    { comment_line lexbuf }

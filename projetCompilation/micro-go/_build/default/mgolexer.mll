{

  open Lexing
  open Mgoparser

  exception Error of string

  let keyword_or_ident =
  let h = Hashtbl.create 17 in
  List.iter (fun (s, k) -> Hashtbl.add h s k)
    [ "else",       ELSE;
      "false",      bool FALSE;
      "true",       bool TRUE;
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
  
rule token = parse
  | ['\n']            { new_line lexbuf; token lexbuf }
  | [' ' '\t' '\r']+  { token lexbuf }

  | "/*"              { comment lexbuf; token lexbuf }

  | '"' fmt '"'       { STRING("fmt") }

  | number as n  { try INT(Int64.of_string n) 
                   with _ -> raise (Error "literal constant too large") }
  | ident as id  { keyword_or_ident id }

  | ";"  { SEMI }
  | "("  { LPAR }
  | ")"  { RPAR }
  | "{"  { BEGIN }
  | "}"  { END }
  | "*"  { STAR }
  | "+"  { PLUS }
  | "-"  { MINUS }
  | "||" { OR }
  | "&&" { AND }
  | "==" { EQ }
  | "!=" { NEQ }
  | ">"  { GT }
  | "<"  { LT }
  | ">=" { GET }
  | "<=" { LET }
  | "/"  { SLASH }
  | "%"  { MOD }
  | "."  { DOT }

  | _    { raise (Error ("unknown character : " ^ lexeme lexbuf)) }
  | eof  { EOF }

and comment = parse
  | '\n' { new_line lexbuf; comment lexbuf }
  | "*/" { () }
  | _    { comment lexbuf }
  | eof  { raise (Error "unterminated comment") }

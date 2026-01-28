
(* The type of tokens. *)

type token = 
  | TYPE
  | STRUCT
  | STRING of (string)
  | STAR
  | SEMI
  | RPAR
  | PACKAGE
  | LPAR
  | INT of (int64)
  | IMPORT
  | IDENT of (string)
  | EOF
  | END
  | BEGIN

(* This exception is raised by the monolithic API functions. *)

exception Error

(* The monolithic API. *)

val prog: (Lexing.lexbuf -> token) -> Lexing.lexbuf -> (Mgoast.program)


module MenhirBasics = struct
  
  exception Error
  
  let _eRR =
    fun _s ->
      raise Error
  
  type token = 
    | TYPE
    | STRUCT
    | STRING of (
# 12 "mgoparser.mly"
       (string)
# 17 "mgoparser.ml"
  )
    | STAR
    | SEMI
    | RPAR
    | PACKAGE
    | LPAR
    | INT of (
# 10 "mgoparser.mly"
       (int64)
# 27 "mgoparser.ml"
  )
    | IMPORT
    | IDENT of (
# 11 "mgoparser.mly"
       (string)
# 33 "mgoparser.ml"
  )
    | EOF
    | END
    | BEGIN
  
end

include MenhirBasics

# 1 "mgoparser.mly"
  

  open Lexing
  open Mgoast

  exception Error


# 52 "mgoparser.ml"

type ('s, 'r) _menhir_state = 
  | MenhirState03 : ('s _menhir_cell0_IDENT, _menhir_box_prog) _menhir_state
    (** State 03.
        Stack shape : IDENT.
        Start symbol: prog. *)

  | MenhirState04 : (('s, _menhir_box_prog) _menhir_cell1_TYPE, _menhir_box_prog) _menhir_state
    (** State 04.
        Stack shape : TYPE.
        Start symbol: prog. *)

  | MenhirState08 : ((('s, _menhir_box_prog) _menhir_cell1_TYPE, _menhir_box_prog) _menhir_cell1_ident, _menhir_box_prog) _menhir_state
    (** State 08.
        Stack shape : TYPE ident.
        Start symbol: prog. *)

  | MenhirState10 : (('s, _menhir_box_prog) _menhir_cell1_varstyp, _menhir_box_prog) _menhir_state
    (** State 10.
        Stack shape : varstyp.
        Start symbol: prog. *)

  | MenhirState23 : (('s _menhir_cell0_IDENT, _menhir_box_prog) _menhir_cell1_IMPORT _menhir_cell0_STRING, _menhir_box_prog) _menhir_state
    (** State 23.
        Stack shape : IDENT IMPORT STRING.
        Start symbol: prog. *)

  | MenhirState26 : (('s, _menhir_box_prog) _menhir_cell1_decl, _menhir_box_prog) _menhir_state
    (** State 26.
        Stack shape : decl.
        Start symbol: prog. *)


and ('s, 'r) _menhir_cell1_decl = 
  | MenhirCell1_decl of 's * ('s, 'r) _menhir_state * (Mgoast.decl)

and ('s, 'r) _menhir_cell1_ident = 
  | MenhirCell1_ident of 's * ('s, 'r) _menhir_state * (Mgoast.ident)

and ('s, 'r) _menhir_cell1_varstyp = 
  | MenhirCell1_varstyp of 's * ('s, 'r) _menhir_state * ((Mgoast.ident * Mgoast.typ) list)

and 's _menhir_cell0_IDENT = 
  | MenhirCell0_IDENT of 's * (
# 11 "mgoparser.mly"
       (string)
# 99 "mgoparser.ml"
) * Lexing.position * Lexing.position

and ('s, 'r) _menhir_cell1_IMPORT = 
  | MenhirCell1_IMPORT of 's * ('s, 'r) _menhir_state

and 's _menhir_cell0_STRING = 
  | MenhirCell0_STRING of 's * (
# 12 "mgoparser.mly"
       (string)
# 109 "mgoparser.ml"
)

and ('s, 'r) _menhir_cell1_TYPE = 
  | MenhirCell1_TYPE of 's * ('s, 'r) _menhir_state

and _menhir_box_prog = 
  | MenhirBox_prog of (Mgoast.program) [@@unboxed]

let _menhir_action_01 =
  fun fl id ->
    (
# 35 "mgoparser.mly"
  ( Struct { sname = id; fields = List.flatten fl; } )
# 123 "mgoparser.ml"
     : (Mgoast.decl))

let _menhir_action_02 =
  fun xt ->
    (
# 46 "mgoparser.mly"
                                ( [xt]      )
# 131 "mgoparser.ml"
     : ((Mgoast.ident * Mgoast.typ) list list))

let _menhir_action_03 =
  fun xt xtl ->
    (
# 47 "mgoparser.mly"
                                ( xt :: xtl )
# 139 "mgoparser.ml"
     : ((Mgoast.ident * Mgoast.typ) list list))

let _menhir_action_04 =
  fun _endpos_id_ _startpos_id_ id ->
    let _endpos = _endpos_id_ in
    let _startpos = _startpos_id_ in
    (
# 30 "mgoparser.mly"
             ( { loc = _startpos, _endpos; id = id } )
# 149 "mgoparser.ml"
     : (Mgoast.ident))

let _menhir_action_05 =
  fun () ->
    (
# 216 "<standard.mly>"
    ( [] )
# 157 "mgoparser.ml"
     : (Mgoast.decl list))

let _menhir_action_06 =
  fun x xs ->
    (
# 219 "<standard.mly>"
    ( x :: xs )
# 165 "mgoparser.ml"
     : (Mgoast.decl list))

let _menhir_action_07 =
  fun () ->
    (
# 145 "<standard.mly>"
    ( [] )
# 173 "mgoparser.ml"
     : ((Mgoast.ident * Mgoast.typ) list list))

let _menhir_action_08 =
  fun x ->
    (
# 148 "<standard.mly>"
    ( x )
# 181 "mgoparser.ml"
     : ((Mgoast.ident * Mgoast.typ) list list))

let _menhir_action_09 =
  fun s ->
    (
# 39 "mgoparser.mly"
                 ( TStruct(s) )
# 189 "mgoparser.ml"
     : (Mgoast.typ))

let _menhir_action_10 =
  fun () ->
    (
# 111 "<standard.mly>"
    ( None )
# 197 "mgoparser.ml"
     : (unit option))

let _menhir_action_11 =
  fun x ->
    (
# 114 "<standard.mly>"
    ( Some x )
# 205 "mgoparser.ml"
     : (unit option))

let _menhir_action_12 =
  fun decls main ->
    (
# 24 "mgoparser.mly"
    ( if main="main" then (false, decls) else raise Error)
# 213 "mgoparser.ml"
     : (Mgoast.program))

let _menhir_action_13 =
  fun decls fmt main ->
    (
# 26 "mgoparser.mly"
    ( if main="main" && fmt="fmt" then (true, decls) else raise Error)
# 221 "mgoparser.ml"
     : (Mgoast.program))

let _menhir_action_14 =
  fun t x ->
    (
# 43 "mgoparser.mly"
                                     ([(x,t)])
# 229 "mgoparser.ml"
     : ((Mgoast.ident * Mgoast.typ) list))

let _menhir_print_token : token -> string =
  fun _tok ->
    match _tok with
    | BEGIN ->
        "BEGIN"
    | END ->
        "END"
    | EOF ->
        "EOF"
    | IDENT _ ->
        "IDENT"
    | IMPORT ->
        "IMPORT"
    | INT _ ->
        "INT"
    | LPAR ->
        "LPAR"
    | PACKAGE ->
        "PACKAGE"
    | RPAR ->
        "RPAR"
    | SEMI ->
        "SEMI"
    | STAR ->
        "STAR"
    | STRING _ ->
        "STRING"
    | STRUCT ->
        "STRUCT"
    | TYPE ->
        "TYPE"

let _menhir_fail : unit -> 'a =
  fun () ->
    Printf.eprintf "Internal failure -- please contact the parser generator's developers.\n%!";
    assert false

include struct
  
  [@@@ocaml.warning "-4-37"]
  
  let _menhir_goto_prog : type  ttv_stack. ttv_stack -> _ -> _menhir_box_prog =
    fun _menhir_stack _v ->
      MenhirBox_prog _v
  
  let _menhir_run_28 : type  ttv_stack. ttv_stack _menhir_cell0_IDENT -> _ -> _menhir_box_prog =
    fun _menhir_stack _v ->
      let MenhirCell0_IDENT (_menhir_stack, main, _, _) = _menhir_stack in
      let decls = _v in
      let _v = _menhir_action_12 decls main in
      _menhir_goto_prog _menhir_stack _v
  
  let _menhir_run_24 : type  ttv_stack. (ttv_stack _menhir_cell0_IDENT, _menhir_box_prog) _menhir_cell1_IMPORT _menhir_cell0_STRING -> _ -> _menhir_box_prog =
    fun _menhir_stack _v ->
      let MenhirCell0_STRING (_menhir_stack, fmt) = _menhir_stack in
      let MenhirCell1_IMPORT (_menhir_stack, _) = _menhir_stack in
      let MenhirCell0_IDENT (_menhir_stack, main, _, _) = _menhir_stack in
      let decls = _v in
      let _v = _menhir_action_13 decls fmt main in
      _menhir_goto_prog _menhir_stack _v
  
  let rec _menhir_run_27 : type  ttv_stack. (ttv_stack, _menhir_box_prog) _menhir_cell1_decl -> _ -> _menhir_box_prog =
    fun _menhir_stack _v ->
      let MenhirCell1_decl (_menhir_stack, _menhir_s, x) = _menhir_stack in
      let xs = _v in
      let _v = _menhir_action_06 x xs in
      _menhir_goto_list_decl_ _menhir_stack _v _menhir_s
  
  and _menhir_goto_list_decl_ : type  ttv_stack. ttv_stack -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _menhir_box_prog =
    fun _menhir_stack _v _menhir_s ->
      match _menhir_s with
      | MenhirState03 ->
          _menhir_run_28 _menhir_stack _v
      | MenhirState26 ->
          _menhir_run_27 _menhir_stack _v
      | MenhirState23 ->
          _menhir_run_24 _menhir_stack _v
      | _ ->
          _menhir_fail ()
  
  let rec _menhir_run_04 : type  ttv_stack. ttv_stack -> _ -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _menhir_s ->
      let _menhir_stack = MenhirCell1_TYPE (_menhir_stack, _menhir_s) in
      let _menhir_s = MenhirState04 in
      let _tok = _menhir_lexer _menhir_lexbuf in
      match (_tok : MenhirBasics.token) with
      | IDENT _v ->
          _menhir_run_05 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s
      | _ ->
          _eRR ()
  
  and _menhir_run_05 : type  ttv_stack. ttv_stack -> _ -> _ -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s ->
      let _startpos = _menhir_lexbuf.Lexing.lex_start_p in
      let _endpos = _menhir_lexbuf.Lexing.lex_curr_p in
      let _tok = _menhir_lexer _menhir_lexbuf in
      let (_endpos_id_, _startpos_id_, id) = (_endpos, _startpos, _v) in
      let _v = _menhir_action_04 _endpos_id_ _startpos_id_ id in
      _menhir_goto_ident _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok
  
  and _menhir_goto_ident : type  ttv_stack. ttv_stack -> _ -> _ -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok ->
      match _menhir_s with
      | MenhirState08 ->
          _menhir_run_11 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok
      | MenhirState10 ->
          _menhir_run_11 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok
      | MenhirState04 ->
          _menhir_run_06 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok
      | _ ->
          _menhir_fail ()
  
  and _menhir_run_11 : type  ttv_stack. ttv_stack -> _ -> _ -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok ->
      let _menhir_stack = MenhirCell1_ident (_menhir_stack, _menhir_s, _v) in
      match (_tok : MenhirBasics.token) with
      | STAR ->
          let _tok = _menhir_lexer _menhir_lexbuf in
          (match (_tok : MenhirBasics.token) with
          | IDENT _v_0 ->
              let _tok = _menhir_lexer _menhir_lexbuf in
              let s = _v_0 in
              let _v = _menhir_action_09 s in
              let MenhirCell1_ident (_menhir_stack, _menhir_s, x) = _menhir_stack in
              let t = _v in
              let _v = _menhir_action_14 t x in
              let _menhir_stack = MenhirCell1_varstyp (_menhir_stack, _menhir_s, _v) in
              (match (_tok : MenhirBasics.token) with
              | SEMI ->
                  let _menhir_s = MenhirState10 in
                  let _tok = _menhir_lexer _menhir_lexbuf in
                  (match (_tok : MenhirBasics.token) with
                  | IDENT _v ->
                      _menhir_run_05 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s
                  | END ->
                      let x = () in
                      let _ = _menhir_action_11 x in
                      _menhir_goto_option_SEMI_ _menhir_stack _menhir_lexbuf _menhir_lexer
                  | _ ->
                      _eRR ())
              | END ->
                  let _ = _menhir_action_10 () in
                  _menhir_goto_option_SEMI_ _menhir_stack _menhir_lexbuf _menhir_lexer
              | _ ->
                  _eRR ())
          | _ ->
              _eRR ())
      | _ ->
          _eRR ()
  
  and _menhir_goto_option_SEMI_ : type  ttv_stack. (ttv_stack, _menhir_box_prog) _menhir_cell1_varstyp -> _ -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer ->
      let MenhirCell1_varstyp (_menhir_stack, _menhir_s, xt) = _menhir_stack in
      let _v = _menhir_action_02 xt in
      _menhir_goto_fields _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s
  
  and _menhir_goto_fields : type  ttv_stack. ttv_stack -> _ -> _ -> _ -> (ttv_stack, _menhir_box_prog) _menhir_state -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s ->
      match _menhir_s with
      | MenhirState08 ->
          _menhir_run_20 _menhir_stack _menhir_lexbuf _menhir_lexer _v
      | MenhirState10 ->
          _menhir_run_15 _menhir_stack _menhir_lexbuf _menhir_lexer _v
      | _ ->
          _menhir_fail ()
  
  and _menhir_run_20 : type  ttv_stack. ((ttv_stack, _menhir_box_prog) _menhir_cell1_TYPE, _menhir_box_prog) _menhir_cell1_ident -> _ -> _ -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v ->
      let x = _v in
      let _v = _menhir_action_08 x in
      _menhir_goto_loption_fields_ _menhir_stack _menhir_lexbuf _menhir_lexer _v
  
  and _menhir_goto_loption_fields_ : type  ttv_stack. ((ttv_stack, _menhir_box_prog) _menhir_cell1_TYPE, _menhir_box_prog) _menhir_cell1_ident -> _ -> _ -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v ->
      let _tok = _menhir_lexer _menhir_lexbuf in
      match (_tok : MenhirBasics.token) with
      | SEMI ->
          let _tok = _menhir_lexer _menhir_lexbuf in
          let MenhirCell1_ident (_menhir_stack, _, id) = _menhir_stack in
          let MenhirCell1_TYPE (_menhir_stack, _menhir_s) = _menhir_stack in
          let fl = _v in
          let _v = _menhir_action_01 fl id in
          let _menhir_stack = MenhirCell1_decl (_menhir_stack, _menhir_s, _v) in
          (match (_tok : MenhirBasics.token) with
          | TYPE ->
              _menhir_run_04 _menhir_stack _menhir_lexbuf _menhir_lexer MenhirState26
          | EOF ->
              let _v_0 = _menhir_action_05 () in
              _menhir_run_27 _menhir_stack _v_0
          | _ ->
              _eRR ())
      | _ ->
          _eRR ()
  
  and _menhir_run_15 : type  ttv_stack. (ttv_stack, _menhir_box_prog) _menhir_cell1_varstyp -> _ -> _ -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v ->
      let MenhirCell1_varstyp (_menhir_stack, _menhir_s, xt) = _menhir_stack in
      let xtl = _v in
      let _v = _menhir_action_03 xt xtl in
      _menhir_goto_fields _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s
  
  and _menhir_run_06 : type  ttv_stack. ((ttv_stack, _menhir_box_prog) _menhir_cell1_TYPE as 'stack) -> _ -> _ -> _ -> ('stack, _menhir_box_prog) _menhir_state -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s _tok ->
      let _menhir_stack = MenhirCell1_ident (_menhir_stack, _menhir_s, _v) in
      match (_tok : MenhirBasics.token) with
      | STRUCT ->
          let _tok = _menhir_lexer _menhir_lexbuf in
          (match (_tok : MenhirBasics.token) with
          | BEGIN ->
              let _menhir_s = MenhirState08 in
              let _tok = _menhir_lexer _menhir_lexbuf in
              (match (_tok : MenhirBasics.token) with
              | IDENT _v ->
                  _menhir_run_05 _menhir_stack _menhir_lexbuf _menhir_lexer _v _menhir_s
              | END ->
                  let _v = _menhir_action_07 () in
                  _menhir_goto_loption_fields_ _menhir_stack _menhir_lexbuf _menhir_lexer _v
              | _ ->
                  _eRR ())
          | _ ->
              _eRR ())
      | _ ->
          _eRR ()
  
  let _menhir_run_00 : type  ttv_stack. ttv_stack -> _ -> _ -> _menhir_box_prog =
    fun _menhir_stack _menhir_lexbuf _menhir_lexer ->
      let _tok = _menhir_lexer _menhir_lexbuf in
      match (_tok : MenhirBasics.token) with
      | PACKAGE ->
          let _tok = _menhir_lexer _menhir_lexbuf in
          (match (_tok : MenhirBasics.token) with
          | IDENT _v ->
              let _startpos = _menhir_lexbuf.Lexing.lex_start_p in
              let _endpos = _menhir_lexbuf.Lexing.lex_curr_p in
              let _menhir_stack = MenhirCell0_IDENT (_menhir_stack, _v, _startpos, _endpos) in
              let _tok = _menhir_lexer _menhir_lexbuf in
              (match (_tok : MenhirBasics.token) with
              | SEMI ->
                  let _tok = _menhir_lexer _menhir_lexbuf in
                  (match (_tok : MenhirBasics.token) with
                  | TYPE ->
                      _menhir_run_04 _menhir_stack _menhir_lexbuf _menhir_lexer MenhirState03
                  | IMPORT ->
                      let _menhir_stack = MenhirCell1_IMPORT (_menhir_stack, MenhirState03) in
                      let _tok = _menhir_lexer _menhir_lexbuf in
                      (match (_tok : MenhirBasics.token) with
                      | STRING _v_0 ->
                          let _menhir_stack = MenhirCell0_STRING (_menhir_stack, _v_0) in
                          let _tok = _menhir_lexer _menhir_lexbuf in
                          (match (_tok : MenhirBasics.token) with
                          | SEMI ->
                              let _tok = _menhir_lexer _menhir_lexbuf in
                              (match (_tok : MenhirBasics.token) with
                              | TYPE ->
                                  _menhir_run_04 _menhir_stack _menhir_lexbuf _menhir_lexer MenhirState23
                              | EOF ->
                                  let _v_1 = _menhir_action_05 () in
                                  _menhir_run_24 _menhir_stack _v_1
                              | _ ->
                                  _eRR ())
                          | _ ->
                              _eRR ())
                      | _ ->
                          _eRR ())
                  | EOF ->
                      let _v_2 = _menhir_action_05 () in
                      _menhir_run_28 _menhir_stack _v_2
                  | _ ->
                      _eRR ())
              | _ ->
                  _eRR ())
          | _ ->
              _eRR ())
      | _ ->
          _eRR ()
  
end

let prog =
  fun _menhir_lexer _menhir_lexbuf ->
    let _menhir_stack = () in
    let MenhirBox_prog v = _menhir_run_00 _menhir_stack _menhir_lexbuf _menhir_lexer in
    v

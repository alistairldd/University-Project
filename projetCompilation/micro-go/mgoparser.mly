%{

  (*open Lexing*)
  open Mgoast

  exception Error
  let flag = ref false
%}

%token <int64> INT
%token <string> IDENT
%token <string> STRING
%token <bool> TRUE FALSE
%token PACKAGE IMPORT TYPE STRUCT
%token LPAR RPAR BEGIN END SEMI COMMA
%token STAR ADD SUB DIV REM INC DEC
%token EQ NEQ GT LT GE LE SET COLONEQ
%token FOR NOT AND OR DOT RETURN FUNC
%token IF ELSE 
%token NIL VAR
%token EOF



%left OR
%left AND

%left EQ NEQ LE LT GT GE


%left ADD SUB
%left STAR DIV REM

%nonassoc unary_minus NOT 
%left DOT


%start prog
%type <Mgoast.program> prog

%%

prog:
| PACKAGE main=IDENT SEMI decls=list(decl) EOF
    { if main="main" then if !flag then(false, decls) else raise Error else raise Error}
| PACKAGE main=IDENT SEMI IMPORT fmt=STRING SEMI decls=list(decl) EOF
    { if main="main" && fmt="fmt" then if !flag then (true, decls) else raise Error else raise Error} 
;

ident:
  id = IDENT { { loc = $startpos, $endpos; id = id } }
;

// Dans l'ordre de la grammaire 

decl:
 | TYPE id=ident STRUCT BEGIN fl=fields END SEMI //structure
  { Struct { sname = id; fields = List.flatten fl }  }

 | FUNC id = ident LPAR vst = fields RPAR ret = return_type bd = block SEMI
  { if id.id = "main" then flag := true;
    Fun { fname = id; params = List.flatten vst; return = ret; body = bd} } 
;

fields:
| separated_list(SEMI, varstyp) { $1 }


varstyp: // vars
  | ids=separated_nonempty_list(COMMA, ident) t=mgotype
    { List.map (fun x -> (x, t)) ids }
;


return_type: //type_return
  | /*vide*/ {[]}
  | t=mgotype { [t] }
  | LPAR tl=separated_list(COMMA, mgotype) RPAR { tl }
;

mgotype: //type
  | id = IDENT {
    match id with
    | "int" -> TInt | "bool" -> TBool | "string" -> TString
    | _ -> raise Error
  }
  | STAR s=IDENT { TStruct(s) }
;

expr:
| e = expr_desc {  { eloc = $startpos, $endpos; edesc = e } }

;



expr_desc:
| n = INT { Int(n) }
| s = STRING { String(s) }
| TRUE {  Bool(true) }
| FALSE {  Bool(false) }
| NIL {  Nil }
| LPAR e = expr RPAR {  e.edesc }
| e = expr DOT id = ident { Dot(e, id) }
| id = ident {  Var id } 


| e1 = ident LPAR args = separated_list(COMMA, expr) RPAR 
        { if e1.id = "new" then
            match args with
            | [ { edesc = Var(s); _} ] -> New (s.id)
            | _ -> raise Error
          else 
          Call(e1, args) }

| e1 = expr DOT e2 = ident LPAR args = separated_list(COMMA, expr) RPAR
    { 
      if (match e1.edesc with 
          | Var(id) when id.id = "fmt" -> e2.id = "Print" 
          | _ -> false)
      then Print(args)
      else raise Error  
    }

| SUB e = expr %prec unary_minus { Unop(Opp, e) }
| NOT e = expr { Unop(Not, e) }

// op 
| e1 = expr ADD e2 = expr   { Binop(Add, e1, e2) }
| e1 = expr SUB e2 = expr   { Binop(Sub, e1, e2) }
| e1 = expr STAR e2 = expr  { Binop(Mul, e1, e2) }
| e1 = expr DIV e2 = expr   { Binop(Div, e1, e2) }
| e1 = expr REM e2 = expr   { Binop(Rem, e1, e2) }
| e1 = expr LT e2 = expr    { Binop(Lt, e1, e2) }
| e1 = expr LE e2 = expr    { Binop(Le, e1, e2) }
| e1 = expr GT e2 = expr    { Binop(Gt, e1, e2) }
| e1 = expr GE e2 = expr    { Binop(Ge, e1, e2) }
| e1 = expr EQ e2 = expr    { Binop(Eq, e1, e2) }
| e1 = expr NEQ e2 = expr   { Binop(Neq, e1, e2) }
| e1 = expr AND e2 = expr   { Binop(And, e1, e2) }
| e1 = expr OR e2 = expr    {Binop(Or, e1, e2) } 
;

block:
| BEGIN instr_list_opt END {$2}

instr_list:
| instr option(SEMI) { [$1] }
| instr SEMI instr_list { $1 :: $3 }

instr_list_opt:
| { [] }
| instr_list { $1 }


instr:
| i = instr_desc {  { iloc = $startpos, $endpos; idesc = i } }



instr_desc:
| instr_simple { $1 }
| block { Block($1) }
| instr_if { $1 }

| VAR ids=separated_nonempty_list(COMMA, ident) t = option(mgotype) init = var_init_opt
    { Vars(ids, t, init)}

| RETURN separated_list(COMMA, expr) { Return($2) }

| FOR block { For({ eloc = $startpos, $endpos; edesc = Nil } , $2) }
| FOR expr block {For($2, $3)}
| FOR i1 = option(instr_simple) SEMI e = expr SEMI i2 = option(instr_simple) b = block {
  let r1 = match i1 with | None -> [] | Some i  -> [{ iloc = $startpos, $endpos; idesc = i }] in 
  let r2 = match i2 with | None -> [] | Some i  -> [{ iloc = $startpos, $endpos; idesc = i }] in 
  For(e, (r1 @ r2 @ b))  } 


var_init_opt:
| {[]}
| SET es = separated_nonempty_list(COMMA, expr) 
     {List.map (fun e -> { iloc = $startpos, $endpos; idesc = Expr e }) es }

instr_simple:
| expr {  Expr($1) }
| expr INC { Inc($1) } 
| expr DEC { Dec($1) }

| g = separated_nonempty_list(COMMA, expr) op = assign_op d = separated_nonempty_list(COMMA, expr)
    { match op with 
        | SET -> Set(g, d)
        | COLONEQ -> begin
          let gg = List.map (fun e -> 
              match e.edesc with 
              | Var id ->  id
              | _ -> raise Error 
          ) g in 
          
          let init_seq = List.map (fun e -> {iloc = e.eloc; idesc = Expr e}) d in
          Vars(gg, None, init_seq)
          end
        | _ -> raise Error
    }


assign_op:
| SET { SET }
| COLONEQ { COLONEQ }


instr_if:
| IF expr block { If($2, $3, [])}
| IF expr block ELSE block { If($2, $3, $5)}
| IF expr block ELSE i2 = instr_if
    { let inner_instr = { iloc = $startpos, $endpos; idesc = i2 } in
      If($2, $3, [inner_instr])}

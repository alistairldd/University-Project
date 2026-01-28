open Mgoast
open Mips

module Env = Map.Make(String)
type env = int Env.t 

let new_label =
  let cpt = ref (-1) in
  fun () -> incr cpt; Printf.sprintf "_label_%i" !cpt

let data_accu = ref nop

let field_offsets = ref Env.empty

let shift_env env n = Env.map (fun off -> off + n) env  

let push_list regs = List.fold_left (fun acc reg -> acc @@ push reg) nop regs
let pop_list regs = List.fold_left (fun acc reg -> acc @@ pop reg) nop (List.rev regs)

let malloc = 
  move a0 t0   
  @@ li v0 9    
  @@ syscall
  @@ move t0 v0 

let alloc_vars ids env start_offset =
  List.fold_left(
    fun (env, offset, code) id -> 
      let new_env = Env.add id.id offset env in 
      let init_code = 
        li t0 0
        @@ sw t0 offset sp
      in
      (new_env, offset - 4, code @@ init_code)
  ) (env, start_offset, nop) ids 





let rec tr_expr env e = match e.edesc with

  | Int(n)  -> li t0 (Int64.to_int n)   
  
  | String(s) -> let lbl = new_label() in 
        data_accu := !data_accu @@ label lbl @@asciiz s; 
        la t0 lbl 
  
  | Var(id) -> let offset = 
                try Env.find id.id env 
              with Not_found -> failwith (" variable non trouvée : " ^id.id) 
              in
              lw t0 offset sp


  | Binop(bop, e1, e2) ->
    let op = match bop with
      | Add -> add
      | Sub -> sub
      | Mul -> mul
      | Div -> div
      | Rem -> rem
      | Lt  -> slt
      | Le  -> sle
      | Gt  -> sgt
      | Ge  -> sge 
      | Eq  -> seq 
      | Neq -> sne 
      | And -> and_
      | Or  -> or_
    in
    tr_expr env e2
    @@ push t0
    @@ tr_expr (shift_env env 4) e1
    @@ pop t1
    @@ op t0 t0 t1

  | Unop(op, e1) ->
        let ope = 
        ( 
          match op with
          | Opp -> neg
          | Not -> not_
        ) in
      tr_expr env e1 @@ ope t0 t0
      


    | Call(fname, args) -> 
        let save_regs = push_list [ra] in 
        let restore_regs = pop_list [ra] in
        
        let env_for_args = shift_env env 4 in 

        let (push_args_code, _) = List.fold_left (fun (code_acc, env_acc) arg -> 
          let code = 
            tr_expr env_acc arg 
            @@ push t0 
          in
          (code_acc @@ code, shift_env env_acc 4)
        ) (nop, env_for_args) args in 

          save_regs 
          @@ push_args_code
          @@ jal fname.id 
          @@ addi sp sp (4 * List.length args)
          @@ restore_regs
          @@ move t0 v0 

  | New (s) -> 
    li t0 8  
    @@ malloc

  | Dot(e, field) -> 
        tr_expr env e 
        @@ (
          let offset = 
            try Env.find field.id !field_offsets 
            with Not_found -> 0 
          in
          lw t0 offset(t0) 
        )

  | Bool b -> 
    let res = if b then 1 else 0 in 
    li t0 res

  | Nil -> li t0 0

| Print(args) -> 
    let print_arg arg = 
      tr_expr env arg 
      @@ push t0 
      @@ (match arg.edesc with
          | Int _ | Bool _ -> 
            move a0 t0 @@ li v0 1 @@ syscall
          | String _ -> 
            move a0 t0 @@ li v0 4 @@ syscall
          | _ -> 
            move a0 t0 @@ li v0 1 @@ syscall)
      @@ pop t0
      @@ li v0 11 @@ li a0 32 @@ syscall 
    in List.fold_left (fun acc arg -> acc @@ print_arg arg) nop args


let rec tr_seq env stack_acc = function
  | []   -> nop
  | i::s -> 
      match i.idesc with
      | Vars(ids, t, body) ->
          let n = List.length ids in
          let frame_size = 4 * n in
          let alloc_frame = addi sp sp (-frame_size) in
          
          let shifted_env = Env.map (fun off -> off + frame_size) env in

let rec build env offset init_code ids_list body_list =
            match ids_list, body_list with
            | [], _ -> (env, init_code)
            
            | [id1; id2], [{idesc=Expr(e) ; _}] -> 
                let env' = Env.add id1.id offset env in
                let env'' = Env.add id2.id (offset + 4) env' in
                let code = tr_expr env e 
                  @@ sw v0 offset sp  
                  @@ S(Printf.sprintf "  sw   $v1, %d($sp)" (offset + 4)) 
                in
                (env'', init_code @@ code)


            | id :: rest_ids, {idesc=Expr(e); _} :: rest_body ->
                let env' = Env.add id.id offset env in
                let code = tr_expr env e @@ sw t0 offset sp in
                build env' (offset + 4) (init_code @@ code) rest_ids rest_body
            | id :: rest_ids, [] ->
                let env' = Env.add id.id offset env in
                let code = li t0 0 @@ sw t0 offset sp in
                build env' (offset + 4) (init_code @@ code) rest_ids []
            | _ -> failwith "Erreur interne Vars"
          in

          let (new_env, init_code) = build shifted_env 0 nop ids body in

          alloc_frame
          @@ init_code

          @@ tr_seq new_env (stack_acc + frame_size) s 
          @@ addi sp sp frame_size

      | _ -> 
          tr_instr env stack_acc i @@ tr_seq env stack_acc s

and tr_instr env stack_acc i = match i.idesc with 
  | If(c, s1, s2) ->
    let then_label = new_label()
    and end_label = new_label()
    in
    tr_expr env c
    @@ bnez t0 then_label
    @@ tr_seq env stack_acc s2
    @@ b end_label
    @@ label then_label
    @@ tr_seq env stack_acc s1
    @@ label end_label

  | For(c, s) ->
    let test_label = new_label()
    and code_label = new_label()
    in
    b test_label
    @@ label code_label
    @@ tr_seq env stack_acc s
    @@ label test_label
    @@ tr_expr env c
    @@ bnez t0 code_label

  | Expr e -> tr_expr env e

  | Set(le, ri) ->
      tr_expr env (List.hd ri)
      @@ push t0
      @@
      (match (List.hd le).edesc with
      | Var id -> 
        let offset = Env.find id.id env in 
        pop t1 @@ sw t1 offset sp
      | Dot(obj, fields) ->
              let offset = 
                try Env.find fields.id !field_offsets 
                with Not_found -> 0 
              in
              tr_expr (shift_env env 4) obj 
              @@ pop t1
              @@ sw t1 offset(t0)
      | _ -> failwith "Affectation pas supportée" )

  | Return exprs -> 
      let code_ret = match exprs with 
      | [] -> nop
      | [e] -> tr_expr env e @@ move v0 t0
      | [e1; e2] -> 

          tr_expr env e1 
          @@ push t0 
   
          @@ tr_expr (shift_env env 4) e2 
          @@ S "  move $v1, $t0" 
          @@ pop v0
      | _ -> failwith "Plus de 2 valeurs de retour non supporté"
      in
      code_ret
      @@ (if stack_acc > 0 then addi sp sp stack_acc else nop)
      @@ jr ra


  | Vars _ -> failwith "Vars doit être géré dans tr_seq"

  | Inc e ->
    tr_expr env e
    @@ addi t0 t0 1
    @@ push t0
    @@ (match e.edesc with
        | Var id -> let offset = Env.find id.id env in pop t1 @@ sw t1 offset sp
        | Dot(obj, field) ->
                    let offset = 
                      try Env.find field.id !field_offsets 
                      with Not_found -> 0 
                    in
                    tr_expr (shift_env env 4) obj 
                    @@ pop t1
                    @@ sw t1 offset(t0)
        | _ -> failwith "Inc error")

  | Dec e ->
    tr_expr env e
    @@ addi t0 t0 (-1)
    @@ push t0
    @@ (match e.edesc with
        | Var id -> let offset = Env.find id.id env in pop t1 @@ sw t1 offset sp
        | Dot(obj, field) ->
                    let offset = 
                      try Env.find field.id !field_offsets 
                      with Not_found -> 0 
                    in
                    tr_expr (shift_env env 4) obj 
                    @@ pop t1
                    @@ sw t1 offset(t0)
        | _ -> failwith "Dec error")
    
  | Block seq -> tr_seq env stack_acc seq


  let rec collect_offsets decls = 
    match decls with
    | [] -> ()
    | Struct s :: rest ->
        let rec add_fields offset fields = 
          match fields with
          | [] -> ()
          | (id, _typ) :: next_fields ->
              field_offsets := Env.add id.id offset !field_offsets;
              add_fields (offset + 4) next_fields
        in
        add_fields 0 s.fields; 
        collect_offsets rest
    | _ :: rest -> collect_offsets rest 


let tr_fun env df =
  let n = List.length df.params in
  let rec add_params env params index =
    match params with
    | [] -> env
    | (id, _)::rest ->
       
        let offset = (n - 1 - index) * 4 in
        let new_env = Env.add id.id offset env in
        add_params new_env rest (index + 1)
  in
  let env_with_params = add_params env df.params 0 in
  
  label df.fname.id
  @@ tr_seq env_with_params 0 df.body
  @@ (if df.fname.id = "main" then li v0 10 @@ syscall else jr ra)

let rec tr_ldecl env  = function
    Fun df::p -> tr_fun env df @@ tr_ldecl env p
  | _ :: p -> tr_ldecl env p
  | [] -> nop

let tr_prog env decls = 
  collect_offsets decls;
  let code = tr_ldecl env decls in
  { text = code ; data = !data_accu }

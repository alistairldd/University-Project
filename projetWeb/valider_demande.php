<?php
$mail = $_POST["mail"];
$id = $_POST["id"];
error_log("Validation request received for ID: $id, Email: $mail");

if ($id === null || $mail === null) {
    die("error");
}
$content = file_get_contents(__DIR__ . '/id.json');
$users = json_decode($content, true);

function randomPassword() { // fonction trouvée sur internet (mais j'aurais pu la coder moi même)
    $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
    $pass = array(); //remember to declare $pass as an array
    $alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
    for ($i = 0; $i < 8; $i++) {
            $n = rand(0, $alphaLength);
            $pass[] = $alphabet[$n];
    }
    return implode($pass); //turn the array into a string
}

$nouvelId = count($users) + 1; // Générer un nouvel ID en fonction du nombre d'utilisateurs existants
$nouvelUser = [
    "id" => $nouvelId,
    "email" => $mail,
    "mdp" => randomPassword(),
    "iddemande" => intval($id)
];

$users[] = $nouvelUser;
file_put_contents(__DIR__ . '/id.json', json_encode($users, JSON_PRETTY_PRINT));

$demandes = json_decode(file_get_contents(__DIR__ . '/demandes.json'), true);



foreach ($demandes as &$demande) {
    if ($demande['id'] == $id) {
        $demande['traite'] = true; // Marquer la demande comme traitée
        break;
    }
}

file_put_contents(__DIR__ . '/demandes.json', json_encode($demandes, JSON_PRETTY_PRINT));

echo json_encode(["success" => true, "password" => $nouvelUser["mdp"]]);
exit;
?>
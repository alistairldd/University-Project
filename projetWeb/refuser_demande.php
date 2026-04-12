<?php

$mail = $_POST["mail"];
$id = $_POST["id"];
error_log("Refusal request received for ID: $id, Email: $mail");

if ($id === null || $mail === null) {
    die("error");
}

$demandes = json_decode(file_get_contents(__DIR__ . '/demandes.json'), true);

foreach ($demandes as &$demande) {
    if ($demande['id'] == $id) {
        $demande['traite'] = true; // Marquer la demande comme traitée
        break;
    }
}

file_put_contents(__DIR__ . '/demandes.json', json_encode($demandes, JSON_PRETTY_PRINT));
?>
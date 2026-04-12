<?php
session_start();

$dateDeb = $_POST['dateDeb'];
$dateFin = $_POST['dateFin'];
$chambres = $_POST['chambre'];
$id = $_POST['id'];

$data = json_decode(file_get_contents(__DIR__ . '/chambre.json'), true);

foreach ($chambres as $pC) {
    foreach ($data as &$chambre) { 
        if ($chambre['nom'] === $pC) {
            
            $nouvelleResa = [
                'debut' => $dateDeb,
                'fin' => $dateFin,
                'idClient' => (int)$id
            ];

            $chambre['occupation'][] = $nouvelleResa;
            error_log("nouvelle réservation : " . json_encode($nouvelleResa));
        }
    }
}

file_put_contents(__DIR__ . '/chambre.json', json_encode($data, JSON_PRETTY_PRINT));

echo json_encode(["success" => true]);

?>
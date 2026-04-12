<?php 
$dateDeb = $_POST['dateDeb'];
$dateFin = $_POST['dateFin'];

if ($dateDeb > $dateFin || $dateDeb < date('Y-m-d') || $dateFin < date('Y-m-d')) {
    echo json_encode(['disponible' => false, 'message' => 'Veuillez sélectionner des dates valides.']);
    exit;
}

$chambres = json_decode(file_get_contents('chambre.json'), true);

$disponible = [];
foreach ($chambres as $chambre) {
    foreach ($chambre['occupation'] as $occ) {
        if ($dateDeb < $occ['fin'] && $dateFin > $occ['debut']) {
            continue 2; // La chambre est occupée, passer à la suivante
        } 
        
    }
    $disponible[] = $chambre["nbPlaces"];
}

$chambredispo = count($disponible);
if ($chambredispo > 0) {
    $places = array_sum($disponible);
    echo json_encode(['disponible' => true, 'message' => 'Il y a ' . $chambredispo . ' capsules disponibles pendant cette période pour un total de ' . $places . ' places.']);
} else {
    echo json_encode(['disponible' => false, 'message' => 'Aucune capsule disponible pendant cette période']);
}
?>
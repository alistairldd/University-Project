<?php
session_start();

// Vérification de sécurité
if (!isset($_SESSION["user"])) {
    echo json_encode(["success" => false, "message" => "Non connecté"]);
    exit();
}

$file = 'activites.json';

$data = json_decode(file_get_contents($file), true);
$action = $_POST['action'] ?? '';

if ($action === 'demander') {

    $nouvelleDemande = [
        "idClient" => $_SESSION["user"]["id"],
        "email" => $_SESSION["user"]["email"],
        "nbParticipants" => $_POST['nbParticipants'],
        "type" => $_POST['type'],
        "date" => $_POST['date'],
        "commentaire" => $_POST['commentaire'] ?? "",
        "statut" => "en_attente"
    ];


    $data['demandes'][] = $nouvelleDemande;

    if (file_put_contents($file, json_encode($data, JSON_PRETTY_PRINT))) {
        echo json_encode(["success" => true, "message" => "Demande enregistrée ! L'administrateur va l'étudier."]);
    } else {
        echo json_encode(["success" => false, "message" => "Erreur d'écriture fichier"]);
    }
    exit();
} else if ($action === 'lister_admin') {
    $dateFiltre = $_POST['date'] ?? '';
    $resultats = [];

    foreach ($data['demandes'] as $demande) {
        if ($demande['statut'] === 'en_attente') {
            // Si pas de filtre OU si la date correspond au filtre
            if ($dateFiltre === '' || $demande['date'] === $dateFiltre) {
                $resultats[] = $demande;
            }
        }
    }

    // Tri par date croissante
    usort($resultats, function($a, $b) {
        return strtotime($a['date']) - strtotime($b['date']);
    });

    echo json_encode([
        "success" => true,
        "donnees" => $resultats
    ]);
    exit();
} else if ($action === 'lister_validees') {
    $dateFiltre = $_POST['date'] ?? '';
    $resultats = [];
    foreach ($data['prevues'] as $prevue) {
        if ($dateFiltre === '' || $prevue['date'] === $dateFiltre) {
            $resultats[] = $prevue;
        }
    }

    // Tri par date croissante
    usort($resultats, function($a, $b) {
        return strtotime($a['date']) - strtotime($b['date']);
    });

    echo json_encode([
        "success" => true,
        "donnees" => $resultats
    ]);
    exit();
} else if ($action === 'details_activite') {
    $id = $_POST['id'];
    $trouve = null;

    foreach ($data['prevues'] as $p) {
        if ($p['id'] === $id) {
            $date = $p['date'];
            $trouve = $p;
            break;
        }
    }

    if ($trouve) {
        $trouve['commentaires'] = [];
        foreach ($data['demandes'] as $d) {
            if (in_array($d['email'], $trouve['participants'])) {
                if ($date === $d['date']) {
                    error_log("Trouvé participant " . $d['email'] . " avec commentaire: " . $d['commentaire']); 
                    $trouve['commentaires'][] = $d['commentaire'];
                }
            }
        }
    }

    if ($trouve) {
        echo json_encode(["success" => true, "donnees" => $trouve]);
    } else {
        echo json_encode(["success" => false]);
    }
    exit();
} else if ($action === 'chercher_partenaires') {
    $type = $_POST['type'];
    $date = $_POST['date'];
    $idExclu = $_POST['idExclu'];
    $trouves = [];

    foreach ($data['demandes'] as $d) {
        if ($d['type'] === $type && $d['date'] === $date && $d['idClient'] != $idExclu && $d['statut'] === 'en_attente') {
            $trouves[] = $d;
        }
    }

    if (!empty($trouves)) {
        echo json_encode(["success" => true, "donnees" => $trouves]);
    } else {
        echo json_encode(["success" => false]);
    }
    exit();
} else if ($action === 'confirmer_activite') {
    $idPart = $_POST['partenairesIds'] ?? [];
    $mailPart = $_POST['partenaires'] ?? [];
    $idsATraiter = array_merge([$_POST['idPrincipal']], $idPart);

    $type = $_POST['type'];
    $date = $_POST['date'];
    $nbTotalParticipants = 0;



    foreach ($data['demandes'] as &$d) {
        foreach ($idsATraiter as $id) {
            if ($d['idClient'] == $id && $d['type'] === $type && $d['date'] === $date) {
                $d['statut'] = 'validee';
                $nbTotalParticipants += $d['nbParticipants'];
            }
        }
    }

        $data['prevues'][] = [
        "id" => uniqid(),
        "type" => $type,
        "date" => $date,
        "animateur" => $_POST['animateur'],
        "participants" => array_merge([$_POST['emailPrincipal']], $mailPart),
        "nbParticipants" => $nbTotalParticipants
    ];

    file_put_contents($file, json_encode($data, JSON_PRETTY_PRINT));
    echo json_encode(["success" => true]);
    exit();
} else if ($action === 'charger_activites_client') {
    $resultats = [];

    foreach ($data['prevues'] as $prevue) {
        foreach ($prevue['participants'] as $p) {
            if ($p === $_SESSION["user"]["email"]) {
                $resultats[] = $prevue;
                break;
            }
        }
    }

    // Tri par date croissante
    usort($resultats, function($a, $b) {
        return strtotime($a['date']) - strtotime($b['date']);
    });

    echo json_encode([
        "success" => true,
        "donnees" => $resultats
    ]);
    exit();
} else {
    echo json_encode(["success" => false, "message" => "Action inconnue"]);
    exit();
}
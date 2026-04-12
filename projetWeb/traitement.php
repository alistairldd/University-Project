<?php 
session_start();

$demandes = json_decode(file_get_contents(__DIR__ . '/demandes.json'), true);

if (isset($_POST["mail"], $_POST["dateDeb"], $_POST["dateFin"], $_POST["nbPersonnes"], $_POST["transport"])) {
    $nouvelleDemande = [
        "id" => count($demandes) + 1,
        "email" => $_POST["mail"],
        "dateDeb" => $_POST["dateDeb"],
        "dateFin" => $_POST["dateFin"], 
        "nbPersonnes" => (int)$_POST["nbPersonnes"],
        "transport" => $_POST["transport"],
        "prestations" => $_POST["prestation"] ?? [],
        "commentaire" => $_POST["commentaire"] ?? "",
        "traite" => false
    ];
    $demandes[] = $nouvelleDemande;

    file_put_contents(__DIR__ . '/demandes.json', json_encode($demandes, JSON_PRETTY_PRINT));

    header('Location: index.php?envoi=success'); exit();

} else if (isset($_POST["nom"], $_POST["mdp"])) {

    $nom = $_POST["nom"];
    $mdp = $_POST["mdp"];

    $ids = json_decode(file_get_contents(__DIR__ . '/id.json'), true);
    error_log("Nom : $nom" . " | Mot de passe : $mdp");
    $correct = false;
    foreach ($ids as $id) {
            if (strtolower($id["email"]) === strtolower($nom) && $id["mdp"] === $mdp) {
                $_SESSION["user"] = $id;
                $isAdmin = isset($id["admin"]) && $id["admin"];
                echo json_encode(['success' => true, 'admin' => $isAdmin]);
                exit();
            }
        }
        echo json_encode(['success' => false, 'message' => 'Identifiants incorrects']);
        exit();
}
?>
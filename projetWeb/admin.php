<?php
session_start();

if (!isset($_SESSION["user"]) || $_SESSION["user"]["admin"] !== true) {
    header('Location: index.php');
    exit();
}


?>

<!-- RAPPORT
theme 
activite 
capture
differentes pages
combien de fichier de données
lien entre les fichiers de données
librairies
comment est organisé le code 
-->

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style.css">
    <title>Admin - Gestion des demandes</title>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="script.js"></script>

</head>
<body>
    <div id ="bgLune">
        <div class="Titre">
            <h1>LA GROSSE LUNE </h1>
        </div>

        <div class = "Deconnect" style =   "position: absolute; top: 25px; right: 10px;">
                
            <button class = "bouton" id="deconnect" type="button" onclick="deconnect()">Déconnexion</button>
                
        </div>

        <div class = "PrincipaleAdmin">

        
            <h4>Bienvenue Patron !</h4>

            <?php
                $demandes = json_decode(file_get_contents("demandes.json"), true);
                $chambres = json_decode(file_get_contents("chambre.json"), true);
                $ids = json_decode(file_get_contents("id.json"), true);
                $activites = json_decode(file_get_contents("activites.json"), true);
                $texte = ""; // Modifié dans le JS
                $animateurs = ["Alice", "Bob", "Charlie", "David", "Eve"];
            ?>

            <div class="admin-wrapper">
                <aside class="sidebar">
                    <button id="tabDemande" class="tab-btn active" onclick="afficherDemande()">Demandes</button>
                    <button id="tabVoyage" class="tab-btn" onclick="afficherVoyage()">Voyages</button>
                    <button id="tabChambre" class="tab-btn" onclick="afficherChambre()">Chambres</button>
                    <button id="tabActivites" class="tab-btn" onclick="afficherActivites()">Activités</button>
                </aside>

                <main class="content-area">
                    <section id="divDemande" class="tab-section">
                        <div id="demandes" class="scrollable">
                            <p>Demandes en attente :</p>
                            <table>
                                <thead>
                                    <tr style="border:none;">
                                        <th>Email</th>
                                        <th>Date début</th>
                                        <th>Date fin</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php foreach ($demandes as $demande):
                                            $traite = $demande['traite'];
                                            if (!$traite): 
                                                $email = $demande['email'];
                                                $id = $demande['id'];
                                        ?>
                                            <tr id="demande-<?= $id ?>" style="border:none;">
                                                <td><?= $email ?></td>
                                                <td><?= $demande['dateDeb'] ?></td>
                                                <td><?= $demande['dateFin'] ?></td>
                                                <td>
                                                    <button id="boutonDetails-<?= $id ?>" onclick="afficherDetailsDemande(<?= $id ?>)">Détails</button>
                                                </td>
                                            </tr>
                                            <tr id="details-row-<?= $id ?>" style="border:none;">
                                                <td colspan="4" style="border:none; padding:8px;">
                                                    <div id="details-<?= $id ?>" class="container-details" style="display:none; margin-top:8px; width:100%;">
                                                        <div id="presta-<?= $id ?>" class="contenu-details">
                                                            <p>Nombre de personnes : <?php echo $demande['nbPersonnes'];?></p>
                                                                

                                                            <p>Prestations demandées :</p>
                                                            <p>
                                                                <?php if (empty($demande['prestations'])){
                                                                    echo "Aucune prestation demandée.";
                                                                } else {
                                                                    foreach ($demande['prestations'] as $presta){
                                                                        echo "- " . $presta . " <br>";
                                                                    }
                                                                } ?>
                                                            </p>
                                                            <p>Infos complémentaires :</p>
                                                            <p><?= $demande['commentaire'] ?></p>
                                                        </div>

                                                        <div class="demande-actions" style="margin-top:8px; display:flex; align-items:center; gap:8px;">
                                                            <div id="retourT-<?= $id ?>" style="display:none; color: green;">&nbsp;&nbsp;ACCEPTEE</div>
                                                            <div id="retourF-<?= $id ?>" style="display:none; color: red;">&nbsp;&nbsp;REFUSEE</div>

                                                            <button id="accepter-<?= $id ?>" onclick="accepterVoyage(<?= $id ?>,'<?= $email ?>')">Accepter</button>
                                                            <button id="refuser-<?= $id ?>" onclick="refuserVoyage(<?= $id ?>,'<?= $email ?>')">Refuser</button>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        <?php endif;
                                    endforeach; ?>
                                </tbody>
                            </table>
                        </div>
                        <div id="messageEnvoi"></div>
                    </section>

                    <section id="divVoyage" class="tab-section" style="display:none;">
                        <div id="voyages" class="scrollable">
                            <?php
                            // Définition de la fonction de traitement (on passe les variables par référence avec &)
                            function traiterVoyages($occ, $isHistorique, $ids, $demandes, $activites, $nomChambre, &$voyages_prevus, &$voyages_historique) {
                                $idDemande = $occ['idClient'] ?? null;
                                if ($idDemande === null) return;

                                $email = "";

                                if (empty($email)) {
                                    foreach ($demandes as $demande) {
                                        if (isset($demande['id']) && $demande['id'] == $idDemande) {
                                            $email = strtolower($demande['email'] ?? '');
                                            break;
                                        }
                                    }
                                }

                                

                                // Trouver les détails dans demandes.json (personnes, transport, prestations)
                                $nbPers = "?";
                                $presta = "Aucune";
                                $transport = "Non spécifié";

                                foreach ($demandes as $dem) {
                                    // On vérifie par id de demande (si on en a un) ou mail
                                    if (($idDemande !== null && $dem['id'] == $idDemande) || strtolower($dem['email'] ?? '') === $email) {
                                        $nbPers = $dem['nbPersonnes'] ?? "?";
                                        $presta = empty($dem['prestations']) ? "Aucune" : implode(", ", $dem['prestations']);
                                        $transport = $dem['transport'] ?? "Non spécifié";
                                        break;
                                    }
                                }

                                
                                // Trouver les activités liées à cet email dans activites.json
                                
                                foreach ($activites['prevues'] as $activite) {
                                    foreach ($activite['participants'] as $inscrit)
                                    if ($inscrit === $email) {
                                        
                                        $acts[] = ($activite['type'] ?? 'Activité') . " (" . ($activite['date'] ?? '??') . ")";
                                    }
                                }
                            
                                
                                $actsFormat = empty($acts) ? "Aucune activité" : "- " . implode("<br>- ", $acts);

                                // Construction de l'objet Voyage pour l'affichage
                                $voyage = [
                                    'email'     => $email,
                                    'debut'     => $occ['debut'] ?? '??',
                                    'fin'       => $occ['fin'] ?? '??',
                                    'chambre'   => $nomChambre,
                                    'nbPers'    => $nbPers,
                                    'transport' => $transport,
                                    'presta'    => $presta,
                                    'acts'      => $actsFormat,
                                    'raison'    => $occ['raison'] ?? 'Terminé',
                                    'uniq'      => uniqid()
                                ];

                                if ($isHistorique) {
                                    $voyages_historique[] = $voyage;
                                } else {
                                    $voyages_prevus[] = $voyage;
                                }
                            }
                            
                            $voyages_prevus = [];
                            $voyages_historique = [];

                            foreach ($chambres as $chambre) {
                                $nomChambre = trim($chambre['nom'] ?? 'Suite Inconnue');

                                // On traite les occupations actuelles
                                if (!empty($chambre['occupation'])) {
                                    foreach ($chambre['occupation'] as $occ) {
                                        traiterVoyages($occ, false, $ids, $demandes, $activites, $nomChambre, $voyages_prevus, $voyages_historique);
                                    }
                                }
                                // On traite l'historique
                                if (!empty($chambre['old_occupation'])) {
                                    foreach ($chambre['old_occupation'] as $occ) {
                                        traiterVoyages($occ, true, $ids, $demandes, $activites, $nomChambre, $voyages_prevus, $voyages_historique);
                                    }
                                }
                            }
                            ?>

                            <div class="voyages-block">
                                <h3>Prochains voyages prévus</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Email client</th>
                                            <th>Date début</th>
                                            <th>Date fin</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <?php if (empty($voyages_prevus)): ?>
                                            <tr><td colspan="4" style="text-align:center; padding:20px;">Aucun voyage prévu dans les chambres.</td></tr>
                                        <?php else: ?>
                                            <?php foreach ($voyages_prevus as $v): ?>
                                                <tr>
                                                    <td><?= $v['email'] ?></td>
                                                    <td><?= $v['debut'] ?></td>
                                                    <td><?= $v['fin'] ?></td>
                                                    <td><button onclick="afficherDetailsVoyage('<?= $v['uniq'] ?>')">Détails</button></td>
                                                </tr>
                                                <tr id="details-row-voyage-<?= $v['uniq'] ?>" style="border:none;">
                                                    <td colspan="4" style="padding:0;">
                                                        <div id="details-voyage-<?= $v['uniq'] ?>" class="container-details" style="display:none; margin: 10px; border-left: 3px solid #4CAF50;">
                                                            <div class="contenu-details">
                                                                <p><strong>Suite :</strong> <?= $v['chambre'] ?></p>
                                                                <p><strong>Pour :</strong> <?= $v['nbPers'] ?> personne(s)</p>
                                                                <p><strong>Transport :</strong> <?= $v['transport'] ?></p>
                                                                <p><strong>Prestations :</strong> <?= $v['presta'] ?></p>
                                                                <p><strong>Activités prévues :</strong><br> <?= $v['acts'] ?></p>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            <?php endforeach; ?>
                                        <?php endif; ?>
                                    </tbody>
                                </table>
                            </div>

                            <div class="voyages-block" style="margin-top:40px;">
                                <h3>Historique des voyages</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Email client</th>
                                            <th>Date début</th>
                                            <th>Date fin</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <?php if (empty($voyages_historique)): ?>
                                            <tr><td colspan="4" style="text-align:center; padding:20px;">Aucun historique de voyage trouvé.</td></tr>
                                        <?php else: ?>
                                            <?php foreach ($voyages_historique as $v): ?>
                                                <tr>
                                                    <td><?= $v['email'] ?></td>
                                                    <td><?= $v['debut'] ?></td>
                                                    <td><?= $v['fin'] ?></td>
                                                    <td><button onclick="afficherDetailsVoyage('<?= $v['uniq'] ?>')">Détails</button></td>
                                                </tr>
                                                <tr id="details-row-voyage-<?= $v['uniq'] ?>" style="border:none;">
                                                    <td colspan="4" style="padding:0;">
                                                        <div id="details-voyage-<?= $v['uniq'] ?>" class="container-details" style="display:none; margin: 10px; border-left: 3px solid #cc0000;">
                                                            <div class="contenu-details">
                                                                <p><strong>Suite :</strong> <?= $v['chambre'] ?></p>
                                                                <p><strong>Statut final :</strong> <?= $v['raison'] ?></p>
                                                                <p><strong>Prestations :</strong> <?= $v['presta'] ?></p>
                                                                <p><strong>Activités effectuées :</strong><br> <?= $v['acts'] ?></p>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            <?php endforeach; ?>
                                        <?php endif; ?>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </section>

                    <section id="divChambre" class="tab-section" style="display:none;">
                        <div id="chambres" class="scrollable">
                            <?php foreach ($chambres as $chambre): 
                                $id = $chambre['id']; 
                            ?>
                                <div id="chambre-<?= $id ?>" style="display:flex; flex-direction:column; padding:20px; border:1px solid white; border-radius:10px; background-color:rgba(87, 86, 86, 0.5); margin-bottom:20px; width:600px;">

                                    <div id="chambreImage-<?= $id ?>">
                                        <img src="/images/chambre<?= $id ?>.jpg" alt="Image de la chambre" style="width:75%; height:auto; border-radius: 10px;">
                                    </div>

                                    <div id="chambreG-<?= $id ?>">
                                        <strong><?= $chambre['nom'] ?></strong> <br>
                                        <?php if (empty($chambre['occupation'])){
                                            echo "Prochaine occupation : Aucune";
                                        } else {
                                            echo "Prochaine occupation : " . $chambre['occupation'][0]['debut'] . " ⭢ " . $chambre['occupation'][0]['fin'];
                                        } ?>
                                    </div>

                                    <div id="chambreD-<?= $id ?>">
                                        <button id="buttonActi-<?= $id ?>" onclick="afficherDetailsChambre(<?= $id ?>)">Voir les détails</button>
                                    </div>

                                    <div id="activites-<?= $id ?>" class="container-details" style="display:none">
                                        <div id="activitesContenu-<?= $id ?>" class="contenu-details">
                                            <p> Superficie : <?= $chambre['superficie'] ?> m²</p>
                                            <p> Capacité : <?= $chambre['nbPlaces'] ?> personnes</p>
                                            <p> Équipements : <?= $chambre['equipements'] ?></p>
                                        </div>
                                    </div>
                                </div>
                            <?php endforeach; ?>
                        </div>
                    </section>

                    <section id="divActivites" class="tab-section" style="display:none;">
                        <div id='activites' class='scrollable' style="width: 100%; padding: 20px; min-height:50vh">
                            
                            <div class ="demandesActivites" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px;">
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <label for="filter-date-acti">Filtrer par date de séance :</label>
                                    <input type="date" id="filter-date-acti" class="input" style="width: auto;" onchange="chargerDemandesActivites()">
                                </div>

                                <button class="bouton" onclick="afficherActivitesValidees()">
                                    Afficher les activites validées
                                </button>
                            </div>

                            <div id="liste-demandes-activites" class = "demandesActivites" >
                                <h3>Liste des demandes d'activités</h3>
                                <hr style="margin: 30px 0;">
                                <div id="tableau-demandes-activites" style="display: flex; ">
                                    <table style="width:100%; border-collapse: collapse;">
                                        <thead>
                                            <tr style="border-bottom: 1px solid white;">
                                            <th style="width: 25%;">Client</th>
                                            <th style="width: 15%;">Nb participants</th>
                                            <th style="width: 20%;">Activité</th>
                                            <th style="width: 15%;">Date</th>
                                            <th style="width: 25%;">Commentaire</th>
                                            <th style="width: 10%;">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="corps-demandes-activites">
                                        </tbody>
                                    </table>
                                </div>
                            </div>


                            <div class = "activitesValidees tab-section" style="display: none; justify-content: space-between; align-items: center; margin-bottom: 20px; background: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px;">
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <label for="filter-date-acti-vali">Filtrer par date de séance :</label>
                                    <input type="date" id="filter-date-acti-vali" class="input" style="width: auto;" onchange="chargerActivitesValidees()">
                                </div>

                                <button class="bouton" onclick="afficherDemandesActivites()">
                                    Afficher les demandes d'activités
                                </button>
                            </div>
                        

                            <div id="planning-activites" class = "activitesValidees" style="display: none;">
                                <h3>Planning des activités validées</h3>
                                <hr style="margin: 30px 0;">
                                <div id="tableau-activites-validees" style="display: flex; ">
                                    <table style="width:100%; border-collapse: collapse;">
                                        <thead>
                                            <tr style="border-bottom: 1px solid white;">
                                            <th style="width: 20%;">Activité</th>
                                            <th style="width: 15%;">Nb participants</th>
                                            <th style="width: 20%;">Animateur</th>
                                            <th style="width: 15%;">Date</th>
                                            <th style="width: 25%;">Action</th>
                                            </tr>
                                        </thead>
                                    <tbody id="corps-activites-validees">
                                    </tbody>
                                    </table>
                            </div>
                            

                        </div>
                    </section>
                </main>
            </div>
        </div>
    </div>

        <div id="zone-validation-activite" style="display:none;">
        <h3>Validation de l'activité</h3>
        <p id="info-activite-selectionnee"></p>
        
        <label>Choisir l'animateur :</label>
        <select id="select-animateur" class="input">
            <?php foreach($animateurs as $a) echo "<option value='$a'>$a</option>";?>
        </select>

        <div id="cas-multi-joueurs" style="display:none;">
            <p id="label-joueurs" style="font-weight: bold;"></p>
            <div id="liste-participants-dispo" style="max-height: 150px; overflow-y: auto;">
            </div>
        </div>

        <div style="margin-top: 20px; display: flex; gap: 10px;">
            <button class="copy-btn" onclick="confirmerValidation()">Confirmer la séance</button>
            <button class="copy-btn" style="background:red;" onclick="$('#zone-validation-activite').fadeOut()">Annuler</button>
        </div>
    </div>
    <div id="details-activite" style = "display:none;">
        <h3>Détails de l'activité</h3>
        <p id="details-activite-contenu"></p>
        <button class="copy-btn" onclick="$('#details-activite').fadeOut()">Fermer</button>
    </div>
</body>
</html>
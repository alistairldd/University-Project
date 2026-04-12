<?php
session_start();

if (!isset($_SESSION["user"]) || (isset($_SESSION["user"]["admin"]) && $_SESSION["user"]["admin"] === true)) {
    header('Location: index.php');
    exit();
}
error_log("Session utilisateur : " . print_r($_SESSION["user"], true)); // Debug : Affiche les données de session
?>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="script.js"></script>

    <?php 
    $user = $_SESSION["user"];
    $demandes = json_decode(file_get_contents(__DIR__ . '/demandes.json'), true);
    foreach($demandes as $demande) {
        if ($demande["id"] === $user["iddemande"]) {
            $infos = $demande;
            break;
        }
    }
    $mois = [
    "01" => "janvier", "02" => "février", "03" => "mars", "04" => "avril", 
    "05" => "mai", "06" => "juin", "07" => "juillet", "08" => "août", 
    "09" => "septembre", "10" => "octobre", "11" => "novembre", "12" => "décembre"
    ];


    $nbJours = (strtotime($infos["dateFin"]) - strtotime($infos["dateDeb"])) / (60 * 60 * 24);

    $prix = [
        
        "Économie" => 100000,
        "Business" => 150000,
        "Première" => 200000,
        "Premium Deluxe" => 400000,
        "Navette" => 1000 * $nbJours,
        "Petit déjeuner" => 500 * $nbJours,
        "Déjeuner" => 1000 * $nbJours,
        "Dîner" => 1000 * $nbJours
    ];

    $tp = $infos["transport"];
 
    $dateExplode = explode("-", $infos["dateDeb"]);
    $dateFrDeb = $dateExplode[2] . " " . $mois[$dateExplode[1]] . " " . $dateExplode[0];
    $dateExplode = explode("-", $infos["dateFin"]);
    $dateFrFin = $dateExplode[2] . " " . $mois[$dateExplode[1]] . " " . $dateExplode[0];

    ?>




    <title>Client</title>
</head>
<body class="bgClient">
    <div class="PrincipaleClient">
        <script>
            $(document).ready(function() {
                <?php 
                        echo "ajouterPanierMultiple(" . json_encode('Classe ' . $tp) . ", " . $prix[$tp] . ", " . $infos["nbPersonnes"] . ");";
                        foreach ($infos["prestations"] as $prestation) {
                            echo "ajouterPanierMultiple(" . json_encode($prestation) . ", " . $prix[$prestation] . ", " . $infos["nbPersonnes"] . ");";
                        }
                    ?>
            });
            

        </script>
        <h1 style= "margin-bottom: 5px;">Bienvenue sur votre espace</h1>

        
        <div class = "hautdroite" style =   "position: absolute; top: 25px; right: 15px;">
            <button id="voirPanier" type="button" style="background: none; border: none; cursor: pointer; color: white;" onclick="voirPanier()">
            <i class="fa-solid fa-cart-shopping fa-xl"></i>
            </button>
                <button id="deconnect" type="button" onclick="deconnect()">Déconnexion</button>
        </div>

        <div class = "Conteneur"> 
            <div id = "achat"> <!-- gauche du conteneur -->
                <h2> Personalisez votre voyage ! </h2>
                <h3> Voyage du <?php echo $dateFrDeb; ?> au <?php echo $dateFrFin . ' (' . $nbJours . ' jours)'; ?> pour <?php echo $infos["nbPersonnes"]; ?> personnes </h3>
                <div id="achattxt">
                    <p> Découvrir les chambres disponibles :
                        <button class = "bouton" id="decouvrirChambre" type="button" onclick="decouvrirChambre()">
                        Découvrir les chambres
                        </button>
                    </p>
                    <p> Location de téléscope privé pour observer les étoiles (5000€/h) :
                        <button class = "bouton" id="don" type="button" onclick="ajouterPanier('Location de téléscope', 5000)">
                        Ajouter au panier
                        </button>
                    <br>
                     Pack Oxygène premium pour un air enrichi aux senteurs terrestres (10 000€/j) :
                        <button class = "bouton" id="don" type="button" onclick="ajouterPanier('Oxygène Premium', 10000)">
                        Ajouter au panier
                        </button>
                    <br>
                    Connexion Wi-Fi "Deep Space" : Très haut débit pour se connecter à la Terre sans latence. (20 000€/j) :
                        <button class = "bouton" id="don" type="button" onclick="ajouterPanier('Connexion Wi-Fi', 20000)">
                        Ajouter au panier
                        </button>
                    </p>
                <div id="section-activites" style="margin-top: 20px; border-top: 1px solid #555; padding-top: 10px;">
                    <h3>Réserver une activité lunaire</h3>

                    <form id="form-activite">
                        <label>Activité :</label>
                        <select id="type-acti" class="input" style="width: auto;">
                            <option value="Tennis Lunaire">Tennis Lunaire </option>
                            <option value="Badminton Lunaire">Badminton Lunaire</option>
                            <option value="Natation Lunaire">Natation Lunaire</option>
                            <option value="Saut en hauteur Lunaire">Saut en hauteur Lunaire</option>
                            <option value="Sortie en Rover">Sortie en Rover</option>
                            <option value="Sortie">Balade hors-base</option>
                        </select>
                        
                        <label>&nbsp; Date :</label>
                        <input type="date" id="date-acti" class="input" style="width: auto;"  
                        min="<?php echo $infos['dateDeb']; ?>" 
                        max="<?php echo $infos['dateFin']; ?>">
                        
                        <label>&nbsp; Nombre de participants :</label>
                        <input type="number" id="nb-participants" class="input" style="width: auto;" min="1" max="<?php echo $infos['nbPersonnes']; ?>">
                        <br>
                        <label>Commentaires (ex: "Débutant") :</label>
                        <textarea id="comm-acti" name="commentaire" rows="2" style="resize: none; margin-top: 10px;"></textarea>
                        <br>
                        
                        <button type="button" class="bouton" onclick="envoyerDemandeActivite()" style="margin-top: 10px;">Demander l'activité</button>
                        <span id="retour-activite" style="margin-left: 10px;"> </span>
                        <button type="button" class="bouton" onclick="chargerActivitesClient()" style="display:flex; margin-left:auto;">Voir mes activités validées</button>
                        
                    </form>
                    
                </div>
            </div>


            </div>

            <div id = "Panier" style="display:none;"> <!-- droite du conteneur -->
                <h2>Votre panier</h2>
                <h4>Prestations : </h4>
                <div id="liste-presta"></div>
                <h4>Articles : </h4>
                <div id="liste-articles"></div>

                <div id="barre-prix">
                    <span>Total de votre séjour :  &nbsp </span>
                    <span id="prix-total"> 0 </span> &nbsp €
                    <span id="payer"><button id="payer" onclick="payer('<?= $infos['dateDeb'] ?>', '<?= $infos['dateFin'] ?>', <?= $infos['id'] ?>)"> Payer </button> </span>
                </div>
            </div>
        </div >
    </div>



    <div id="zone-chambres" style="display: none; ">
    <div id="fond-chambre" class="background-chambres"></div>
    <div class="carousel">

    
        <button class="bouton" id = "bouton-gauche" onclick="changerChambre(-1)">❮</button>
        <div id="affichage-chambre">
            <script>
                // On récupère le nombre de personnes du voyage depuis PHP
                const MAX_PLACES_VOYAGE = <?php echo $infos["nbPersonnes"]; ?>;
                let nbPlacesPris = 0; // Variable globale pour suivre le cumul
            </script>

            <?php 
            $chambres = json_decode(file_get_contents('chambre.json'), true);
            
            foreach ($chambres as $index => $chambre) : 
                $display = ($index === 0) ? 'flex' : 'none';
            ?>                          

                <div class="item-chambre" data-index="<?php echo $index; ?>" style="display: <?php echo $display; ?>;font-size: 25px; ">
                    <div class="background-chambres" 
                        style="background-image: url('images/chambre<?= $index + 1 ?>.jpg');">
                    </div>
                    <h3><?php echo $chambre['nom']; ?></h3>
                    <p><strong>Prix par nuit :</strong> <?php echo $chambre['prix']; ?> €</p>
                    <p><strong>Superficie :</strong> <?php echo $chambre['superficie']; ?> m²</p>
                    <p><strong>Places :</strong> <?php echo $chambre['nbPlaces']; ?></p>
                    <p style=" font-style: italic;">
                        <strong>Équipements :</strong> <?php echo $chambre['equipements']; ?>
                    </p> 



                    
                    <button class="bouton bouton-chambre" id="bouton-reserver" onclick="reserverChambre('<?php echo $chambre['nom']; ?>', <?php echo $chambre['prix'] * $infos['nbPersonnes'] * $nbJours; ?>, <?php echo $chambre['nbPlaces']; ?>)">Réserver cette suite</button>
                </div>
                
            <?php endforeach; ?>

            <div class="points" style="display: flex; justify-content: center; gap: 10px; margin-top: 10px; margin-bottom: 10px;">
                <?php foreach ($chambres as $index => $chambre) : ?>
                    <span class="dot <?php echo ($index === 0) ? 'active' : ''; ?>" 
                        onclick="allerAChambre(<?php echo $index; ?>)">
                    </span>
                <?php endforeach; ?>
            </div>
            
        </div>


        <button class="bouton" id = "bouton-droite" onclick="changerChambre(1)">❯</button>
    </div>
</div>

    <div id="zone-acti-client" style="display: none; ">


        <div id="planning-activites" class = "activitesValidees" style="">
        <h3>Planning des activités validées</h3>
        <hr style="margin: 30px 0;">
        <div id="tableau-activites-validees" style="display: flex; ">
            <table style="width:100%; border-collapse: collapse;">
                <thead>
                    <tr style="border-bottom: 1px solid white;">
                    <th>Activité</th>
                    <th>Nb participants</th>
                    <th>Animateur</th>
                    <th>Date</th>
                    <th>Action</th>
                    </tr>
                </thead>
            <tbody id="corps-activites-client" style="text-align: center;">
            </tbody>
            </table>
        </div>
        <div id="details-activite-client">
            <h3>Détails de l'activité</h3>
            <p id="details-activite-contenu"></p>
            
        </div>
        <button class="copy-btn" onclick="$('#zone-acti-client').fadeOut()">Fermer</button>
    </div>

    
</body>
</html>
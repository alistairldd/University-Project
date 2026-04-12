<!DOCTYPE html>

<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>La grosse lune</title>


        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="script.js"></script>

        <?php $envoi = $_GET["envoi"] ?? null; ?>

    </head>

    <body >
        <?php if ($envoi !== "success") : ?>
        <video autoplay muted width='100%' class='video'>
            <source src='images/fusee.mp4' type='video/mp4'>
            Votre navigateur ne supporte pas la lecture de vidéos HTML5.
        </video>
        <?php endif; ?>
        <div class="Overlay" style="display:none;" id ="bgLune">
            <div class="Titre">
                <h1>LA GROSSE LUNE </h1>
            </div>

            <div class = "Principale" >
                <?php
                    if ($envoi === "success") {
                        echo "
                        <button class= 'bouton' id = 'boutonRetour' type='button' onclick='retourDefaut()'> Retour </button>
                        <p>Vol vers la Lune enregistré ! <br> <br>

                                Votre demande a bien été transmise à nos administrateurs. <br>
                                Un email vous sera envoyé prochainement avec vos accès (url, nom, mot de passe) dès qu'on aura validé votre séjour. <br> <br>

                                Délai de réponse moyen : 24h terrestres. <br> </p>";

                    } else {
                        ?>

                        <button class = "bouton" id="boutonConnexion" type="button" onclick="seConnecter()">Se connecter</button>

                        <div id="formConnexion" style="display:none;">
                            <p> Veuillez entrer vos identifiants pour vous connecter. </p>

                            <div id="messageErreur"></div>

                            <form id="formuConnexion" method="post" action="traitement.php">
                                <label for="nom">Identifiant :</label><br> 
                                <input type="email" id="nom" name="nom" required class = "input"><br>
                                <label for="mdp">Mot de Passe :</label><br>
                                <input type="password" id="mdp" name="mdp" required class = "input"><br>
                                <input class = "bouton" type='submit' id="validerConnexion" value='Connexion'><br>
                            </form>
                            <p> Pas encore de compte ? <br> Faites votre demande de voyage pour en créer un ! </p>
                        </div>  
                        
                        <button class = "bouton" id ="boutonform" type="button" style="display:none;" onclick="demanderVoyage()">Demande de voyage</button>

                        <div id = "demandeVoyage">
                            <p> Bienvenue sur le site de la Grosse Lune ! <br> Completez le formulaire si dessous pour faire votre demande de voyage. </p>
                            <form method="post" action="traitement.php">

                                <label for="mail">Adresse mail :</label>
                                <input type="email" id="mail" name="mail" required class="input"><br>
                                <label for="dateDeb">Date de début : </label>
                                <input type="date" id="dateDeb" name="dateDeb" required class="input"> <br>
                                <label for="dateFin">Date de fin : </label>
                                <input type="date" id="dateFin" name="dateFin" required class="input">
                                <div id="msg-dispo"></div><br>
                                <label for="nbPersonnes">Nombre de personnes : </label>
                                <input type="number" id="nbPersonnes" name="nbPersonnes" min="1" max="10" required class="input"><br><br>
                                <button id = "boutonAPrestations" type="button" onclick="affichePresta()">Voir les prestations</button>
                                <button id = "boutonCPrestations" type="button" onclick="cacherPresta()" style="display:none;">Cacher les prestations</button>

                                <div id = "prestations" style="display:none;">
                                    <span>Prestations disponibles :</span>
                                    <p> Transport :</p>
                                    <input type="radio" id="economie" name="transport" value="Économie" required>
                                    <label for="economie"> Économie (100.000€)</label><br>
                                    <input type="radio" id="business" name="transport" value="Business" required>
                                    <label for="business"> Business (150.000€)</label><br>
                                    <input type="radio" id="premiere" name="transport" value="Première" required>
                                    <label for="premiere"> Première (200.000€)</label><br>
                                    <input type="radio" id="deluxe" name="transport" value="Premium Deluxe" required>
                                    <label for="deluxe"> Premium deluxe (400.000€)</label><br>

                                    <input type="checkbox" id="navette" name="prestation[]" value="Navette">
                                    <label for="navette"> Navette (sur la Lune) (1000€/j)</label><br>
                                    <p> Repas (des restaurants seront aussi disponibles sur place) :</p>

                                    <input type="checkbox" id="petitDej" name="prestation[]" value="Petit déjeuner">
                                    <label for="petitDej"> Petit déjeuner (500€/j)</label><br>
                                    <input type="checkbox" id="dejeuner" name="prestation[]" value="Déjeuner">
                                    <label for="dejeuner"> Déjeuner (1.000€/j)</label><br>
                                    <input type="checkbox" id="diner" name="prestation[]" value="Dîner">
                                    <label for="diner"> Dîner (1.000€/j)</label><br>
                                </div>

                                <div id = "commentaire">
                                    <label for="commentaire">Commentaires : </label> <br>
                                    <textarea id="commentaire" name="commentaire" rows="5" style="resize: none;"></textarea>
                                </div>

                                <input type="submit" value="Demander" id="demander" class="demander-no">
                            </form>
                            <?php } ?>
                        </div>
                <?php 

                ?>
            </div>
        </div>
    </body>
</html>
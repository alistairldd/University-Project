
/*

INDEX DES FONCTIONS :

- deconnect() : Permet de se déconnecter en effaçant les champs de connexion et redirige vers la page d'accueil.
- seConnecter() : Affiche le formulaire de connexion et masque les autres sections.

index.php : 

- pagePrincipale() : Affiche la vidéo d'introduction puis affiche l'overlay principal après un délai.
- demanderVoyage() : Affiche le formulaire de demande de voyage et masque les autres sections.
- retourDefaut() : Réinitialise l'affichage et redirige vers la page d'accueil.
- Ajax pour la vérification des dates : Vérifie la disponibilité des dates sélectionnées pour les voyages.
- Ajax pour la connexion : Gère la soumission du formulaire de connexion et redirige en fonction du rôle de l'utilisateur.
- affichePresta() / cacherPresta() : Affiche ou cache la section des prestations.

admin.php :

- afficherDetailsDemande(idDemande) : Affiche les détails d'une demande spécifiqueée par son ID.
- afficherDetailsVoyage(idVoyage) : Affiche les détails d'un voyage spécifique.
- afficherDetailsChambre(idDemande) : Affiche les détails d'une chambre spécifique liée à une demande.
- Ajax pour accepter une demande de voyage : Accepte une demande de voyage et envoie un message de confirmation à l'utilisateur.
- refuserVoyage(idDemande, emailUser) : Refuse une demande de voyage et met à jour l'affichage en conséquence.
- afficherDemande() / afficherVoyage() / afficherChambre() / afficherActivites() : Affiche les différentes sections du tableau de bord admin.
- afficherActivitesValidees() / afficherDemandesActivites() : Bascule entre l'affichage des activités validées et des demandes d'activités.
- chargerDemandesActivites() : Charge les demandes d'activités pour l'admin avec un filtre de date.
- Ajax pour charger les activitées validées : Charge les activités validées pour l'admin avec un filtre de date.
- Ajax pour afficher les demandes d'activités : Charge les demandes d'activités en fonction de la date sélectionnée.
- Ajax pour afficher les détails d'une activité : Affiche les détails d'une activité spécifique.
- ouvrirValidationActivite(id, type, email, date) : Ouvre la zone de validation pour une demande d'activité spécifique.
- Ajax pour charger les participants disponibles : Charge les participants disponibles pour une activité à une date donnée en excluant une demande spécifique.
- Ajax pour confirmer la validation d'une activité : Confirme la validation d'une activité en envoyant les données nécessaires via AJAX.

client.php :
- chargerActivitesClient() : Charge les activités validées pour le client et les affiche dans un tableau.
- voirPanier() : Affiche ou cache le panier.
- ajouterPanier(nom, prix) / ajouterPanierMultiple(nom, prix, quantite) : Ajoute une prestation au panier en gérant les quantités et le prix total.
- ajouterPanierMultiple(nom, prix, quantite) : Ajoute une prestation au panier en gérant les quantités et le prix total.
- decouvrirChambre() : Affiche la section des chambres et scroll jusqu'à elle.
- changerChambre(direction) : Permet de naviguer entre les différentes chambres disponibles.
- allerAChambre(n) : Permet d'aller directement à une chambre spécifique.
- mettreAJourAffichage() : Met à jour l'affichage des chambres et des indicateurs de navigation.
- reserverChambre(nom, prix, places) : Réserve une chambre en ajoutant la prestation au panier et en gérant les capacités.
- Ajax pour envoyer une demande d'activité : Envoie une demande d'activité via AJAX et affiche le résultat.


*/

function deconnect() {
    $('#nom').val('');
    $('#mdp').val('');
    $('#deconnect').hide();
    window.location.href = "index.php";
}

function seConnecter() {
    $('#formConnexion').show();
    $('#demandeVoyage').hide();
    $('#boutonform').show();
    $('#boutonConnexion').hide();
}

// --- INDEX.PHP ---

function pagePrincipale() {
    $('.video').fadeOut();
    setTimeout
        (function () {
            $('.Overlay').fadeIn();
        }, 500);
}

function demanderVoyage() {
    $(`#demandeVoyage`).show();
    $(`#boutonform`).hide();
    $('#formConnexion').hide();
    $('#boutonConnexion').show();
}

function retourDefaut() {
    $('boutonRetour').hide();
    $('#envoi').val('');
    window.location.href = "index.php";
}

$(document).ready(function () {

    // On récupère les paramètres de l'URL (ex: index.php?envoi=success)
    const urlParams = new URLSearchParams(window.location.search);
    const envoiStatus = urlParams.get('envoi');

    if (envoiStatus !== 'success') {
        setTimeout(pagePrincipale, 5000);
    } else { $('.Overlay').show(); }


    /* Ajax pour la vérification des dates */
    $('#dateDeb, #dateFin').on('change', function () {
        var dateDeb = $('#dateDeb').val();
        var dateFin = $('#dateFin').val();

        $.ajax({
            url: 'check_dates.php',
            method: 'POST',
            data: {
                dateDeb: dateDeb,
                dateFin: dateFin
            },
            dataType: 'json'
        })
            .done(function (response) {
                $('#msg-dispo').removeClass('dispo-ok dispo-no');
                if (response.disponible) {
                    $('#msg-dispo').text(response.message).addClass('dispo-ok').fadeIn();
                    $('#demander').prop('disabled', false);
                } else {
                    $('#msg-dispo').text(response.message).addClass('dispo-no').fadeIn();
                    $('#demander').prop('disabled', true);
                }
            });

    });

    $('#adminDateDeb, #adminDateFin, #verifierDispoAdmin').on('change click', function () {
        var dateDeb = $('#adminDateDeb').val();
        var dateFin = $('#adminDateFin').val();

        if (!dateDeb || !dateFin) {
            $('#msg-dispo-admin').removeClass('dispo-ok dispo-no').hide();
            return;
        }

        $.ajax({
            url: 'check_dates.php',
            method: 'POST',
            data: {
                dateDeb: dateDeb,
                dateFin: dateFin
            },
            dataType: 'json'
        })
            .done(function (response) {
                $('#msg-dispo-admin').removeClass('dispo-ok dispo-no');
                if (response.disponible) {
                    $('#msg-dispo-admin').text(response.message).addClass('dispo-ok').fadeIn();
                    $('#demander').prop('disabled', false);
                } else {
                    $('#msg-dispo-admin').text(response.message).addClass('dispo-no').fadeIn();
                    $('#demander').prop('disabled', true);
                }
            });

    });

    /* Ajax pour la connexion */

    $('#formuConnexion').on('submit', function (e) {
        e.preventDefault();
        var donnees = {
            nom: $('#nom').val(),
            mdp: $('#mdp').val()
        };

        $.ajax({
            url: 'traitement.php',
            method: 'POST',
            data: donnees,
            dataType: 'json'
        })
            .done(function (reponse) {
                if (reponse.success) {
                    if (reponse.admin) window.location.href = 'admin.php';
                    else window.location.href = 'client.php';
                } else {
                    $('#messageErreur').text(reponse.message);
                    $('#messageErreur').fadeIn();
                }
            });
    });


    /* pour le formulaire de demande de Voyage */
    $('#mail, #dateDeb, #dateFin, #nbPersonnes, #economie, #business, #premiere, #deluxe').on('change', function () {
        var donnees = {
            mail: $('#mail').val(),
            dateDeb: $('#dateDeb').val(),
            dateFin: $('#dateFin').val(),
            nbPersonnes: $('#nbPersonnes').val(),
            prestation: $('#economie').is(':checked') || $('#business').is(':checked') || $('#premiere').is(':checked') || $('#deluxe').is(':checked')
        };

        if (donnees.mail && donnees.dateDeb && donnees.dateFin && donnees.nbPersonnes && donnees.prestation) {
            $('#demander').addClass('demander-ok');
            $('#demander').removeClass('demander-no');
        } else {
            $('#demander').addClass('demander-no');
            $('#demander').removeClass('demander-ok');
        }
    });

});

function affichePresta() {
    $('#prestations').fadeIn();
    $('#boutonAPrestations').hide();
    $('#boutonCPrestations').show();
}

function cacherPresta() {
    $('#prestations').hide();
    $('#boutonCPrestations').hide();
    $('#boutonAPrestations').show();
}

// --- ADMIN.PHP ---

const CONFIG_ACTIVITES = {
    "Tennis Lunaire": { min: 2 },
    "Badminton Lunaire": { min: 2 },
    "Natation Lunaire": { min: 3 },
    "Saut en hauteur Lunaire": { min: 3 },
    "Sortie en Rover": { min: 6 },
    "Sortie": { min: 6 },
};

function afficherDetailsDemande(idDemande) {
    $('#details-' + idDemande).slideToggle(180);
}

function afficherDetailsVoyage(idVoyage) {
    $('#details-voyage-' + idVoyage).slideToggle(180);
}

function afficherDetailsChambre(idDemande) {
    $('#activites-' + idDemande).slideToggle(180);
}

function accepterVoyage(idDemande, emailUser) {
    console.log(idDemande);
    $.ajax({
        url: 'valider_demande.php',
        method: 'POST',
        dataType: 'json',
        data: {
            id: idDemande,
            mail: emailUser
        },
        success: function (reponse) {
            if (reponse && reponse.success) {
                $('#accepter-' + idDemande).hide();
                $('#refuser-' + idDemande).hide();
                $('#retourT-' + idDemande).fadeIn();

                var ligne1 = "Bienvenue : " + emailUser + " ! <br><br>";
                var ligne2 = "Votre demande pour un voyage sur la lune a été acceptée.<br> Voici votre mot de passe : <span class='message-password'>" + reponse.password + "</span><br>";
                var ligne3 = "Vous pouvez dès à présent choisir vos activités en vous connectant.<br> Merci pour votre confiance, à bientôt !<br><br> - MA";
                var texte = "<div class='message-content'>" + ligne1 + ligne2 + ligne3 + "</div>";

                var actions = "<div style='margin-top:12px; display:flex; justify-content:space-between; align-items:center; gap:12px;'><button id='hideMessageBtn' class='copy-btn'>Masquer le message</button><button id='copyMessageBtn' class='copy-btn'>Copier le message</button></div>";

                $('#messageEnvoi').html(texte + actions).fadeIn(300);

                $('#hideMessageBtn').on('click', function () {
                    $('#messageEnvoi').fadeOut(200, function () {
                        $(this).empty();
                    });
                });

                $('#copyMessageBtn').on('click', function () {
                    var textToCopy = $('#messageEnvoi .message-content').text();
                    if (!textToCopy) {
                        alert('Aucun texte à copier.');
                        return;
                    }
                    navigator.clipboard.writeText(textToCopy)
                    alert('Message copié dans le presse-papiers.');
                });
            }
            else {
                alert("Une erreur est survenue lors de l'acceptation du voyage pour " + emailUser + ".");
            }
        },

        error: function () {
            alert("Une erreur est survenue lors de l'acceptation du voyage pour " + emailUser + ".");
        }
    })
}

function refuserVoyage(idDemande, emailUser) {
    console.log(idDemande);
    $.ajax({
        url: 'refuser_demande.php',
        method: 'POST',
        data: {
            id: idDemande,
            mail: emailUser
        },
        success: function (reponse) {
            $('#accepter-' + idDemande).hide();
            $('#refuser-' + idDemande).hide();
            $('#retourF-' + idDemande).fadeIn();

        },

        error: function () {
            alert("Une erreur est survenue lors du refus du voyage pour " + emailUser + ".");
        }
    })
}


function afficherDemande() {
    // bascule visuelle onglet
    $('.tab-btn').removeClass('active');
    $('#tabDemande').addClass('active');

    // afficher la section correspondante
    $('#divDemande').show();
    $('#divVoyage').hide();
    $('#divChambre').hide();
    $('#divActivites').hide();
}

function afficherVoyage() {
    $('.tab-btn').removeClass('active');
    $('#tabVoyage').addClass('active');

    $('#divDemande').hide();
    $('#divVoyage').show();
    $('#divChambre').hide();
    $('#divActivites').hide();
}

function afficherChambre() {
    $('.tab-btn').removeClass('active');
    $('#tabChambre').addClass('active');

    $('#divDemande').hide();
    $('#divVoyage').hide();
    $('#divChambre').show();
    $('#divActivites').hide();
}

function afficherActivites() {
    $('.tab-btn').removeClass('active');
    $('#tabActivites').addClass('active');

    $('#divDemande').hide();
    $('#divVoyage').hide();
    $('#divChambre').hide();
    $('#divActivites').show();
    chargerDemandesActivites();
    chargerActivitesValidees();
}

function afficherActivitesValidees() {
    $('.demandesActivites').hide();
    $('.activitesValidees').show();
    chargerActivitesValidees();
}

function afficherDemandesActivites() {
    $('.activitesValidees').hide();
    $('.demandesActivites').show();
}


function chargerDemandesActivites() {
    const dateFiltre = $('#filter-date-acti').val();
    const conteneur = $('#corps-demandes-activites');

    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'lister_admin',
            date: dateFiltre
        },
        success: function (reponse) {
            if (reponse.success) {
                if (reponse.donnees.length === 0) {
                    conteneur.html("");
                    return;
                }

                let html = ""

                reponse.donnees.forEach(function (demande) {
                    html += `
                        <tr style="border-bottom: 1px solid #444;">
                            <td style="padding: 10px;">${demande.email}</td>
                            <td>${demande.nbParticipants}</td>
                            <td>${demande.type}</td>
                            <td>${demande.date}</td>
                            <td>${demande.commentaire}</td>
                            <td>
                                <button class="copy-btn" onclick="ouvrirValidationActivite('${demande.idClient}', '${demande.type}', '${demande.email}', '${demande.date}', '${demande.nbParticipants}')">
                                    Valider
                                </button>
                            </td>
                        </tr>`;
                });

                conteneur.html(html);
            }
        },
    });
}



let activiteEnCours = null;

function ouvrirValidationActivite(id, type, email, date, nbParticipants) {
    activiteEnCours = { id: id, type: type, email: email, date: date };
    const config = CONFIG_ACTIVITES[type];

    $('#info-activite-selectionnee').html("<b>" + type + "</b> pour <b>" + email + "</b>");

    $('#label-joueurs').text(`Sélectionner les ${Math.max(0,config.min - nbParticipants)} autres participants :`);

    if (config.min > 1) {
        $('#cas-multi-joueurs').show();
        chargerParticipantsDisponibles(type, activiteEnCours.date, id);
    }

    $('#zone-validation-activite').fadeIn();
}

function chargerActivitesValidees() {
    const conteneur = $('#corps-activites-validees');
    const dateFiltre = $('#filter-date-acti-vali').val();

    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'lister_validees',
            date: dateFiltre
        },
        success: function (reponse) {
            if (reponse.success) {
                if (reponse.donnees.length === 0) {
                    conteneur.html("");
                    return;
                }

                let html = ""

                reponse.donnees.forEach(function (activite) {
                    html += `
                        <tr style="border-bottom: 1px solid #444;">
                            <td>${activite.type}</td>
                            <td>${activite.nbParticipants}</td>
                            <td>${activite.animateur}</td>
                            <td>${activite.date}</td>
                            <td><button class="btn btn-info" onclick="afficherDetailsActivite('${activite.id}')">Details</button></td>
                        </tr>`;
                });

                conteneur.html(html);
            }
        },
    });
}

function afficherDetailsActivite(idActivite) {
    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'details_activite',
            id: idActivite
        },
        success: function (reponse) {
            if (reponse.success) {
                const activite = reponse.donnees;
                console.log(activite);
                let participantTxt = "";
                $('#details-activite-contenu').text("");
                for (let i = 0; i < activite.participants.length; i++) {
                    participantTxt += `<li>${activite.participants[i]} (Note: ${activite.commentaires[i] })</li>`;
                }

                $('#details-activite-contenu').html(participantTxt);
                $('#details-activite').fadeIn();
            }
        },
    });
}

function chargerParticipantsDisponibles(type, date, idExclu) {
    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: { action: 'chercher_partenaires', type: type, date: date, idExclu: idExclu },
        success: function (reponse) {
            if (reponse.success && reponse.donnees.length > 0) {
                $('#liste-participants-dispo').text("");
                reponse.donnees.forEach(p => {

                    $('#liste-participants-dispo').append(`
                        <div style="margin: 5px 0;">
                            <input type="checkbox" class="check-partenaire" value="${p.email}" data-id="${p.idClient}"> 
                            ${p.email} pour ${p.nbParticipants} personnes (Note: ${p.commentaire})
                        </div>
                    `);
                });
            } else {
                $('#liste-participants-dispo').html("<p style='font-size:0.8em; color:gray;'>Aucun autre client n'a réservé cette activité à cette date.</p>");
            }
        }
    });
}

function confirmerValidation() {
    let partenaires = []; // mails
    let partenairesIds = []; // ids
    let PartenairesCoches = $('.check-partenaire:checked');


    for (let i = 0; i < PartenairesCoches.length; i++) {
        let element = $(PartenairesCoches[i]);
        partenaires.push(element.val());
        partenairesIds.push(element.data('id'));
    }

    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'confirmer_activite',
            idPrincipal: activiteEnCours.id,
            emailPrincipal: activiteEnCours.email,
            type: activiteEnCours.type,
            date: activiteEnCours.date,
            animateur: $('#select-animateur').val(),
            partenaires: partenaires,
            partenairesIds: partenairesIds
        },
        success: function (reponse) {
            if (reponse.success) {
                $('#zone-validation-activite').fadeOut();
                chargerDemandesActivites();
                alert("Séance programmée avec succès !");
                activiteEnCours = null;
            }
        }
    });
}

// --- CLIENT.PHP ---

function chargerActivitesClient(){
    const conteneur = $('#corps-activites-client');
    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'charger_activites_client',
        },
        success: function (reponse) {
            if (reponse.success) {
                if (reponse.donnees.length === 0) {
                    $('#details-activite-contenu').html("<p style='font-size:0.8em; color:gray;'>Vous n'avez aucune activité validée pour le moment.</p>");
                    $('#zone-acti-client').fadeIn();
                    return;
                }
                
                let html = ""
                reponse.donnees.forEach(function (activite) {
                    html += `
                        <tr style="border-bottom: 1px solid #444;">
                            <td>${activite.type}</td>
                            <td>${activite.nbParticipants}</td>
                            <td>${activite.animateur}</td>
                            <td>${activite.date}</td>
                            <td><button class="btn btn-info" onclick="afficherDetailsActivite('${activite.id}')">Details</button></td>
                        </tr>`;
                });

                conteneur.html(html);
                $('#zone-acti-client').fadeIn();

                }
            }
        }
    );
}

function voirPanier() {
    $('#Panier').fadeToggle(300);
}

// Variable globale pour garder le prix total
let totalGlobal = 0;

function ajouterPanier(nom, prix) {
    $('#Panier').fadeIn(300);
    let pastrouve = true;

    $('.article-panier').each(function () {
        let $chi = $(this).find(".na");
        let $mul = $(this).find(".mul");
        let $cumul = $(this).find(".cumul");
        let mul = parseInt($mul.text().substring(1));
        if (Number.isNaN(mul)) mul = 1;
        if ($chi.html() == nom + " :&nbsp;") {
            pastrouve = false;
            $mul.text("x" + (mul + 1));
            $cumul.text(((mul + 1) * prix).toLocaleString('fr-FR') + "€");
        }
    });

    if (pastrouve) {
        let ligne = `
            <div class="article-panier" style="display:none;">
                <span class="na">${nom} :&nbsp;</span>

                <span>${prix.toLocaleString('fr-FR')}€ &nbsp</span>

                <span class="mul">  </span>

                <span class="cumul">${prix.toLocaleString('fr-FR')}€ </span>
            </div>
        `;

        $('#liste-articles').append(ligne);

        $('#liste-articles .article-panier:last').slideDown(400);
    }
    totalGlobal += parseFloat(prix);

    $('#prix-total').text(totalGlobal.toLocaleString('fr-FR'));


}

pChambres = []

function payer( dateDeb, dateFin, id){
    console.log("Paiement demandé pour les chambres : " + dateDeb + " " + dateFin);
    $.ajax({
        url: 'payer.php',
        method: 'POST',
        dataType: 'json',
        data: {
            dateDeb: dateDeb,
            dateFin: dateFin,
            chambre: pChambres,
            id: id,
        },
        success: function (reponse) {
            if (reponse.success) {
                alert("Paiement effectué avec succès !");
                pChambres = [];
            }
        },
    });
}

function ajouterPanierMultiple(nom, prix, quantite) {
    $('#Panier').fadeIn(300);
    let pastrouve = true;

    $('.article-panier').each(function () {
        let $chi = $(this).find(".na");
        let $mul = $(this).find(".mul");
        let $cumul = $(this).find(".cumul");
        let mul = parseInt($mul.text().substring(1));
        if (Number.isNaN(mul)) mul = 1;
        if ($chi.html() == nom + " :&nbsp;") {
            pastrouve = false;
            $mul.text("x" + (mul + quantite));
            $cumul.text(((mul + quantite) * prix) + "€");
        }
    });

    if (pastrouve) {
        let ligne = `
            <div class="article-panier" style="display:none;">
                <span class="na">${nom} :&nbsp;</span>

                <span>${prix.toLocaleString('fr-FR')}€ &nbsp</span>

                <span class="mul">x${quantite}</span>

                <span class="cumul">${(prix * quantite).toLocaleString('fr-FR')}€ </span>
            </div>
        `;

        $('#liste-presta').append(ligne);

        $('#liste-presta .article-panier:last').slideDown(400);
    }
    totalGlobal += parseFloat(prix) * quantite;

    $('#prix-total').text(totalGlobal.toLocaleString('fr-FR'));
}



let indexActuel = 0;

function decouvrirChambre() {
    $('#zone-chambres').fadeIn(600);
    $('html, body').animate({
        scrollTop: $("#zone-chambres").offset().top
    }, 800);
    mettreAJourFondChambre(0);
}

function changerChambre(direction) {
    const total = $('.item-chambre').length;

    indexActuel += direction;
    if (indexActuel >= total) indexActuel = 0;
    if (indexActuel < 0) indexActuel = total - 1;

    mettreAJourAffichage();
}

function allerAChambre(n) {
    const chambres = $('.item-chambre');

    $(chambres[indexActuel]).hide();

    indexActuel = n;

    mettreAJourAffichage();
}

function mettreAJourFondChambre(index) {
    let zoneFond = $('#fond-chambre');
    let nouvelleImage = "images/chambre" + (index + 1) + ".jpg";


    zoneFond.css('background-image', 'url(' + nouvelleImage + ')');
    zoneFond.addClass('background-visible');
}

function mettreAJourAffichage() {
    const chambres = $('.item-chambre');
    const dots = $('.dot');

    chambres.hide();
    $(chambres[indexActuel]).fadeIn(400);

    dots.removeClass('active');
    $(dots[indexActuel]).addClass('active');

    mettreAJourFondChambre(indexActuel);
}

function reserverChambre(nom, prix, places) {
    pChambres.push(nom);
    ajouterPanierMultiple(nom, prix, 1);
    nbPlacesPris += parseInt(places);
    const boutons = $('.bouton-chambre');
    $(boutons[indexActuel]).prop('disabled', true);
    if (nbPlacesPris >= MAX_PLACES_VOYAGE) {
        $(boutons).prop('disabled', true).css('opacity', '0.5').text('Capacité atteinte');
    }
    $('html, body').animate({
        scrollTop: 0
    }, 800);

}

function envoyerDemandeActivite() {

    const typeActi = $('#type-acti').val();
    const dateActi = $('#date-acti').val();
    const commActi = $('#comm-acti').val();
    const nbParticipants = $('#nb-participants').val();
    const retour = $('#retour-activite');
    if (!dateActi) {
        retour.css('color', 'red').text("Veuillez choisir une date.");
        return;
    }
    if (!nbParticipants || nbParticipants < 1) {
        retour.css('color', 'red').text("Veuillez indiquer un nombre de participants valide.");
        return;
    }

    $.ajax({
        url: 'gestion_activites.php',
        method: 'POST',
        dataType: 'json',
        data: {
            action: 'demander',
            type: typeActi,
            date: dateActi,
            nbParticipants: nbParticipants,
            commentaire: commActi
        },
        beforeSend: function () {
            retour.css('color', 'white').text("Envoi de la demande vers la station orbitale...");
        },
        success: function (reponse) {
            if (reponse.success) {
                retour.css('color', 'lightgreen').text(reponse.message);

            } else {
                retour.css('color', 'red').text("Erreur : " + reponse.message);
            }
        },
    });
}




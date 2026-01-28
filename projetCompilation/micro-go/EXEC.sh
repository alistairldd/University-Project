#!/bin/bash

COMPILATEUR=("spim" "-f")

REPERTOIRE_TESTS="tests"

echo "--- Démarrage de l'exécution de tous les fichiers ---"

if [ ! -d "$REPERTOIRE_TESTS" ]; then
    echo "Erreur : le dossier '$REPERTOIRE_TESTS' n'existe pas."
    exit 1
fi

shopt -s nullglob   # pr pas que *.s s'étende si y'a aucun fichier
FICHIERS=("$REPERTOIRE_TESTS"/*.s)

if [ ${#FICHIERS[@]} -eq 0 ]; then
    echo "Aucun fichier .s trouvé dans '$REPERTOIRE_TESTS'."
    exit 0
fi

for FICHIER_SOURCE in "${FICHIERS[@]}"
do
    echo -e "\n\n=== Exécution de : $FICHIER_SOURCE === \n"

    "${COMPILATEUR[@]}" "$FICHIER_SOURCE"
    STATUT=$?

    if [ $STATUT -eq 0 ]; then
        echo "SUCCESS : $FICHIER_SOURCE exécuté avec succès (Code 0)."
    else
        echo "FAILURE : $FICHIER_SOURCE a échoué (Code $STATUT)."
    fi
done

echo -e "\n--- Fin de l'exécution ---"

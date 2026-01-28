#!/bin/bash

COMPILATEUR="./mgoc.exe"

REPERTOIRE_TESTS="tests"

echo "--- Démarrage de la compilation de tous les fichiers ---"


for FICHIER_SOURCE in "$REPERTOIRE_TESTS"/*.go
do
    if [ -e "$FICHIER_SOURCE" ]; then
        
        echo -e "\n\n=== Compilation de : $FICHIER_SOURCE ==="

        "$COMPILATEUR" "$FICHIER_SOURCE"
        STATUT=$?
        
        if [ $STATUT -eq 0 ]; then
            echo "SUCCESS: $FICHIER_SOURCE a été compilé avec succès (Code 0)."
        else
            echo "FAILURE: $FICHIER_SOURCE a échoué la compilation (Code $STATUT)."
        fi
        
    fi
done

echo -e "\n--- Fin de la compilation ---"
echo -e "\nPour executer les fichiers .s de $REPERTOIRE_TESTS : (individuel :) spim -f tests/nom_du_fichier.s ;; (tous ensemble :) ./EXEC.sh\n" 
echo -e "Pour supprimer les fichiers .s de $REPERTOIRE_TESTS : ./CLEAN.SH \n"

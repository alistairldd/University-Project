#!/bin/bash

TARGET_DIR="tests"

if [ ! -d "$TARGET_DIR" ]; then
  echo "Erreur : le dossier '$TARGET_DIR' n'existe pas."
  exit 1
fi

FILES=$(find "$TARGET_DIR" -type f -name "*.s")

if [ -z "$FILES" ]; then
  echo "Aucun fichier .s trouvé dans '$TARGET_DIR'."
  exit 0
fi

echo "Les fichiers suivants vont être supprimés :"
echo "$FILES"
echo
read -p "Voulez-vous vraiment les supprimer ? (Y/N) " confirm

if [[ "$confirm" == "y" || "$confirm" == "Y" ]]; then
  find "$TARGET_DIR" -type f -name "*.s" -delete
  echo "Tous les fichiers .s ont été supprimés."
else
  echo "Opération annulée."
fi

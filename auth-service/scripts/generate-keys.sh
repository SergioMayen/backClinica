#!/usr/bin/env sh
set -eu
KEY_DIR="${1:-keys}"
mkdir -p "$KEY_DIR"
umask 077
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:3072 -out "$KEY_DIR/private.pem"
openssl pkey -in "$KEY_DIR/private.pem" -pubout -out "$KEY_DIR/public.pem"
chmod 600 "$KEY_DIR/private.pem"
chmod 644 "$KEY_DIR/public.pem"
printf 'RSA keys generated in %s\n' "$KEY_DIR"

#!/usr/bin/env bash
set -eu

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
rm -rf "$ROOT/target"
echo "VERIFICADO: artefactos Maven eliminados; la base de datos no fue modificada."
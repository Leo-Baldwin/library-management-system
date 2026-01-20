#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$ROOT_DIR/out"

mkdir -p "$OUT_DIR"

find "$ROOT_DIR/src" -name "*.java" > "$OUT_DIR/sources.txt"

javac -d "$OUT_DIR" @"$OUT_DIR/sources.txt"
java -cp "$OUT_DIR" app.App

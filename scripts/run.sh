#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$ROOT_DIR/out"

mkdir -p "$OUT_DIR"

find "$ROOT_DIR/src" -name "*.java" > "$OUT_DIR/sources.txt"

GSON_JAR="$ROOT_DIR/lib/gson-2.10.1.jar"
CLASSPATH="$OUT_DIR:$GSON_JAR"

javac -cp "$GSON_JAR" -d "$OUT_DIR" @"$OUT_DIR/sources.txt"

if [[ -d "$ROOT_DIR/src/resources" ]]; then
  mkdir -p "$OUT_DIR/resources"
  cp -R "$ROOT_DIR/src/resources/"* "$OUT_DIR/resources/"
fi

MAIN_CLASS="app.App"
if [[ "${1:-}" == "web" ]]; then
  MAIN_CLASS="app.WebServer"
fi

java -cp "$CLASSPATH" "$MAIN_CLASS"

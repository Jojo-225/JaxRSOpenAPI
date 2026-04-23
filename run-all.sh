#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "[1/4] Preparation des dependances..."
if [[ ! -f "target/dependency/hsqldb-2.7.2.jar" ]]; then
  mvn dependency:copy-dependencies
fi

echo "[2/4] Demarrage du serveur HSQLDB..."
mkdir -p data
(
  cd data
  java -cp ../target/dependency/hsqldb-2.7.2.jar org.hsqldb.Server
) > /tmp/hsqldb-server.log 2>&1 &
HSQLDB_PID=$!
echo "HSQLDB PID: $HSQLDB_PID (logs: /tmp/hsqldb-server.log)"

sleep 2

echo "[3/4] Ouverture du client HSQLDB..."
java -cp ./target/dependency/hsqldb-2.7.2.jar \
  org.hsqldb.util.DatabaseManagerSwing \
  --driver org.hsqldb.jdbcDriver \
  --url jdbc:hsqldb:hsql://localhost/ \
  --user SA > /tmp/hsqldb-browser.log 2>&1 &
echo "HSQLDB Browser lance (logs: /tmp/hsqldb-browser.log)"

echo "[4/4] Demarrage de l'API (Jetty)..."
mvn jetty:run


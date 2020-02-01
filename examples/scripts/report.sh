#!/usr/bin/env bash

set -e -o pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"/..

PROJECT_ROOT_DIR=$(pwd -P)
REPORT_NAME=${1:-"report-$(date +"%Y-%m-%d-%H-%M-%S")"}
REPORT_PATH="$PROJECT_ROOT_DIR/.reports/$REPORT_NAME.html"

set -x
clj -A:shadow-cljs run shadow.cljs.build-report app "$REPORT_PATH"

exec open "$REPORT_PATH"

#!/usr/bin/env bash

set -e -o pipefail

rm -rf public/.compiled

exec clj -A:shadow-cljs "${@}" release app

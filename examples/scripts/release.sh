#!/usr/bin/env bash

set -e -o pipefail

rm -rf public/.compiled

set -x
exec clj "${@}" release app

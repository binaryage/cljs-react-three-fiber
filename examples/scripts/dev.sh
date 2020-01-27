#!/usr/bin/env bash

set -e -o pipefail

set -x
exec clj -A:shadow-cljs "${@}" watch app

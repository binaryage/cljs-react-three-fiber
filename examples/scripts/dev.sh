#!/usr/bin/env bash

set -e -o pipefail

exec clj -A:shadow-cljs "${@}" watch app

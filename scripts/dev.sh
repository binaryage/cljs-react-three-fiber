#!/usr/bin/env bash

set -e -o pipefail

set -x
# shellcheck disable=SC2086
exec clj -A:shadow-cljs $SHADOW_EXTRA_ARGS "${@}" watch app

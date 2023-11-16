#! /usr/bin/env bash

set -e

function show_help() {
  echo "$0 <new version>"
  exit
}

if [[ $# -lt 1 ]]; then
  show_help
fi

current_dir="$(pwd)"
properties_file="$current_dir/gradle.properties"
lib_file="$current_dir/gradle/libs.versions.toml"
version="$1"

current_version=$(awk -F "=" '/^kradle.version/{print $NF}' gradle.properties)

PLATFORM="$(uname -s | tr 'A-Z' 'a-z')"

function sedi() {
  if [[ "${PLATFORM}" == "darwin" ]]; then
    sed -i '' "$@"
  else
    sed -i "$@"
  fi
}

echo "$current_version -> $version"

# Updates the new version
sedi -e "s/kradle.version=$current_version/kradle.version=$version/g" "$properties_file"

# Updates the kotlin dependency to the previous (current) version
sedi -e "s/^kradle = .*/kradle = \"$current_version\"/g" "$lib_file"

doctoc --github CHANGELOG.md

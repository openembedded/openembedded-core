#!/bin/sh

# Prepare the build system within the extensible SDK

target_sdk_dir="$1"
sdk_targets="$2"

bitbake $sdk_targets --setscene-only || exit 1

inherit pkgconfig

require openssl.inc

PR = "r9"

SRC_URI += "file://debian.patch;patch=1 \
            file://configure-targets.patch;patch=1 \
            file://shared-libs.patch;patch=1"

PARALLEL_MAKE = ""

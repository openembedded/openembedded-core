require openssl.inc

PR = "r11"

SRC_URI += "file://debian.patch;patch=1 \
            file://configure-targets.patch;patch=1 \
            file://fix-md5-x86_64.patch;patch=1 \
            file://shared-libs.patch;patch=1"

BBCLASSEXTEND = "native"

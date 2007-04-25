require openssl.inc

PR = "r4"

SRC_URI += "file://debian.patch;patch=1 \
            file://armeb.patch;patch=1 \
            file://gnueabi-arm.patch;patch=1"

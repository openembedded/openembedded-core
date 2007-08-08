SUMMARY = "A simple tool to create sha256 hashes from a file"
LICENSE = "LGPL"

PR      = "r1"

S = "${WORKDIR}"


SRC_URI = "file://main.c         \
           file://mhash_sha256.h \
           file://sha256.c "


do_configure() {
    :
}

do_compile() {
    $CC $CFLAGS $CPPFLAGS -o oe_sha256sum main.c sha256.c
}

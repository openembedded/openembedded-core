DESCRIPTION = "This is a modified version of the icecc-create-env script in order to\
make it work with OE."
SECTION = "base"
PRIORITY = "optional"
# source file has just a "GPL" word, but upstream is GPLv2+.
# most probably just GPL would be a mistake
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://icecc-create-env;beginline=2;endline=5;md5=ae1df3d6a058bfda40b66094c5f6065f"

PR = "r2"

DEPENDS = ""
INHIBIT_DEFAULT_DEPS = "1"

inherit native

PATCHTOOL = "patch"
SRC_URI = "file://icecc-create-env"

S = "${WORKDIR}"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/icecc-create-env ${D}/${bindir}
}

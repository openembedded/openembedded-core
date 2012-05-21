SUMMARY = "GNU cpio is a program to manage archives of files"
DESCRIPTION = "GNU cpio is a tool for creating and extracting archives, or copying files from one place to \
another. It handles a number of cpio formats as well as reading and writing tar files."
HOMEPAGE = "http://www.gnu.org/software/cpio/"
SECTION = "base"

DEPENDS = "texinfo-native"

SRC_URI = "${GNU_MIRROR}/cpio/cpio-${PV}.tar.gz \
    file://statdef.patch \
    "

inherit autotools gettext

S = "${WORKDIR}/cpio-${PV}"

do_install () {
    autotools_do_install
    install -d ${D}${base_bindir}/
    mv "${D}${bindir}/cpio" "${D}${base_bindir}/cpio"
    rmdir ${D}${bindir}/
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN} = "cpio rmt"

ALTERNATIVE_LINK_NAME[cpio] = "${base_bindir}/cpio"

ALTERNATIVE_PRIORITY[rmt] = "50"
ALTERNATIVE_LINK_NAME[rmt] = "${libexecdir}/rmt"

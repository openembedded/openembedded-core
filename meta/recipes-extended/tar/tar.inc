SUMMARY = "GNU file archiving program"
DESCRIPTION = "GNU tar saves many files together into a single tape \
or disk archive, and can restore individual files from the archive."
HOMEPAGE = "http://www.gnu.org/software/tar/"
SECTION = "base"

SRC_URI = "${GNU_MIRROR}/tar/tar-${PV}.tar.bz2"

inherit autotools gettext

EXTRA_OECONF += "DEFAULT_RMT_DIR=${base_sbindir}"

EXTRAINSTALL = "do_install_extra"

do_install () {
    autotools_do_install
    ${EXTRAINSTALL}
}

do_install_extra () {
    install -d ${D}${base_bindir}
    mv ${D}${bindir}/tar ${D}${base_bindir}/tar
    rmdir ${D}${bindir}/
}

PACKAGES =+ "${PN}-rmt"

FILES_${PN}-rmt = "${base_sbindir}/rmt*"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN} = "tar"
ALTERNATIVE_${PN}-rmt = "rmt"

ALTERNATIVE_LINK_NAME[tar] = "${base_bindir}/tar"
ALTERNATIVE_LINK_NAME[rmt] = "${base_sbindir}/rmt"

SUMMARY = "GNU file archiving program"
DESCRIPTION = "GNU tar saves many files together into a single tape \
or disk archive, and can restore individual files from the archive."
HOMEPAGE = "http://www.gnu.org/software/tar/"
SECTION = "base"

SRC_URI = "${GNU_MIRROR}/tar/tar-${PV}.tar.bz2"

inherit autotools gettext texinfo

EXTRA_OECONF += "DEFAULT_RMT_DIR=${base_sbindir}"

# Let aclocal use the relative path for the m4 file rather than the
# absolute since tar has a lot of m4 files, otherwise there might
# be an "Argument list too long" error when it is built in a long/deep
# directory.
acpaths = "-I ./m4"

EXTRAINSTALL = "do_install_extra"
EXTRAINSTALL_class-nativesdk = ""

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
ALTERNATIVE_${PN}_class-nativesdk = ""
ALTERNATIVE_${PN}-rmt_class-nativesdk = ""

ALTERNATIVE_LINK_NAME[tar] = "${base_bindir}/tar"
ALTERNATIVE_LINK_NAME[rmt] = "${base_sbindir}/rmt"

BBCLASSEXTEND = "nativesdk"

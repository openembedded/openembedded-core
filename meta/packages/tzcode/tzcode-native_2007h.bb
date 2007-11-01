DESCRIPTION = "tzcode, timezone zoneinfo utils -- zic, zdump, tzselect"

PR = "r0"

SRC_URI = "ftp://elsie.nci.nih.gov/pub/tzcode${PV}.tar.gz \
           ftp://elsie.nci.nih.gov/pub/tzdata${PV}.tar.gz"

S = "${WORKDIR}"

inherit native

do_stage () {
        install -d ${STAGING_BINDIR_NATIVE}
        install -m 755 zic ${STAGING_BINDIR_NATIVE}/
        install -m 755 zdump ${STAGING_BINDIR_NATIVE}/
        install -m 755 tzselect ${STAGING_BINDIR_NATIVE}/
}

do_install () {
        :
}

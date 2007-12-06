require gperf_${PV}.bb

S = "${WORKDIR}/gperf-${PV}"

inherit native

do_stage() {
    install -d ${STAGING_BINDIR_NATIVE}
    install ${S}/src/gperf ${STAGING_BINDIR_NATIVE}
}

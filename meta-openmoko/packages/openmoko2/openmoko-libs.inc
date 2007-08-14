HOMEPAGE = "http://www.openmoko.org"
LICENSE = "GPL"
PV = "0.0.1+svn${SRCDATE}"

inherit autotools pkgconfig

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/libraries;module=${REAL_PN};proto=http"

S = "${WORKDIR}/${PN}"

do_stage() {
    autotools_stage_all
}

HOMEPAGE = "http://www.openmoko.org"
LICENSE = "GPL"
DEPENDS = "gtk+ eds-dbus"
PV = "0.0.1+svn${SRCDATE}"

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/applications;module=${REAL_PN};proto=http"

S = "${WORKDIR}/${REAL_PN}"

inherit autotools

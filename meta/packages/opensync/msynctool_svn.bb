LICENSE = "GPL"
DEPENDS = "libopensync"
HOMEPAGE = "http://www.opensync.org/"
PV = "0.22+svnr${SRCREV}"

SRC_URI = "svn://svn.opensync.org/multisync;module=trunk;proto=http"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

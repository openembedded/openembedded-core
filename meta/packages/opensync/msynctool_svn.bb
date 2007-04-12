PV = "0.22+svn${SRCDATE}"
SRC_URI = "svn://svn.opensync.org/multisync;module=trunk;proto=http"
S = "${WORKDIR}/trunk"

LICENSE = "GPL"
DEPENDS = "libopensync"
HOMEPAGE = "http://www.opensync.org/"

inherit autotools pkgconfig


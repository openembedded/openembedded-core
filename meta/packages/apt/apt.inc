DESCRIPTION = "Advanced front-end for dpkg."
LICENSE = "GPL"
SECTION = "base"

SRC_URI = "${DEBIAN_MIRROR}/main/a/apt/apt_${PV}.tar.gz"
S = "${WORKDIR}/apt-${PV}"

inherit autotools gettext

EXTRA_AUTORECONF = "--exclude=autopoint,autoheader"

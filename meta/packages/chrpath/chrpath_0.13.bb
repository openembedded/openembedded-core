DESCRIPTION = "chrpath allows you to change the rpath (where the application \
looks for libraries) in an application. It does not (yet) allow you to add an \
rpath if there isn't one already."
LICENSE = "GPL"

SRC_URI = "${DEBIAN_MIRROR}/main/c/chrpath/chrpath_${PV}.orig.tar.gz"

inherit autotools

S = "${WORKDIR}/chrpath-${PV}"

BBCLASSEXTEND = "native"

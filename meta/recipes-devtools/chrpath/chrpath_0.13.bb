DESCRIPTION = "chrpath allows you to change the rpath (where the application \
looks for libraries) in an application. It does not (yet) allow you to add an \
rpath if there isn't one already."
LICENSE = "GPL"
PR = "r2"

SRC_URI = "${DEBIAN_MIRROR}/main/c/chrpath/chrpath_${PV}.orig.tar.gz"

inherit autotools

S = "${WORKDIR}/chrpath-${PV}"

# We don't have a staged chrpath-native for ensuring our binary is relocatable
# so must use the one we've just built
CHRPATH_BIN_virtclass-native = "${S}/chrpath"

BBCLASSEXTEND = "native"

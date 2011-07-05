SUMMARY = "Tool to edit rpath in ELF binaries"
DESCRIPTION = "chrpath allows you to change the rpath (where the application \
looks for libraries) in an application. It does not (yet) allow you to add an \
rpath if there isn't one already."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r3"

SRC_URI = "${DEBIAN_MIRROR}/main/c/chrpath/chrpath_${PV}.orig.tar.gz \
        file://standarddoc.patch"

SRC_URI[md5sum] = "b73072a8fbba277558c50364b65bb407"
SRC_URI[sha256sum] = "c1aa5342eac0daad81b8da05aa282ae1ccd6f68bc75ca94064304f541eed071b"

inherit autotools

S = "${WORKDIR}/chrpath-${PV}"

# We don't have a staged chrpath-native for ensuring our binary is relocatable
# so must use the one we've just built
CHRPATH_BIN_virtclass-native = "${S}/chrpath"

BBCLASSEXTEND = "native nativesdk"

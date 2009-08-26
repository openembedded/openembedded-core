DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPL"
RDEPENDS = "python"
RDEPENDS_virtclass-native = ""
PR = "r4"

SRC_URI = "svn://svn.openmoko.org/trunk/src/host/;module=opkg-utils;proto=http \
           file://index-ignore-filenotfound.patch;patch=1"

inherit autotools_stage

S = "${WORKDIR}/opkg-utils"

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES_virtclass-native = ""

BBCLASSEXTEND = "native"

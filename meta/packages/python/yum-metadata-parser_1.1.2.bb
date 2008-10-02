DESCRIPTION = "C-based metadata parser to quickly parse xml metadata into sqlite databases."
HOMEPAGE = "http://linux.duke.edu/projects/yum/download.ptml"
SECTION = "devel/python"
PRIORITY = "optional"
DEPENDS = "python sqlite3 glib-2.0 libxml2"
LICENSE = "GPL"

SRC_URI = "http://linux.duke.edu/projects/yum/download/yum-metadata-parser/yum-metadata-parser-${PV}.tar.gz"
S = "${WORKDIR}/yum-metadata-parser-${PV}"

TARGET_CFLAGS += "-I${STAGING_LIBDIR}/glib-2.0"

inherit distutils

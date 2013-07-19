PR = "${INC_PR}.0"

SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
           file://fix_doc.patch \
          "
SRC_URI[md5sum] = "12edc291dba8127f2e9696e69f36299e"
SRC_URI[sha256sum] = "f3a6898e3a765bb94435b04a6668db9e5d19b3e90e0c69a503a2773ae936c269"

require wget.inc

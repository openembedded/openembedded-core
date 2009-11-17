require update-alternatives-dpkg.inc

PROVIDES += "virtual/update-alternatives"
RPROVIDES_${PN} += "update-alternatives"
EXTRA_RDEPENDS = "perl dpkg"
EXTRA_RDEPENDS_virtclass-native = ""
RDEPENDS_${PN} += "${EXTRA_RDEPENDS}"

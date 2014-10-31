SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
          "

SRC_URI[md5sum] = "293a37977c41b5522f781d3a3a078426"
SRC_URI[sha256sum] = "b977fc10ac7a72d987d48136251aeb332f2dced1aabd50d6d56bdf72e2b79101"

require wget.inc

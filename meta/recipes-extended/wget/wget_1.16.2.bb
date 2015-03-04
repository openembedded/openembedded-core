SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
          "

SRC_URI[md5sum] = "d33b130ff23155e360d0a5172575bf56"
SRC_URI[sha256sum] = "311a28f211117a338501009bfaba5bfeb9cd71564a7b9ad5775b8f5f4369f972"

require wget.inc

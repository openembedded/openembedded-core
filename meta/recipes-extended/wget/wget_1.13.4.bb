PR = "${INC_PR}.2"

SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://fix_makefile.patch \
          "
SRC_URI[md5sum] = "1df489976a118b9cbe1b03502adbfc27"
SRC_URI[sha256sum] = "24c7710bc9f220ce23d8a9e0f5673b0efc1cace62db6de0239b5863ecc934dcd"

require wget.inc

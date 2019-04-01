SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://0002-improve-reproducibility.patch \
          "

SRC_URI[md5sum] = "2692f6678e93601441306b5c1fc6a77a"
SRC_URI[sha256sum] = "7e43b98cb5e10234836ebef6faf24c4d96c0ae7a480e49ff658117cc4793d166"

require wget.inc

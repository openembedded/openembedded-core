require flex.inc
PR = "r3"
LICENSE="BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e4742cf92e89040b39486a6219b68067"
BBCLASSEXTEND = "native"

SRC_URI += "file://avoid-FORTIFY-warnings.patch \
            file://int-is-not-the-same-size-as-size_t.patch"

SRC_URI[md5sum] = "10714e50cea54dc7a227e3eddcd44d57"
SRC_URI[sha256sum] = "0becbd4b2b36b99c67f8c22ab98f7f80c9860aec70f0350a0018f29a88704e7b"

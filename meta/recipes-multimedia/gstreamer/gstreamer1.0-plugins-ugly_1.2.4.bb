include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "
SRC_URI[md5sum] = "c87a27db498bb736f6c266198657ea74"
SRC_URI[sha256sum] = "4ef6f76a47d57b8385d457c7b620b4c28fc041ab1d7c2e3e2f5f05b12b988ea8"
S = "${WORKDIR}/gst-plugins-ugly-${PV}"


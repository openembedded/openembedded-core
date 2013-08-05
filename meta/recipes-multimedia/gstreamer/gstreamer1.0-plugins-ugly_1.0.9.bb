include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

SRC_URI[md5sum] = "ea8b4e05ed5beb982762b45aba266720"
SRC_URI[sha256sum] = "11250fe9e44b0169c3a289e981b31874b483643ed78f619682ae1644d7088379"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"


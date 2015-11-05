include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

SRC_URI[md5sum] = "2c20faa2406afb840699e97e913d8fb4"
SRC_URI[sha256sum] = "0cc3f90e4322efe1f774272e8fe5c185be37cf7999cd5ca7e0e0607e03d56a57"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"


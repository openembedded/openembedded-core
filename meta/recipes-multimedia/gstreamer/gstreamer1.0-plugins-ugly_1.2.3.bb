include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "
SRC_URI[md5sum] = "7ae60e2f759f58f32af5fcdc3c9193c4"
SRC_URI[sha256sum] = "537b0a7607eee499026388bb705b5b68985a3fd59fe22ee09accaf8cdf57eb3b"
S = "${WORKDIR}/gst-plugins-ugly-${PV}"


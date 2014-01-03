include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "
SRC_URI[md5sum] = "ed0fd639a10d91870cc1d55727bb4f44"
SRC_URI[sha256sum] = "fa90cf21eac0a77f9393100356aef99ae42072c31dc218d3ae2e7f86cd5ced69"
S = "${WORKDIR}/gst-plugins-base-${PV}"

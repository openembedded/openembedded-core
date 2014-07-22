include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

SRC_URI += "file://do-not-change-eos-event-to-gap-event-if.patch \
"

SRC_URI[md5sum] = "278e0a1872ecb981e91830b2cb7f3e98"
SRC_URI[sha256sum] = "4d6273dc3f5a94bcc53ccfe0711cfddd49e31371d1136bf62fa1ecc604fc6550"
S = "${WORKDIR}/gst-plugins-base-${PV}"

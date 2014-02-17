include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "
SRC_URI[md5sum] = "17aeabfbcd232526f50c9bee375f1b97"
SRC_URI[sha256sum] = "61edec35c270f86928bad434bd059da4efff865d1ef01bcc64ecbd903625dae1"
S = "${WORKDIR}/gst-plugins-base-${PV}"

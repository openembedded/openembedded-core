include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

SRC_URI += "file://do-not-change-eos-event-to-gap-event-if.patch \
            file://get-caps-from-src-pad-when-query-caps.patch \
            file://taglist-not-send-to-down-stream-if-all-the-frame-cor.patch \
"

SRC_URI[md5sum] = "357165af625c0ca353ab47c5d843920e"
SRC_URI[sha256sum] = "77bd8199e7a312d3d71de9b7ddf761a3b78560a2c2a80829d0815ca39cbd551d"

S = "${WORKDIR}/gst-plugins-base-${PV}"

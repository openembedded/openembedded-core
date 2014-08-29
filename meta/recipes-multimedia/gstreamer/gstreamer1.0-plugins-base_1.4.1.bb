include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

SRC_URI += "file://do-not-change-eos-event-to-gap-event-if.patch \
            file://get-caps-from-src-pad-when-query-caps.patch \
"

SRC_URI[md5sum] = "a825628225bd0a58c0df87cdd2a5db91"
SRC_URI[sha256sum] = "aea9e25be6691bd3cc0785d005b2b5d70ce313a2c897901680a3f7e7cab5a499"
S = "${WORKDIR}/gst-plugins-base-${PV}"

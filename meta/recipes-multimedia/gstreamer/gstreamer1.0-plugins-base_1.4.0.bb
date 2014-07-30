include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

SRC_URI += "file://do-not-change-eos-event-to-gap-event-if.patch \
            file://get-caps-from-src-pad-when-query-caps.patch \
"

SRC_URI[md5sum] = "1ff06280b03b9098a706d1290d8bb3bd"
SRC_URI[sha256sum] = "5daed4b983b64e4e3fbe9cd29063e4302812cd03ba685a15b06a790911d04c1e"
S = "${WORKDIR}/gst-plugins-base-${PV}"

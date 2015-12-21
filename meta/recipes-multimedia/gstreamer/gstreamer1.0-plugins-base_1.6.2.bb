include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

SRC_URI += "file://get-caps-from-src-pad-when-query-caps.patch \
            file://0003-ssaparse-enhance-SSA-text-lines-parsing.patch \
            file://0004-subparse-set-need_segment-after-sink-pad-received-GS.patch \
            file://encodebin-Need-more-buffers-in-output-queue-for-bett.patch \
"

SRC_URI[md5sum] = "f530fbbe287edce79c55a37bd1a39fe2"
SRC_URI[sha256sum] = "c75dd400e451526ed71e1c4955e33d470a2581f5e71ecf84920a41c0a5c75322"

S = "${WORKDIR}/gst-plugins-base-${PV}"

include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"

# Note: The dependency on freetype was dropped shortly after the 1.7.1 release
# so these lines should be removed during the update to 1.8.x
# http://cgit.freedesktop.org/gstreamer/gst-plugins-base/commit/?id=183610c035dd6955c9b3540b940aec50474af031

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-base/gst-plugins-base-${PV}.tar.xz \
    file://get-caps-from-src-pad-when-query-caps.patch \
    file://0003-ssaparse-enhance-SSA-text-lines-parsing.patch \
    file://0004-subparse-set-need_segment-after-sink-pad-received-GS.patch \
    file://encodebin-Need-more-buffers-in-output-queue-for-bett.patch \
"

SRC_URI[md5sum] = "20cc8231609318310f2a55f64c86cbb4"
SRC_URI[sha256sum] = "abc0acc1d15b4b9c97c65cd9689bd6400081853b9980ea428d3c8572dd791522"

S = "${WORKDIR}/gst-plugins-base-${PV}"

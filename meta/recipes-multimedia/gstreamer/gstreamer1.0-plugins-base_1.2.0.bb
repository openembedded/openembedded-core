include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "
SRC_URI[md5sum] = "d0f7bb7f6c781be127902bff89b87c5c"
SRC_URI[sha256sum] = "8656e20bf4b675e5696fb4af193793351926d428ca02826c5667a6384729a45d"

S = "${WORKDIR}/gst-plugins-base-${PV}"

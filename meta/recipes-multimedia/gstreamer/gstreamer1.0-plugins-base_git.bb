DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"

SRC_URI = " \
    git://anongit.freedesktop.org/gstreamer/gst-plugins-base;name=base \
    git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common \
    file://make-gio_unix_2_0-dependency-configurable.patch \
"

PV = "1.7.1+git${SRCPV}"

SRCREV_base = "5f98203bd753c32666c8fa7a2fde6d186c2a4247"
SRCREV_common = "86e46630ed8af8d94796859db550a9c3d89c9f65"
SRCREV_FORMAT = "base"

S = "${WORKDIR}/git"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}

SUMMARY = "Simple Xserver Init Script (no dm)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SECTION = "x11"

SRC_URI = "file://emptty.conf.in \
           file://default.desktop"

S = "${UNPACKDIR}"

inherit features_check

REQUIRED_DISTRO_FEATURES = "x11"

PACKAGECONFIG ??= "blank"
# dpms and screen saver will be on only if 'blank' is in PACKAGECONFIG
PACKAGECONFIG[blank] = ""
PACKAGECONFIG[nocursor] = ""

do_install() {
    install -D -p -m0644 ${S}/emptty.conf.in ${D}${sysconfdir}/emptty/conf
    install -D -p -m0644 ${S}/default.desktop ${D}${datadir}/xsessions/default.desktop

    BLANK_ARGS="${@bb.utils.contains('PACKAGECONFIG', 'blank', '', '-s 0 -dpms', d)}"
    NO_CURSOR_ARG="${@bb.utils.contains('PACKAGECONFIG', 'nocursor', '-nocursor', '', d)}"

    sed -i "s:@NO_CURSOR_ARG@:${NO_CURSOR_ARG}:" ${D}${sysconfdir}/emptty/conf
    sed -i "s:@BLANK_ARGS@:${BLANK_ARGS}:" ${D}${sysconfdir}/emptty/conf
}

FILES:${PN} = "${sysconfdir}/emptty/conf \
               ${datadir}/xsessions/default.desktop"

RDEPENDS:${PN} = "emptty xuser-account"
RPROVIDES:${PN} += "virtual-emptty-conf"
